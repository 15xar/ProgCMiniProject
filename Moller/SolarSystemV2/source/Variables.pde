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