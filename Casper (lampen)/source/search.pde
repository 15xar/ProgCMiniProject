class search {
  //variabler
  int smallestDistance;
  int arrayPlace;
  //int listen skal kun være lige så lang som vores liste vi skal compare ("toBecompared")
  int[] distanceArray = new int[toBecompared.size()];

  //denne funktion giver det ord som er det tætteste på input ordet
  void searchCompared() {
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