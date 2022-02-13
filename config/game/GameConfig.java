package config.game;

import java.util.List;

import config.game.microbitdefence.MicrobitDefenceConfig;
import interactions.game.Player;

public class GameConfig {
  public int scoreToWin;
  public List<Player> players;
  public MicrobitDefenceConfig microbitDefence;
}
