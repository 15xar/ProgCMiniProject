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
  void Run()
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
  void AddAnimal(float xPos, float yPos)
  {
    Animal newAnimal = new Animal(xPos, yPos, birds.size());
    birds.add(newAnimal);
  }
  
  // adds a number of aninals to the list
  void AddAnimal(int amount, float xPos, float yPos)
  {
    for (int i = 0; i < amount; i++)
    {
      Animal newAnimal = new Animal(xPos, yPos, birds.size());
      birds.add(newAnimal);
    }
  }
  
  void ResetToDefault(int amount)
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