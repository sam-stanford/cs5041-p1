package io.events.output;

// TODO: Refactor
public enum OutputEvent {
  MICROBIT_LOW_BEEP("o_l"),
  MICROBIT_HIGH_BEEP("o_h"),
  MICROBIT_DISPLAY_SAD("o_s"),
  MICROBIT_DISPLAY_HAPPY("o_p");

  private final String text;

  OutputEvent(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
