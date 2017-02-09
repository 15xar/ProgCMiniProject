class Planets 
{
  float radius, orbitSpeed, rotationSpeed, distance;
  String name;
  PImage texture;
  PShape planet;
  color c;
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

  void moons(String name, float size, float distance) 
  {
  }

  void display() 
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

  void printName() 
  {
    pushStyle();
    stroke(2);
    pushMatrix();
    //rotates the names with camera rotations
    rotateX(cameraRotations[0]);
    //rotateY(-angle);
    rotateY(cameraRotations[1]);
    rotateZ(cameraRotations[2]);
    translate(0, 0 - (radius + (radius * 0.4)));
    textSize(constrain(radius * 4, 5, 40));
    textAlign(CENTER);
    fill(255);
    text(name, 0, 0, 0);
    popMatrix();
    popStyle();
  }
  
  void drawOrbit() 
  {
    pushMatrix();
    pushStyle();
    rotateX(PI/2);
    strokeWeight(0.8);
    stroke(255, 60);
    noFill();
    ellipse(0, 0, distance*2, distance*2);
    popStyle();
    popMatrix();
  }
}