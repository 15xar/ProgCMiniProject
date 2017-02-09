/*
  Notes:
 Orrery model of the solar system Heliocentric 
 Sources:
 https://en.wikipedia.org/wiki/Solar_System
 http://mrfeinberg.com/peasycam/
 */
// library by Jonathan Feinberg - http://mrfeinberg.com/peasycam/
import peasy.*;

void setup() 
{
  fullScreen(P3D);
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

void draw() 
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

void keyReleased() 
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
void mousePressed()
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