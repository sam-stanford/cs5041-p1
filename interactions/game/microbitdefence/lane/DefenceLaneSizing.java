package interactions.game.microbitdefence.lane;

import utils.Position;

public class DefenceLaneSizing {
  private float leftBoundaryX;
  private float width;
  private float height;

  public DefenceLaneSizing(float leftBoundaryX, float width, float height) {
    this.leftBoundaryX = leftBoundaryX;
    this.width = width;
    this.height = height;
  }

  public float getLeftBoundaryX() {
    return leftBoundaryX;
  }

  public float getRightBoundaryX() {
    return leftBoundaryX + width;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public float getCenterX() {
    return leftBoundaryX + (width / 2);
  }

  public float getCenterY() {
    return height / 2;
  }

  public Position getCenter() {
    return new Position(getCenterX(), getCenterY());
  }

}
