// Gochi by Jacob Bom
// NOTE: this source code contains both Android and PC version.
// Comments are written using we and I pronouns but everything is written by me alone nonetheless
// To switch between them, uncomment the appropiate ANDROID/PC ONLY segments throughout the code.
// Also see the file/tab android_workarounds for Ani workaround.

// This is android only, but it doesn't crash on PC (provided you have
// the correct library) so therefore we just keep it here always
import ketai.sensors.*;

// For prettier animation
import de.looksgood.ani.*;

// For warning if you're not using android
// --- PC ONLY ---
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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

void setup() {
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
  smooth();
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
    String msg = "Dette program er lavet til at køre på Android.\n" + 
                 "Det kan godt køre på din PC, men du får ikke den fulde oplevelse.\n" + 
                 "F.eks. har din computer, modsat android telefoner, ingen skidttæller.\n" +
                 "Det betyder at du i stedet for at tage skridt bliver nødt til at trykke på 's' knappen\nen masse gange for at emulere at du tager skridt i den virkelige verden.\n" +
                 "Tryk på OK for at fjerne denne besked.\n";
    showPopup(msg, "STOP! VIGTIGT!", JOptionPane.WARNING_MESSAGE);
  }
  // --- END PC ONLY ---
  
  println("Setup complete");
}

void draw() {
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

void keyPressed() {
  // DEBUG
  if (key == 's') pet.step(pet.steps+1+pet.initialSteps);
  else if (key == 'w') pet.waterAdd(1);
  else if (key == 'f') pet.foodAdd(1);
  else if (key == 'l') pet.likesAdd(1);
}

// We simply propagate these events to the proper class
void mousePressed() {
  ui.mousePressed();
}
void mouseReleased() {
  ui.mouseReleased();
}
void mouseDragged() {
  ui.mouseDragged();
}

// This gets called by ketai whenever there is a sensor update.
// It is therefore ANDROID ONLY, but it's safe to leave it here even on PC.
void onStepCounterEvent(float s) {
  pet.step(s);
}

// --- PC ONLY ---
void settings() {
  // We have this here due to wanting to have a proper icon
  // And PJOGL is only defined here
  // See comments about size and such in setup()!
  
  //size(540, 960);
  //fullScreen(P2D);
  size(540, 960, P2D);
  PJOGL.setIcon("icon.png");
}
// --- END PC ONLY