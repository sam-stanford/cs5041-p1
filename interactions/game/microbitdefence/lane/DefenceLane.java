package interactions.game.microbitdefence.lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import drawable.Drawable;
import interactions.game.microbitdefence.attacker.Attacker;
import interactions.game.microbitdefence.attacker.AttackerFactory;
import interactions.game.microbitdefence.target.DefenceTarget;
import io.devices.IODevice;
import io.events.InputEvent;
import io.events.OutputEvent;
import processing.core.PApplet;
import utils.Position;
import utils.Randomiser;

public class DefenceLane implements Drawable {

  private final float LANE_MARGIN = 200;

  private DefenceLaneSizing sizing;
  private List<Attacker> attackers;
  private DefenceTarget target;
  private boolean omnidirectional;

  public DefenceLane(DefenceLaneSizing sizing, DefenceTarget target, boolean omnidirectional) {
    this.sizing = sizing;
    this.target = target;
    this.omnidirectional = omnidirectional;
    this.attackers = new ArrayList<>();
  }

  @Override
  public void draw(PApplet app) {
    drawAttackers(app);
    drawTarget(app);
  }

  private void drawAttackers(PApplet app) {
    for (Attacker a : attackers) {
      a.draw(app);
    }
  }

  private void drawTarget(PApplet app) {
    target.draw(app);
  }

  public boolean isTargetDestroyed() {
    return target.isDestroyed();
  }

  public void handleInputEvents(Queue<InputEvent> events) {
    while (!events.isEmpty()) {
      InputEvent e = events.poll();
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

  public void update(IODevice ioDevice) {
    updateAttackerPositions();
    removeDeadAttackers();
    checkAndHandleCollisions(ioDevice);
  }

  public void updateAttackerPositions() {
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

  private void checkAndHandleCollisions(IODevice ioDevice) {
    List<Integer> collidedAttackerIndexes = getCollidedAttackerIndexes();
    List<Attacker> collidedAttackers = getAttackersAtIndexes(collidedAttackerIndexes);
    registerAttackerCollisions(collidedAttackers, ioDevice);
    removeAttackersAtIndexes(collidedAttackerIndexes);
  }

  private List<Integer> getCollidedAttackerIndexes() {
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < attackers.size(); i += 1) {
      Attacker a = attackers.get(i);
      if (a.collidedWith(target)) {
        indexes.add(i);
      }
    }
    return indexes;
  }

  private void registerAttackerCollisions(List<Attacker> collidedAttackers, IODevice ioDevice) {
    for (Attacker a : collidedAttackers) {
      for (OutputEvent e : a.getDamageType().toOutputEvents()) {
        ioDevice.outputEvent(e);
      }
    }

    if (collidedAttackers.size() > 0) {
      target.damage(sumAttackersDamage(collidedAttackers));
    }
  }

  private int sumAttackersDamage(List<Attacker> attackers) {
    int sum = 0;
    for (Attacker a : attackers) {
      sum += a.getDamageDealt().toDamage();
    }
    return sum;
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

  // TODO: Ramp-up
  // TODO: Take config for frequency
  public void spawnAttackers(AttackerFactory factory, List<InputEvent> possibleInputEvents) {
    // TODO: Time limiter from config
    float p = 0.05f;// TODO: Get proba from config - maybe replace with timeout actually
    // TODO: Maybe timeout range for spawning
    boolean shouldSpawn = Randomiser.eventWithProbability(p);
    if (!shouldSpawn) {
      return;
    }

    Position initialPosition = generateAttackerPosition();
    Position targetPosition = target.getPosition();
    InputEvent damagingInput = selectRandomInputEvent(possibleInputEvents);
    boolean shouldSpawnBig = Randomiser.eventWithProbability(0.2f); // TODO: Get proba from config

    Attacker a = shouldSpawnBig
        ? factory.createBigAttacker(initialPosition, targetPosition, damagingInput)
        : factory.createSmallAttacker(initialPosition, targetPosition, damagingInput);

    attackers.add(a);
  }

  private InputEvent selectRandomInputEvent(List<InputEvent> options) {
    int r = Randomiser.intZeroToN(options.size());
    return options.get(r);
  }

  private Position generateAttackerPosition() {
    return omnidirectional
        ? generateOmnidirectionalAttackerPosition()
        : generateUnidirectionalAttackerPosition();
  }

  private Position generateUnidirectionalAttackerPosition() {
    float y = -100;
    float randomXOffset = (sizing.getWidth() - (2 * LANE_MARGIN)) * Randomiser.floatZeroToOne();
    float x = LANE_MARGIN + sizing.getLeftBoundaryX() + randomXOffset;
    return new Position(x, y);
  }

  private Position generateOmnidirectionalAttackerPosition() {
    float xPosition;
    float yPosition;

    switch (Randomiser.intZeroToN(4)) {
      case 0:
        // Top
        xPosition = Randomiser.floatZeroToOne() * sizing.getHeight();
        yPosition = -100;
        break;

      case 1:
        // Right
        xPosition = sizing.getRightBoundaryX() + 100;
        yPosition = Randomiser.floatZeroToOne() * sizing.getHeight();
        break;

      case 2:
        // Bottom
        xPosition = Randomiser.floatZeroToOne() * sizing.getHeight();
        yPosition = sizing.getHeight() + 100;
        break;

      case 3:
      default:
        // Left
        xPosition = -100;
        yPosition = Randomiser.floatZeroToOne() * sizing.getHeight();
        break;
    }

    return new Position(xPosition, yPosition);
  }

  public int getTargetHealth() {
    return target.getHealth();
  }

}
