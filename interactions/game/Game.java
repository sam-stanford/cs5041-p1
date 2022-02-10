package interactions.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import interactions.Interaction;
import interactions.InteractionType;
import interactions.game.attacker.Attacker;
import interactions.game.attacker.SmallAttacker;
import io.events.input.InputEvent;
import io.events.input.InputEventQueue;
import io.events.output.OutputEvent;
import io.events.output.OutputEventQueue;
import processing.core.PApplet;
import utils.Color;
import utils.Position;
import utils.Randomiser;

// TODO: Rename
// TODO: Abstract scoring system
public class Game implements Interaction {

  public Player player1;
  public Player player2;

  private InputEventQueue inputEventQueue; // TODO
  private OutputEventQueue outputEventQueue;
  private DefenceBase defenceBase;
  private List<Attacker> attackers;

  private Color background = new Color(255, 240, 240);

  // TODO: Wrap down to "level" so as to abstract score and rounds

  // TODO: Use global config for display width & height

  public Game(float displayWidth, float displayHeight, InputEventQueue inputEventQueue,
      OutputEventQueue outputEventQueue, String player1Name,
      String player2Name) {
    player1 = new Player(player1Name);
    player2 = new Player(player2Name);
    defenceBase = new DefenceBase(new Position(displayWidth / 2, displayHeight / 2));
    attackers = new ArrayList<>();
    this.inputEventQueue = inputEventQueue;
    this.outputEventQueue = outputEventQueue;
  }

  @Override
  public void draw(PApplet app) {
    app.background(background.red, background.green, background.blue);
    defenceBase.draw(app);
    drawAttackers(app);
    handleInputEvents();
    updateAttackerPositions();
    removeDeadAttackers();
    checkAndHandleCollisions();
    spawnAttackers(app.width, app.height, defenceBase.getPosition());
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
      if (a.collidedWith(defenceBase)) {
        indexes.add(i);
      }
    }
    return indexes;
  }

  private void registerAttackerCollisions(List<Attacker> collidedAttackers) {
    for (Attacker a : collidedAttackers) {
      for (OutputEvent e : a.getDamageType().toOutputEvents())
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
  private void spawnAttackers(float displayWidth, float displayHeight, Position target) {
    // TODO: Time limiter
    // TODO: Rails
    // TODO: Left to right?
    boolean shouldCreateSmall = Randomiser.int0To100() > 90;
    if (shouldCreateSmall) {
      attackers.add(
          new SmallAttacker(generateAttackerPosition(displayWidth, displayHeight), target,
              selectRandomKeyboardInput()));
    }
    // TODO: Other types
  }

  private InputEvent selectRandomKeyboardInput() {
    int r = Randomiser.intZeroToN(2);
    switch (r) {
      case 0:
        return InputEvent.KEYBOARD_F;
      case 1:
        return InputEvent.KEYBOARD_J;
      default:
        return InputEvent.KEYBOARD_F;
    }
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