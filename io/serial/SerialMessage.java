package io.serial;

import io.events.InputEvent;
import io.events.OutputEvent;

public class SerialMessage {
  public SerialMessageType type;
  public InputEvent inputEvent;
  public OutputEvent outputEvent;

  public SerialMessage(InputEvent inputEvent, OutputEvent outputEvent) {
    this.type = inputEvent != null ? SerialMessageType.INPUT : SerialMessageType.OUTPUT;
    this.inputEvent = inputEvent;
    this.outputEvent = outputEvent;
  }
}
