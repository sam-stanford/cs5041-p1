package interactions.game.attacker;

import io.events.input.InputEvent;
import utils.Color;
import utils.Position;

// TODO: Some kind of attacker config in global config

public class SmallAttacker extends Attacker {

  private boolean isDead;

  private float damage = 10;
  private float size = 60;

  // TODO: Take config here
  public SmallAttacker(Position initialPosition, Position target, InputEvent damagingInput) {
    // TODO: Get stats from config
    super(initialPosition, target, 2, 60, new Color(90), damagingInput);
    isDead = false;
  }

  @Override
  public float getAttackDamageDealt() {
    return damage;
  }

  @Override
  public void damage() {
    isDead = true;
  }

  @Override
  public float getCollisionRadius() {
    return size / 2;
  }

  @Override
  public boolean isDead() {
    return isDead;
  }

  @Override
  public DamageType getDamageType() {
    return DamageType.SMALL;
  }
}
