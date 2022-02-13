package interactions.game.microbitdefence.target;

import config.game.microbitdefence.defence.DefenceTargetConfig;
import drawable.Drawable;
import images.microbit.MicrobitColor;
import images.microbit.MicrobitImage;
import images.microbit.MicrobitState;
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
  private int health;

  private PImage defaultImage;
  private PImage damagedImage;
  private MicrobitColor imageColor;
  private boolean haveTriedToLoadImages = false;

  private float width;
  private float height;
  private Color defaultColor;
  private Color damagedColor;

  public DefenceTarget(DefenceTargetConfig config, Position centerPoint, MicrobitColor color) {
    this.centerPoint = centerPoint;
    this.damagedDisplayDuration = config.damagedDisplayDuration;
    this.health = config.initialHealth;
    this.imageColor = color;
    this.width = config.noImageHeight;
    this.height = config.noImageWidth;
    this.defaultColor = config.noImageDefaultColor;
    this.damagedColor = config.noImageDamagedColor;
  }

  private boolean imagesAreValid() {
    return defaultImage != null && damagedImage != null;
  }

  @Override
  public void draw(PApplet app) {
    loadImagesIfNotLoaded(app);
    if (imagesAreValid()) {
      drawAsImage(app);
    } else {
      drawAsRectangle(app);
    }
  }

  private void loadImagesIfNotLoaded(PApplet app) {
    if (haveTriedToLoadImages) {
      return;
    }
    try {
      this.defaultImage = getDefaultImage(app);
      this.damagedImage = getDamagedImage(app);
      this.width = defaultImage.width;
      this.height = defaultImage.height;
    } catch (Exception e) {
      // Ignore
    }
    haveTriedToLoadImages = true;
  }

  private PImage getDamagedImage(PApplet app) {
    MicrobitImage img = new MicrobitImage(this.imageColor, MicrobitState.SAD);
    return img.toProcessingImage(app);
  }

  private PImage getDefaultImage(PApplet app) {
    MicrobitImage img = new MicrobitImage(this.imageColor, MicrobitState.HAPPY);
    return img.toProcessingImage(app);
  }

  private void drawAsImage(PApplet app) {
    app.imageMode(PApplet.CORNER);
    PImage img = shouldDisplayAsDamaged() ? damagedImage : defaultImage;
    app.image(img, centerPoint.x - width / 2, centerPoint.y - height / 2);
  }

  private void drawAsRectangle(PApplet app) {
    app.rectMode(PApplet.CORNER);
    Color c = shouldDisplayAsDamaged() ? damagedColor : defaultColor;
    app.fill(c.blue, c.green, c.blue);
    app.rect(centerPoint.x - width / 2, centerPoint.y - height / 2, width, height);
  }

  private boolean shouldDisplayAsDamaged() {
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

  public void damage(int damageToDeal) {
    this.lastDamagedTime = Time.getCurrentTimeMillis();
    this.health -= damageToDeal;
  }

  public int getHealth() {
    return health;
  }

  public boolean isDestroyed() {
    return health <= 0;
  }

}
