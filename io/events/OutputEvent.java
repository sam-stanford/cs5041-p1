package io.events;

public enum OutputEvent {
  MICROBIT_SHORT_BEEP("bs"),
  MICROBIT_LONG_BEEP("bl"),
  MICROBIT_DISPLAY_SAD("ds"),
  MICROBIT_DISPLAY_HAPPY("dh"),
  UNRECOGNISED("");

  private final String eventValue;

  OutputEvent(final String eventValue) {
    this.eventValue = eventValue;
  }

  public String toEventValue() {
    return eventValue;
  }

  public boolean isValid() {
    return this != UNRECOGNISED;
  }

  public static OutputEvent fromEventValue(String eventValue) {
    switch (eventValue) {
      case "bs":
        return MICROBIT_SHORT_BEEP;
      case "bl":
        return MICROBIT_LONG_BEEP;
      case "ds":
        return MICROBIT_DISPLAY_SAD;
      case "dh":
        return MICROBIT_DISPLAY_HAPPY;
      default:
        return UNRECOGNISED;
    }
  }

}
