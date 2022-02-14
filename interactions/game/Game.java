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
import utils.Time;

public class Game implements Interaction, Clickable {

  private Interaction currentInteraction;
  private MicrobitDefence currentRound;
  private long currentRoundStartTime;
  private long currentRoundEndTime;

  private List<Player> players;
  private int scoreToWin;
  private MicrobitDefenceConfig roundConfig;
  private DisplayConfig displayConfig;
  private long roundEndScreenDuration;

  public Game(GameConfig config, DisplayConfig displayConfig) {
    if (config.players.size() == 0) {
      throw new NotEnoughPlayersException();
    }
    this.scoreToWin = config.scoreToWin;
    this.players = config.players;
    this.roundConfig = config.microbitDefence;
    this.displayConfig = displayConfig;
    this.roundEndScreenDuration = config.roundEndScreenDuration;
    startNewRound();
  }

  private void startNewRound() {
    currentRound = new MicrobitDefence(roundConfig, displayConfig, players);
    currentInteraction = currentRound;
    currentRoundStartTime = Time.getCurrentTimeMillis();
  }

  @Override
  public void draw(PApplet app) {
    if (currentInteraction.isDone()) {
      startNextInteraction();
    }
    currentInteraction.draw(app);
  }

  private void startNextInteraction() {
    switch (currentInteraction.getType()) {
      case GAME_ROUND:
        currentRoundEndTime = Time.getCurrentTimeMillis();
        if (isMultiPlayer() && !playerHasWon()) {
          Player roundWinner = currentRound.getCurrentLeadingPlayer();
          roundWinner.score += 1;
          currentInteraction = new RoundEndScreen(displayConfig, roundEndScreenDuration, roundWinner.name);
        }
        break;
      case ROUND_END_SCREEN:
        startNewRound();
      default:
        break;
    }
  }

  @Override
  public boolean isDone() {
    boolean isDone = playerHasWon() || (isSinglePlayer() && currentRound.isDone());
    if (isDone) {
      currentRoundEndTime = Time.getCurrentTimeMillis();
    }
    return isDone;
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

  public long getRoundDurationInSeconds() {
    if (currentRoundStartTime == 0 || currentRoundEndTime == 0 || currentRoundEndTime < currentRoundStartTime) {
      return -1;
    }
    return (currentRoundEndTime - currentRoundStartTime) / 1000;
  }

  public String getEndGameText() {
    if (isSinglePlayer()) {
      return generateSinglePlayerEndGameText();
    }
    return generateMultiPlayerEndGameText();
  }

  private String generateSinglePlayerEndGameText() {
    StringBuilder s = new StringBuilder();
    s.append("You Survived ");
    s.append(getRoundDurationInSeconds());
    s.append(" Seconds!");
    return s.toString();
  }

  private String generateMultiPlayerEndGameText() {
    StringBuilder s = new StringBuilder();
    s.append(currentRound.getCurrentLeadingPlayer().name);
    s.append(" Wins the Game!");
    return s.toString();
  }

  private class NotEnoughPlayersException extends RuntimeException {
  }

}
