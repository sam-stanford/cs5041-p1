package interactions;

import config.display.DisplayConfig;
import processing.core.PApplet;
import utils.Color;

public class EndScreen implements Interaction {

  private String text;
  private DisplayConfig displayConfig;

  public EndScreen(DisplayConfig displayConfig, String textToDisplay) {
    this.displayConfig = displayConfig;
    this.text = textToDisplay;
  }

  @Override
  public void draw(PApplet app) {
    setBackground(app);
    drawText(app);
  }

  private void setBackground(PApplet app) {
    Color c = displayConfig.backgroundColor;
    app.background(c.red, c.green, c.blue);
  }

  private void drawText(PApplet app) {
    Color c = displayConfig.defaultTextColor;
    app.fill(c.red, c.green, c.blue);
    app.textSize(displayConfig.bigTextSize);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text(text, displayConfig.width / 2, displayConfig.height / 2);
  }

  @Override
  public boolean isDone() {
    return true;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.END_SCREEN;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    return; // Ignore
  }

}