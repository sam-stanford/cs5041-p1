package io.serial;

import java.util.LinkedList;
import java.util.Queue;

import processing.core.PApplet;
import processing.serial.Serial;

public abstract class SerialCommunicator {

  private Serial port;

  public SerialCommunicator(PApplet app, String serialPort, int baudRate) {
    this.port = new Serial(app, serialPort, baudRate);
  }

  public abstract String generateMessageString(SerialMessage message);

  public abstract String readNextMessageString();

  public abstract SerialMessage parseMessageString(String message);

  public void writeMessage(SerialMessage message) {
    String m = this.generateMessageString(message);
    port.write(m);
  }

  public boolean hasNextMessage() {
    // TODO
    return false;
  }

  public SerialMessage readNextMessage() {
    String s = this.readNextMessageString();
    return this.parseMessageString(s);
  }

  public Queue<SerialMessage> readAllMessages() {
    Queue<SerialMessage> messages = new LinkedList<>();
    while (hasNextMessage()) {
      messages.add(readNextMessage());
    }
    return messages;
  }

}