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
  void Tick()
  {
    velocity.add(acceleration);
    velocity.limit(maxSpeed);
    pos.add(velocity);
    acceleration.mult(0);
    Wrap();
  }
  
  // applies a force
  void ApplyForce(PVector force)
  {
    acceleration.add(force);
  }
  
  // makes the animal steer towards a position
  PVector MoveTowards(PVector target)
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
  PVector Wander()
  {
    float circleDistance = 200;             // how far away the circle is
    float circleRadius = 50;
    
    float change = 0.5;                     // how much the angle of the direction circle can change each run 
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
  PVector Separate(ArrayList<Animal> animals)
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
        stroke(#FF0000);
        line(pos.x, pos.y, pos.x + drawSum.x, pos.y + drawSum.y);
        stroke(#000000);
      }
    }
    return sum; 
  }
  
  // makes the animal move towards other animals within he cohesionDist
  PVector Cohesion(ArrayList<Animal> animals)
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
  PVector Align(ArrayList<Animal> animals)
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
  PVector NoiseMove()
  {
    PVector noise = new PVector(random(2) - 1, random(2) -1);
    noise.setMag(maxSpeed);
    noise.sub(velocity);
    noise.limit(maxForce);
    return noise;
  }
  
  // returns of any other animals are within any of the given distances the bird can use.
  // this determines how far the animal can see
  boolean Alone(ArrayList<Animal> animals)
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
  void Wrap () 
  {
    pos.x = (pos.x + width) % width;
    pos.y = (pos.y + height) % height;
  }
  
  // display the animal on the screen
  // this is enterily coded by Daniel Shiffman
  void Display() 
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