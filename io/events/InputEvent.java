package io.events;

import io.devices.input.InputDeviceType;

public enum InputEvent {
  KEYBOARD_SPACEBAR("Space"),
  KEYBOARD_F("F"),
  KEYBOARD_J("J"),

  MICROBIT_LEFT_BTN("Left\nButton"),
  MICROBIT_RIGHT_BTN("Right\nButton"),
  MICROBIT_SHAKE("Shake"),
  MICROBIT_TILT_LEFT("Tilt\nLeft"),
  MICROBIT_TILT_RIGHT("Tilt\nRight"),
  MICROBIT_MICROPHONE("Shout"),
  MICROBIT_FLIP("Flip"),

  UNRECOGNISED("ERROR");

  private final String displayValue;

  InputEvent(final String displayValue) {
    this.displayValue = displayValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }

  public boolean isValid() {
    return this != UNRECOGNISED;
  }

  public static InputEvent fromEventValue(char eventValue, InputDeviceType deviceType) {
    switch (deviceType) {
      case KEYBOARD:
        return fromKeyboardEvent(eventValue);
      case MICROBIT:
        return fromMicrobitEvent(eventValue);
      default:
        return InputEvent.UNRECOGNISED;
    }
  }

  private static InputEvent fromKeyboardEvent(char event) {
    switch (event) {
      case 'f':
        return InputEvent.KEYBOARD_F;
      case 'j':
        return InputEvent.KEYBOARD_J;
      // TODO: Spacebar & more keys
      default:
        return InputEvent.UNRECOGNISED;
    }
  }

  // TODO: Doc this interface
  private static InputEvent fromMicrobitEvent(char event) {
    switch (event) {
      case 'a':
        return InputEvent.MICROBIT_LEFT_BTN;
      case 'b':
        return InputEvent.MICROBIT_RIGHT_BTN;
      case 's':
        return InputEvent.MICROBIT_SHAKE;
      case 'l':
        return InputEvent.MICROBIT_TILT_LEFT;
      case 'r':
        return InputEvent.MICROBIT_TILT_RIGHT;
      case 'f':
        return InputEvent.MICROBIT_FLIP;
      default:
        return InputEvent.UNRECOGNISED;
    }
  }
}
