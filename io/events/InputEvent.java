package io.events;

public enum InputEvent {
  KEYBOARD_F("F", "k_f"),
  KEYBOARD_J("J", "k_j"),

  MICROBIT_LEFT_BTN("Left\nButton", "m_lb"),
  MICROBIT_RIGHT_BTN("Right\nButton", "m_rb"),
  MICROBIT_SHAKE("Shake", "m_s"),
  MICROBIT_TILT_LEFT("Tilt\nLeft", "m_tl"),
  MICROBIT_TILT_RIGHT("Tilt\nRight", "m_tr"),
  MICROBIT_MICROPHONE("Shout", "m_m"),
  MICROBIT_FLIP("Flip", "m_f"),

  UNRECOGNISED("ERROR", "ERROR");

  private final String displayValue;
  private final String eventValue;

  InputEvent(final String displayValue, final String eventValue) {
    this.displayValue = displayValue;
    this.eventValue = eventValue;
  }

  public String toDisplayValue() {
    return displayValue;
  }

  public String toEventValue() {
    return eventValue;
  }

  public boolean isValid() {
    return this != UNRECOGNISED;
  }

  public static InputEvent fromEventValue(String eventValue) {
    switch (eventValue) {
      case "k_f":
        return InputEvent.KEYBOARD_F;
      case "k_j":
        return InputEvent.KEYBOARD_J;
      case "m_lb":
        return InputEvent.MICROBIT_LEFT_BTN;
      case "m_rb":
        return InputEvent.MICROBIT_RIGHT_BTN;
      case "m_s":
        return InputEvent.MICROBIT_SHAKE;
      case "m_tl":
        return InputEvent.MICROBIT_TILT_LEFT;
      case "m_tr":
        return InputEvent.MICROBIT_TILT_RIGHT;
      case "m_f":
        return InputEvent.MICROBIT_FLIP;
      case "m_m":
        return InputEvent.MICROBIT_MICROPHONE;
      default:
        return InputEvent.UNRECOGNISED;
    }
  }

}
