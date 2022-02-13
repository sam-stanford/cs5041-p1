package io.serial;

import java.util.Arrays;
import java.util.List;

import io.events.InputEvent;
import io.events.OutputEvent;
import processing.core.PApplet;

public class SimpleSerialCommunicator extends SerialCommunicator {

  private List<SerialMessageType> supportedMessageTypes = Arrays.asList(new SerialMessageType[] {
      SerialMessageType.INPUT, SerialMessageType.OUTPUT,
  });

  public SimpleSerialCommunicator(PApplet app, String serialPort, int baudRate) {
    super(app, serialPort, baudRate);
  }

  @Override
  public char getMessageDelimeter() {
    return ':';
  }

  @Override
  public String generateMessageString(SerialMessage message) {
    if (isUnsupportedMessageType(message.type)) {
      return "";
    }
    return message.type == SerialMessageType.INPUT
        ? generateInputEventMessageString(message)
        : generateOutputEventMessageString(message);
  }

  private boolean isUnsupportedMessageType(SerialMessageType messageType) {
    return !supportedMessageTypes.contains(messageType);
  }

  private String generateInputEventMessageString(SerialMessage message) {
    StringBuilder sb = new StringBuilder();
    sb.append(message.inputEvent.toEventValue());
    sb.append(getMessageDelimeter());
    return sb.toString();
  }

  private String generateOutputEventMessageString(SerialMessage message) {
    StringBuilder sb = new StringBuilder();
    sb.append(message.outputEvent.toEventValue());
    sb.append(getMessageDelimeter());
    return sb.toString();
  }

  @Override
  public SerialMessage parseMessageString(String message) {
    if (message.length() < 2) {
      return SerialMessage.emptyMessage();
    }

    InputEvent parsedInputEvent = InputEvent.fromEventValue(message);
    OutputEvent parsedOutputEvent = OutputEvent.fromEventValue(message);

    if (parsedInputEvent.isValid()) {
      InputEvent e = InputEvent.fromEventValue(message.substring(1));
      return new SerialMessage(e, null);
    }

    if (parsedOutputEvent.isValid()) {
      OutputEvent e = OutputEvent.fromEventValue(message.substring(1));
      return new SerialMessage(null, e);
    }

    return SerialMessage.emptyMessage();
  }

}
