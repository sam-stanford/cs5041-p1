package interactions.game.hideandseek;

import config.game.hideandseek.HideAndSeekConfig;
import interactions.Interaction;
import interactions.InteractionType;
import processing.core.PApplet;
import utils.Time;

public class HideCountdown implements Interaction {

  private long startTime;

  private long duration;
  private int digitToDisplayUpTo;

  public HideCountdown(HideAndSeekConfig config) {
    this.duration = config.duration;
    this.digitToDisplayUpTo = config.digitToDisplayUpTo;

    this.startTime = Time.getCurrentTimeMillis();
  }

  @Override
  public void draw(PApplet app) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isDone() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public InteractionType getType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    // TODO Auto-generated method stub

  }

}
