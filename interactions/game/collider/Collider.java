package interactions.game.collider;

import utils.Position;

public abstract class Collider {

  // public abstract Position getTopLeftBounds();

  // public abstract Position getBottomRightBounds();

  // public boolean collidedWith(Collider other) {
  // return collidedWithX(other) && collidedWithY(other);
  // }

  // private boolean collidedWithX(Collider other) {
  // return collidedWithXFromLeft(other) || collidedWithXFromRight(other);
  // }

  // private boolean collidedWithXFromLeft(Collider other) {
  // // other hits left side of this
  // return this.getTopLeftBounds().x < other.getBottomRightBounds().x
  // && this.getBottomRightBounds().x > other.getBottomRightBounds().x;
  // }

  // private boolean collidedWithXFromRight(Collider other) {
  // // other hits right side of this
  // return this.getBottomRightBounds().x > other.getTopLeftBounds().x
  // && this.getTopLeftBounds().x < other.getTopLeftBounds().x;
  // }

  // private boolean collidedWithY(Collider other) {
  // return collidedWithYFromTop(other) || collidedWithYFromBottom(other);
  // }

  // private boolean collidedWithYFromTop(Collider other) {
  // // other hits top side of this
  // return this.getTopLeftBounds().y < other.getBottomRightBounds().y
  // && this.getBottomRightBounds().y > other.getBottomRightBounds().y;
  // }

  // private boolean collidedWithYFromBottom(Collider other) {
  // // other hits bottom side of this
  // return this.getBottomRightBounds().y > other.getTopLeftBounds().y
  // && this.getTopLeftBounds().y < other.getTopLeftBounds().y;
  // }

  public abstract Position getPosition();

  public abstract float getCollisionRadius();

  public boolean collidedWith(Collider other) {
    float collisionDistance = Math.max(this.getCollisionRadius(), other.getCollisionRadius());
    float currentDistance = this.getPosition().distanceTo(other.getPosition());
    return currentDistance <= collisionDistance;
  }

}
