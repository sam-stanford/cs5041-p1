package io.devices.input;

import java.util.List;
import java.util.Queue;

import io.events.InputEvent;

public interface InputDevice {
  public List<InputEvent> getAvailableInputEvents();

  public Queue<InputEvent> getInputEvents();

  public void addInputEvent(InputEvent e);
}