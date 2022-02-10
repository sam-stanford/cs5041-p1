package interactions.game;

import drawable.Drawable;
import interactions.game.collider.Collider;
import processing.core.PApplet;
import utils.Color;
import utils.Position;

// TODO: Rename
public class DefenceBase extends Collider implements Drawable {

  private Position centerPoint;

  private float width = 300;
  private float height = 300;
  private Color color = new Color(40);

  public DefenceBase(Position centerPoint) {
    this.centerPoint = centerPoint;
  }

  // TODO: replace with image
  @Override
  public void draw(PApplet app) {
    app.rectMode(PApplet.CORNER);
    app.fill(color.blue, color.green, color.blue);
    app.rect(centerPoint.x - width / 2, centerPoint.y - height / 2, width, height);
  }

  @Override
  public Position getPosition() {
    return centerPoint;
  }

  @Override
  public float getCollisionRadius() {
    return width > height ? width / 1.5f : height / 1.5f;
  }
}
