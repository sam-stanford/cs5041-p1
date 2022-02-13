package io.events;

public enum OutputEvent {
  MICROBIT_SHORT_BEEP("m_bs"),
  MICROBIT_LONG_BEEP("m_bl"),
  MICROBIT_DISPLAY_SAD("m_ds"),
  MICROBIT_DISPLAY_HAPPY("m_dh"),
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
      case "m_bs":
        return MICROBIT_SHORT_BEEP;
      case "m_bl":
        return MICROBIT_LONG_BEEP;
      case "m_ds":
        return MICROBIT_DISPLAY_SAD;
      case "m_dh":
        return MICROBIT_DISPLAY_HAPPY;
      default:
        return UNRECOGNISED;
    }
  }

}
