package io.devices.output;

import java.util.List;
import java.util.Queue;

import io.events.OutputEvent;

public interface OutputDevice {

  public Queue<OutputEvent> getOutputEventQueue();

  public List<OutputEvent> getAvailableOutputEvents();

  public OutputDeviceType getOutputDeviceType();

  public boolean acceptsOutputEvent(OutputEvent e);

  public void outputEvent(OutputEvent e);

}
