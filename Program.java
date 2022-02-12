
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import config.display.DisplayConfig;
import config.game.GameConfig;
import config.game.hideandseek.HideAndSeekConfig;
import config.game.microbitdefence.MicrobitDefenceConfig;
import config.game.microbitdefence.attacker.AttackersConfig;
import config.game.microbitdefence.attacker.SingleAttackerConfig;
import config.game.microbitdefence.defence.DefenceTargetConfig;
import interactions.EndScreen;
import interactions.Interaction;
import interactions.IntroScreen;
import interactions.game.Game;
import interactions.game.GameMode;
import interactions.game.Player;
import interactions.game.microbitdefence.attacker.DamageType;
import interactions.selection.Selection;
import io.devices.Keyboard;
import io.devices.Microbit;
import io.devices.input.InputDevice;
import io.devices.output.OutputDevice;
import io.events.IOEventQueues;
import io.events.InputEvent;
import io.events.OutputEvent;
import io.serial.SerialDevice;
import processing.core.PApplet;
import processing.serial.Serial;
import utils.Color;
import utils.ImageLoader;

public class Program extends PApplet {

  private static Queue<InputEvent> inputEventQueue = new LinkedList<>();
  private static Queue<OutputEvent> outputEventQueue = new LinkedList<>();

  private Interaction currentInteraction;
  private GameMode selectedGameMode;

  private InputDevice inputDevice1;
  private InputDevice inputDevice2;
  private Keyboard keyboardInputDevice = new Keyboard();
  private List<OutputDevice> outputDevices;

