class Pet {
  // How grown is the pet (see ENUMS.java for different stages)
  Stage stage;
  // Determines the height of the pet
  float size;
  // Base food value (a double due to bad float precission - and we substact a tiny amount each frame - it adds up)
  double food;
  // Base hydration level
  double water;
  // How many steps the player has taken this stage
  float steps = 0;
  // Where did we start
  float initialSteps = -1;
  
  // We start this at 1 so that you don't need likes to get out of egg stage
  // (and to avoid dividing by zero and breaking maths)
  float likes = 1;
  
  // The egg's position
  PVector pos;

  // Rocking Animation
  Ani ani;
  // Value controlled by Ani to control rocking animation
  float r = 0;
  // Is the rocking moving right?
  boolean movingRight = false; 
  // How much "stray"/movement the rocking animation has
  int rockingStray = 30+50;
  
  // Evolve animation index
  // If it's not -1 then it will start
  int evolveAniI = -1;
  
  // Ani for the heart that appears when you gain "likes"
  Ani heartAni;
  float heartR;
  
  // How quickly should food and water go down?
  double hungerRate = 0.0001;
  double thirstRate = 0.0005;
  
  // Moved this instead of inside the tick method so we can debug it easily
  float progress;

  Pet(Stage stage_, boolean showAnimation) {
    // Better to delegate to a method
    changeStage(stage_, showAnimation);
    // Start in the middle (it's out of 10, though it can technically go
    // into the negatives, and there you'll start losing likes)
    food = 5;
    water = 5;

    // The bottom point (anchor) of the pet
    pos = new PVector(width/2, height-wh/6);
    // The height was originally 2 but I upped it since the pictures have paddding (since they aren't the same size)
    size = wh/1.5;
    
    // Start Rocking animation
    ani = new Ani(this, 4, "r", 100, Ani.SINE_IN_OUT);
    
    // Prepare heart animation
    Ani.noAutostart();
    heartAni = new Ani(this, 2, "heartR", 100, Ani.QUART_OUT);
    Ani.autostart();
  }

  void draw() {
    pushMatrix();
    // Translate to bottom point of pet and rotate around that
    translate(pos.x, pos.y);
    if (movingRight) {
      rotate(map(r, 0, 100, -PI/rockingStray, PI/rockingStray));
    } else {
      rotate(map(r, 0, 100, PI/rockingStray, -PI/rockingStray));
    }
    
    // We + some stuff cause our pictures have slight padding at the bottom
    float y = -size/2 + size*0.08;
    image(sprites.pet.get(stage), 0, y);
    popMatrix();
    
    // If we're evolving show the proper frame
    // We divide by 15 so that we change frame every 15 actual frames
    // Which makes the animation take ~3/4 of a sec
    if (evolveAniI != -1) {
      image(sprites.poof[evolveAniI/15], pos.x, pos.y+y);
    }
    
    // Show heart if heart needs to be shown
    if (heartAni.isPlaying()) {
      // Make it red, and fade it out
      fill(0, 80, 80, 100-heartR);
      // Show it
      heart(pos.x*1.2, pos.y-size/2-(wh/2/100)*heartR, wh/200, false);
    }
    
    pushMatrix();
    // If we're hungry show a thought bubble with apple in it
    // That might be better fit to be in UI,
    // but it has more to do with the pet I'd say
    if (food <= 2) {
      image(sprites.thought, width*0.75, height*0.4);
      image(sprites.smallFood, width*0.75, height*0.39);
    }
    if (water <= 2) {
      // Flip it
      scale(-1, 1);
      // Note the negative x coord due to the scaling
      image(sprites.thought, -width*0.25, height*0.4);
      image(sprites.smallWater, -width*0.25, height*0.39);
    }
    popMatrix();
  }
  
  // So we don't have to always say if we wanna show the animation
  void changeStage(Stage newStage) {
    changeStage(newStage, true);
  }
  
  void changeStage(Stage newStage, boolean showAnimation) {
    // If it's dead make it move slowly and change state to GAMEOVER
    if (newStage == Stage.DEAD) {
      ani.setDuration(30);
      state = State.GAMEOVER;
    }
    
    // If we should animate set the animation frame to 0 (and therefore not -1)
    if (showAnimation) evolveAniI = 0;
    // Actually update the stage
    stage = newStage;
  }

