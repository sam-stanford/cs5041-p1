package interactions.game.microbitdefence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import config.display.DisplayConfig;
import config.game.microbitdefence.MicrobitDefenceConfig;
import interactions.Interaction;
import interactions.InteractionType;
import interactions.game.Player;
import interactions.game.microbitdefence.attacker.Attacker;
import interactions.game.microbitdefence.attacker.AttackerFactory;
import interactions.game.microbitdefence.defence.DefenceTarget;
import io.devices.input.InputDevice;
import io.events.IOEventQueues;
import io.events.InputEvent;
import io.events.OutputEvent;
import processing.core.PApplet;
import utils.Color;
import utils.Position;
import utils.Randomiser;

public class MicrobitDefence implements Interaction {

  private Queue<InputEvent> inputEventQueue;
  private Queue<OutputEvent> outputEventQueue;
  private Player player;

  private DefenceTarget defenceTarget;
  private List<Attacker> attackers;
  private Color background;

  private AttackerFactory attackerFactory;
  private DisplayConfig displayConfig;

  public MicrobitDefence(MicrobitDefenceConfig config, DisplayConfig displayConfig, IOEventQueues eventQueues,
      Player player) {
    this.defenceTarget = new DefenceTarget(config.defenceTarget, getDefenceTargetPosition(displayConfig));
    this.attackers = new ArrayList<>();
    this.inputEventQueue = eventQueues.inputEventQueue;
    this.outputEventQueue = eventQueues.outputEventQueue;
    this.background = displayConfig.backgroundColor;
    this.displayConfig = displayConfig;
    this.attackerFactory = new AttackerFactory(config.attackers);
    this.player = player;
  }

  private Position getDefenceTargetPosition(DisplayConfig displayConfig) {
    return new Position(displayConfig.width / 2, displayConfig.height / 2);
  }

  @Override
  public void draw(PApplet app) {
    app.background(background.red, background.green, background.blue);
    defenceTarget.draw(app);
    drawAttackers(app);
    handleInputEvents();
    updateAttackerPositions();
    removeDeadAttackers();
    checkAndHandleCollisions();
    spawnAttackers();
  }

  private void drawAttackers(PApplet app) {
    for (Attacker a : attackers) {
      a.draw(app);
    }
  }

  private void handleInputEvents() {
    while (!inputEventQueue.isEmpty()) {
      InputEvent e = inputEventQueue.poll();
      damageAttackersWithMatchingInput(e);
    }
  }

  private void damageAttackersWithMatchingInput(InputEvent input) {
    for (Attacker a : attackers) {
      if (a.getDamagingInput() == input) {
        a.damage();
      }
    }
  }

  private void updateAttackerPositions() {
    for (Attacker a : attackers) {
      a.updatePosition();
    }
  }

  private void removeDeadAttackers() {
    List<Integer> indexesToRemove = new ArrayList<>();
    for (int i = 0; i < attackers.size(); i += 1) {
      Attacker a = attackers.get(i);
      if (a.isDead()) {
        indexesToRemove.add(i);
      }
    }
    removeAttackersAtIndexes(indexesToRemove);
  }

  private void checkAndHandleCollisions() {
    List<Integer> collidedAttackerIndexes = getCollidedAttackerIndexes();
    List<Attacker> collidedAttackers = getAttackersAtIndexes(collidedAttackerIndexes);
    registerAttackerCollisions(collidedAttackers);
    removeAttackersAtIndexes(collidedAttackerIndexes);
  }

  private List<Integer> getCollidedAttackerIndexes() {
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < attackers.size(); i += 1) {
      Attacker a = attackers.get(i);
      if (a.collidedWith(defenceTarget)) {
        indexes.add(i);
      }
    }
    return indexes;
  }

  private void registerAttackerCollisions(List<Attacker> collidedAttackers) {
    for (Attacker a : collidedAttackers) {
      // TODO: Also add bluetooth output queue
      for (OutputEvent e : a.getDamageType().toOutputEvents()) {
        outputEventQueue.add(e);
      }
    }

    if (collidedAttackers.size() > 0) {
      defenceTarget.damage();
    }
  }

  private void removeAttackersAtIndexes(List<Integer> indexes) {
    Collections.sort(indexes, Comparator.reverseOrder());
    for (Integer i : indexes) {
      attackers.remove(i.intValue());
    }
  }

  private List<Attacker> getAttackersAtIndexes(List<Integer> indexes) {
    List<Attacker> attackersAtIndexes = new ArrayList<>();
    for (Integer i : indexes) {
      attackersAtIndexes.add(attackers.get(i.intValue()));
    }
    return attackersAtIndexes;
  }

  // TODO: Take config for frequency
  private void spawnAttackers() {
    // TODO: Time limiter from config
    float p = 0.05f;// TODO: Get proba from config - maybe replace with timeout actually
    // TODO: Maybe timeout range for spawning
    boolean shouldSpawn = Randomiser.eventWithProbability(p);
    if (!shouldSpawn) {
      return;
    }

    Position initialPosition = generateAttackerPosition(displayConfig.width, displayConfig.height);
    Position target = defenceTarget.getPosition();
    InputEvent damagingInput = selectRandomInput();
    boolean shouldSpawnBig = Randomiser.eventWithProbability(0.2f); // TODO: Get proba from config

    Attacker a = shouldSpawnBig
        ? attackerFactory.createBigAttacker(initialPosition, target, damagingInput)
        : attackerFactory.createSmallAttacker(initialPosition, target, damagingInput);

    attackers.add(a);
  }

  private InputEvent selectRandomInput() {
    List<InputEvent> possibleEvents = player.inputDevice.getAvailableInputEvents();
    int r = Randomiser.intZeroToN(possibleEvents.size());
    return possibleEvents.get(r);
  }

  private Position generateAttackerPosition(float displayWidth, float displayHeight) {
    float xPosition;
    float yPosition;

    switch (Randomiser.intZeroToN(4)) {
      case 0:
        // Top
        xPosition = Randomiser.floatZeroToOne() * displayWidth;
        yPosition = -100;
        break;

      case 1:
        // Right
        xPosition = 100;
        yPosition = Randomiser.floatZeroToOne() * displayHeight;
        break;

      case 2:
        // Bottom
        xPosition = Randomiser.floatZeroToOne() * displayWidth;
        yPosition = displayHeight + 100;
        break;

      case 3:
      default:
        // Left
        xPosition = -100;
        yPosition = Randomiser.floatZeroToOne() * displayHeight;
        break;
    }

    return new Position(xPosition, yPosition);
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
}