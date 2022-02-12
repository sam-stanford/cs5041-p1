package io.devices;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.devices.input.InputDevice;
import io.devices.input.InputDeviceType;
import io.devices.output.OutputDevice;
import io.devices.output.OutputDeviceType;
import io.events.InputEvent;
import io.events.OutputEvent;
import io.serial.SerialCommunicator;
import io.serial.SerialMessage;
import io.serial.SerialMessageType;
import io.serial.SimpleSerialCommunicator;
import processing.core.PApplet;

public class Microbit implements InputDevice, OutputDevice {

  private final int SERIAL_BAUD_RATE = 115200;

  private Queue<OutputEvent> outputEventQueue;
  private SerialCommunicator serialCommunicator;

  private List<InputEvent> availableInputEvents = Arrays.asList(new InputEvent[] {
      InputEvent.KEYBOARD_SPACEBAR,
      InputEvent.KEYBOARD_F,
      InputEvent.KEYBOARD_J
  });

  private List<OutputEvent> availableOutputEvents = Arrays.asList(new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_HAPPY,
      OutputEvent.MICROBIT_DISPLAY_SAD,
      OutputEvent.MICROBIT_LONG_BEEP,
      OutputEvent.MICROBIT_SHORT_BEEP,
  });

  public Microbit(PApplet app, String serialPort) {
    this.outputEventQueue = new LinkedList<>();
    this.serialCommunicator = new SimpleSerialCommunicator(app, serialPort, SERIAL_BAUD_RATE);
  }

  @Override
  public List<InputEvent> getAvailableInputEvents() {
    return availableInputEvents;
  }

  @Override
  public List<OutputEvent> getAvailableOutputEvents() {
    return availableOutputEvents;
  }

  @Override
  public OutputDeviceType getOutputDeviceType() {
    return OutputDeviceType.MICROBIT;
  }

  @Override
  public InputDeviceType getInputDeviceType() {
    return InputDeviceType.MICROBIT;
  }

  @Override
  public Queue<OutputEvent> getOutputEventQueue() {
    return this.outputEventQueue;
  }

  @Override
  public boolean acceptsOutputEvent(OutputEvent e) {
    System.out.println(availableInputEvents.size());
    return availableOutputEvents.contains(e);
  }

  @Override
  public void outputEvent(OutputEvent e) {
    if (!acceptsOutputEvent(e)) {
      return;
    }
    serialCommunicator.writeMessage(new SerialMessage(null, e));
  }

  @Override
  public Queue<InputEvent> getInputEvents() {
    Queue<SerialMessage> messages = serialCommunicator.readAllMessages();
    Queue<InputEvent> events = new LinkedList<>();
    for (SerialMessage m : messages) {
      if (m.type == SerialMessageType.INPUT) {
        events.add(m.inputEvent);
      }
    }
    return events;
  }
}
