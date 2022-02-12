package io.devices.input;

import java.util.List;
import java.util.Queue;

import io.events.InputEvent;

public interface InputDevice {
  public List<InputEvent> getAvailableInputEvents();

  public InputDeviceType getInputDeviceType();

  public Queue<InputEvent> getInputEvents();
}