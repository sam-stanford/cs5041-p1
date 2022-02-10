package interactions.game.attacker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.devices.output.OutputDeviceType;
import io.events.output.OutputEvent;

public enum DamageType {
  SMALL,
  BIG;

  public int toScore() {
    switch (this) {
      case SMALL:
        return 1;
      case BIG:
        return 3;
      default:
        return 0;
    }
  }

  public List<OutputEvent> toOutputEvents(OutputDeviceType deviceType) {
    switch (deviceType) {
      case MICROBIT:
        return this.toMicrobitEvents();
      default:
        return Collections.emptyList();
    }
  }

  private List<OutputEvent> toMicrobitEvents() {
    switch (this) {
      case SMALL:
        return Arrays.asList(new OutputEvent[] {
            OutputEvent.MICROBIT_DISPLAY_SAD, OutputEvent.MICROBIT_HIGH_BEEP
        });

      case BIG:
        return Arrays.asList(new OutputEvent[] {
            OutputEvent.MICROBIT_DISPLAY_SAD, OutputEvent.MICROBIT_LOW_BEEP
        });

      default:
        return Collections.emptyList();
    }
  }

}
