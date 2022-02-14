package io.devices;

public enum IODeviceType {
  KEYBOARD("Keyboard"),
  MICROBIT("Micro:Bit"),
  DISPLAY_ONLY_MICROBIT("Micro:Bit (Display Only)");

  private String displayValue;

  private IODeviceType(final String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }
}
