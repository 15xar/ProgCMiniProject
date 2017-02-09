import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.util.Iterator; 
import java.util.List; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SimulAves extends PApplet {

 // GUI library



/**
  Simulation of boids, originally created by Craig Reynolds in 1986.
  Most of this code is inspired by the examples in Daniel Shiffman's Nature Of Code,
  and many similarities may appear.

  Author: Pierre Bak Rouvillain 
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

public void setup()
{
  
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

public void draw()
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
public void mousePressed()
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

public void showWelcome()
{
  textSize(64);
  fill(255, welcomeTimer / 1);
  text("SimulAves", width / 2, height / 2 - 100);
  textSize(32);
  text("Flocking simulation based on Boids by Craig Reynolds", width / 2, height / 2);
  text("Try clicking on the screen", width / 2, height / 2 + 50);
}

public void setDefaultFields()
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
  maxForce = 0.2f;
  wanderMult = 1;
  separationMult = 1;
  cohesionMult = 1;
  alignMult = 1;
  noiseMult = 1;
  flock.ResetToDefault(birdAmount);
}
class Animal
{
  PVector pos; // The position of the Animal
  PVector acceleration; // The acceleration of the animal
  PVector velocity; // how far the animal will move
  float wanderAngle; // field used by wander()  
  int id;
  
  Animal(float xPos, float yPos, int id)
  {
    pos = new PVector(xPos, yPos);
    velocity = new PVector(0,0);
    acceleration = new PVector(0,0);   
    wanderAngle = 0;
    this.id = id;
  }
  
  // updates the animal
  public void Tick()
  {
    velocity.add(acceleration);
    velocity.limit(maxSpeed);
    pos.add(velocity);
    acceleration.mult(0);
    Wrap();
  }
  
  // applies a force
  public void ApplyForce(PVector force)
  {
    acceleration.add(force);
  }
  
  // makes the animal steer towards a position
  public PVector MoveTowards(PVector target)
  {
    PVector desired = PVector.sub(target,pos);
    float distance = desired.mag();
    desired.normalize();
    if(distance < alignDist)
    {
      float m = map(distance,0,alignDist,0,maxSpeed);
      desired.mult(m);
    }
    else
    {
      desired.mult(maxSpeed);
    }
    PVector steer = PVector.sub(desired,velocity);
    steer.limit(maxForce);
    return steer;
  }  
  
  // makes the animal wander around on its own in a realistic manner
  // it is controlled by an algoritm of circles. Turn on debugging
  // on the main tab to see the circles calculating the movement
  public PVector Wander()
  {
    float circleDistance = 200;             // how far away the circle is
    float circleRadius = 50;
    
    float change = 0.5f;                     // how much the angle of the direction circle can change each run 
    wanderAngle += random(-change, change); // changing the angle   
    
    // calculate where the circle is
    PVector circleCenterPos = PVector.fromAngle(velocity.heading()); 
    circleCenterPos.setMag(circleDistance);
    circleCenterPos.add(pos);
    
    float newAngle = (velocity.heading() + wanderAngle);   // calculates the new angle
    PVector radius = PVector.fromAngle(newAngle);          // make a PVector in order to calculate the target position
    radius.setMag(circleRadius);
    PVector target = PVector.add(circleCenterPos, radius); // calculate the position of the target
    
    // OPTIONAL: if debugging is turned on, this will display the circles which represent the
    // calculation
    if(debugging)
    {
      noFill();
      line(pos.x, pos.y, circleCenterPos.x, circleCenterPos.y);
      ellipse(circleCenterPos.x, circleCenterPos.y, circleRadius * 2, circleRadius * 2);
      ellipse(target.x, target.y, 10, 10);
      line(circleCenterPos.x, circleCenterPos.y, target.x, target.y);
    }   
    
    // Finally, seek towards the target
    return MoveTowards(target);
  }
  
  // If any other animals are too close, move away from that animal
  // most of this code is from Daniel Shifmann's 'Nature Of Code', chapter 6, example 6.7
  public PVector Separate(ArrayList<Animal> animals)
  {
    float desiredSeperationDistance = separationDist; 
    PVector sum = new PVector();
    int count = 0;
    for (Animal other : animals)
    {
      float d = PVector.dist(pos, other.pos);  
      if((d > 0) && (d < desiredSeperationDistance))
      {
        PVector diff = PVector.sub(pos, other.pos);
        diff.normalize();
        diff.div(d); // The closer the other animal is, the more we should steer away from it
        sum.add(diff);
        count++;
      }
    }
    if (count > 0)
    {
      // lav et gennemsnit af alle retningerne
      sum.div(count);
      sum.normalize();
      sum.mult(maxSpeed);
      sum.sub(velocity);
      sum.limit(maxForce); 
      if(debugging)
      {
        //line(pos.x, pos.y, other.pos.x, other.pos.y);
        PVector drawSum = sum.copy();
        drawSum.mult(100);
        stroke(0xffFF0000);
        line(pos.x, pos.y, pos.x + drawSum.x, pos.y + drawSum.y);
        stroke(0xff000000);
      }
    }
    return sum; 
  }
  
  // makes the animal move towards other animals within he cohesionDist
  public PVector Cohesion(ArrayList<Animal> animals)
  {
    float desiredMaxDistance = cohesionDist;   
    PVector sum = new PVector();
    int count = 0;
    for (Animal other : animals)
    {
      float distance = PVector.dist(pos, other.pos);
      if((distance > 0) && (distance > desiredMaxDistance) && (distance < alignDist)) // here is the main difference from seperation distance > desiredMaxDistanca
      {
        PVector diff = PVector.sub(other.pos, pos);
        diff.normalize();
        diff.mult(distance); // The further away the other animal is, the mere we should steer away from it
        sum.add(diff);
        count++; // increments each time another animals has been accounted for
      }
    }   
    if (count > 0)
    {
      // make an average of all the directions by dividing with the total number of animals accounted for
      sum.div(count);
      sum.normalize();
      sum.mult(maxSpeed);
      sum.sub(velocity);
      sum.limit(maxForce);
    } 
    
    return sum; 
  }
  
  // turn towards the average direction of all other animals within the alignDist
  public PVector Align(ArrayList<Animal> animals)
  {
    PVector sum = new PVector(0,0);
    int count = 0;
    for(Animal other : animals)
    {
      float d = PVector.dist(pos, other.pos);
      if ((d > 0) && (d < alignDist))
      {
        sum.add(other.velocity);
        count++;
      }
    }
    if (count > 0)
    {
      sum.div(animals.size());
      sum.setMag(maxSpeed);
      sum.sub(velocity);
      sum.limit(maxForce);
    }
    
    return sum;
  }
  
  // returns a random direction
  public PVector NoiseMove()
  {
    PVector noise = new PVector(random(2) - 1, random(2) -1);
    noise.setMag(maxSpeed);
    noise.sub(velocity);
    noise.limit(maxForce);
    return noise;
  }
  
  // returns of any other animals are within any of the given distances the bird can use.
  // this determines how far the animal can see
  public boolean Alone(ArrayList<Animal> animals)
  {
    boolean result = true;
    Iterator it = animals.iterator();
    while(it.hasNext() && result)
    {
      Animal other = (Animal)it.next();
      if (id != other.id)
      {
        float dist = PVector.dist(pos, other.pos);
        result = (dist > cohesionDist) && (dist > separationDist) && (dist > alignDist);
      }
    }
    return result;
  }
  
  // makes the animal wrap around the borders of the window.
  public void Wrap () 
  {
    pos.x = (pos.x + width) % width;
    pos.y = (pos.y + height) % height;
  }
  
  // display the animal on the screen
  // this is enterily coded by Daniel Shiffman
  public void Display() 
  {
    float theta = velocity.heading() + PI/2;
    fill(175);
    stroke(0);
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(theta);
    beginShape();
    vertex(0, -birdSize*2);
    vertex(-birdSize, birdSize*2);
    vertex(birdSize, birdSize*2);
    endShape(CLOSE);
    popMatrix();
  } 
}
class Flock
{
  ArrayList<Animal> birds;
  
  Flock(int amount)
  {
    birds = new ArrayList();
    for (int i = 0; i < amount; i++)
    {
      // when starting up, place the animals randomly on the screen
      Animal newBird = new Animal(width / 2 + i * random(100), height / 2 + i * random(100), i);
      birds.add(newBird);
    }
  }
  
  // runs through all the enabled behaveviours of all the birds, and applies them
  public void Run()
  {
    for(int i = 0; i < birds.size(); i++)
    {
      Animal thisBird = (Animal)birds.get(i);
      
      // if the bird is all alone, it should just wander around
      if(thisBird.Alone(birds) && wandering)
      {
        PVector wander = thisBird.Wander();
        wander.mult(wanderMult);
        thisBird.ApplyForce(wander);
      }
      // if not alone, apply appropiate behaviours
      else
      {
        if (separating)
        {
          PVector separate = thisBird.Separate(birds);
          separate.mult(separationMult);
          thisBird.ApplyForce(separate);
        }
        if (cohesing)
        {
          PVector cohese = thisBird.Cohesion(birds);
          cohese.mult(cohesionMult);
          thisBird.ApplyForce(cohese);
        }
        if (aligning)
        {
          PVector align = thisBird.Align(birds);
          align.mult(alignMult);
          thisBird.ApplyForce(align);
        }
        if (noiseEnabled)
        {
          PVector noise = thisBird.NoiseMove();
          noise.mult(noiseMult);
          thisBird.ApplyForce(noise);
        }
      }   
      thisBird.Tick();
      thisBird.Display();
      birds.set(i, thisBird);     
    }
  }
  
  // adds an animal to the list
  public void AddAnimal(float xPos, float yPos)
  {
    Animal newAnimal = new Animal(xPos, yPos, birds.size());
    birds.add(newAnimal);
  }
  
  // adds a number of aninals to the list
  public void AddAnimal(int amount, float xPos, float yPos)
  {
    for (int i = 0; i < amount; i++)
    {
      Animal newAnimal = new Animal(xPos, yPos, birds.size());
      birds.add(newAnimal);
    }
  }
  
  public void ResetToDefault(int amount)
  {
    if (!birds.isEmpty())
    {
    birds.clear();
    for (int i = 0; i < amount; i++)
      {
        // when starting up, place the animals randomly on the screen
        Animal newBird = new Animal(width / 2 + i * random(100), height / 2 + i * random(100), i);
        birds.add(newBird);
      }
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "SimulAves" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
