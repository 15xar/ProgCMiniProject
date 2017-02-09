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
  
  void loadSizes(){
  //startScreen
  startScreenX = width/5;
  startScreenY = height/5;
  startScreenWidth = width/1.5;
  startScreenHeight = height/2;
  //startButton
  startButtonWidth = startScreenWidth;
  startButtonHeight = startScreenHeight/5;
  startButtonX = startScreenX;
  startButtonY = startScreenY + (startButtonHeight * 4);
  //settingsButton
  settingsButtonWidth = width/19.5;
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