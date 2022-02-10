package utils;

public class Position {
  public float x;
  public float y;

  public Position(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float distanceTo(Position other) {
    float xDistance = this.x - other.x;
    float yDistance = this.y - other.y;
    return (float) Math.sqrt(square(xDistance) + square(yDistance));
  }

  private double square(float a) {
    return Math.pow(a, 2);
  }
}
