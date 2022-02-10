package interactions;

import processing.core.PApplet;

public class EndScreen implements Interaction {

  @Override
  public void draw(PApplet app) {
    // TODO
  }

  @Override
  public boolean isDone() {
    return false;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.END_SCREEN;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    return;
  }
}