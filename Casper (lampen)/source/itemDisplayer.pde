class itemDisplayer {
  boolean firstMatch = true;
  //denne funktion tegner den ellipse, der hvor varen ligger
  void displayItem() {
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