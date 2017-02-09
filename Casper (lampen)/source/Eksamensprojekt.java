import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Eksamensprojekt extends PApplet {

//libraries




Minim minim;
AudioPlayer searchSound;
AudioPlayer noText;

//variabler
String text = "";
String textDisplay = "";
int textDisplayLength;
int substring = 0;
char letter;
int justOpened = 300;
boolean keyReleased = false;
int timer = 0;


//variabler (Arrays)
String comparing ="\u00e6ble,p\u00e6re,\u00e6blejuice,kakao,oksek\u00f8d,suppe,slik,energidrik,sodavand,cola,fanta,m\u00e6lk,sm\u00f8r,banan,mango,pasta,fl\u00e5ede tomater,sandwich,drikke dunk,knive,b\u00f8rne t\u00f8j,franskbr\u00f8d";
//3D float array
float[][][] pos;

//Stringen bliver splittet ind i et Array
String[] splited = comparing.split(",");
//String arrayed bliver smidt ind i en arrayliste ved hj\u00e6lp af "import java.util.*" Hvor Array.asList bliver brugt
ArrayList<String> toBecompared = new ArrayList<String>(Arrays.asList(splited));


//Classes
layout L = new layout();
display D = new display();
displayText DT = new displayText();
itemDisplayer ID = new itemDisplayer();
search S = new search();

//Setup
public void setup() {
  
  minim = new Minim(this);
  searchSound = minim.loadFile("searchSound.wav");
  noText = minim.loadFile("0text.wav");

  pos = new float[][][] {
    //x
    {
      {width - width/12}, {width - width/15}, {width/3*2}, {width/3*2 - width/10}, {width/2 + width/10, width - width/15}, {width/2 + width/20}, {width/3, width/3 - width/75}, {width/3*2 - width/10}, {width/3*2 - width/12}, {width/3*2 - width/14}, {width/3*2 - width/8}, {width/8}, {width/5}, {width - width/30}, {width - width/20}, {width - width/3 + width/100}, {width/3*2 + width/100}, {width/2 - width/30}, {width - width/12 + width/20}, {width/3 + width/3 + width/100}, {width/15}, {width/60}
    }, 
    //y
    {
      {height - height/5.8f}, {height - height/5.8f}, {height - height/5.4f}, {height - height/5.4f}, {height/3 - height/100, height/5}, {height/3 - height/100}, {height - height/6 - height/50, height - height/3 + height/55}, {height - height/5.4f}, {height - height/5.4f}, {height - height/5.4f}, {height - height/5.4f}, {height/5.1f}, {height/5.1f}, {height - height/4}, {height - height/3}, {height - height/3}, {height - height/3 - height/10}, {height/5.1f}, {height/2}, {height/2 + height/6}, {height/2 + height/35}, {height/5}
    }
  };
}



public void draw() {
  background(255);
  D.searcher();
  S.searchCompared();
  DT.displayText();
  DT.displaySearch();
  D.indgang();
  D.frostVare();
  D.frossetKoed();
  D.frugt();
  D.emballeresFoedevarer();
  D.kurve();
  D.mejeriProdukter();
  D.delikatesser();
  D.noneFood();
  D.tekstil();
  D.udgang();
  D.slagter();
  D.koelevare();
  D.slik();
  D.broed();
  ID.displayItem();
  //Laver en tekst de f\u00f8rste 5 sekunder (300 frame / 60 frames pr. sek = 5 sek)
  if (justOpened >= 0) {
    pushStyle();
    fill(0);
    textSize(width/85);
    textAlign(CENTER, CENTER);
    text("Press 'delete' to clear all text", width/2, height/75);
    popStyle();
    justOpened--;
  }
  timer--;
}
class displayText {
  //variabler
  int textLength = 20;
  boolean textShow = true;
  int clickDelay = 120;

  //hvis alt teksten ikke kan v\u00e6re p\u00e5 sk\u00e6rmen, og der skrives videre, skal det f\u00f8rste charactor fjernes, s\u00e5 der er plads til en ny i slutningen.
  //Dog er det kun i "textDisplay" der skal fjernes den f\u00f8rste charactor
  public void displayText() {
    if (textDisplay.length() >= textLength) {
      textDisplay = textDisplay.substring(1);
      substring++;
    }

    //hvis der ikke et noget input, skrives der "Skriv noget" i input boxen
    //hvis der er et input, skal inputet st\u00e5 der i stedet for "skriv noget"
    if (textDisplay.length() == 0) {
      pushStyle();
      textAlign(CENTER, CENTER);
      textSize(width/65);
      fill(0, 0, 0, 100);
      text("Skriv noget", 0, 0, width, height/9-height/80);
      popStyle();
    } else {
      pushStyle();
      textAlign(CENTER, CENTER);
      textSize(width/65);
      fill(0);
      text(textDisplay, 0, 0, width, height/9-height/80);
      popStyle();
    }
  }

  //tjekker om dit input er mere end 2 charactors langt, f\u00f8r den kommer med forslag, derudover tjekkes der for om "text" er lig med "toBecompared.get(S.arrayPlace)", som er det ord som er forslaget
  //hvis begge de ting er opfyldt, tegnes der en box, hvor forslaget st\u00e5r i.
  public void displaySearch() {
    if (text.length() >= 2) {
      if (!text.equals(toBecompared.get(S.arrayPlace))) {
        rect(width/2-width/8, height/8-height/10 + height/20, width/8*2, height/20 );
        pushStyle();
        textAlign(CENTER, CENTER);
        textSize(width/65);
        fill(0);
        text(toBecompared.get(S.arrayPlace), 0, 0, width, height/9-height/80 + height/20*2);
        popStyle();
      }
    }
  }
}
class display {
  //disse funktioner definere hvordan butikken ser ud.
  public void searcher() {
    rect(width/2 - width/8, height/8 - height/10, width/8*2, height/20 );
    rect(0, height/6, width, 0);
  }

  public void indgang() {
    L.indgang(width - width/4, height - height/12.5f);
  }

  public void frostVare() {
    L.frostVare(width/2, height/3.5f);
    L.frostVare(width/2 + (width/20)*1, height/3.5f);
    L.frostVare(width/2 + (width/20)*2, height/3.5f);
    L.frostVare(width/2 + (width/20)*3, height/3.5f);
    L.frostVare(width/2 + (width/20)*4, height/3.5f);
    L.frostVare(width/2, height/2.6f);
    L.frostVare(width/2 + (width/20)*1, height/2.6f);
    L.frostVare(width/2 + (width/20)*2, height/2.6f);
    L.frostVare(width/2 + (width/20)*3, height/2.6f);
    L.frostVare(width/2 + (width/20)*4, height/2.6f);
  }

  public void frossetKoed() {
    L.frossetKoed(width/3, height/3.5f);
    L.frossetKoed(width/3 - (width/20)*1, height/3.5f);
    L.frossetKoed(width/3 - (width/20)*2, height/3.5f);
    L.frossetKoed(width/3 - (width/20)*3, height/3.5f);
    L.frossetKoed(width/3 - (width/20)*4, height/3.5f);
    L.frossetKoed(width/3, height/2.6f);
    L.frossetKoed(width/3 - (width/20)*1, height/2.6f);
    L.frossetKoed(width/3 - (width/20)*2, height/2.6f);
    L.frossetKoed(width/3 - (width/20)*3, height/2.6f);
    L.frossetKoed(width/3 - (width/20)*4, height/2.6f);
  }

  public void frugt () {
    L.frugtVandrette(width - width/10, height - height/5);
    L.frugtVandrette(width - width/10, height - height/5 - (height/20)*1.5f);
    L.frugtVandrette(width - width/10, height - height/5 - (height/20)*3);
    L.frugtVandrette(width - width/10, height - height/5 - (height/20)*4.5f);
  }

  public void emballeresFoedevarer() {
    L.emballeresFoedevarerVandrette(width/3 + width/3, height - height/5);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - width/13, height - height/5);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - (width/13)*2, height - height/5);
    L.emballeresFoedevarerVandrette(width/3 + width/3, height - height/4 - height/40);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - width/13, height - height/4 - height/40);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - (width/13)*2, height - height/4 - height/40);
    L.emballeresFoedevarerLodrette(width/3 + width/3 + width/16, height - height/4 - height/40 - height/5.75f);
    L.emballeresFoedevarerLodrette(width/3 + width/3, height - height/4 - height/40 - height/5.75f);
    L.emballeresFoedevarerLodrette(width/3 + width/3 - width/13, height - height/4 - height/40 - height/5.75f);
    L.emballeresFoedevarerLodrette(width/3 + width/3 - (width/13)*2, height - height/4 - height/40 - height/5.75f);
  }

  public void kurve() {
    L.kurve(width/30, height - height/6);
    L.kurve(width/30, height - height/6 - (height/20));
    L.kurve(width/30, height - height/6 - (height/20)*2);
  }

  public void mejeriProdukter() { 
    L.mejeriProdukter(width/30 + width/20, height/5.9f);
    L.mejeriProdukter(width/30 + (width/20)*2, height/5.9f);
    L.mejeriProdukter(width/30 + (width/20)*3, height/5.9f);
    L.mejeriProdukter(width/30 + (width/20)*4, height/5.9f);
    L.mejeriProdukter(width/30 + (width/20)*5, height/5.9f);
    L.mejeriProdukter(width/30 + (width/20)*6, height/5.9f);
  }

  public void delikatesser() {
    L.delikatesser(width/120 + width/30 + (width/20)*7, height/5.9f);
    L.delikatesser(width/120 + width/30 + (width/20)*8, height/5.9f);
  }

  public void noneFood() {
    L.noneFoodLodrette(width - width/10, height/2 -height/10);
    L.noneFoodLodrette(width - width/25, height/2 -height/10);
    L.noneFoodVandrette(width/2 - width/16, height - height/5);
    L.noneFoodVandrette(width/2 - width/16 - (width/13), height - height/5);
    L.noneFoodVandrette(width/2 - width/16, height - height/4 - height/40);
    L.noneFoodVandrette(width/2 - width/16 - (width/13), height - height/4 - height/40);
    L.noneFoodVandrette(width/2 - width/16 - (width/13), height - height/4 - height/31 - height/20);
    L.noneFoodVandrette(width/2 - width/16 - (width/13), height/2 + (height/18)*2);
    L.noneFoodVandrette(width/2 - width/16 - (width/13)*2, height/2 + (height/18)*2);
    L.noneFoodVandrette(width/2 - width/16, height/2 + height/20);
    L.noneFoodVandrette(width/2 - width/16 - (width/13), height/2 + height/20);
    L.noneFoodVandrette(width/2 - width/16 - (width/13)*2, height/2 + height/20);
    L.noneFoodLodrette(width/2 - width/16 + (width/13) - width/65, height/2 + height/20);
  }

  public void tekstil() {
    L.tekstilVandrette(width/50, height - height/3);
    L.tekstilVandrette(width/50, height - height/3 - (height/20));
    L.tekstilVandrette(width/50, height - height/3 - (height/20)*2);
    L.tekstilVandrette(width/50, height - height/3 - (height/20)*3);
  }

  public void udgang() {
    L.udgang(width/3.25f, height - height/12.5f);
    L.udgang(width/3.25f - width/12, height - height/12.5f);
    L.udgang(width/3.25f - (width/12)*2, height - height/12.5f);
  }

  public void slagter() {
    L.slagter(width - width/8 - width/59, height/5.9f);
    L.slagter(width - width/8 - width/59 + width/20, height/5.9f);
  }

  public void koelevare() {
    L.koelevare(width/2, height/5.9f);
    L.koelevare(width/2 + (width/20), height/5.9f);
    L.koelevare(width/2 + (width/20)*2, height/5.9f);
    L.koelevare(width/2 + (width/20)*3, height/5.9f);
    L.koelevare(width/2 + (width/20)*4, height/5.9f);
    L.koelevare(width/2 + (width/20)*5, height/5.9f);
    L.koelevare(width/2 + (width/20)*6, height/5.9f);
  }

  public void slik() {
    L.slikVandrette(width/2 - width/16 - (width/13)*2, height - height/5);
    L.slikVandrette(width/2 - width/16 - (width/13)*2, height - height/4 - height/40);
    L.slikVandrette(width/2 - width/16 - (width/13)*2, height - height/4 - height/31 - height/20);
  }
  public void broed() {
    L.broed((width/width)*3, height/5.9f);
    L.broed((width/width)*3, height/5.9f + (height/15)*1);
    L.broed((width/width)*3, height/5.9f + (height/15)*2);
    L.broed((width/width)*3, height/5.9f + (height/15)*3);
  }
}
class itemDisplayer {
  boolean firstMatch = true;
  //denne funktion tegner den ellipse, der hvor varen ligger
  public void displayItem() {
    for (int i = 0; i < toBecompared.size(); i++) {
      if (text.equals(toBecompared.get(i))) {
        for (int z = 0; z < pos[0][i].length; z++) {
          pushStyle();
          fill(40, 255, 40);
          ellipse(pos[0][i][z], pos[1][i][z], width/100, width/100);
          popStyle();
          if (searchSound.isPlaying() == false && firstMatch == true) {
            searchSound.rewind();  
            searchSound.play();
            firstMatch = false;
          }
        }
      }
    }
  }
}
public void keyPressed() {

  //tester for hvilke keys der bliver klikket p\u00e5 
  //tester for om der kan slettes, skrives mere, eller om der ikke er plads i string (max 300), s\u00e5 skal den enten slette, eller s\u00e5 skal den adde mere til stringen, eller s\u00e5 skal der ikke ades mer til stringen

  if (keyCode == BACKSPACE) {
    if (textDisplay.length() > 0) {
      textDisplay = textDisplay.substring(0, textDisplay.length()-1);
      if (substring >= 1) {
        textDisplayLength = text.length()-DT.textLength;
        if (text.length()-DT.textLength >= 0) {
          letter = text.charAt(textDisplayLength);
          textDisplay = letter + textDisplay;
        }
      }
    } else if (timer <= 0) {
      noText.rewind();
      noText.play();
      timer = 60;
    }
    if (text.length() > 0) {
      text = text.substring(0, text.length()-1);
    }

    //tester for om "DELETE" bliver klikket p\u00e5, og hvis "DELETE" bliver klikket p\u00e5, skal alt tekst fjernes i "textDisplay" og i "text"
  } else if (keyCode == DELETE) {
    textDisplay = "";
    text = "";
  } else if (keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
    textDisplay = textDisplay + key;
    text = text + key;
    ID.firstMatch = true;

    //checker at Stringen ikke er over 300 charactors lang, for hvis den er det skal det sidste input ikke skrives
    if (text.length() >= 300) {
      text = text.substring(0, text.length()-1);
      textDisplay = textDisplay.substring(0, textDisplay.length()-1);
    }
    //checker om der bliver klikket p\u00e5 "ENTER", hvis der g\u00f8r skal den fjerne "ENTER" s\u00e5 der ikke bliver linje skift
    //hvis "ENTER" er klikket p\u00e5, og forslaget bliver vist ("text.length() >= 2"), skal den s\u00e6tte det forsl\u00e5et ord ind p\u00e5 "text"'s plads og ind p\u00e5 "textDisplay"'s plads
  }
  if (keyCode == ENTER) { 
    textDisplay = textDisplay.substring(0, textDisplay.length()-1);
    text = text.substring(0, text.length()-1);
    if (text.length() >= 2) {
      text = toBecompared.get(S.arrayPlace);
      textDisplay = toBecompared.get(S.arrayPlace);
    }
  }
}

