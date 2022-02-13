package interactions.game;

import images.microbit.MicrobitColor;
import io.devices.IODevice;

public class Player {
  public String name;
  public int score;
  public IODevice ioDevice;
  public MicrobitColor color;

  public Player(String name, IODevice ioDevice, MicrobitColor color) {
    this.name = name;
    this.ioDevice = ioDevice;
    this.color = color;
    this.score = 0;
    return;
  }
}