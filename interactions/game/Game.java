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

  private MicrobitDefence microbitDefence;
  // private HideAndSeek hideAndSeek;
  private Interaction currentInteraction;

  private int roundNumber = 0; // TODO

  private Player player1;
  private Player player2;
  private int scoreToWin;

  public Game(GameConfig config, DisplayConfig displayConfig, IOEventQueues eventQueues) {
    this.scoreToWin = config.scoreToWin;
    this.player1 = config.player1;
    this.player2 = config.player2;

    this.microbitDefence = new MicrobitDefence(config.microbitDefence, displayConfig, eventQueues, player1);
    // this.hideAndSeek = new HideAndSeek();

    switch (config.gameMode) {
      case HIDE_AND_SEEK_AND_DEFENCE:
        // TODO
        break;

      case HIDE_AND_SEEK_ONLY:
        // TODO
        break;

      case DEFENCE_ONLY:
        // TODO
        break;
    }

    this.currentInteraction = microbitDefence; // TODO: Move to switch
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
