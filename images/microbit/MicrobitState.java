package images.microbit;

public enum MicrobitState {
  SAD("sad"),
  HAPPY("happy");

  private String value;

  private MicrobitState(final String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

}
