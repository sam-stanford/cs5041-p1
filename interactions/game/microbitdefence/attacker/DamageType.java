package interactions.game.microbitdefence.attacker;

import io.events.OutputEvent;

public enum DamageType {
  SMALL,
  BIG;

  OutputEvent[] smallDamageOutputEvents = new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_SAD, OutputEvent.MICROBIT_SHORT_BEEP
  };

  OutputEvent[] bigOutputEvents = new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_SAD, OutputEvent.MICROBIT_LONG_BEEP
  };

  public int toDamage() {
    switch (this) {
      case SMALL:
        return 10;
      case BIG:
        return 30;
      default:
        return 0;
    }
  }

  public OutputEvent[] toOutputEvents() {
    switch (this) {
      case SMALL:
        return smallDamageOutputEvents;
      case BIG:
        return bigOutputEvents;
      default:
        return new OutputEvent[0];
    }
  }
}
