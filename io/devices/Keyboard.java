package io.devices;

import java.util.Arrays;
import java.util.List;

import io.devices.input.InputDevice;
import io.devices.input.InputDeviceType;
import io.events.input.InputEvent;

public class Keyboard implements InputDevice {

  private InputEvent[] availableInputEvents = new InputEvent[] {
      InputEvent.KEYBOARD_SPACEBAR,
      InputEvent.KEYBOARD_F,
      InputEvent.KEYBOARD_J
  };

  @Override
  public List<InputEvent> getAvailableInputEvents() {
    return Arrays.asList(availableInputEvents);
  }

  @Override
  public InputDeviceType getInputDeviceType() {
    return InputDeviceType.KEYBOARD;
  }
}