//checker for om der bliver klikket p\u00e5 det forsl\u00e5et ord, og checker for om det forsl\u00e5et ord bliver vist ("text.length() >= 2"), og hvis der g\u00f8r, skal det forsl\u00e5et ord blive til "tekst" og "textDisplay"
public void mousePressed () {
  if (mouseX >= width/2-width/8 && mouseX <= width/2-width/8 + width/8*2  && mouseY >= height/8-height/10 + height/20  && mouseY <= height/8-height/10 + height/20 + height/20) {
    if (text.length() >= 2) {
      text = toBecompared.get(S.arrayPlace);
      textDisplay = text;
    }
  }
}
class layout {
  //Disse funktioner definere hvordan de enkelte elementer ser ud
  public void kasse(float xPos, float yPos) {
    pushStyle();
    popStyle();
  }

  public void indgang(float xPos, float yPos) {
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

  public void frostVare(float xPos, float yPos) {
    float frostVareWidth = width/20;
    float frostVareHeight = height/20;
    pushStyle();
    noStroke();
    fill(0, 255, 255);
    rect(xPos, yPos, frostVareWidth, frostVareHeight);
    popStyle();
  }

  public void frossetKoed(float xPos, float yPos) {
    float frossetKoedWidth = width/20;
    float frossetKoedHeight = height/20;
    pushStyle();
    noStroke();
    fill(206, 100, 60);
    rect(xPos, yPos, frossetKoedWidth, frossetKoedHeight);
    popStyle();
  }

  public void frugtLodrette(float xPos, float yPos) {
    float frugtWidth = width/50;
    float frugtHeight = height/9;
    pushStyle();
    noStroke();
    fill(0, 255, 0);
    rect(xPos, yPos, frugtWidth, frugtHeight);
    popStyle();
  }
  public void frugtVandrette(float xPos, float yPos) {
    float frugtWidth = width/13;
    float frugtHeight = height/25;
    pushStyle();
    noStroke();
    fill(0, 255, 0);
    rect(xPos, yPos, frugtWidth, frugtHeight);
    popStyle();
  }

  public void emballeresFoedevarerLodrette(float xPos, float yPos) {
    float emballeresFoedevarerWidth = width/65;
    float emballeresFoedevarerHeight = height/7;
    pushStyle();
    noStroke();
    fill(255, 255, 0);
    rect(xPos, yPos, emballeresFoedevarerWidth, emballeresFoedevarerHeight);
    popStyle();
  }

  public void emballeresFoedevarerVandrette(float xPos, float yPos) {
    float emballeresFoedevarerWidth = width/13;
    float emballeresFoedevarerHeight = height/40;
    pushStyle();
    noStroke();
    fill(255, 255, 0);
    rect(xPos, yPos, emballeresFoedevarerWidth, emballeresFoedevarerHeight);
    popStyle();
  }

  public void kurve(float xPos, float yPos) {
    float kurveWidth = width/20;
    float kurveHeight = height/20;
    pushStyle();
    noStroke();
    fill(255, 0, 255);
    rect(xPos, yPos, kurveWidth, kurveHeight);
    popStyle();
  }

  public void mejeriProdukter(float xPos, float yPos) {
    float mejeriProdukterWidth = width/20;
    float mejeriProdukterHeight = height/20;
    pushStyle();
    noStroke();
    fill(0, 255, 100);
    rect(xPos, yPos, mejeriProdukterWidth, mejeriProdukterHeight);
    popStyle();
  }

  public void koelevare(float xPos, float yPos) {
    float koelevareWidth = width/20;
    float koelevareHeight = height/20;
    pushStyle();
    noStroke();
    fill(150, 0, 255);
    rect(xPos, yPos, koelevareWidth, koelevareHeight);
    popStyle();
  }

  public void delikatesser(float xPos, float yPos) {
    float DelikatesserWidth = width/20;
    float DelikatesserHeight = height/20;
    pushStyle();
    noStroke();
    fill(255, 150, 0);
    rect(xPos, yPos, DelikatesserWidth, DelikatesserHeight);
    popStyle();
  }


  public void noneFoodLodrette(float xPos, float yPos) {
    float noneFoodWidth = width/65;
    float noneFoodHeight = height/7;
    pushStyle();
    noStroke();
    fill(160, 210, 100);
    rect(xPos, yPos, noneFoodWidth, noneFoodHeight);
    popStyle();
  }

  public void noneFoodVandrette(float xPos, float yPos) {
    float noneFoodWidth = width/13;
    float noneFoodHeight = height/40;
    pushStyle();
    noStroke();
    fill(160, 210, 100);
    rect(xPos, yPos, noneFoodWidth, noneFoodHeight);
    popStyle();
  }

  public void slikLodrette(float xPos, float yPos) {
    float slikWidth = width/65;
    float slikHeight = height/7;
    pushStyle();
    noStroke();
    fill(215, 160, 250);
    rect(xPos, yPos, slikWidth, slikHeight);
    popStyle();
  }

  public void slikVandrette(float xPos, float yPos) {
    float slikWidth = width/13;
    float slikHeight = height/40;
    pushStyle();
    noStroke();
    fill(215, 160, 250);
    rect(xPos, yPos, slikWidth, slikHeight);
    popStyle();
  }

  public void broed(float xPos, float yPos) {
    float broedWidth = width/30;
    float broedHeight = height/15;
    pushStyle();
    noStroke();
    fill(160, 240, 61);
    rect(xPos, yPos, broedWidth, broedHeight);
    popStyle();
  }



  public void tekstilLodrette(float xPos, float yPos) {
    float tekstilWidth = width/65;
    float tekstilHeight = height/7;
    pushStyle();
    noStroke();
    fill(100, 0, 255);
    rect(xPos, yPos, tekstilWidth, tekstilHeight);
    popStyle();
  }

  public void tekstilVandrette(float xPos, float yPos) {
    float tekstilWidth = width/13;
    float tekstilHeight = height/40;
    pushStyle();
    noStroke();
    fill(100, 0, 255);
    rect(xPos, yPos, tekstilWidth, tekstilHeight);
    popStyle();
  }

  public void udgang(float xPos, float yPos) {
    float udgangsWidth = width/100;
    float udgangsHeight = height/17;
    pushStyle();
    noStroke();
    fill(255, 0, 120);
    rect(xPos, yPos, udgangsWidth, udgangsHeight);
    rect(xPos - udgangsWidth*3, yPos, udgangsWidth, udgangsHeight);
    popStyle();
  }

  public void slagter(float xPos, float yPos) {
    float slagterWidth = width/20;
    float slagterHeight = height/20;
    pushStyle();
    noStroke();
    fill(255, 0, 0);
    rect(xPos, yPos, slagterWidth, slagterHeight);
    popStyle();
  }
}
class search {
  //variabler
  int smallestDistance;
  int arrayPlace;
  //int listen skal kun v\u00e6re lige s\u00e5 lang som vores liste vi skal compare ("toBecompared")
  int[] distanceArray = new int[toBecompared.size()];

  //denne funktion giver det ord som er det t\u00e6tteste p\u00e5 input ordet
  public void searchCompared() {
    try {
      for (int w = 0; w <= toBecompared.size(); w++) {
        //inputter vores "text" input og vores compare liste
        int distance = LevenshteinDistance.computeDistance(text.toLowerCase(), toBecompared.get(w));
        distanceArray[w] = distance;
      }
    }
    catch(Exception e) {
    }
    try {
      smallestDistance = min(distanceArray);
      for (int q = 0; q <= toBecompared.size(); q++) {
        if (smallestDistance == distanceArray[q]) {
          arrayPlace = q;
        }
      }
    }
    catch(Exception e) {
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#00FF00", "--stop-color=#FF0000", "Eksamensprojekt" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
