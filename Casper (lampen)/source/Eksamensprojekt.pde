//libraries
import ddf.minim.*;
import java.util.*;


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
String comparing ="æble,pære,æblejuice,kakao,oksekød,suppe,slik,energidrik,sodavand,cola,fanta,mælk,smør,banan,mango,pasta,flåede tomater,sandwich,drikke dunk,knive,børne tøj,franskbrød";
//3D float array
float[][][] pos;

//Stringen bliver splittet ind i et Array
String[] splited = comparing.split(",");
//String arrayed bliver smidt ind i en arrayliste ved hjælp af "import java.util.*" Hvor Array.asList bliver brugt
ArrayList<String> toBecompared = new ArrayList<String>(Arrays.asList(splited));


//Classes
layout L = new layout();
display D = new display();
displayText DT = new displayText();
itemDisplayer ID = new itemDisplayer();
search S = new search();

//Setup
void setup() {
  fullScreen();
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
      {height - height/5.8}, {height - height/5.8}, {height - height/5.4}, {height - height/5.4}, {height/3 - height/100, height/5}, {height/3 - height/100}, {height - height/6 - height/50, height - height/3 + height/55}, {height - height/5.4}, {height - height/5.4}, {height - height/5.4}, {height - height/5.4}, {height/5.1}, {height/5.1}, {height - height/4}, {height - height/3}, {height - height/3}, {height - height/3 - height/10}, {height/5.1}, {height/2}, {height/2 + height/6}, {height/2 + height/35}, {height/5}
    }
  };
}



void draw() {
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
  //Laver en tekst de første 5 sekunder (300 frame / 60 frames pr. sek = 5 sek)
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