package io.serial;

import processing.core.PApplet;
import processing.serial.Serial;

public abstract class SerialCommunicator {

  private Serial port;

  public SerialCommunicator(PApplet app, String serialPort, int baudRate) {
    this.port = new Serial(app, serialPort, baudRate);
    this.port.clear();
  }

  public abstract String generateMessageString(SerialMessage message);

  public abstract char getMessageDelimeter();

  public abstract SerialMessage parseMessageString(String message);

  public void writeMessage(SerialMessage message) {
    String m = this.generateMessageString(message);
    port.write(m);
  }
}