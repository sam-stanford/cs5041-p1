package io.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import processing.core.PApplet;
import processing.serial.Serial;

public abstract class SerialCommunicator {

  private Serial port;
  private byte[] inBuffer = new byte[128];
  private int inBufferContentsLength = 0;

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

  private void readAvailableFromPortIntoBuffer() {
    int remainingBufferSpace = inBuffer.length - inBufferContentsLength;
    byte[] buf = new byte[remainingBufferSpace];
    int read = port.readBytes(buf);
    inBufferContentsLength += read;
    // System.out.println("READ BYTES: " + read);
  }

  private void clearStartOfBufferAndAdjustContentsLength(int sizeToClear) {
    if (sizeToClear <= 0) {
      return;
    }

    byte[] newBuf = new byte[inBuffer.length];
    int newContentsLength = inBufferContentsLength - sizeToClear;

    for (int i = 0; i < newContentsLength; i += 1) {
      newBuf[i] = inBuffer[i + sizeToClear];
    }

    inBuffer = newBuf;
    inBufferContentsLength = newContentsLength;
  }

  public Queue<SerialMessage> readAllMessages() {
    readAvailableFromPortIntoBuffer();
    List<String> messageStrings = readAllMessageStrings();
    return parseMessagesFromStrings(messageStrings);
  }

  private List<String> readAllMessageStrings() {
    List<String> messages = new ArrayList<>();
    char delimter = getMessageDelimeter();

    int byteCountRead = 0;
    int nextMessageStartIndex = 0;

    for (int i = 0; i < inBufferContentsLength; i += 1) {
      if (inBuffer[i] == '0') {
        break;
      }

      if (inBuffer[i] == delimter) {
        byte[] messageBytes = Arrays.copyOfRange(inBuffer, nextMessageStartIndex, i);
        messages.add(new String(messageBytes));
        nextMessageStartIndex = i + 1;
        byteCountRead += (i + 1 - nextMessageStartIndex);
      }
    }

    clearStartOfBufferAndAdjustContentsLength(byteCountRead);
    return messages;
  }

  private Queue<SerialMessage> parseMessagesFromStrings(List<String> messageStrings) {
    Queue<SerialMessage> messages = new LinkedList<>();
    for (String s : messageStrings) {
      SerialMessage m = parseMessageString(s);
      if (m != null) {
        messages.add(m);
        System.out.println("READ STRING: " + m);
      }
    }
    return messages;
  }
}