import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Solar_System_V2 extends PApplet {

/*
  Notes:
 Orrery model of the solar system Heliocentric 
 Sources:
 https://en.wikipedia.org/wiki/Solar_System
 http://mrfeinberg.com/peasycam/
 */
// library by Jonathan Feinberg - http://mrfeinberg.com/peasycam/


public void setup() 
{
  
  //size(900, 600, P3D);
  frameRate(60);
  surface.setTitle("Solar System V2");
  loadImages(imageQual);
  loadSizes();

  // planets initializing
  // new Planets(name, radius, distance, texture, orbitSpeed, rotationSpeed, startingAngle);
  sun = new Planets("Sun", sunRadius, 0, sunMesh, 0, 0, 0, 0);
  mercury = new Planets("Mercury", mercuryRadius, mercuryDistance, mercuryMesh, mercurySpeed, mercuryRotation, 0, mercuryInclination);
  venus = new Planets("Venus", venusRadius, venusDistance, venusMesh, venusSpeed, venusRotation, 0, venusInclination);
  earth = new Planets("Earth", earthRadius, earthDistance, earthMesh, earthSpeed, earthRotation, 0, earthInclination);
  mars = new Planets("Mars", marsRadius, marsDistance, marsMesh, marsSpeed, marsRotation, 0, marsInclination);
  jupiter = new Planets("Jupiter", jupiterRadius, jupiterDistance, jupiterMesh, jupiterSpeed, jupiterRotation, 0, jupiterInclination);
  saturn = new Planets("Saturn", saturnRadius, saturnDistance, saturnMesh, saturnSpeed, saturnRotation, 0, saturnInclination);
  uranus = new Planets("Uranus", uranusRadius, uranusDistance, uranusMesh, uranusSpeed, uranusRotation, 0, uranusInclination);
  neptune = new Planets("Neptune", neptuneRadius, neptuneDistance, neptuneMesh, neptuneSpeed, neptuneRotation, 0, neptuneInclination);

  // adding objects to arraylist (im excluding the sun to make lighting works)
  planets.add(mercury);
  planets.add(venus);
  planets.add(earth);
  planets.add(mars);
  planets.add(jupiter);
  planets.add(saturn);
  planets.add(uranus);
  planets.add(neptune);

  // using peasycam library to get a controlled camera
  cam = new PeasyCam(this, 750);
  cam.setMinimumDistance(camMinimum);
  cam.setMaximumDistance(camMaximum);
  cam.setSuppressRollRotationMode();
  cam.setCenterDragHandler(null);
  cam.setResetOnDoubleClick(false);

  // Menu initializing
  ui = new UI();
}

public void draw() 
{
  //Using its rotations to place name of planets
  cameraRotations = cam.getRotations();

  //First resizing the background so it fits all resolutions then applying it
  background.resize(width, height);
  background(background);

  // make you able to change image quality
  if (imageQual != imageQualTemp)
  {
    loadImages(imageQual);
    imageQualTemp = imageQual;
  }

  //Drawing sun before light cause otherwise the inside of the sun would light up
  sun.display();

  //Lights (r, g, b, x, y, z) it shines outwards
  pointLight(255, 255, 255, 0, 0, 0);

  //Enhanced for loop to go through all planets created
  for (Planets planet : planets) 
  {
    planet.display();
  }

  //DEBUGGING
  if (debug)
  {
    debug();
  }
  //Menu for information about the planets
  ui.controller(startScreen, showUI);
}

public void keyReleased() 
{
  if (!startScreen) {
    if (key == 't' || key == 'T') 
    {
      drawNames = !drawNames;
    }
    if (key == ' ') 
    {
      cam.reset();
    }
    if (key == 'f' || key == 'F') 
    {
      drawOrbit = !drawOrbit;
    }
    if (key == '+' || key == '?')
    {
      debug = !debug;
    }
    if (key == 'z' || key == 'Z')
    {
      showUI = !showUI;
    }
  }
}
public void mousePressed()
{
  if (showUI)
  {
    if (mouseX > settingsButtonX && mouseX < settingsButtonX + settingsButtonWidth && mouseY > settingsButtonY)
    {
      disableCam();
      settingsBox = !settingsBox;
    }
    if(settingsBox){
      if(mouseX > escSettingsBoxX && mouseX < escSettingsBoxX + escSettingsBoxWidth && mouseY > escSettingsBoxY){
      settingsBox = !settingsBox;
      enableCam();
      }
    }
  }

  if (startScreen && mouseX > startButtonX && mouseX < startButtonX + startButtonWidth && mouseY > startButtonY && mouseY < startButtonY + startButtonHeight)
  {
    startScreen = !startScreen;
    enableCam();
  }
}
  //startScreen
  float startScreenX;
  float startScreenY;
  float startScreenWidth;
  float startScreenHeight;
  //startButton
  float startButtonWidth;
  float startButtonHeight;
  float startButtonX;
  float startButtonY;
  //settingsButton
  float settingsButtonWidth;
  float settingsButtonHeight;
  float settingsButtonX;
  float settingsButtonY;
  //settingsBox
  float settingsBoxX;
  float settingsBoxY;
  float settingsBoxWidth;
  float settingsBoxHeight;
    //close settingsBox
    float escSettingsBoxX;
    float escSettingsBoxY;
    float escSettingsBoxWidth;
    float escSettingsBoxHeight;
  
  public void loadSizes(){
  //startScreen
  startScreenX = width/5;
  startScreenY = height/5;
  startScreenWidth = width/1.5f;
  startScreenHeight = height/2;
  //startButton
  startButtonWidth = startScreenWidth;
  startButtonHeight = startScreenHeight/5;
  startButtonX = startScreenX;
  startButtonY = startScreenY + (startButtonHeight * 4);
  //settingsButton
  settingsButtonWidth = width/19.5f;
  settingsButtonHeight = settingsButtonWidth;
  settingsButtonX = 0;
  settingsButtonY = height-settingsButtonHeight;
  //settingsBox
  settingsBoxX = width/5;
  settingsBoxY = height/5;
  settingsBoxWidth = width - settingsBoxX*2;
  settingsBoxHeight = height - settingsBoxY*2;
    // close settingsBox
    escSettingsBoxWidth = settingsBoxWidth/13;
    escSettingsBoxHeight = escSettingsBoxWidth;
    escSettingsBoxX = (settingsBoxX + settingsBoxWidth) - escSettingsBoxWidth;
    escSettingsBoxY = settingsBoxY;
  }
// loads images with the fitting quality
public void loadImages(int imageQual) 
{
  
  //Low - Standard
  if(imageQual == 1)
  {
  background = loadImage("low/background.jpg");
  sunMesh = loadImage("low/sun_texture.jpg");
  mercuryMesh = loadImage("low/mercury_texture.png");
  venusMesh = loadImage("low/venus_texture.jpg");
  earthMesh = loadImage("low/earth_texture.jpg");
  marsMesh = loadImage("low/mars_texture.jpg");
  jupiterMesh = loadImage("low/jupiter_texture.jpg");
  saturnMesh = loadImage("low/saturn_texture.jpg");
  uranusMesh = loadImage("low/uranus_texture.jpg");
  neptuneMesh = loadImage("low/neptune_texture.jpg");
  }
  
  //Medium
  if(imageQual == 2)
  {
  background = loadImage("medium/background.jpg");
  sunMesh = loadImage("medium/sun_texture.jpg");
  mercuryMesh = loadImage("medium/mercury_texture.png");
  venusMesh = loadImage("medium/venus_texture.jpg");
  earthMesh = loadImage("medium/earth_texture.jpg");
  marsMesh = loadImage("medium/mars_texture.jpg");
  jupiterMesh = loadImage("medium/jupiter_texture.jpg");
  saturnMesh = loadImage("medium/saturn_texture.jpg");
  uranusMesh = loadImage("medium/uranus_texture.jpg");
  neptuneMesh = loadImage("medium/neptune_texture.jpg");
  }
  
  //High
  if(imageQual == 3)
  {
  background = loadImage("high/background.jpg");
  sunMesh = loadImage("high/sun_texture.jpg");
  mercuryMesh = loadImage("high/mercury_texture.png");
  venusMesh = loadImage("high/venus_texture.jpg");
  earthMesh = loadImage("high/earth_texture.jpg");
  marsMesh = loadImage("high/mars_texture.jpg");
  jupiterMesh = loadImage("high/jupiter_texture.jpg");
  saturnMesh = loadImage("high/saturn_texture.jpg");
  uranusMesh = loadImage("high/uranus_texture.jpg");
  neptuneMesh = loadImage("high/neptune_texture.jpg");
  }
  
  //Utility
  settingsButton = loadImage("utility/SettingsButton.png");
  escape = loadImage("utility/EscapeButton.png");
}

public void debug() 
{
  pushStyle();
  stroke(200);
  //X-axis
  line(0, 0, 0, debugDistance, 0, 0);
  line(0, 0, 0, -debugDistance, 0, 0);
  //Y-axis
  line(0, 0, 0, 0, debugDistance, 0);
  line(0, 0, 0, 0, -debugDistance, 0);
  //Z-axis
  line(0, 0, 0, 0, 0, debugDistance);
  line(0, 0, 0, 0, 0, -debugDistance);
  popStyle();
  
  //Information about how the program is running and the camera
  println(frameRate);
  println(cam.getDistance());
  println(cameraRotations);
}
// homemade functions for enabling and disabling the camera
public void disableCam()
{
cam.setActive(false);
}
public void enableCam()
{
cam.setActive(true);
}

public boolean hover(float x1, float y1, float x2, float y2){
  if(mouseX > x1 && mouseX < x2 + x1 && mouseY > y1 && mouseY < y2 + y1)
  {
  return true;
  }
  else{
  return false;
  }
}
class UI 
{

  public void controller(boolean startScreen, boolean showUI)
  {
    // "makes it 2D" draw things that you want relative to the camera's position and orientation
    cam.beginHUD();
    pushStyle();
    // design of HUD
    strokeJoin(ROUND);
    strokeWeight(3);
    stroke(20);
    fill(10);
    if (startScreen)
    {
      startScreen();
    } else
    {
      if (showUI)
      {
        if (settingsBox)
        {
          settingsBox();
          escSettingsBox();
        } else
        {
          planetSelect();
          settingsButton();
        }
      }
    }
    popStyle();
    cam.endHUD();
  }
  // the welcome menu
  public void startScreen()
  {
    disableCam();
    rect(startScreenX, startScreenY, startScreenWidth, startScreenHeight);
    pushStyle();
    fill(255);
    text("Welcome to a simulation of our solar system",startScreenX, startScreenY, startScreenWidth, startScreenHeight-startButtonHeight);
    popStyle();
    startButton();
  }
  // the button to start the simulation
  public void startButton() 
  {
    pushStyle();
    if (hover(startButtonX, startButtonY, startButtonWidth, startButtonHeight)) {
      fill(180);
    } else 
    {
      fill(90);
    }
    rect(startButtonX, startButtonY, startButtonWidth, startButtonHeight);
    pushStyle();
    fill(255);
    text("Press this button to start the simulation!",startButtonX, startButtonY, startButtonWidth, startButtonHeight);
    popStyle();
    popStyle();
  }

  // choose what planet you want to look at (yet to get implemented)
  public void planetSelect() 
  {
    rect(0, 0, width/8, height/4);
  }
  
  // the settings menu
  public void settingsBox()
  {
    rect(settingsBoxX, settingsBoxY, settingsBoxWidth, settingsBoxHeight);
  }
  
  // used for closing the settings
  public void escSettingsBox()
  {
    rect(escSettingsBoxX, escSettingsBoxY, escSettingsBoxWidth, escSettingsBoxHeight);
    image(escape, escSettingsBoxX, escSettingsBoxY, escSettingsBoxWidth, escSettingsBoxHeight);
  }
  
  // used for getting into the settings
  public void settingsButton()
  {
    if(hover(settingsButtonX, settingsButtonY, settingsButtonWidth, settingsButtonHeight)){
    pushStyle();
    fill(60);
    rect(settingsButtonX, settingsButtonY, settingsButtonWidth, settingsButtonHeight);
    popStyle();
    }else{
    rect(settingsButtonX, settingsButtonY, settingsButtonWidth, settingsButtonHeight);
    }
    pushStyle();
    fill(255);
    image(settingsButton, settingsButtonX, settingsButtonY, settingsButtonWidth, settingsButtonHeight);
    popStyle();
  }
}
// Planet sizes
float earthRadius = 6.378f; // original st\u00f8rrelse = 6.378, noget km
float sunRadius = earthRadius * 13; // eneste der ikke er akkurat og bruger jorden til at definere de andre
float mercuryRadius = earthRadius * 0.383f;
float venusRadius = earthRadius * 0.95f;
float marsRadius = earthRadius * 0.532f;
float jupiterRadius = earthRadius * 11;
float saturnRadius = earthRadius * 9;
float uranusRadius = earthRadius * 4;
float neptuneRadius = earthRadius * 3.86f;

// Planet distance
// AU = Astronomical Unit = 149,597,870,700 m ~ 149,597,870.7 km  https://en.wikipedia.org/wiki/Astronomical_unit
// using their Semi-Major axis (the medium of their orbits)
// Dividing AU with scale to make it fit the window
float distanceScale = 660000;
float AU = 149597870.7f / distanceScale;
float mercuryDistance = AU * 0.387f;
float venusDistance = AU * 0.723f;
float earthDistance = AU * 1;
float marsDistance = AU * 1.52f;
float jupiterDistance = AU * 5.2f;
float saturnDistance = AU * 10;
float uranusDistance = AU * 20.1f;
float neptuneDistance = AU * 30.33f;

// Planets inclination to Sun's equator in degrees (hvor meget den h\u00e6lder til siden) (BRUG DET HER TIL H\u00c6LDNING!!!)
float mercuryInclination = 3.38f;
float venusInclination = 3.86f;
float earthInclination = 7.155f;
float marsInclination = 5.65f;
float jupiterInclination = 6.09f;
float saturnInclination = 5.51f;
float uranusInclination = 6.48f;
float neptuneInclination = 6.43f;

// Orbit speed ( earth's averag orbital speed = 29.78 km/s) (find more precise numbers!!)
float mercurySpeed = 0.0075f;
float venusSpeed = 0.006f;
float earthSpeed = 0.005f;
float marsSpeed = 0.004f;
float jupiterSpeed = 0.002f;
float saturnSpeed =  0.00165f;
float uranusSpeed = 0.00125f;
float neptuneSpeed = 0.001f;

// Rotation speed (find precise numbers!!)
float mercuryRotation = 0.005f;
float venusRotation = 0.005f;
float earthRotation = 0.05f;
float marsRotation = 0.0075f;
float jupiterRotation = 0.02f;
float saturnRotation = 0.002f;
float uranusRotation = 0.002f;
float neptuneRotation = 0.002f;
class Planets 
{
  float radius, orbitSpeed, rotationSpeed, distance;
  String name;
  PImage texture;
  PShape planet;
  int c;
  float angle;
  float rotation = 0;
  float inclination;


  Planets(String name, float radius, float distance, PImage texture, float orbitSpeed, float rotationSpeed, float startAngle, float inclination) 
  {
    this.texture = texture;
    this.orbitSpeed = orbitSpeed;
    this.rotationSpeed = rotationSpeed;
    this.name = name;
    this.distance = distance;
    this.radius = radius;
    this.angle = 0 + startAngle;
    this.inclination = inclination;
  }

  public void moons(String name, float size, float distance) 
  {
  }

  public void display() 
  {
    // find a way to make speed a multiplier for angle
    // the angle of the orbit
    if (angle < TWO_PI*2) {
      pushMatrix();
      beginShape();
      noStroke();
      planet = createShape(SPHERE, radius);
      planet.setTexture(texture);
      endShape();
      rotateY(angle);
      translate(distance, 0);
      // the planets own rotation
      if (rotation < TWO_PI*2) 
      {
        pushMatrix();
        rotateY(rotation);
        shape(planet, 0, 0);
        popMatrix();
        rotation += rotationSpeed;
      } else 
      {
        rotation = 0;
      }
      //drawing the planets name
      if (drawNames) 
      {
        printName();
      }
      //drawing a planets moon(s)
      //moons();
      popMatrix();
      angle+= orbitSpeed;
    } else 
    {
      angle = 0;
    }
    if (drawOrbit) 
    {
      drawOrbit();
    }
  }

  public void printName() 
  {
    pushStyle();
    stroke(2);
    pushMatrix();
    //rotates the names with camera rotations
    rotateX(cameraRotations[0]);
    //rotateY(-angle);
    rotateY(cameraRotations[1]);
    rotateZ(cameraRotations[2]);
    translate(0, 0 - (radius + (radius * 0.4f)));
    textSize(constrain(radius * 4, 5, 40));
    textAlign(CENTER);
    fill(255);
    text(name, 0, 0, 0);
    popMatrix();
    popStyle();
  }
  
  public void drawOrbit() 
  {
    pushMatrix();
    pushStyle();
    rotateX(PI/2);
    strokeWeight(0.8f);
    stroke(255, 60);
    noFill();
    ellipse(0, 0, distance*2, distance*2);
    popStyle();
    popMatrix();
  }
}
// My array of planets
ArrayList<Planets> planets = new ArrayList<Planets>();

// Planets
Planets sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune;

// Textures
PImage background, sunMesh, mercuryMesh, venusMesh, earthMesh, marsMesh, jupiterMesh, saturnMesh, uranusMesh, neptuneMesh, settingsButton, escape;

// Classes
PeasyCam cam;
UI ui;

// Global Variables
float camMinimum = 200;
float camMaximum = 10000;
boolean drawNames = false;
boolean drawOrbit = false;
boolean tiltTrue = false;
boolean debug = false;
boolean startScreen = true;
boolean showUI = true;
boolean settingsBox = false;
float[] cameraRotations;
float debugDistance = 3000;
int imageQual = 3;
int imageQualTemp = imageQual;
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Solar_System_V2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
