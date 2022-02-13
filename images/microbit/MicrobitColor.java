package images.microbit;

public enum MicrobitColor {
  BLUE("blue"),
  YELLOW("yellow"),
  GREEN("green"),
  RED("red");

  private String value;

  private MicrobitColor(final String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
