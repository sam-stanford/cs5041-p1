package interactions.game;

public enum GameMode {
  HIDE_AND_SEEK_ONLY("Hide and Seek"),
  DEFENCE_ONLY("Defence"),
  HIDE_AND_SEEK_AND_DEFENCE("Full Game (Recommended)");

  private String displayValue;

  private GameMode(final String displayValue) {
    this.displayValue = displayValue;
  }

  public String toString() {
    return displayValue;
  }
}
