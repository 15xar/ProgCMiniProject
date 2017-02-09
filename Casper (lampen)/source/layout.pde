class layout {
  //Disse funktioner definere hvordan de enkelte elementer ser ud
  void kasse(float xPos, float yPos) {
    pushStyle();
    popStyle();
  }

  void indgang(float xPos, float yPos) {
    float indgangslength = width/8;
    float indgangsWidth = width/100;
    float indgangsHighestHeight = height/20;
    float indgangsSmallestHeigth = height/50;
    pushStyle();
    noStroke();
    fill(0, 0, 255);
    rect(xPos, yPos, indgangsWidth, indgangsHighestHeight);
    rect(xPos, yPos + indgangsHighestHeight - indgangsHighestHeight/indgangsHighestHeight, indgangslength, indgangsSmallestHeigth);
    rect(xPos +  indgangslength - indgangsWidth, yPos, indgangsWidth, indgangsHighestHeight);
    popStyle();
  }

  void frostVare(float xPos, float yPos) {
    float frostVareWidth = width/20;
    float frostVareHeight = height/20;
    pushStyle();
    noStroke();
    fill(0, 255, 255);
    rect(xPos, yPos, frostVareWidth, frostVareHeight);
    popStyle();
  }

  void frossetKoed(float xPos, float yPos) {
    float frossetKoedWidth = width/20;
    float frossetKoedHeight = height/20;
    pushStyle();
    noStroke();
    fill(206, 100, 60);
    rect(xPos, yPos, frossetKoedWidth, frossetKoedHeight);
    popStyle();
  }

  void frugtLodrette(float xPos, float yPos) {
    float frugtWidth = width/50;
    float frugtHeight = height/9;
    pushStyle();
    noStroke();
    fill(0, 255, 0);
    rect(xPos, yPos, frugtWidth, frugtHeight);
    popStyle();
  }
  void frugtVandrette(float xPos, float yPos) {
    float frugtWidth = width/13;
    float frugtHeight = height/25;
    pushStyle();
    noStroke();
    fill(0, 255, 0);
    rect(xPos, yPos, frugtWidth, frugtHeight);
    popStyle();
  }

  void emballeresFoedevarerLodrette(float xPos, float yPos) {
    float emballeresFoedevarerWidth = width/65;
    float emballeresFoedevarerHeight = height/7;
    pushStyle();
    noStroke();
    fill(255, 255, 0);
    rect(xPos, yPos, emballeresFoedevarerWidth, emballeresFoedevarerHeight);
    popStyle();
  }

  void emballeresFoedevarerVandrette(float xPos, float yPos) {
    float emballeresFoedevarerWidth = width/13;
    float emballeresFoedevarerHeight = height/40;
    pushStyle();
    noStroke();
    fill(255, 255, 0);
    rect(xPos, yPos, emballeresFoedevarerWidth, emballeresFoedevarerHeight);
    popStyle();
  }

  void kurve(float xPos, float yPos) {
    float kurveWidth = width/20;
    float kurveHeight = height/20;
    pushStyle();
    noStroke();
    fill(255, 0, 255);
    rect(xPos, yPos, kurveWidth, kurveHeight);
    popStyle();
  }

  void mejeriProdukter(float xPos, float yPos) {
    float mejeriProdukterWidth = width/20;
    float mejeriProdukterHeight = height/20;
    pushStyle();
    noStroke();
    fill(0, 255, 100);
    rect(xPos, yPos, mejeriProdukterWidth, mejeriProdukterHeight);
    popStyle();
  }

  void koelevare(float xPos, float yPos) {
    float koelevareWidth = width/20;
    float koelevareHeight = height/20;
    pushStyle();
    noStroke();
    fill(150, 0, 255);
    rect(xPos, yPos, koelevareWidth, koelevareHeight);
    popStyle();
  }

  void delikatesser(float xPos, float yPos) {
    float DelikatesserWidth = width/20;
    float DelikatesserHeight = height/20;
    pushStyle();
    noStroke();
    fill(255, 150, 0);
    rect(xPos, yPos, DelikatesserWidth, DelikatesserHeight);
    popStyle();
  }


  void noneFoodLodrette(float xPos, float yPos) {
    float noneFoodWidth = width/65;
    float noneFoodHeight = height/7;
    pushStyle();
    noStroke();
    fill(160, 210, 100);
    rect(xPos, yPos, noneFoodWidth, noneFoodHeight);
    popStyle();
  }

  void noneFoodVandrette(float xPos, float yPos) {
    float noneFoodWidth = width/13;
    float noneFoodHeight = height/40;
    pushStyle();
    noStroke();
    fill(160, 210, 100);
    rect(xPos, yPos, noneFoodWidth, noneFoodHeight);
    popStyle();
  }

  void slikLodrette(float xPos, float yPos) {
    float slikWidth = width/65;
    float slikHeight = height/7;
    pushStyle();
    noStroke();
    fill(215, 160, 250);
    rect(xPos, yPos, slikWidth, slikHeight);
    popStyle();
  }

  void slikVandrette(float xPos, float yPos) {
    float slikWidth = width/13;
    float slikHeight = height/40;
    pushStyle();
    noStroke();
    fill(215, 160, 250);
    rect(xPos, yPos, slikWidth, slikHeight);
    popStyle();
  }

  void broed(float xPos, float yPos) {
    float broedWidth = width/30;
    float broedHeight = height/15;
    pushStyle();
    noStroke();
    fill(160, 240, 61);
    rect(xPos, yPos, broedWidth, broedHeight);
    popStyle();
  }



  void tekstilLodrette(float xPos, float yPos) {
    float tekstilWidth = width/65;
    float tekstilHeight = height/7;
    pushStyle();
    noStroke();
    fill(100, 0, 255);
    rect(xPos, yPos, tekstilWidth, tekstilHeight);
    popStyle();
  }

  void tekstilVandrette(float xPos, float yPos) {
    float tekstilWidth = width/13;
    float tekstilHeight = height/40;
    pushStyle();
    noStroke();
    fill(100, 0, 255);
    rect(xPos, yPos, tekstilWidth, tekstilHeight);
    popStyle();
  }

  void udgang(float xPos, float yPos) {
    float udgangsWidth = width/100;
    float udgangsHeight = height/17;
    pushStyle();
    noStroke();
    fill(255, 0, 120);
    rect(xPos, yPos, udgangsWidth, udgangsHeight);
    rect(xPos - udgangsWidth*3, yPos, udgangsWidth, udgangsHeight);
    popStyle();
  }

  void slagter(float xPos, float yPos) {
    float slagterWidth = width/20;
    float slagterHeight = height/20;
    pushStyle();
    noStroke();
    fill(255, 0, 0);
    rect(xPos, yPos, slagterWidth, slagterHeight);
    popStyle();
  }
}