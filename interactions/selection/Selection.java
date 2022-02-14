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
  private SelectionType type;

  private int pageNumber = 0;
  private int visibleCount;

  private DisplayConfig displayConfig;

  private float windowMargin = 100;
  private float titleAreaHeight = 400;
  private float optionGutter = 40;
  private float optionWidth = 600;
  private float optionHeight = 100;

  public Selection(DisplayConfig displayConfig, String title, SelectionType type, List<T> options) {
    this.title = title;
    this.type = type;
    this.displayConfig = displayConfig;
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
              displayConfig,
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
    setBackground(app);
    drawTitle(app);
    if (options.size() == 0) {
      drawNoOptionsMessage(app);
    } else {
      drawOptions(app);
    }
    updateMouseCursor(app);
  }

  private void setBackground(PApplet app) {
    Color c = displayConfig.backgroundColor;
    app.background(c.red, c.blue, c.green);
  }

  private void drawTitle(PApplet app) {
    Color c = displayConfig.defaultTextColor;
    app.fill(c.red, c.green, c.blue);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.textSize(displayConfig.bigTextSize);
    app.text(title, app.width / 2, windowMargin + (titleAreaHeight / 2));
  }

  private void drawNoOptionsMessage(PApplet app) {
    Color c = displayConfig.defaultTextColor;
    app.fill(c.red, c.green, c.blue);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.textSize(displayConfig.smallTextSize);
    app.text("No Options Available...", app.width / 2, windowMargin + titleAreaHeight);
  }

  private void drawOptions(PApplet app) {
    int startIndexToShow = visibleCount * pageNumber;
    int endIndexToShow = Math.min(options.size() - startIndexToShow - 1, startIndexToShow + visibleCount);
    for (int i = startIndexToShow; i <= endIndexToShow; i += 1) {
      SelectionOption<T> o = options.get(i);
      o.draw(app);
    }
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

  public SelectionType getSelectionType() {
    return type;
  }

}