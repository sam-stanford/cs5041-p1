package images.microbit;

import processing.core.PApplet;
import processing.core.PImage;
import utils.ImageLoader;

public class MicrobitImage {

  private final String BASE_FILEPATH = "./assets/images/microbit/";
  private final String FILEPATH_EXTENSION = ".png";

  private MicrobitColor color;
  private MicrobitState state;

  public MicrobitImage(MicrobitColor color, MicrobitState state) {
    this.color = color;
    this.state = state;
  }

  public PImage toProcessingImage(PApplet processingApp) {
    return ImageLoader.loadImageFromFilepath(processingApp, getImageFilepath());
  }

  public String getImageFilepath() {
    StringBuilder s = new StringBuilder(BASE_FILEPATH);
    s.append(color.toString());
    s.append('_');
    s.append(state.toString());
    s.append(FILEPATH_EXTENSION);
    return s.toString();
  }
}
