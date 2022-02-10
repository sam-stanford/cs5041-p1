package io.devices.input;

import java.util.List;

import io.events.input.InputEvent;

public interface InputDevice {
  public List<InputEvent> getAvailableInputEvents();

  public InputDeviceType getInputDeviceType();
}