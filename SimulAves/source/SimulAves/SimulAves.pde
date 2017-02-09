import controlP5.*; // GUI library
import java.util.Iterator;
import java.util.List;

/**
  Simulation of boids, originally created by Craig Reynolds in 1986.
  Most of this code is inspired by the examples in Daniel Shiffman's Nature Of Code,
  and many similarities may appear.

  Author: brou 
*/

ControlP5 cp5; // GUI controller
PFont font;
Flock flock;
boolean debugging = false;
boolean mouseStatus;
int birdAmount = 100;

float separationDist; // desired separation between birds
float cohesionDist;   // max distance between birds cohesing
float alignDist;      // max distance between birds aligning
float birdSize;       //size of birds
boolean wandering;    // if the wandering behaviour is enabled
boolean separating;   // if the separating behaviour is enabled
boolean cohesing;     // if the cohesing behaviour is enabled
boolean aligning;     // if the aligning behaviour is enabled
boolean noiseEnabled; // if the noise behaviour is enabled
float maxSpeed;       // the maximum speed of all birds
float maxForce;       // the maximum force a bird can apply to itself, with out being manipulated by the mult variables
float wanderMult;     // a factor determining how much force each bird will use in its wander behaviour. 1 is default
float separationMult; // a factor determining how much force each bird will use in its seperation behaviour. 1 is default
float cohesionMult;   // a factor determining how much force each bird will use in its cohesion behaviour. 1 is default
float alignMult;      // a factor determining how much force each bird will use in its align behaviour. 1 is default
float noiseMult;      // a factor determining how much force each bird will use in its noise behaviour. 1 is default

boolean welcomeFading;// determines of the welcome text is currently fading away
int welcomeTimer;     // a timer determining how much time the welcome text will stay on screen

void setup()
{
  fullScreen();
  textAlign(CENTER);
  
  // create a new flock
  flock = new Flock(birdAmount);
  setDefaultFields();
  // GUI
  font = createFont("arial", 14);
  
  cp5 = new ControlP5(this, font);
  
  
  Group settings = cp5.addGroup("settings")
                .setPosition(10,15)
                .setBackgroundHeight(570)
                .setWidth(350)
                .setBarHeight(16)
                .setBackgroundColor(color(0,25))
                ;
                
  cp5.addSlider("separationDist")
     .setPosition(10,10)
     .setSize(180,16)
     .setGroup(settings)
     ;
  
  cp5.addSlider("cohesionDist")
     .setPosition(10,30)
     .setSize(180,16)
     .setGroup(settings)
     ;
  
  cp5.addSlider("alignDist")
     .setPosition(10,50)
     .setSize(180,16)
     .setGroup(settings)
     ;
     
  cp5.addSlider("birdSize")
     .setRange(0, 20)
     .setPosition(10,70)
     .setSize(180,16)
     .setGroup(settings)
     ;
     
   cp5.addToggle("wandering")
     .setPosition(10,100)
     .setSize(50,20)
     .setGroup(settings)
     ;
     
   cp5.addToggle("separating")
     .setPosition(10,150)
     .setSize(50,20)
     .setGroup(settings)
     ;
     
   cp5.addToggle("cohesing")
     .setPosition(10,200)
     .setSize(50,20)
     .setGroup(settings)
     ;
     
   cp5.addToggle("aligning")
     .setPosition(10,250)
     .setSize(50,20)
     .setGroup(settings)
     ;
     
   cp5.addToggle("noiseEnabled")
     .setPosition(10,300)
     .setSize(50,20)
     .setGroup(settings)
     ;
     
   cp5.addSlider("wanderMult")
     .setPosition(10,350)
     .setSize(180,16)
     .setRange(0,5)
     .setGroup(settings)
     ;
     
   cp5.addSlider("separationMult")
     .setPosition(10,370)
     .setSize(180,16)
     .setRange(0,5)
     .setGroup(settings)
     ;
     
   cp5.addSlider("cohesionMult")
     .setPosition(10,390)
     .setSize(180,16)
     .setRange(0,5)
     .setGroup(settings)
     ;
     
   cp5.addSlider("alignMult")
     .setPosition(10,410)
     .setSize(180,16)
     .setRange(0,5)
     .setGroup(settings)
     ;
   
   cp5.addSlider("noiseMult")
     .setPosition(10,430)
     .setSize(180,16)
     .setRange(0,5)
     .setGroup(settings)
     ;
     
   cp5.addSlider("maxSpeed")
     .setPosition(10,460)
     .setSize(180,16)
     .setRange(0,10)
     .setGroup(settings)
     ;
     
   cp5.addSlider("maxForce")
     .setPosition(10,480)
     .setSize(180,16)
     .setRange(0,3)
     .setGroup(settings)
     ;
     
   cp5.addBang("setDefaultFields")
     .setLabel("Reset")
     .setPosition(10,520)
     .setSize(50,20)
     .setGroup(settings)
     ;
     
   cp5.hide();
   welcomeTimer = 255;
   welcomeFading = false;
}

void draw()
{
  background(214);
  showWelcome();
  // Is the welcome text fading away?
  if (welcomeTimer > 0 && welcomeFading)
  {
    welcomeTimer --;
  }
  flock.Run();  
}

// the fist time the mouse is pressed, the menu should start fading away
// also pressing the mouse creates 10 new birds
void mousePressed()
{
  if(!welcomeFading)
  {
    welcomeFading = true;
    cp5.show();
  }
  if(!cp5.isMouseOver()) // check if were pressing a button
  {
    flock.AddAnimal(10, mouseX, mouseY);
  }
}

void showWelcome()
{
  textSize(64);
  fill(255, welcomeTimer / 1);
  text("SimulAves", width / 2, height / 2 - 100);
  textSize(32);
  text("Flocking simulation based on Boids by Craig Reynolds", width / 2, height / 2);
  text("Try clicking on the screen", width / 2, height / 2 + 50);
}

void setDefaultFields()
{
  separationDist = 25;
  cohesionDist = 50;
  alignDist = 50;
  birdSize = 5;
  wandering = true;
  separating = true;
  cohesing = true;
  aligning = true;
  noiseEnabled = true;
  maxSpeed = 3;
  maxForce = 0.2;
  wanderMult = 1;
  separationMult = 1;
  cohesionMult = 1;
  alignMult = 1;
  noiseMult = 1;
  flock.ResetToDefault(birdAmount);
}