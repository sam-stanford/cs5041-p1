package io.devices;

import java.util.Arrays;
import java.util.List;

import io.events.OutputEvent;
import processing.core.PApplet;

public class DisplayOnlyMicrobit extends Microbit {

  public DisplayOnlyMicrobit(PApplet app, String serialPort) {
    super(app, serialPort);
  }

  private List<OutputEvent> availableOutputEvents = Arrays.asList(new OutputEvent[] {
      OutputEvent.MICROBIT_DISPLAY_HAPPY,
      OutputEvent.MICROBIT_DISPLAY_SAD,
  });

  @Override
  public List<OutputEvent> getAvailableOutputEvents() {
    return availableOutputEvents;
  }

  @Override
  public boolean acceptsOutputEvent(OutputEvent e) {
    return availableOutputEvents.contains(e);
  }

  @Override
  public void outputEvent(OutputEvent e) {
    if (!acceptsOutputEvent(e)) {
      return;
    }
    super.outputEvent(e);
  }
}
