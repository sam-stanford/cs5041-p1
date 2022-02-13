package io.devices;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.events.InputEvent;
import io.events.OutputEvent;

public class Keyboard implements IODevice {

  private Queue<InputEvent> inputEvents;

  private InputEvent[] availableInputEvents = new InputEvent[] {
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
  public Queue<InputEvent> getInputEvents() {
    Queue<InputEvent> events = this.inputEvents;
    inputEvents = new LinkedList<>();
    return events;
  }

  public void addInputEvent(InputEvent e) {
    inputEvents.add(e);
  }

  @Override
  public List<OutputEvent> getAvailableOutputEvents() {
    return Collections.emptyList();
  }

  @Override
  public boolean acceptsOutputEvent(OutputEvent e) {
    return false;
  }

  @Override
  public void outputEvent(OutputEvent e) {
    // Do nothing
    return;
  }
}
