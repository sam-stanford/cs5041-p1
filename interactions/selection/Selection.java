package interactions.selection;

import java.util.ArrayList;
import java.util.List;

import config.display.DisplayConfig;
import interactions.Interaction;
import interactions.InteractionType;
import processing.core.PApplet;
import utils.Color;
import utils.Position;

public class Selection<T> implements Interaction {

  private List<SelectionOption<T>> options;
  private SelectionOption<T> selectedOption;
  private String title;

  private int pageNumber = 0;
  private int visibleCount;

  private Color backgroundColor;

  private float windowMargin = 100;
  private float titleAreaHeight = 400;
  private float optionGutter = 40;
  private float optionWidth = 400;
  private float optionHeight = 100;

  public Selection(DisplayConfig displayConfig, String title, List<T> options) {
    this.title = title;
    this.backgroundColor = displayConfig.backgroundColor;
    createOptions(displayConfig, options);
    calculateVisibleCount(options.size(), displayConfig.width);
  }

  private void calculateVisibleCount(int numOptions, float windowHeight) {
    float optionAreaHeight = windowHeight - (2 * windowMargin) - titleAreaHeight;
    this.visibleCount = (int) Math.floor(optionAreaHeight / (optionHeight + optionGutter));
  }

  private void createOptions(DisplayConfig displayConfig, List<T> values) {
    this.options = new ArrayList<>();
    for (int i = 0; i < values.size(); i += 1) {
      this.options.add(
          new SelectionOption<T>(
              getOptionPosition(i, displayConfig.width),
              optionWidth,
              optionHeight,
              values.get(i)));
    }
  }

  private Position getOptionPosition(int index, float windowWidth) {
    return new Position(getOptionX(windowWidth), getOptionY(index));
  }

  private float getOptionX(float windowWidth) {
    return (windowWidth / 2) - (optionWidth / 2);
  }

  private float getOptionY(int index) {
    float distanceFromStart = index * (optionHeight + optionGutter);
    return windowMargin + titleAreaHeight + distanceFromStart - (optionHeight / 2);
  }

  @Override
  public boolean isDone() {
    return selectedOption != null;
  }

  @Override
  public InteractionType getType() {
    return InteractionType.SELECTION;
  }

  @Override
  public void onClick(float mouseX, float mouseY) {
    for (SelectionOption<T> o : options) {
      if (o.isHovered(mouseX, mouseY)) {
        selectedOption = o;
        return;
      }
    }
  }

  public T getSelectedValue() {
    return selectedOption.getValue();
  }

  public int getSelectedValueIndex() {
    return options.indexOf(selectedOption);
  }

  @Override
  public void draw(PApplet app) {
    app.background(backgroundColor.red, backgroundColor.blue, backgroundColor.green);
    drawTitle(app);
    if (options.size() == 0) {
      drawNoOptionsMessage(app);
    } else {
      drawOptions(app);
      drawPageNavigation(app);
    }
    updateMouseCursor(app);
  }

  private void drawNoOptionsMessage(PApplet app) {
    // TODO: Color
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.textSize(52);
    app.text("No Options Available...", app.width / 2, windowMargin + titleAreaHeight);
  }

  private void drawTitle(PApplet app) {
    // TODO: Color
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.textSize(52);
    app.text(title, app.width / 2, windowMargin + (titleAreaHeight / 2));
  }

  private void drawOptions(PApplet app) {
    for (int i = visibleCount * pageNumber; i < visibleCount; i += 1) {
      SelectionOption<T> o = options.get(i);
      o.draw(app);
    }
  }

  private void drawPageNavigation(PApplet app) {
    // TODO
  }

  private void updateMouseCursor(PApplet app) {
    for (SelectionOption<T> o : options) {
      if (o.isHovered(app.mouseX, app.mouseY)) {
        app.cursor(PApplet.HAND);
        return;
      }
    }
    app.cursor(PApplet.ARROW);
  }

}