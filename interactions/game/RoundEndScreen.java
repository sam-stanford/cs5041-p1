package interactions.game;

import config.display.DisplayConfig;
import interactions.Interaction;
import interactions.InteractionType;
import processing.core.PApplet;
import utils.Color;
import utils.Time;

public class RoundEndScreen implements Interaction {

  private String winnerName;
  private DisplayConfig displayConfig;
  private long startTime;
  private long duration;

  public RoundEndScreen(DisplayConfig displayConfig, long duration, String winnerName) {
    this.displayConfig = displayConfig;
    this.winnerName = winnerName;
    this.duration = duration;
    this.startTime = Time.getCurrentTimeMillis();
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
    app.text(getDisplayText(), displayConfig.width / 2, displayConfig.height / 2);
  }

  private String getDisplayText() {
    return winnerName + " Wins the Round";
  }

  @Override
  public boolean isDone() {
    long now = Time.getCurrentTimeMillis();
    return now > startTime + duration;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.ROUND_END_SCREEN;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    return; // Ignore
  }

}
