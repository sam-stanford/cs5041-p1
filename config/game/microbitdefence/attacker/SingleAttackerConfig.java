package config.game.microbitdefence.attacker;

import interactions.game.microbitdefence.attacker.DamageType;
import utils.Color;

public class SingleAttackerConfig {
  public Color defaultFillColor;
  public Color defaultTextColor;
  public Color damagedFillColor;
  public Color damagedTextColor;

  public float speed;
  public float size;
  public int health;
  public DamageType damageDealt;
  public long damagedDisplayDuration;
}
