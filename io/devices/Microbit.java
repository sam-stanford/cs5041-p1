package io.devices;

import java.util.Arrays;
import java.util.List;

import io.devices.input.InputDevice;
import io.devices.input.InputDeviceType;
import io.devices.output.OutputDevice;
import io.devices.output.OutputDeviceType;
import io.events.input.InputEvent;
import io.events.output.OutputEvent;

public class Microbit implements InputDevice, OutputDevice {
  private InputEvent[] availableInputEvents = new InputEvent[] {
      InputEvent.KEYBOARD_SPACEBAR,
      InputEvent.KEYBOARD_F,
      InputEvent.KEYBOARD_J
  };

  private OutputEvent[] availableOutputEvents = new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_HAPPY,
      OutputEvent.MICROBIT_DISPLAY_SAD,
      OutputEvent.MICROBIT_HIGH_BEEP,
      OutputEvent.MICROBIT_LOW_BEEP,
  };

  @Override
  public List<InputEvent> getAvailableInputEvents() {
    return Arrays.asList(availableInputEvents);
  }

  @Override
  public List<OutputEvent> getAvailableOutputEvents() {
    return Arrays.asList(availableOutputEvents);
  }

  @Override
  public OutputDeviceType getOutputDeviceType() {
    return OutputDeviceType.MICROBIT;
  }

  @Override
  public InputDeviceType getInputDeviceType() {
    return InputDeviceType.MICROBIT;
  }
}
