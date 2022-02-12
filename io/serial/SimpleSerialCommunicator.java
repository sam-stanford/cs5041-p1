package io.serial;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;

public class SimpleSerialCommunicator extends SerialCommunicator {

  private List<SerialMessageType> supportedMessageTypes = Arrays.asList(new SerialMessageType[] {
      SerialMessageType.INPUT, SerialMessageType.OUTPUT,
  });

  public SimpleSerialCommunicator(PApplet app, String serialPort, int baudRate) {
    super(app, serialPort, baudRate);
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
    sb.append("i");
    sb.append(message.inputEvent.toString());
    return sb.toString();
  }

  private String generateOutputEventMessageString(SerialMessage message) {
    StringBuilder sb = new StringBuilder();
    sb.append("o");
    sb.append(message.outputEvent.toString());
    return sb.toString();
  }

  @Override
  public String readNextMessageString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SerialMessage parseMessageString(String message) {
    // TODO Auto-generated method stub
    return null;
  }

}
