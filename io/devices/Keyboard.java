package io.devices;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.devices.input.InputDevice;
import io.devices.input.InputDeviceType;
import io.events.InputEvent;

public class Keyboard implements InputDevice {

  private Queue<InputEvent> inputEvents;

  private InputEvent[] availableInputEvents = new InputEvent[] {
      InputEvent.KEYBOARD_SPACEBAR,
      InputEvent.KEYBOARD_F,
      InputEvent.KEYBOARD_J
  };

  public Keyboard() {
    this.inputEvents = new LinkedList<>();
  }

  @Override
  public List<InputEvent> getAvailableInputEvents() {
    return Arrays.asList(availableInputEvents);
  }

  @Override
  public InputDeviceType getInputDeviceType() {
    return InputDeviceType.KEYBOARD;
  }

  @Override
  public Queue<InputEvent> getInputEvents() {
    Queue<InputEvent> events = this.inputEvents;
    inputEvents = new LinkedList<>();
    return events;
  }

  public void addInputEvent(InputEvent e) {
    inputEvents.add(e);
  }
}
