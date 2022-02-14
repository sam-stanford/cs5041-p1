package interactions.selection;

import config.display.DisplayConfig;
import drawable.Drawable;
import processing.core.PApplet;
import utils.Color;
import utils.Position;

public class SelectionOption<T> implements Drawable {

  private Position position;
  private float width;
  private float height;
  private T value;
  private DisplayConfig displayConfig;

  private Color fillColor = new Color(210);
  private Color hoveredFillColor = new Color(180);

  public SelectionOption(DisplayConfig displayConfig, Position position, float width, float height, T value) {
    this.position = position;
    this.width = width;
    this.height = height;
    this.value = value;
    this.displayConfig = displayConfig;
  }

  @Override
  public void draw(PApplet app) {
    drawContainer(app);
    drawText(app);
  }

  private void drawContainer(PApplet app) {
    Color c = isHovered(app.mouseX, app.mouseY) ? hoveredFillColor : fillColor;
    app.rectMode(PApplet.CORNER);
    app.fill(c.red, c.blue, c.green);
    app.rect(position.x, position.y, width, height);
  }

  private void drawText(PApplet app) {
    Color c = displayConfig.defaultTextColor;
    app.textSize(displayConfig.smallTextSize);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.fill(c.red, c.blue, c.green);
    app.text(value.toString(), position.x + (width / 2), position.y + (height / 2));
  }

  public boolean isHovered(float mouseX, float mouseY) {
    boolean xInBounds = mouseX >= position.x && mouseX <= position.x + width;
    boolean yInBounds = mouseY >= position.y && mouseY <= position.y + height;
    return xInBounds && yInBounds;
  }

  public T getValue() {
    return value;
  }
}
