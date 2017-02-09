void keyPressed() {

  //tester for hvilke keys der bliver klikket på 
  //tester for om der kan slettes, skrives mere, eller om der ikke er plads i string (max 300), så skal den enten slette, eller så skal den adde mere til stringen, eller så skal der ikke ades mer til stringen

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

    //tester for om "DELETE" bliver klikket på, og hvis "DELETE" bliver klikket på, skal alt tekst fjernes i "textDisplay" og i "text"
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
    //checker om der bliver klikket på "ENTER", hvis der gør skal den fjerne "ENTER" så der ikke bliver linje skift
    //hvis "ENTER" er klikket på, og forslaget bliver vist ("text.length() >= 2"), skal den sætte det forslået ord ind på "text"'s plads og ind på "textDisplay"'s plads
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

//checker for om der bliver klikket på det forslået ord, og checker for om det forslået ord bliver vist ("text.length() >= 2"), og hvis der gør, skal det forslået ord blive til "tekst" og "textDisplay"
void mousePressed () {
  if (mouseX >= width/2-width/8 && mouseX <= width/2-width/8 + width/8*2  && mouseY >= height/8-height/10 + height/20  && mouseY <= height/8-height/10 + height/20 + height/20) {
    if (text.length() >= 2) {
      text = toBecompared.get(S.arrayPlace);
      textDisplay = text;
    }
  }
}