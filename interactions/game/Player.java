package interactions.game;

import io.devices.input.InputDevice;

public class Player {
  public String name;
  public int score;
  public InputDevice inputDevice;

  public Player(String name, InputDevice inputDevice) {
    this.name = name;
    this.inputDevice = inputDevice;
    this.score = 0;
    return;
  }
}