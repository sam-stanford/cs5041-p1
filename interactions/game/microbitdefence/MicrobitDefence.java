package interactions.game.microbitdefence;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import config.display.DisplayConfig;
import config.game.microbitdefence.MicrobitDefenceConfig;
import config.game.microbitdefence.defence.DefenceTargetConfig;
import interactions.Interaction;
import interactions.InteractionType;
import interactions.game.Player;
import interactions.game.microbitdefence.attacker.AttackerFactory;
import interactions.game.microbitdefence.lane.DefenceLane;
import interactions.game.microbitdefence.lane.DefenceLaneSizing;
import interactions.game.microbitdefence.target.DefenceTarget;
import io.events.InputEvent;
import processing.core.PApplet;
import utils.Color;
import utils.Position;

public class MicrobitDefence implements Interaction {

  private AttackerFactory attackerFactory;
  private List<Player> players;
  private List<DefenceLane> defenceLanes;
  private DisplayConfig displayConfig;

  public MicrobitDefence(MicrobitDefenceConfig config, DisplayConfig displayConfig, List<Player> players) {
    if (players.size() > config.maxPlayers) {
      throw new TooManyPlayersException();
    }
    if (players.size() == 0) {
      throw new NotEnoughPlayersException();
    }
    this.displayConfig = displayConfig;
    this.players = players;
    this.attackerFactory = new AttackerFactory(config.attackers);
    createDefenceLanes(config.defenceTarget);
  }

  private void createDefenceLanes(DefenceTargetConfig targetConfig) {
    defenceLanes = new ArrayList<>();

    int numLanes = players.size();
    float laneWidth = displayConfig.width / numLanes;

    for (int i = 0; i < players.size(); i += 1) {
      Player p = players.get(i);
      DefenceLaneSizing sizing = new DefenceLaneSizing(i * laneWidth, laneWidth, displayConfig.height);
      DefenceTarget target = new DefenceTarget(targetConfig, getDefenceTargetPosition(sizing), p.color);
      DefenceLane lane = new DefenceLane(sizing, target, isGameOmnidirectional());
      defenceLanes.add(lane);
    }
  }

  private boolean isGameOmnidirectional() {
    return players.size() == 1;
  }

  private Position getDefenceTargetPosition(DefenceLaneSizing laneSizing) {
    float yPos = players.size() == 1 ? displayConfig.height / 2 : displayConfig.height - 200;
    return new Position(laneSizing.getCenterX(), yPos);
  }

  @Override
  public boolean isDone() {
    return false;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.GAME;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    return;
  }

  @Override
  public void draw(PApplet app) {
    setBackground(app);
    drawLanes(app);

    getAndHandleInputEvents();
    updateLanes();
    spawnAttackers();
  }

  private void setBackground(PApplet app) {
    Color c = displayConfig.backgroundColor;
    app.background(c.red, c.green, c.blue);
  }

  private void drawLanes(PApplet app) {
    for (DefenceLane l : defenceLanes) {
      l.draw(app);
    }
  }

  private void getAndHandleInputEvents() {
    for (int i = 0; i < players.size(); i += 1) {
      Player p = players.get(i);
      Queue<InputEvent> events = getPlayerInputEvents(p);

      DefenceLane l = defenceLanes.get(i);
      l.handleInputEvents(events);
    }
  }

  private Queue<InputEvent> getPlayerInputEvents(Player player) {
    return player.ioDevice.getInputEvents();
  }

  private void spawnAttackers() {
    for (int i = 0; i < players.size(); i += 1) {
      Player p = players.get(i);
      DefenceLane l = defenceLanes.get(i);
      l.spawnAttackers(attackerFactory, getAvailableInputEventsForPlayer(p));
    }
  }

  private List<InputEvent> getAvailableInputEventsForPlayer(Player player) {
    return player.ioDevice.getAvailableInputEvents();
  }

  private void updateLanes() {
    for (int i = 0; i < players.size(); i += 1) {
      Player p = players.get(i);
      DefenceLane l = defenceLanes.get(i);
      l.update(p.ioDevice);
    }
  }

  public Player getCurrentLeadingPlayer() {
    Player leader = players.get(0);
    DefenceLane leaderLane = defenceLanes.get(0);

    for (int i = 1; i < players.size(); i += 1) {
      DefenceLane l = defenceLanes.get(i);
      if (l.getTargetHealth() > leaderLane.getTargetHealth()) {
        leaderLane = l;
        leader = players.get(i);
      }
    }

    return leader;
  }

  private class NotEnoughPlayersException extends RuntimeException {
  }

  private class TooManyPlayersException extends RuntimeException {
  }
}