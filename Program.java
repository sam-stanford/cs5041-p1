import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import interactions.Interaction;
import interactions.IntroScreen;
import interactions.game.Game;
import interactions.selection.Selection;
import io.devices.input.InputDevice;
import io.devices.input.InputDeviceType;
import io.events.input.InputEvent;
import io.events.input.InputEventQueue;
import io.events.input.SingleInputEventQueue;
import io.events.output.MultiOutputEventQueue;
import io.events.output.OutputEvent;
import io.events.output.OutputEventQueue;
import processing.core.PApplet;

// TODO: Global style config
// TODO: Also, global game config (hide and seek or just defence? timing? input
// method?)

// TODO: Fix click not working

public class Program extends PApplet {

  private static InputEventQueue inputEventQueue = new SingleInputEventQueue();
  private static OutputEventQueue outputEventQueue = new MultiOutputEventQueue(new String[] { "test1", "test2" }); // TODO

  private Interaction currentInteraction;
  private InputDevice inputDevice;

  public Program() {
    currentInteraction = new IntroScreen();
  }

  @Override
  public void settings() {
    fullScreen();
  }

  @Override
  public void draw() {
    if (currentInteraction.isDone()) {
      clearInputEventQueue();
      startNextInteraction();
    }
    currentInteraction.draw(this);
  }

  private void clearInputEventQueue() {
    inputEventQueue = new SingleInputEventQueue();
  }

  // 1) Game mode (i.e. just h&s, just defence, both)
  // 2) Input device 1
  // 3) (Optional) Input device 2 (only required for both)

  private void startNextInteraction() {
    switch (currentInteraction.getType()) {
      case INTRO_SCREEN:

        List<String> testList = new ArrayList<>();
        testList.add("Option 1");
        testList.add("Option 2");
        testList.add("Option 3");
        testList.add("Option 4");
        testList.add("Option 5");
        testList.add("Option 6");
        currentInteraction = new Selection("Select an option", testList, this.width, this.height);
        break;

      case SELECTION:
        Selection s = (Selection) currentInteraction;
        System.out.println(s.getSelectedValue());
        currentInteraction = new Game(this.width, this.height, inputEventQueue, outputEventQueue, "P1", "P2");
        break;

      case END_SCREEN:
        // TODO
        break;

      default:
        break;
    }
  }

  @Override
  public void mouseClicked() {
    currentInteraction.onClick(mouseX, mouseY);
  }

  @Override
  public void keyPressed() {
    char key = this.key;
    InputEvent e = InputEvent.fromEventValue(key, InputDeviceType.KEYBOARD);
    if (e.isValid()) {
      inputEventQueue.add(e);
    }
  }

  public static void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Program" };
    PApplet.main(appletArgs);
  }
}
