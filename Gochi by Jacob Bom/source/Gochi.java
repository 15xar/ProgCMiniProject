import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.sensors.*; 
import de.looksgood.ani.*; 
import javax.swing.JDialog; 
import javax.swing.JOptionPane; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Gochi extends PApplet {

// Gochi by Jacob Bom
// NOTE: this source code contains both Android and PC version.
// Comments are written using we and I pronouns but everything is written by me alone nonetheless
// To switch between them, uncomment the appropiate ANDROID/PC ONLY segments throughout the code.
// Also see the file/tab android_workarounds for Ani workaround.

// This is android only, but it doesn't crash on PC (provided you have
// the correct library) so therefore we just keep it here always


// For prettier animation


// For warning if you're not using android
// --- PC ONLY ---


// --- END PC ONLY

// Only used on android
KetaiSensor sensor;

// What are we doing right now?
// Check ENUMS.java for possible values
State state;
// Our "avatar" so to speak
Pet pet;
// Handles everything that gets drawn
UI ui;
// Handles images
Sprites sprites;

// A "constant" that I use to size things
float wh;

ArrayList<Ani> anis; // see android_workarounds tab for info
ArrayList anisToUnregister; // see android_workarounds tab for info

public void setup() {
  println("Starting...");
  
  // --- PC ONLY ---
  // 540x960 is a pretty good 16:9 mobile size
  // I highly recommend using the P2D render for this program
  // (in fact some things will only work with it)
  // since it relies a lot on images, which the default render is quite horrendous at
  // Default render is faster to launch though, which is why I use it for debugging
  // Fullscreen on a desktop pc is NOT recommended as it does not handle landscape very well
  
  // NOTE: I moved size() and such to settings() since I needed to do advanced stuff
  // --- END PC ONLY ---
  
  // --- ANDROID ONLY ---
  //// Make it "fullScreen"
  //size(displayWidth, displayHeight);
  //// Lock it in portrait, otherwise the program will reboot if you tilt the phone
  //orientation(PORTRAIT);
  //// Init the sensor
  //sensor = new KetaiSensor(this);
  //// Start collecting data
  //sensor.start();
  // --- END ANDROID ONLY ---
  
  // Don't need any strokes (and if I do in a special case I can just turn it back on)
  noStroke();
  // Should be on by default, but sometimes not on android.
  
  // Prefer this colorMode for most things
  // makes calls to fill and stroke be (HUE (degrees), saturation (percent), brightness (percent), opacity (percent))
  // Not that many colours are used after I added images, but it's still nice to have.
  colorMode(HSB, 360, 100, 100, 100);
  // I like to use centers for everything
  rectMode(CENTER);
  imageMode(CENTER);
  // I cannot phantom why this is not default
  ellipseMode(RADIUS);
  
  // Used to size pretty much all elements
  wh = (width + height) / 2;
  
  // We start in this state (see inside UIs draw for what this means)
  state = State.START;

  // Ani needs a reference to the main PApplet to work, so we supply it here
  Ani.init(this);
  
  // Init own classes (make an object of them)
  sprites = new Sprites();
  ui = new UI();
  pet = new Pet(Stage.EGG, false);
  
  anis = new ArrayList<Ani>(); // see android_workarounds tab for info
  anisToUnregister = new ArrayList(); // see android_workarounds tab for info
  
  // Code to check which render is in use and warn about game mechanics if not on android
  // This is therefore
  // --- PC ONLY
  println("Checking sketch render");
  String renderer = sketchRenderer();
  println("Using " + renderer);
  if (renderer != "processing.core.PGraphicsAndroid2D") {
    String msg = "Dette program er lavet til at k\u00f8re p\u00e5 Android.\n" + 
                 "Det kan godt k\u00f8re p\u00e5 din PC, men du f\u00e5r ikke den fulde oplevelse.\n" + 
                 "F.eks. har din computer, modsat android telefoner, ingen skidtt\u00e6ller.\n" +
                 "Det betyder at du i stedet for at tage skridt bliver n\u00f8dt til at trykke p\u00e5 's' knappen\nen masse gange for at emulere at du tager skridt i den virkelige verden.\n" +
                 "Tryk p\u00e5 OK for at fjerne denne besked.\n";
    showPopup(msg, "STOP! VIGTIGT!", JOptionPane.WARNING_MESSAGE);
  }
  // --- END PC ONLY ---
  
  println("Setup complete");
}

public void draw() {
  // Android doesn't have a surface (window)
  // --- PC ONLY ---
  surface.setTitle(String.format("Gochi by Jacob Bom [%1$.3ffps] [Use 's' to emulate a step]", frameRate));
  // --- END PC ONLY ---
  
  // We draw the bg in UI instead
  //background(0);
  
  // First draw then tick (order doesn't really matter since processing doesn't actually draw anything before PApplet.draw() finishes
  ui.draw();
  ui.tick();

  updateAnis(); // see android_workarounds tab for info
}

public void keyPressed() {
  // DEBUG
  if (key == 's') pet.step(pet.steps+1+pet.initialSteps);
  else if (key == 'w') pet.waterAdd(1);
  else if (key == 'f') pet.foodAdd(1);
  else if (key == 'l') pet.likesAdd(1);
}

// We simply propagate these events to the proper class
public void mousePressed() {
  ui.mousePressed();
}
public void mouseReleased() {
  ui.mouseReleased();
}
public void mouseDragged() {
  ui.mouseDragged();
}

// This gets called by ketai whenever there is a sensor update.
// It is therefore ANDROID ONLY, but it's safe to leave it here even on PC.
public void onStepCounterEvent(float s) {
  pet.step(s);
}

// --- PC ONLY ---
public void settings() {
  // We have this here due to wanting to have a proper icon
  // And PJOGL is only defined here
  // See comments about size and such in setup()!
  
  //size(540, 960);
  //fullScreen(P2D);
  size(540, 960, P2D);
  PJOGL.setIcon("icon.png");
}
// --- END PC ONLY
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
  double hungerRate = 0.0001f;
  double thirstRate = 0.0005f;
  
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
    size = wh/1.5f;
    
    // Start Rocking animation
    ani = new Ani(this, 4, "r", 100, Ani.SINE_IN_OUT);
    
    // Prepare heart animation
    Ani.noAutostart();
    heartAni = new Ani(this, 2, "heartR", 100, Ani.QUART_OUT);
    Ani.autostart();
  }

  public void draw() {
    pushMatrix();
    // Translate to bottom point of pet and rotate around that
    translate(pos.x, pos.y);
    if (movingRight) {
      rotate(map(r, 0, 100, -PI/rockingStray, PI/rockingStray));
    } else {
      rotate(map(r, 0, 100, PI/rockingStray, -PI/rockingStray));
    }
    
    // We + some stuff cause our pictures have slight padding at the bottom
    float y = -size/2 + size*0.08f;
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
      heart(pos.x*1.2f, pos.y-size/2-(wh/2/100)*heartR, wh/200, false);
    }
    
    pushMatrix();
    // If we're hungry show a thought bubble with apple in it
    // That might be better fit to be in UI,
    // but it has more to do with the pet I'd say
    if (food <= 2) {
      image(sprites.thought, width*0.75f, height*0.4f);
      image(sprites.smallFood, width*0.75f, height*0.39f);
    }
    if (water <= 2) {
      // Flip it
      scale(-1, 1);
      // Note the negative x coord due to the scaling
      image(sprites.thought, -width*0.25f, height*0.4f);
      image(sprites.smallWater, -width*0.25f, height*0.39f);
    }
    popMatrix();
  }
  
  // So we don't have to always say if we wanna show the animation
  public void changeStage(Stage newStage) {
    changeStage(newStage, true);
  }
  
  public void changeStage(Stage newStage, boolean showAnimation) {
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

  public void tick() {
    // How far are we towards our goal?
    // Calculated based on both steps and likeability points
    // We want a number between 0 and 1 even if we are at later stages so we have to factor in how many steps and points it took to get here
    float stepProgress = constrain(((steps-initialSteps)-stage.prevStepGoal()) / stage.stepGoal(), 0, 1);
    float likeProgress = constrain((likes-stage.prevLikeGoal()) / stage.likeGoal(), 0, 1);
    progress = (stepProgress + likeProgress) / 2;
    
    // Evolve once we have enough progress
    if (progress >= 1.0f && evolveAniI == -1) {
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
    if (ani.getSeek() >= 0.48f && ani.getSeek() <= 0.52f) {
      rockingStray = (int) map(progress, 0, 1, 30+50, 30);
    }
    // If the rocking animation reached the end
    if (ani.isEnded()) {
      // If we are not dead then change the animation timing so it's more violent the closer to "evolution" you are
      if (stage != Stage.DEAD) {
        // Do this after ani had ended to avoid jerks due to timing shifts
        ani.setDuration(map(progress, 0, 1, 4, 0.3f));
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
        likes -= 0.5f;
      }
    }
    if (likes < 1) likes = 1; // We don't wanna accidently divide by zero
    
    // Actually decrease food and water by their respective rates
    food -= hungerRate;
    water -= thirstRate;
    
    
  }
  
  public boolean collision(PVector otherPos, float otherR) {
    // The middle of the pet (the different stages have different middles/sizes)
    // Based on the bottom anchor point (pos)
    PVector middlePos;
    float collisionR;
    if (stage == Stage.EGG) {
      middlePos = new PVector(0, -size*0.32f);
      collisionR = size/3;
    } else if (stage == Stage.BABY) {
      middlePos = new PVector(0, -size/4);
      collisionR = size/4;
    } else if (stage == Stage.ADULT) {
      middlePos = new PVector(0, -size*0.4f);
      collisionR = size/3;
    } else if (stage == Stage.DEAD) {
      middlePos = new PVector(0, -size*0.2f);
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
  
  public void showHeart() {
    // Rewind the animation and start it
    heartAni.seek(0);
    heartAni.start();
  }

  public void step(float steps_) {
    // If the step sensor was already running on the phone it will report a number of steps
    // that've been taking without having ever opened the program
    // So we have to compensate by saving that initial number
    if (initialSteps == -1) {
      initialSteps = steps_;
    } else {
      steps = steps_;
    }
  }
  
  public void foodAdd(float food_) {
    // Add food but not beyond 10 and also add a like
    if (food + food_ <= 10) {
      food += food_;
      likesAdd(1);
    }
  }
  
  public void waterAdd(float water_) {
    // Add water but not beyond 10 and also add half a like
    // (thirstRate is a lot faster than hungerRate so its only fair)
    if (water + water_ <= 10) {
      water += water_;
      likesAdd(0.5f);
    }
  }
  
  public void likesAdd(float likes_) {
    // If the likes we are adding takes us above the next 0.25 or we're adding 0.25 or more
    if (((likes % 0.25f) > ((likes + likes_) % 0.25f)) || likes_ >= 0.25f) {
      // Then show a heart
      showHeart();
    }
    likes += likes_;
  }
}
// Allows us to refrence a sprite just as sprite.poof etc.

class Sprites {
  PImage[] poof = new PImage[3];
  
  // Maps a picture to each Stage
  HashMap<Stage,PImage> pet = new HashMap<Stage,PImage>();
  
  PImage bg, planet;
  PImage thought;
  PImage food, water, brush;
  PImage smallFood, smallWater;

  Sprites() {
    // Poof sprites
    for (int i = 0; i<poof.length; i++) {
      // I originally wanted to use a spritesheet for these,
      // but there's a bug in android that means I can't resize an
      // image that processing has generated so I had to do it this way 
      poof[i] = loadImage("poof_" + i + ".png");
      poof[i].resize(PApplet.parseInt(234*(1+wh/600)), 0);
    }
    
    // Main pet sprites
    for (Stage s : Stage.values()) {
      PImage tmp;
      tmp = loadImage(s.toString() + ".png");
      tmp.resize(PApplet.parseInt(wh/1.5f), 0);
      pet.put(s, tmp);
    }
    
    // BG
    bg = loadImage("bg3.png");
    
    // I originally had the BG rotating since turned to be too cpu intensive :(
    //bg.resize((height)*2+width/2, 0);
    
    // Resize so the picture is the big enough to fit the entire screen (figure out which direction needs scaling)
    if ((width/height) < (bg.width/bg.height)) {
      bg.resize(0, height);
    } else {
      bg.resize(width, 0);
    }
    // Crop to screen size so it works with the background() method
    bg = bg.get(0, 0, width, height);
    
    // Planet
    // Orignally wanted this to spin too, so I had the whole planet
    // Now I just use a cutout of the top since drawing the rest (even if out of screen) is CPU intensive
    planet = loadImage("planet_top.png");
    planet.resize(PApplet.parseInt(width*1.5f), 0);
    
    // Thought bubble
    thought = loadImage("thought2.png");
    thought.resize(PApplet.parseInt(width/2.3f), 0);
    
    // UI Elements
    food = loadImage("food.png");
    food.resize(width/4, 0);
    water = loadImage("water.png");
    water.resize(width/4, 0);
    brush = loadImage("brush.png");
    brush.resize(width/4, 0);
    
    // Small UI elements for use in thought bubble
    smallFood = loadImage("food.png");
    smallFood.resize(width/8, 0);
    smallWater = loadImage("water.png");
    smallWater.resize(width/8, 0);
  }
}
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

  public void draw() {
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
    // We should be doing complicated calculations \u00e1 la
    // https://forum.processing.org/two/discussion/13105/how-to-make-a-string-of-any-length-fit-within-text-box
    // here, but I just couldn't be arsed
    textSize(wh/25);
    textAlign(CENTER, LEFT);
    fill(350);
    if (state == State.START) {
      // Formats the countdown to be seconds and also adds 0.99 sec so it doesn't ever show 0sec remaining
      text(String.format(startText, PApplet.parseInt((tutorialWait - hasHadFocus)/1000+0.99f)), width/2, height/10*5, width/10*9, height/10*5);
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

  public void tick() {
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

  public void drawBackground() {
    // This is much faster than image(), but the image has to be the exact dimensions of the canvas
    background(sprites.bg);
  }

  public void drawPlanet() {
    // The planet (mars) that the pet is standing on
    pushMatrix();
    translate(width/2, height-wh/5+sprites.planet.height/2);
    // Rotating is too CPU intensive (more into in sprites.pde)
    //rotate(map(frameCount % (60*80), 0, (60*80), 0, TAU));
    image(sprites.planet, 0, 0);
    popMatrix();
  }

  public void drawDebugInfo() {
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
  public void mousePressed() {
    // For each UI element/button
    for (int i = 0; i<dragables.length; i++) {
      // Propagate the event
      dragables[i].mousePressed();
    }
  }
  public void mouseReleased() {
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
  public void mouseDragged() {
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
  public abstract void draw();

  // Overwritten in subclasses
  // They actually all have the same in them it seems,
  // but that's because I was thinking that they might have
  // different shapes and such in the future or something
  public abstract boolean mouseOver();

  // Overwritten in subclasses
  public abstract void callback();

  // Is the mouse over the pet?
  public boolean petMouseOver() {
    return pet.collision(pos, size);
  }

  public void mousePressed() {
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
  public void mouseReleased() {
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
  public void mouseDragged() {
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

  public void draw() {
    image(sprites.food, pos.x, pos.y);
    //pushMatrix();
    //fill(0, 80, 80);
    //ellipse(pos.x, pos.y, size, size);
    //popMatrix();
  }

  public boolean mouseOver() {
    return dist(mouseX, mouseY, pos.x, pos.y) <= size;
  }

  public void callback() {
    if (state == State.MAIN) {
      pet.foodAdd(1);
    }
  }
}

class Water extends Dragable {
  Water() {
    super(1, width/8);
  }

  public void draw() {
    image(sprites.water, pos.x, pos.y);
    //pushMatrix();
    //fill(215, 80, 80);
    //ellipse(pos.x, pos.y, size, size);
    //popMatrix();
  }

  public boolean mouseOver() {
    return dist(mouseX, mouseY, pos.x, pos.y) <= size;
  }

  public void callback() {
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

  public void draw() {
    image(sprites.brush, pos.x, pos.y);
    //pushMatrix();
    //fill(100, 80, 80);
    //ellipse(pos.x, pos.y, size, size);
    //popMatrix();
  }

  public boolean mouseOver() {
    return dist(mouseX, mouseY, pos.x, pos.y) <= size;
  }

  public void callback() {
    // Do nothing
  }

  public void mouseDragged() {
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
            pet.likesAdd(map(diff, 0, 300, 0, 0.05f));
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
// The libraray Ani that I'm using is broken on android due to the lack of a proper registerPre
// The code below (and some bits in the main file) simulate the proper behaviour.

// workaround -------------------------------------------
public void updateAnis() {
  if (anis.size() == 0) return;

  for (int i=0; i < anis.size(); i++) {
    Ani aniTmp = (Ani)anis.get(i);
    aniTmp.pre();
  }

  if (anisToUnregister.size() > 0) {
    for (int i=0; i < anisToUnregister.size(); i++) {
      anis.remove(i);
      anisToUnregister.remove(i);
      println("removed");
    }
  }
  println(anis.size());
}

public void registerPre(Object obj) {
  anis.add( (Ani)obj );
}

public void unregisterPre(Object obj) {
  int index = anis.indexOf(  (Ani)obj );
  anisToUnregister.add(index);
}
// Makes a non-modal JOptionPane messagebox
public void showPopup(Object msg, String title, int messageType) {
  // --- PC ONLY ---
  // Make the pane
  JOptionPane pane = new JOptionPane(msg, messageType);
  // Make the pane create the dialog for us
  JDialog dialog = pane.createDialog(null, title);
  // Tweak it to be non-modal
  // The reason we want this, is that if it was modal it would halt processings draw thread,
  // which means you see nothing but a white screen while the popup is open
  dialog.setModal(false);
  // Display it
  dialog.show();
  // Put it on top of processings own window
  dialog.setVisible(true);
  // --- END PC ONLY
}

// Copied from previous project
// YAY code reuse
public void heart(float x, float y, float size, boolean half) {
  pushMatrix();
  translate(x-50*size, y-20*size);
  beginShape();
  if (!half) {
    vertex(50*size, 15*size);
    bezierVertex(50*size, -5*size, 90*size, 5*size, 50*size, 40*size); // Right
  }
  vertex(50*size, 15*size);
  bezierVertex(50*size, -5*size, 10*size, 5*size, 50*size, 40*size); // Left
  endShape();
  popMatrix();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Gochi" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
