class UI {
  // Text shown when game is over
  String gameoverText = "Oh noes... maybe keeping a pet in the empty vacuum of space wasn't such a good idea after all. " + 
    "To start over please say goodbye to the pet by petting it one last time.";
  // Text shown right when opening game
  String startText = "Welcome to Gochi by Jacob Bom. The tutorial will commence in \n%1$d seconds.\n To skip the tutorial please pet the egg (weird, I know).";
  // Text to be shown during tutorial
  String[] tutorialStepsStrings = {"Hello! Meet your new pet! It's currently an egg, but if you treat it well, it will soon hatch!\nTap the egg to continue...", 
    "To hatch, and later evolve, your pet, you will need to take a walk. You will also need to feed it and give it water!\nTap the egg to continue...", 
    "To feed it simply drag the apple to the pet's mouth. Same applies to water.\nTap the egg to continue...", 
    "To make your pet like you even more you can also pet it using the brush. Unlike the apple and water you have to drag and then rub on the pet while dragging.\nTap the egg to continue...", 
    "Note that you of course do not have to feed the egg and such. That would be silly. Just walk a lot!\nTap the egg to start the game..."};

  // Our ui elements all subclass Dragable
  Dragable[] dragables;
  
  
  // When did we last check if we have focus
  float lastFocusCheck = -1;
  // How long have we had continues focus for?
  float hasHadFocus = 0;
  // How long to wait at the beginning before starting the tutorial
  float tutorialWait = 10000;
  // How far are we in the tutorial
  int tutorialStep = 0;

  UI() {
    // We simply have 3 UI elements
    dragables = new Dragable[] {new Food(), new Water(), new Brush()};
  }

  void draw() {
    // Always need the BGs
    drawBackground();
    drawPlanet();
    
    // And the pet
    pet.draw();
    // And the UI
    for (int i = 0; i<dragables.length; i++) {
      dragables[i].draw();
    }

    // Draw text depending on which state we're in
    // We should be doing complicated calculations á la
    // https://forum.processing.org/two/discussion/13105/how-to-make-a-string-of-any-length-fit-within-text-box
    // here, but I just couldn't be arsed
    textSize(wh/25);
    textAlign(CENTER, LEFT);
    fill(350);
    if (state == State.START) {
      // Formats the countdown to be seconds and also adds 0.99 sec so it doesn't ever show 0sec remaining
      text(String.format(startText, int((tutorialWait - hasHadFocus)/1000+0.99)), width/2, height/10*5, width/10*9, height/10*5);
    } else if (state == State.TUTORIAL) {
      text(tutorialStepsStrings[tutorialStep], width/2, height/10*5, width/10*9, height/10*5);
    } else if (state == State.MAIN) {
      // We don't have any text here
    } else if (state == State.GAMEOVER) {
      text(gameoverText, width/2, height/10*5, width/10*9, height/10*5);
    }
    
    // Draw a ton of numbers that are useful when debugging
    //drawDebugInfo();
  }

  void tick() {
    // Makes it so that you have to have continous focus for x sec before the tutorial starts automatically
    if (state == State.START) {
      if (focused) {
        hasHadFocus += millis() - lastFocusCheck;
      } else {
        hasHadFocus = 0;
      }
      lastFocusCheck = millis();
      if (hasHadFocus >= tutorialWait) {
        state = State.TUTORIAL;
      }
    }
    
    // Remember to tick the pet!
    pet.tick();
  }

  void drawBackground() {
    // This is much faster than image(), but the image has to be the exact dimensions of the canvas
    background(sprites.bg);
  }

  void drawPlanet() {
    // The planet (mars) that the pet is standing on
    pushMatrix();
    translate(width/2, height-wh/5+sprites.planet.height/2);
    // Rotating is too CPU intensive (more into in sprites.pde)
    //rotate(map(frameCount % (60*80), 0, (60*80), 0, TAU));
    image(sprites.planet, 0, 0);
    popMatrix();
  }

  void drawDebugInfo() {
    pushMatrix();
    fill(300, 99, 75);
    textAlign(LEFT, BOTTOM);
    textSize(24);
    text("Steps: " + pet.steps + "\n" +
      "Likes: " + pet.likes + "\n" +
      "Progress: " + pet.progress + "\n" +
      "Food: " + pet.food + "\n" +
      "Water: " + pet.water + "\n" + 
      "FPS: " + frameRate, 0, height);
    popMatrix();
  }

  // Sometimes explicit is better than implicit
  // Though I will admit there's probably a better way to do these (they are mostly all the same)
  void mousePressed() {
    // For each UI element/button
    for (int i = 0; i<dragables.length; i++) {
      // Propagate the event
      dragables[i].mousePressed();
    }
  }
  void mouseReleased() {
    for (int i = 0; i<dragables.length; i++) {
      dragables[i].mouseReleased();
    }
    // If we're doing the tutorial and mouse is over the pet (and we've released/clicked the mouse)
    if (state == State.TUTORIAL && (pet.collision(new PVector(mouseX, mouseY), 2))) {
      // Go to next step in the tutorial (show next string)
      tutorialStep++;
      // If we're out of tutorial strings start the actual game
      if (tutorialStep >= tutorialStepsStrings.length) {
        state = State.MAIN;
      }
    }
  }
  void mouseDragged() {
    for (int i = 0; i<dragables.length; i++) {
      dragables[i].mouseDragged();
    }
  }
}

