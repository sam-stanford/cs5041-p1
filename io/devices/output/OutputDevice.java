package io.devices.output;

import java.util.List;

import io.events.OutputEvent;

public interface OutputDevice {

  public List<OutputEvent> getAvailableOutputEvents();

  public boolean acceptsOutputEvent(OutputEvent e);

  public void outputEvent(OutputEvent e);

}
