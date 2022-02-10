package interactions;

import drawable.Drawable;

public interface Interaction extends Drawable {
  public boolean isDone();

  public InteractionType getType();

  public void onClick(float mouseX, float mouseY);
}