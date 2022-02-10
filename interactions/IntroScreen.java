package interactions;

import processing.core.PApplet;

public class IntroScreen implements Interaction {
  private long creationTime;

  public IntroScreen() {
    creationTime = System.currentTimeMillis();
  }

  @Override
  public void draw(PApplet app) {
    app.background(250);
    app.fill(210);
    app.circle(200, 200, 200);
  }

  @Override
  public boolean isDone() {
    // return System.currentTimeMillis() - creationTime > 5000;
    return System.currentTimeMillis() - creationTime > 1;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.INTRO_SCREEN;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    return;
  }
}