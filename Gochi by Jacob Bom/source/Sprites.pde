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
      poof[i].resize(int(234*(1+wh/600)), 0);
    }
    
    // Main pet sprites
    for (Stage s : Stage.values()) {
      PImage tmp;
      tmp = loadImage(s.toString() + ".png");
      tmp.resize(int(wh/1.5), 0);
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
    planet.resize(int(width*1.5), 0);
    
    // Thought bubble
    thought = loadImage("thought2.png");
    thought.resize(int(width/2.3), 0);
    
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