package interactions.game.microbitdefence.attacker;

import config.game.microbitdefence.attacker.AttackersConfig;
import config.game.microbitdefence.attacker.SingleAttackerConfig;
import io.events.InputEvent;
import utils.Position;

public class AttackerFactory {

  private SingleAttackerConfig smallAttackerConfig;
  private SingleAttackerConfig bigAttackerConfig;

  public AttackerFactory(AttackersConfig config) {
    this.smallAttackerConfig = config.smallAttacker;
    this.bigAttackerConfig = config.bigAttacker;
  }

  public Attacker createSmallAttacker(Position initialPosition, Position target, InputEvent damagingInput) {
    return createAttacker(initialPosition, target, damagingInput, smallAttackerConfig);
  }

  public Attacker createBigAttacker(Position initialPosition, Position target, InputEvent damagingInput) {
    return createAttacker(initialPosition, target, damagingInput, bigAttackerConfig);
  }

  private Attacker createAttacker(Position initialPosition, Position target, InputEvent damagingInput,
      SingleAttackerConfig config) {
    return new Attacker(config, initialPosition, target, damagingInput);
  }

}
