package utils;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoader {

  public static PImage loadImageFromFilepath(PApplet app, String filepath) {
    PImage img;
    try {
      img = app.loadImage(filepath);
    } catch (Exception e) {
      return null;
    }
    return img;
  }

}
