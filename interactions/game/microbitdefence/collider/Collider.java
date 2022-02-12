package interactions.game.microbitdefence.collider;

import utils.Position;

public abstract class Collider {

  public abstract Position getPosition();

  public abstract float getCollisionRadius();

  public boolean collidedWith(Collider other) {
    float collisionDistance = Math.max(this.getCollisionRadius(), other.getCollisionRadius());
    float currentDistance = this.getPosition().distanceTo(other.getPosition());
    return currentDistance <= collisionDistance;
  }

}
