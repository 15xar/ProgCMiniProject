// Makes a non-modal JOptionPane messagebox
void showPopup(Object msg, String title, int messageType) {
  // --- PC ONLY ---
  // Make the pane
  JOptionPane pane = new JOptionPane(msg, messageType);
  // Make the pane create the dialog for us
  JDialog dialog = pane.createDialog(null, title);
  // Tweak it to be non-modal
  // The reason we want this, is that if it was modal it would halt processings draw thread,
  // which means you see nothing but a white screen while the popup is open
  dialog.setModal(false);
  // Display it
  dialog.show();
  // Put it on top of processings own window
  dialog.setVisible(true);
  // --- END PC ONLY
}

// Copied from previous project
// YAY code reuse
void heart(float x, float y, float size, boolean half) {
  pushMatrix();
  translate(x-50*size, y-20*size);
  beginShape();
  if (!half) {
    vertex(50*size, 15*size);
    bezierVertex(50*size, -5*size, 90*size, 5*size, 50*size, 40*size); // Right
  }
  vertex(50*size, 15*size);
  bezierVertex(50*size, -5*size, 10*size, 5*size, 50*size, 40*size); // Left
  endShape();
  popMatrix();
}