package interactions.game.microbitdefence.defence;

import config.game.microbitdefence.defence.DefenceTargetConfig;
import drawable.Drawable;
import interactions.game.microbitdefence.collider.Collider;
import processing.core.PApplet;
import processing.core.PImage;
import utils.Color;
import utils.Position;
import utils.Time;

public class DefenceTarget extends Collider implements Drawable {

  private Position centerPoint;

  private long damagedDisplayDuration;
  private long lastDamagedTime = 0;

  private PImage defaultImage;
  private PImage damagedImage;

  private float width;
  private float height;
  private Color defaultColor;
  private Color damagedColor;

  public DefenceTarget(DefenceTargetConfig config, Position centerPoint) {
    this.centerPoint = centerPoint;
    this.defaultImage = config.defaultImage;
    this.damagedImage = config.damagedImage;
    this.damagedDisplayDuration = config.damagedDisplayDuration;

    if (imagesAreValid()) {
      this.width = defaultImage.width;
      this.height = defaultImage.height;
    } else {
      this.width = config.noImageHeight;
      this.height = config.noImageWidth;
      this.defaultColor = config.noImageDefaultColor;
      this.damagedColor = config.noImageDamagedColor;
    }
  }

  private boolean imagesAreValid() {
    return defaultImage != null && damagedImage != null;
  }

  @Override
  public void draw(PApplet app) {
    if (imagesAreValid()) {
      drawAsImage(app);
    } else {
      drawAsRectangle(app);
    }
  }

  private void drawAsImage(PApplet app) {
    app.imageMode(PApplet.CORNER);
    PImage img = displayAsDamaged() ? damagedImage : defaultImage;
    app.image(img, centerPoint.x - width / 2, centerPoint.y - height / 2);
  }

  private void drawAsRectangle(PApplet app) {
    app.rectMode(PApplet.CORNER);
    Color c = displayAsDamaged() ? damagedColor : defaultColor;
    app.fill(c.blue, c.green, c.blue);
    app.rect(centerPoint.x - width / 2, centerPoint.y - height / 2, width, height);
  }

  private boolean displayAsDamaged() {
    return Time.getCurrentTimeMillis() < lastDamagedTime + damagedDisplayDuration;
  }

  @Override
  public Position getPosition() {
    return centerPoint;
  }

  @Override
  public float getCollisionRadius() {
    return width > height ? width / 1.5f : height / 1.5f;
  }

  public void damage() {
    this.lastDamagedTime = Time.getCurrentTimeMillis();
  }

}
