class display {
  //disse funktioner definere hvordan butikken ser ud.
  void searcher() {
    rect(width/2 - width/8, height/8 - height/10, width/8*2, height/20 );
    rect(0, height/6, width, 0);
  }

  void indgang() {
    L.indgang(width - width/4, height - height/12.5);
  }

  void frostVare() {
    L.frostVare(width/2, height/3.5);
    L.frostVare(width/2 + (width/20)*1, height/3.5);
    L.frostVare(width/2 + (width/20)*2, height/3.5);
    L.frostVare(width/2 + (width/20)*3, height/3.5);
    L.frostVare(width/2 + (width/20)*4, height/3.5);
    L.frostVare(width/2, height/2.6);
    L.frostVare(width/2 + (width/20)*1, height/2.6);
    L.frostVare(width/2 + (width/20)*2, height/2.6);
    L.frostVare(width/2 + (width/20)*3, height/2.6);
    L.frostVare(width/2 + (width/20)*4, height/2.6);
  }

  void frossetKoed() {
    L.frossetKoed(width/3, height/3.5);
    L.frossetKoed(width/3 - (width/20)*1, height/3.5);
    L.frossetKoed(width/3 - (width/20)*2, height/3.5);
    L.frossetKoed(width/3 - (width/20)*3, height/3.5);
    L.frossetKoed(width/3 - (width/20)*4, height/3.5);
    L.frossetKoed(width/3, height/2.6);
    L.frossetKoed(width/3 - (width/20)*1, height/2.6);
    L.frossetKoed(width/3 - (width/20)*2, height/2.6);
    L.frossetKoed(width/3 - (width/20)*3, height/2.6);
    L.frossetKoed(width/3 - (width/20)*4, height/2.6);
  }

  void frugt () {
    L.frugtVandrette(width - width/10, height - height/5);
    L.frugtVandrette(width - width/10, height - height/5 - (height/20)*1.5);
    L.frugtVandrette(width - width/10, height - height/5 - (height/20)*3);
    L.frugtVandrette(width - width/10, height - height/5 - (height/20)*4.5);
  }

  void emballeresFoedevarer() {
    L.emballeresFoedevarerVandrette(width/3 + width/3, height - height/5);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - width/13, height - height/5);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - (width/13)*2, height - height/5);
    L.emballeresFoedevarerVandrette(width/3 + width/3, height - height/4 - height/40);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - width/13, height - height/4 - height/40);
    L.emballeresFoedevarerVandrette(width/3 + width/3 - (width/13)*2, height - height/4 - height/40);
    L.emballeresFoedevarerLodrette(width/3 + width/3 + width/16, height - height/4 - height/40 - height/5.75);
    L.emballeresFoedevarerLodrette(width/3 + width/3, height - height/4 - height/40 - height/5.75);
    L.emballeresFoedevarerLodrette(width/3 + width/3 - width/13, height - height/4 - height/40 - height/5.75);
    L.emballeresFoedevarerLodrette(width/3 + width/3 - (width/13)*2, height - height/4 - height/40 - height/5.75);
  }

  void kurve() {
    L.kurve(width/30, height - height/6);
    L.kurve(width/30, height - height/6 - (height/20));
    L.kurve(width/30, height - height/6 - (height/20)*2);
  }

  void mejeriProdukter() { 
    L.mejeriProdukter(width/30 + width/20, height/5.9);
    L.mejeriProdukter(width/30 + (width/20)*2, height/5.9);
    L.mejeriProdukter(width/30 + (width/20)*3, height/5.9);
    L.mejeriProdukter(width/30 + (width/20)*4, height/5.9);
    L.mejeriProdukter(width/30 + (width/20)*5, height/5.9);
    L.mejeriProdukter(width/30 + (width/20)*6, height/5.9);
  }

  void delikatesser() {
    L.delikatesser(width/120 + width/30 + (width/20)*7, height/5.9);
    L.delikatesser(width/120 + width/30 + (width/20)*8, height/5.9);
  }

  void noneFood() {
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

  void tekstil() {
    L.tekstilVandrette(width/50, height - height/3);
    L.tekstilVandrette(width/50, height - height/3 - (height/20));
    L.tekstilVandrette(width/50, height - height/3 - (height/20)*2);
    L.tekstilVandrette(width/50, height - height/3 - (height/20)*3);
  }

  void udgang() {
    L.udgang(width/3.25, height - height/12.5);
    L.udgang(width/3.25 - width/12, height - height/12.5);
    L.udgang(width/3.25 - (width/12)*2, height - height/12.5);
  }

  void slagter() {
    L.slagter(width - width/8 - width/59, height/5.9);
    L.slagter(width - width/8 - width/59 + width/20, height/5.9);
  }

  void koelevare() {
    L.koelevare(width/2, height/5.9);
    L.koelevare(width/2 + (width/20), height/5.9);
    L.koelevare(width/2 + (width/20)*2, height/5.9);
    L.koelevare(width/2 + (width/20)*3, height/5.9);
    L.koelevare(width/2 + (width/20)*4, height/5.9);
    L.koelevare(width/2 + (width/20)*5, height/5.9);
    L.koelevare(width/2 + (width/20)*6, height/5.9);
  }

  void slik() {
    L.slikVandrette(width/2 - width/16 - (width/13)*2, height - height/5);
    L.slikVandrette(width/2 - width/16 - (width/13)*2, height - height/4 - height/40);
    L.slikVandrette(width/2 - width/16 - (width/13)*2, height - height/4 - height/31 - height/20);
  }
  void broed() {
    L.broed((width/width)*3, height/5.9);
    L.broed((width/width)*3, height/5.9 + (height/15)*1);
    L.broed((width/width)*3, height/5.9 + (height/15)*2);
    L.broed((width/width)*3, height/5.9 + (height/15)*3);
  }
}