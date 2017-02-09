// The libraray Ani that I'm using is broken on android due to the lack of a proper registerPre
// The code below (and some bits in the main file) simulate the proper behaviour.

// workaround -------------------------------------------
void updateAnis() {
  if (anis.size() == 0) return;

  for (int i=0; i < anis.size(); i++) {
    Ani aniTmp = (Ani)anis.get(i);
    aniTmp.pre();
  }

  if (anisToUnregister.size() > 0) {
    for (int i=0; i < anisToUnregister.size(); i++) {
      anis.remove(i);
      anisToUnregister.remove(i);
      println("removed");
    }
  }
  println(anis.size());
}

void registerPre(Object obj) {
  anis.add( (Ani)obj );
}

void unregisterPre(Object obj) {
  int index = anis.indexOf(  (Ani)obj );
  anisToUnregister.add(index);
}