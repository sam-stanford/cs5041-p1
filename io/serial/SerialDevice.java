package io.serial;

import processing.core.PApplet;

public interface SerialDevice {
  public String getSerialPort();

  public void initialiseSerialIO(PApplet app);
}
