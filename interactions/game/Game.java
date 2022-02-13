package interactions.game;

import java.util.List;

import clickable.Clickable;
import config.display.DisplayConfig;
import config.game.GameConfig;
import config.game.microbitdefence.MicrobitDefenceConfig;
import interactions.Interaction;
import interactions.InteractionType;
import interactions.game.microbitdefence.MicrobitDefence;
import processing.core.PApplet;

public class Game implements Interaction, Clickable {

  private MicrobitDefence currentRound;

  private List<Player> players;
  private int scoreToWin;
  private MicrobitDefenceConfig roundConfig;
  private DisplayConfig displayConfig;

  public Game(GameConfig config, DisplayConfig displayConfig) {
    if (config.players.size() == 0) {
      throw new NotEnoughPlayersException();
    }
    this.scoreToWin = config.scoreToWin;
    this.players = config.players;
    this.roundConfig = config.microbitDefence;
    this.displayConfig = displayConfig;
    startNewRound();
  }

  private void startNewRound() {
    currentRound = new MicrobitDefence(roundConfig, displayConfig, players);
  }

  @Override
  public void draw(PApplet app) {
    if (currentRound.isDone() && isMultiPlayer()) {
      currentRound.getCurrentLeadingPlayer().score += 1;
      if (!playerHasWon()) {
        startNewRound();
      }
    }
    currentRound.draw(app);
  }

  @Override
  public boolean isDone() {
    return playerHasWon() || (isSinglePlayer() && currentRound.isDone());
  }

  private boolean playerHasWon() {
    for (Player p : players) {
      if (p.score >= scoreToWin) {
        return true;
      }
    }
    return false;
  }

  private boolean isSinglePlayer() {
    return players.size() == 1;
  }

  private boolean isMultiPlayer() {
    return players.size() > 1;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.GAME;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    currentRound.onClick(mouseX, mouseY);
  }

  public Player getCurrentLeadingPlayer() {
    Player leadingPlayer = players.get(0);
    for (Player p : players) {
      if (p.score > leadingPlayer.score) {
        leadingPlayer = p;
      }
    }
    return leadingPlayer;
  }

  private class NotEnoughPlayersException extends RuntimeException {
  }

}
