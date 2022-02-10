package interactions.selection;

import java.util.ArrayList;
import java.util.List;

import interactions.Interaction;
import interactions.InteractionType;
import processing.core.PApplet;
import utils.Position;

public class Selection implements Interaction {

  private List<SelectionOption> options;
  private SelectionOption selectedOption;
  private String title;

  private int pageNumber = 0;
  private int visibleCount;

  private int backgroundColor = 250;

  private float windowMargin = 100;
  private float titleAreaHeight = 400;
  private float optionGutter = 40;
  private float optionWidth = 400;
  private float optionHeight = 100;

  public Selection(String title, List<String> options, float windowWidth, float windowHeight) {
    this.title = title;
    createOptions(options, windowWidth, windowHeight);
    calculateVisibleCount(options.size(), windowHeight);
  }

  private void calculateVisibleCount(int numOptions, float windowHeight) {
    float optionAreaHeight = windowHeight - (2 * windowMargin) - titleAreaHeight;
    this.visibleCount = (int) Math.floor(optionAreaHeight / (optionHeight + optionGutter));
  }

  private void createOptions(List<String> values, float windowWidth, float windowHeight) {
    this.options = new ArrayList<>();
    for (int i = 0; i < values.size(); i += 1) {
      this.options.add(
          new SelectionOption(
              getOptionPosition(i, windowWidth),
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
    for (SelectionOption o : options) {
      if (o.isHovered(mouseX, mouseY)) {
        selectedOption = o;
        return;
      }
    }
  }

  public String getSelectedValue() {
    return selectedOption.getValue();
  }

  public int getSelectedValueIndex() {
    return options.indexOf(selectedOption);
  }

  @Override
  public void draw(PApplet app) {
    app.background(backgroundColor);
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
      SelectionOption o = options.get(i);
      o.draw(app);
    }
  }

  private void drawPageNavigation(PApplet app) {
    // TODO
  }

  private void updateMouseCursor(PApplet app) {
    for (SelectionOption o : options) {
      if (o.isHovered(app.mouseX, app.mouseY)) {
        app.cursor(PApplet.HAND);
        return;
      }
    }
    app.cursor(PApplet.ARROW);
  }

}