class displayText {
  //variabler
  int textLength = 20;
  boolean textShow = true;
  int clickDelay = 120;

  //hvis alt teksten ikke kan være på skærmen, og der skrives videre, skal det første charactor fjernes, så der er plads til en ny i slutningen.
  //Dog er det kun i "textDisplay" der skal fjernes den første charactor
  void displayText() {
    if (textDisplay.length() >= textLength) {
      textDisplay = textDisplay.substring(1);
      substring++;
    }

    //hvis der ikke et noget input, skrives der "Skriv noget" i input boxen
    //hvis der er et input, skal inputet stå der i stedet for "skriv noget"
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

  //tjekker om dit input er mere end 2 charactors langt, før den kommer med forslag, derudover tjekkes der for om "text" er lig med "toBecompared.get(S.arrayPlace)", som er det ord som er forslaget
  //hvis begge de ting er opfyldt, tegnes der en box, hvor forslaget står i.
  void displaySearch() {
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