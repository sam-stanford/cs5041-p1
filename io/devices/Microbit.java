package io.devices;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.devices.serial.SerialDevice;
import io.events.InputEvent;
import io.events.OutputEvent;
import io.serial.SerialCommunicator;
import io.serial.SerialMessage;
import io.serial.SimpleSerialCommunicator;
import processing.core.PApplet;

public class Microbit implements IODevice, SerialDevice {

  private final int SERIAL_BAUD_RATE = 115200;

  private Queue<InputEvent> inputEvents;
  private String serialPortName;
  private SerialCommunicator serialCommunicator;

  private List<InputEvent> availableInputEvents = Arrays.asList(new InputEvent[] {
      InputEvent.MICROBIT_LEFT_BTN,
      InputEvent.MICROBIT_LEFT_BTN,
      InputEvent.MICROBIT_TILT_LEFT,
      InputEvent.MICROBIT_TILT_RIGHT,
      InputEvent.MICROBIT_SHAKE,
      InputEvent.MICROBIT_MICROPHONE,
      InputEvent.MICROBIT_FLIP,
  });

  private List<OutputEvent> availableOutputEvents = Arrays.asList(new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_HAPPY,
      OutputEvent.MICROBIT_DISPLAY_SAD,
      OutputEvent.MICROBIT_LONG_BEEP,
      OutputEvent.MICROBIT_SHORT_BEEP,
  });

  public Microbit(PApplet app, String serialPortName) {
    this.inputEvents = new LinkedList<>();
    this.serialPortName = serialPortName;
    this.serialCommunicator = new SimpleSerialCommunicator(app, serialPortName, SERIAL_BAUD_RATE);
  }

  @Override
  public String getSerialPortName() {
    return this.serialPortName;
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
  public boolean acceptsOutputEvent(OutputEvent e) {
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
  public void addInputEvent(InputEvent e) {
    inputEvents.add(e);
  }

  @Override
  public Queue<InputEvent> getInputEvents() {
    Queue<InputEvent> q = inputEvents;
    inputEvents = new LinkedList<>();
    return q;
  }

}