  public static void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Program" };
    PApplet.main(appletArgs);
  }

  public Program() {
    currentInteraction = new IntroScreen();
    outputDevices = new ArrayList<>();
  }

  @Override
  public void settings() {
    fullScreen();
  }

  @Override
  public void mouseClicked() {
    currentInteraction.onClick(mouseX, mouseY);
  }

  @Override
  public void keyPressed() {
    String input = Character.toString(this.key);
    InputEvent e = InputEvent.fromEventValue(input);
    if (e.isValid()) {
      keyboardInputDevice.addInputEvent(e);
    }
  }

  @Override
  public void draw() {
    handleOutputEvents();

    if (currentInteraction.isDone()) {
      clearInputEventQueue();
      startNextInteraction();
    }
    currentInteraction.draw(this);
  }

  private void handleOutputEvents() {
    while (!outputEventQueue.isEmpty()) {
      OutputEvent e = outputEventQueue.poll();
      sendOutputEventToAllAcceptingOutputDevivces(e);
    }
  }

  private void sendOutputEventToAllAcceptingOutputDevivces(OutputEvent e) {
    for (OutputDevice d : outputDevices) {
      if (d.acceptsOutputEvent(e)) {
        d.outputEvent(e);
      }
    }
  }

  private void clearInputEventQueue() {
    inputEventQueue = new LinkedList<>();
  }

  private void startNextInteraction() {
    resetCursorState();
    switch (currentInteraction.getType()) {
      case INTRO_SCREEN:
      case SELECTION:
        saveSelectedValueFromCurrentInteractionIfSelection(currentInteraction);

        Selection<?> nextSelection = generateNextSelection();
        if (nextSelection != null) {
          currentInteraction = nextSelection;
        } else {
          GameConfig gameConfig = createGameConfig(selectedGameMode, inputDevice1, inputDevice2);
          currentInteraction = new Game(gameConfig, createDisplayConfig(), getEventQueues());
        }
        break;

      case GAME:
        currentInteraction = new EndScreen();

      case END_SCREEN:
        // TODO
        break;

      default:
        break;
    }
  }

  private void resetCursorState() {
    this.cursor(PApplet.ARROW);
  }

  private void saveSelectedValueFromCurrentInteractionIfSelection(Object selection) {
    if (!(currentInteraction instanceof Selection<?>)) {
      return;
    }

    Object selectedValue = ((Selection<?>) currentInteraction).getSelectedValue();
    if (selectedValue instanceof GameMode) {
      this.selectedGameMode = (GameMode) selectedValue;
      return;
    }
    if (selectedValue instanceof InputDevice) {
      if (this.inputDevice1 == null) {
        this.inputDevice1 = (InputDevice) selectedValue;
        if (this.inputDevice1 instanceof SerialDevice) {
          ((SerialDevice) this.inputDevice1).initialiseSerialIO(this);
        }
        addAsOutputDeviceIfApplicable(selectedValue);
        return;
      }
      if (inputDevice2 == null) {
        this.inputDevice2 = (InputDevice) selectedValue;
        if (this.inputDevice2 instanceof SerialDevice) {
          ((SerialDevice) this.inputDevice2).initialiseSerialIO(this);
        }
        addAsOutputDeviceIfApplicable(selectedValue);
        return;
      }
    }
  }

  private void addAsOutputDeviceIfApplicable(Object o) {
    if (o instanceof OutputDevice) {
      this.outputDevices.add((OutputDevice) o);
    }
  }

  private Selection<?> generateNextSelection() {
    if (selectedGameMode == null) {
      return generateGameModeSelection();
    }
    if (inputDevice1 == null) {
      return generateInputDeviceSelectionForDevice1();
    }
    if (selectedGameMode == GameMode.HIDE_AND_SEEK_AND_DEFENCE && inputDevice2 == null) {
      return generateInputDeviceSelectionForDevice2();
    }
    return null;
  }

  private Selection<GameMode> generateGameModeSelection() {
    List<GameMode> options = Arrays.asList(new GameMode[] {
        GameMode.HIDE_AND_SEEK_AND_DEFENCE, GameMode.DEFENCE_ONLY, GameMode.HIDE_AND_SEEK_ONLY,
    });
    return new Selection<GameMode>(createDisplayConfig(), "Select a Game Mode", options);
  }

  private Selection<InputDevice> generateInputDeviceSelection(String title, List<SerialDevice> devicesToExclude) {
    List<InputDevice> options = getAvailableSerialInputDevices(devicesToExclude);
    return new Selection<InputDevice>(createDisplayConfig(), title, options);
  }

  private List<InputDevice> getAvailableSerialInputDevices(List<SerialDevice> devicesToExclude) {
    List<String> ports = new ArrayList<>();
    ports.addAll(Arrays.asList(Serial.list()));

    for (SerialDevice d : devicesToExclude) {
      ports.remove(d.getSerialPort());
    }

    List<InputDevice> devices = new ArrayList<>();
    for (String port : ports) {
      // Convert all to Microbits for now
      InputDevice d = new Microbit(this, port);
      devices.add(d);
    }
    if (inputDevice1 == null || !inputDevice1.equals(keyboardInputDevice)) {
      devices.add(keyboardInputDevice);
    }
    return devices;
  }

  private Selection<InputDevice> generateInputDeviceSelectionForDevice1() {
    return generateInputDeviceSelection("Choose Your Input Device for Player 1", Collections.emptyList());
  }

  private Selection<InputDevice> generateInputDeviceSelectionForDevice2() {
    List<SerialDevice> devicesToExclude = inputDevice1 instanceof SerialDevice
        ? Collections.singletonList((SerialDevice) inputDevice1)
        : Collections.emptyList();

    return generateInputDeviceSelection("Choose Your Input Device for Player 2", devicesToExclude);
  }

  private IOEventQueues getEventQueues() {
    return new IOEventQueues(inputEventQueue, outputEventQueue);
  }

  private DisplayConfig createDisplayConfig() {
    DisplayConfig config = new DisplayConfig();
    config.height = this.height;
    config.width = this.width;
    config.backgroundColor = new Color(255);
    return config;
  }

  private GameConfig createGameConfig(GameMode gameMode, InputDevice player1InputDevice,
      InputDevice player2InputDevice) {
    GameConfig config = new GameConfig();
    config.gameMode = gameMode;
    config.microbitDefence = creatMicrobitDefenceConfig();
    config.hideAndSeek = createHideAndSeekConfig();
    config.player1 = new Player("Player 1", player1InputDevice);
    config.player2 = new Player("Player 2", player2InputDevice);
    config.scoreToWin = 3;
    return config;
  }

  private MicrobitDefenceConfig creatMicrobitDefenceConfig() {
    MicrobitDefenceConfig config = new MicrobitDefenceConfig();

    config.attackers = new AttackersConfig();
    config.attackers.smallAttacker = new SingleAttackerConfig();
    config.attackers.smallAttacker.defaultFillColor = new Color(10);
    config.attackers.smallAttacker.defaultTextColor = new Color(255, 255, 0);
    config.attackers.smallAttacker.damagedFillColor = new Color(255, 0, 0);
    config.attackers.smallAttacker.damagedTextColor = new Color(0);
    config.attackers.smallAttacker.health = 1;
    config.attackers.smallAttacker.size = 60;
    config.attackers.smallAttacker.speed = 8;
    config.attackers.smallAttacker.damageDealt = DamageType.SMALL;
    config.attackers.smallAttacker.damagedDisplayDuration = 200;

    config.attackers.bigAttacker = new SingleAttackerConfig();
    config.attackers.bigAttacker.defaultFillColor = new Color(100);
    config.attackers.bigAttacker.defaultTextColor = new Color(255, 0, 255);
    config.attackers.bigAttacker.damagedFillColor = new Color(255, 0, 0);
    config.attackers.bigAttacker.damagedTextColor = new Color(0);
    config.attackers.bigAttacker.health = 3;
    config.attackers.bigAttacker.size = 180;
    config.attackers.bigAttacker.speed = 3;
    config.attackers.bigAttacker.damageDealt = DamageType.BIG;
    config.attackers.bigAttacker.damagedDisplayDuration = 200;

    config.defenceTarget = new DefenceTargetConfig();
    config.defenceTarget.damagedDisplayDuration = 400;
    config.defenceTarget.defaultImage = ImageLoader.loadImageFromFilepath(this, "./assets/microbit_happy.png");
    config.defenceTarget.damagedImage = ImageLoader.loadImageFromFilepath(this, "./assets/microbit_sad.png");
    config.defenceTarget.noImageDefaultColor = new Color(100, 255, 100);
    config.defenceTarget.noImageDamagedColor = new Color(255, 50, 50);
    config.defenceTarget.noImageHeight = 300;
    config.defenceTarget.noImageWidth = 300;

    return config;
  }

  private HideAndSeekConfig createHideAndSeekConfig() {
    HideAndSeekConfig config = new HideAndSeekConfig();

    config.duration = 15000;
    config.digitToDisplayUpTo = 9;

    return config;
  }

}
