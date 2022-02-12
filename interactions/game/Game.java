package interactions.game;

import clickable.Clickable;
import config.display.DisplayConfig;
import config.game.GameConfig;
import interactions.Interaction;
import interactions.InteractionType;
import interactions.game.microbitdefence.MicrobitDefence;
import io.events.IOEventQueues;
import processing.core.PApplet;

public class Game implements Interaction, Clickable {

  private Interaction currentInteraction;

  private Player player1;
  private Player player2;
  private int scoreToWin;

  public Game(GameConfig config, DisplayConfig displayConfig, IOEventQueues eventQueues) {
    scoreToWin = config.scoreToWin;

    currentInteraction = new MicrobitDefence(config.microbitDefence, displayConfig, eventQueues);
  }

  @Override
  public void draw(PApplet app) {
    currentInteraction.draw(app);
  }

  @Override
  public boolean isDone() {
    return player1.score >= scoreToWin || player2.score >= scoreToWin;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.GAME;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    currentInteraction.onClick(mouseX, mouseY);
  }

  public Player getCurrentLeadingPlayer() {
    return player1.score > player2.score ? player1 : player2;
  }

}
