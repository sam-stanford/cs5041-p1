package io.events;

// TODO: Refactor
public enum OutputEvent {
  MICROBIT_SHORT_BEEP("o_bs"),
  MICROBIT_LONG_BEEP("o_bl"),
  MICROBIT_DISPLAY_SAD("o_ds"),
  MICROBIT_DISPLAY_HAPPY("o_dh");

  private final String text;

  OutputEvent(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
