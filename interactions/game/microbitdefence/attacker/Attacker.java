package interactions.game.microbitdefence.attacker;

import config.game.microbitdefence.attacker.SingleAttackerConfig;
import drawable.Drawable;
import interactions.game.microbitdefence.collider.Collider;
import io.events.InputEvent;
import processing.core.PApplet;
import utils.Color;
import utils.Position;
import utils.Time;

public class Attacker extends Collider implements Drawable {

  private int health;
  private long lastDamagedTime;
  private Position position;

  private Position target;
  private float speed;
  private float size;
  private long damagedDisplayDuration;
  private InputEvent damagingInput;
  private DamageType damageDealt;

  private Color defaultFillColor;
  private Color defaultTextColor;
  private Color damagedFillColor;
  private Color damagedTextColor;

  public Attacker(SingleAttackerConfig config, Position initialPosition, Position target, InputEvent damagingInput) {
    this.position = initialPosition;
    this.target = target;
    this.speed = config.speed;
    this.size = config.size;
    this.defaultFillColor = config.defaultFillColor;
    this.defaultTextColor = config.defaultTextColor;
    this.damagedFillColor = config.damagedFillColor;
    this.damagedTextColor = config.damagedTextColor;
    this.damagingInput = damagingInput;
    this.damageDealt = config.damageDealt;
    this.damagedDisplayDuration = config.damagedDisplayDuration;
    this.health = config.health;
  }

  @Override
  public void draw(PApplet app) {
    Color fillColor = displayAsDamaged() ? damagedFillColor : defaultFillColor;
    Color textColor = displayAsDamaged() ? damagedTextColor : defaultTextColor;

    app.ellipseMode(PApplet.CENTER);
    app.fill(fillColor.red, fillColor.green, fillColor.blue);
    app.circle(position.x, position.y, size);

    app.fill(textColor.red, textColor.green, textColor.blue);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text(getDamagingInput().toDisplayValue(), position.x, position.y - 5);
  }

  private boolean displayAsDamaged() {
    return Time.getCurrentTimeMillis() < lastDamagedTime + damagedDisplayDuration;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  public DamageType getDamageDealt() {
    return damageDealt;
  }

  public void damage() {
    lastDamagedTime = Time.getCurrentTimeMillis();
    health -= 1;
  }

  public float getCollisionRadius() {
    return size / 2;
  }

  public boolean isDead() {
    return health <= 0;
  }

  public DamageType getDamageType() {
    return DamageType.SMALL;
  }

  public InputEvent getDamagingInput() {
    return damagingInput;
  }

  public void updatePosition() {
    float remainingX = target.x - position.x;
    float remainingY = target.y - position.y;
    float absRemainingX = Math.abs(remainingX);
    float absRemainingY = Math.abs(remainingY);

    float deltaX, deltaY;
    if (Math.abs(remainingX) > Math.abs(remainingY)) {
      deltaX = speed;
      deltaY = speed * (absRemainingY / absRemainingX);
    } else {
      deltaX = speed * (absRemainingX / absRemainingY);
      deltaY = speed;
    }

    deltaX = remainingX > 0 ? deltaX : -deltaX;
    deltaY = remainingY > 0 ? deltaY : -deltaY;

    this.position = new Position(position.x + deltaX, position.y + deltaY);
  }
}
