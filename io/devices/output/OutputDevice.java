package io.devices.output;

import java.util.List;

import io.events.output.OutputEvent;

public interface OutputDevice {
  public List<OutputEvent> getAvailableOutputEvents();

  public OutputDeviceType getOutputDeviceType();

}
