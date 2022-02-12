package config.game;

import config.game.hideandseek.HideAndSeekConfig;
import config.game.microbitdefence.MicrobitDefenceConfig;
import interactions.game.GameMode;
import interactions.game.Player;

public class GameConfig {
  public GameMode gameMode;

  public int scoreToWin;

  public Player player1;
  public Player player2;

  public MicrobitDefenceConfig microbitDefence;
  public HideAndSeekConfig hideAndSeek;
}
