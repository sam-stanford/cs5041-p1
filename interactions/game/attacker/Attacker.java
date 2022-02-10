package interactions.game.attacker;

import drawable.Drawable;
import interactions.game.collider.Collider;
import io.events.input.InputEvent;
import processing.core.PApplet;
import utils.Color;
import utils.Position;

public abstract class Attacker extends Collider implements Drawable {

  private Position position;
  private Position target;
  private float speed;
  private float size;
  private Color color;
  private InputEvent damagingInput;

  public Attacker(Position initialPosition, Position target, float speed, float size, Color color,
      InputEvent damagingInput) {
    this.position = initialPosition;
    this.target = target;
    this.speed = speed;
    this.size = size;
    this.color = color;
    this.damagingInput = damagingInput;
  }

  public abstract float getAttackDamageDealt();

  public abstract void damage();

  public abstract boolean isDead();

  public abstract DamageType getDamageType();

  @Override
  public void draw(PApplet app) {
    app.ellipseMode(PApplet.CENTER);
    app.fill(color.red, color.green, color.blue);
    app.circle(position.x, position.y, size);

    app.fill(255, 255, 0);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text(getDamagingInput().getDisplayValue(), position.x, position.y - 5);
  }

  public InputEvent getDamagingInput() {
    return damagingInput;
  }

  @Override
  public Position getPosition() {
    return position;
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

  public void setColor(Color c) {
    this.color = c;
  }

}
