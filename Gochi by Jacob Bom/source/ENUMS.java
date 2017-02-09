enum State {
  START, TUTORIAL, MAIN, GAMEOVER
}

enum Stage {
  EGG, BABY, ADULT, DEAD;
  
  // The last value is so we don't have to implement a ton of edge cases when we're dead
  // Makes animation very slow (though we do that artificially in Pet too) and the rockingStray very low
  // They can be private as they are only used locally (and the java convention is to do that)
  private int[] stepGoals = {10, 30, 50, Integer.MAX_VALUE};
  private int[] likeGoals = {1, 20, 50, Integer.MAX_VALUE};
  
  // To convert a stage into fx a filename
  // Converts EGG to Egg.
  public String toString() {
      return name().toLowerCase();
  }
  
  // Gets how many steps it took to get here
  public int prevStepGoal() {
    int goal = 0;
    // ordinal() is the numerical index of the enum (fx. ADULT's ordinal is 2)
    for (int i = 0; i<ordinal(); i++) {
      goal += stepGoals[i];
    }
    return goal;
  }
  
  // Ditto but for likeability points
  public int prevLikeGoal() {
    int goal = 0;
    for (int i = 0; i<ordinal(); i++) {
      goal += likeGoals[i];
    }
    return goal;
  }
  
  // Gets how many steps we need to advance to next stage
  public int stepGoal() {
    return stepGoals[ordinal()];
  }
  
  // Ditto but for likeability points
  public int likeGoal() {
    return likeGoals[ordinal()];
  }
}