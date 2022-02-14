package config.game.microbitdefence;

import config.game.microbitdefence.attacker.AttackersConfig;
import config.game.microbitdefence.defence.DefenceTargetConfig;

public class MicrobitDefenceConfig {
  public int maxPlayers;
  public DefenceTargetConfig defenceTarget;
  public AttackersConfig attackers;
  public int rampUpTime;
}