abstract class Dragable {
  // Base position
  PVector startPos;
  // Current posision (is different from startPos when we're dragging it)
  PVector pos;
  // Where on the button did we click?
  PVector clickOffset;
  // Radius
  float size;
  // Is it currently being dragged?
  boolean dragged;

  // This is always called by the subclass
  Dragable(int order, float size_) {
    // Find a suitable start position so that we can fit 3 elements at the top of the screen
    startPos = new PVector(width/7*2*order+width/14*3, height/20+width/7);
    // PVector has a copy() method, but it seems to be missing on android so we use something that works on both pc and android
    pos = new PVector(startPos.x, startPos.y);
    size = size_;
  }
  
  // Overwritten in subclasses
  abstract void draw();

  // Overwritten in subclasses
  // They actually all have the same in them it seems,
  // but that's because I was thinking that they might have
  // different shapes and such in the future or something
  abstract boolean mouseOver();

  // Overwritten in subclasses
  abstract void callback();

  // Is the mouse over the pet?
  boolean petMouseOver() {
    return pet.collision(pos, size);
  }

  void mousePressed() {
    // is the mouse over the element/button?
    if (mouseOver()) {
      // Well then we're dragging it now
      dragged = true;
      // Record where on the button we clicked
      clickOffset = PVector.sub(startPos, new PVector(mouseX, mouseY));
      // Fire first drag event ourselves to update position
      mouseDragged();
    }
  }
  void mouseReleased() {
    // Were we dragging and is our mouse now over the pet?
    if (dragged && petMouseOver()) {
      // Then call the callback (which is implemented in subclass)
      callback();
    }
    // We're no longer dragging
    dragged = false;

    // PVector has a copy() method, but it seems to be missing on android so we use something that works on both pc and android
    pos = new PVector(startPos.x, startPos.y);
  }
  void mouseDragged() {
    // If we're dragging THIS element then update it's position
    if (dragged) {
      pos.x = mouseX + clickOffset.x;
      pos.y = mouseY + clickOffset.y;
    }
  }
}

// Not too much to say about these
class Food extends Dragable {
  Food() {
    super(0, width/8);
  }

  void draw() {
    image(sprites.food, pos.x, pos.y);
    //pushMatrix();
    //fill(0, 80, 80);
    //ellipse(pos.x, pos.y, size, size);
    //popMatrix();
  }

  boolean mouseOver() {
    return dist(mouseX, mouseY, pos.x, pos.y) <= size;
  }

  void callback() {
    if (state == State.MAIN) {
      pet.foodAdd(1);
    }
  }
}

class Water extends Dragable {
  Water() {
    super(1, width/8);
  }

  void draw() {
    image(sprites.water, pos.x, pos.y);
    //pushMatrix();
    //fill(215, 80, 80);
    //ellipse(pos.x, pos.y, size, size);
    //popMatrix();
  }

  boolean mouseOver() {
    return dist(mouseX, mouseY, pos.x, pos.y) <= size;
  }

  void callback() {
    if (state == State.MAIN) {
      pet.waterAdd(1);
    }
  }
}

// This one is more interesting
class Brush extends Dragable {
  // When did we last drag
  float lastDrag;
  // Were we brushing on last drag event?
  boolean wasBrushing;

  Brush() {
    super(2, width/8);
  }

  void draw() {
    image(sprites.brush, pos.x, pos.y);
    //pushMatrix();
    //fill(100, 80, 80);
    //ellipse(pos.x, pos.y, size, size);
    //popMatrix();
  }

  boolean mouseOver() {
    return dist(mouseX, mouseY, pos.x, pos.y) <= size;
  }

  void callback() {
    // Do nothing
  }

  void mouseDragged() {
    // We want the old functionality we just wanna expand upon it slightly
    super.mouseDragged();
    // Make sure that we're dragging THIS element
    if (dragged) {
      // Are we brushing/petting the pet?
      if (wasBrushing && petMouseOver()) {
        if (state == State.MAIN) {
          // Get likability points based on how long you brush
          // I don't want you just to be able to idlily hover the brush over pet
          // So if we don't get a mouseDragged event in 300ms we don't count it
          float diff = millis() - lastDrag;
          if (diff < 300) {
            pet.likesAdd(map(diff, 0, 300, 0, 0.05));
          }
          lastDrag = millis();
        } else if ((state == State.GAMEOVER) || (state == State.START)) {
          // "Brushing the pet" is how you skip the tutorial
          // and how to restart the game after you get gameover
          pet = new Pet(Stage.EGG, true);
          state = State.MAIN;
        }
      } else {
        // If we're over the pet now then we wanna know that next time this get's called
        // Since that means we ARE brushing it
        wasBrushing = petMouseOver();
      }
    }
  }
}