  void tick() {
    // How far are we towards our goal?
    // Calculated based on both steps and likeability points
    // We want a number between 0 and 1 even if we are at later stages so we have to factor in how many steps and points it took to get here
    float stepProgress = constrain(((steps-initialSteps)-stage.prevStepGoal()) / stage.stepGoal(), 0, 1);
    float likeProgress = constrain((likes-stage.prevLikeGoal()) / stage.likeGoal(), 0, 1);
    progress = (stepProgress + likeProgress) / 2;
    
    // Evolve once we have enough progress
    if (progress >= 1.0 && evolveAniI == -1) {
      if (stage == Stage.EGG) {
        changeStage(Stage.BABY);
      } else if (stage == Stage.BABY) {
        changeStage(Stage.ADULT);
      } else if (stage == Stage.ADULT) {
        changeStage(Stage.DEAD);
      } 
    }
    
    // Control rocking animation    
    // We need to do this in the middle so that the egg/pet doesn't just suddently jerk a little towards the middle due to higher stray
    if (ani.getSeek() >= 0.48 && ani.getSeek() <= 0.52) {
      rockingStray = (int) map(progress, 0, 1, 30+50, 30);
    }
    // If the rocking animation reached the end
    if (ani.isEnded()) {
      // If we are not dead then change the animation timing so it's more violent the closer to "evolution" you are
      if (stage != Stage.DEAD) {
        // Do this after ani had ended to avoid jerks due to timing shifts
        ani.setDuration(map(progress, 0, 1, 4, 0.3));
      }
      // Start moving the other way
      ani.start();
      movingRight = !movingRight;
    }
    
    // If we trigged the evolve animation by setting it to another value than -1
    if (evolveAniI != -1) {
      evolveAniI++;
      // Once we're through reset it back to -1
      if (evolveAniI/15 >= sprites.poof.length) {
        evolveAniI = -1;
      }
    }
    
    // If we're starving
    if (food <= 0) {
      // Then for each 1 food point that we get below 0 remove a like
      if ((food % 1) < ((food - hungerRate) % 1)) {
        likes -= 1;
      }
    }
    // If we're starving of thirst (sidenote: what the hell is that called??)
    if (water <= 0) {
      // Then for each 1 water point that we get below 0 remove half a like
      if ((water % 1) < ((water - thirstRate) % 1)) {
        likes -= 0.5;
      }
    }
    if (likes < 1) likes = 1; // We don't wanna accidently divide by zero
    
    // Actually decrease food and water by their respective rates
    food -= hungerRate;
    water -= thirstRate;
    
    
  }
  
  boolean collision(PVector otherPos, float otherR) {
    // The middle of the pet (the different stages have different middles/sizes)
    // Based on the bottom anchor point (pos)
    PVector middlePos;
    float collisionR;
    if (stage == Stage.EGG) {
      middlePos = new PVector(0, -size*0.32);
      collisionR = size/3;
    } else if (stage == Stage.BABY) {
      middlePos = new PVector(0, -size/4);
      collisionR = size/4;
    } else if (stage == Stage.ADULT) {
      middlePos = new PVector(0, -size*0.4);
      collisionR = size/3;
    } else if (stage == Stage.DEAD) {
      middlePos = new PVector(0, -size*0.2);
      collisionR = size/4;
    } else { //WTF! this should never happen, but java needs it to be happy
      middlePos = new PVector();
      collisionR = 0;
    }
    
    // Visualize hitbox
    // Requires repeating invocation of this method
    //ellipse(PVector.add(pos, middlePos).x, PVector.add(pos, middlePos).y, collisionR + otherR, collisionR + otherR);
    
    // Returns a boolean by testing if the dist is greater than the combined radius
    return PVector.add(pos, middlePos).dist(otherPos) <= collisionR + otherR;
  }
  
  void showHeart() {
    // Rewind the animation and start it
    heartAni.seek(0);
    heartAni.start();
  }

  void step(float steps_) {
    // If the step sensor was already running on the phone it will report a number of steps
    // that've been taking without having ever opened the program
    // So we have to compensate by saving that initial number
    if (initialSteps == -1) {
      initialSteps = steps_;
    } else {
      steps = steps_;
    }
  }
  
  void foodAdd(float food_) {
    // Add food but not beyond 10 and also add a like
    if (food + food_ <= 10) {
      food += food_;
      likesAdd(1);
    }
  }
  
  void waterAdd(float water_) {
    // Add water but not beyond 10 and also add half a like
    // (thirstRate is a lot faster than hungerRate so its only fair)
    if (water + water_ <= 10) {
      water += water_;
      likesAdd(0.5);
    }
  }
  
  void likesAdd(float likes_) {
    // If the likes we are adding takes us above the next 0.25 or we're adding 0.25 or more
    if (((likes % 0.25) > ((likes + likes_) % 0.25)) || likes_ >= 0.25) {
      // Then show a heart
      showHeart();
    }
    likes += likes_;
  }
}