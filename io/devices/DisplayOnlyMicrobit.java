package io.devices;

import java.util.Arrays;
import java.util.List;

import io.events.OutputEvent;
import processing.core.PApplet;

public class DisplayOnlyMicrobit extends Microbit {

  public DisplayOnlyMicrobit(PApplet app, String serialPort) {
    super(app, serialPort);
  }

  private OutputEvent[] availableOutputEvents = new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_HAPPY,
      OutputEvent.MICROBIT_DISPLAY_SAD,
  };

  @Override
  public List<OutputEvent> getAvailableOutputEvents() {
    return Arrays.asList(availableOutputEvents);
  }
}
