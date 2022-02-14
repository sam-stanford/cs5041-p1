
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import config.display.DisplayConfig;
import config.game.GameConfig;
import config.game.microbitdefence.MicrobitDefenceConfig;
import config.game.microbitdefence.attacker.AttackersConfig;
import config.game.microbitdefence.attacker.SingleAttackerConfig;
import config.game.microbitdefence.defence.DefenceTargetConfig;
import images.microbit.MicrobitColor;
import interactions.EndScreen;
import interactions.Interaction;
import interactions.IntroScreen;
import interactions.game.Game;
import interactions.game.Player;
import interactions.game.microbitdefence.attacker.DamageType;
import interactions.selection.Selection;
import interactions.selection.SelectionType;
import io.devices.DisplayOnlyMicrobit;
import io.devices.IODevice;
import io.devices.IODeviceType;
import io.devices.Keyboard;
import io.devices.Microbit;
import io.devices.input.InputDevice;
import io.devices.serial.SerialDevice;
import io.events.InputEvent;
import processing.core.PApplet;
import processing.serial.Serial;
import utils.Color;

public class Program extends PApplet {

  private Keyboard keyboard = new Keyboard();
  private Interaction currentInteraction;

  private int playerCount;
  private List<IODevice> playerIoDevices = new ArrayList<>();
  private List<MicrobitColor> playerColors = new ArrayList<>();

  private IODeviceType previosulySelectedDeviceType;

  public static void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Program" };
    PApplet.main(appletArgs);
  }

  public Program() {
    currentInteraction = new IntroScreen();
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
    String input = "k_" + Character.toString(this.key);
    InputEvent e = InputEvent.fromEventValue(input);
    if (e.isValid()) {
      keyboard.addInputEvent(e);
    }
  }

  // Called by Processing:
  // https://processing.org/reference/libraries/serial/Serial_serialEvent_.html
  public void serialEvent(Serial serial) {
    String message = serial.readStringUntil(':');
    if (message == null) {
      return;
    }

    message = message.substring(0, message.length() - 1);
    InputEvent event = InputEvent.fromEventValue(message);
    if (!event.isValid()) {
      return;
    }

    String serialPortName = serial.port.getPortName();
    InputDevice d = getMatchingIoDeviceForSerialPort(serialPortName);
    if (d != null) {
      d.addInputEvent(event);
    }
  }

  private InputDevice getMatchingIoDeviceForSerialPort(String portName) {
    for (IODevice device : playerIoDevices) {
      if (device instanceof SerialDevice) {
        SerialDevice s = (SerialDevice) device;
        if (s.getSerialPortName().equals(portName)) {
          return device;
        }
      }
    }
    return null;
  }

  @Override
  public void draw() {
    if (currentInteraction.isDone()) {
      startNextInteraction();
    }
    currentInteraction.draw(this);
  }

  private void startNextInteraction() {
    resetCursorState();
    switch (currentInteraction.getType()) {
      case INTRO_SCREEN:
        currentInteraction = createPlayerCountSelection();
        return;
      case SELECTION:
        Selection<?> s = (Selection<?>) currentInteraction;
        switch (s.getSelectionType()) {
          case PLAYER_COUNT:
            this.playerCount = (int) s.getSelectedValue();
            this.currentInteraction = createIoDeviceTypeSelection();
            break;

          case IO_DEVICE_TYPE:
            IODeviceType selectedType = (IODeviceType) s.getSelectedValue();
            saveSelectedDeviceType(selectedType);
            if (selectedType == IODeviceType.KEYBOARD) {
              if (playerIoDevices.size() == playerCount) {
                currentInteraction = createPlayerColorSelection();
                break;
              }
              currentInteraction = createIoDeviceTypeSelection();
              break;
            }
            currentInteraction = createSerialPortSelection();
            break;

          case SERIAL_PORT:
            String serialPortName = (String) s.getSelectedValue();
            IODevice device = null;
            switch (previosulySelectedDeviceType) {
              case MICROBIT:
                device = new Microbit(this, serialPortName);
                break;
              case DISPLAY_ONLY_MICROBIT:
                device = new DisplayOnlyMicrobit(this, serialPortName);
                break;
              default:
                break;
            }
            if (device != null) {
              playerIoDevices.add(device);
            }
            if (playerIoDevices.size() == playerCount) {
              currentInteraction = createPlayerColorSelection();
              break;
            }
            currentInteraction = createIoDeviceTypeSelection();
            break;

          case COLOR:
            MicrobitColor c = (MicrobitColor) s.getSelectedValue();
            playerColors.add(c);
            if (playerColors.size() != playerCount) {
              currentInteraction = createPlayerColorSelection();
              break;
            }
            currentInteraction = createGame();
            break;
        }
        break;

      case GAME:
        Game g = (Game) currentInteraction;
        currentInteraction = new EndScreen(createDisplayConfig(), g.getEndGameText());

      case END_SCREEN:
        break;

      default:
        break;
    }
  }

  private void resetCursorState() {
    this.cursor(PApplet.ARROW);
  }

  private void saveSelectedDeviceType(IODeviceType selectedType) {
    switch (selectedType) {
      case KEYBOARD:
        this.playerIoDevices.add(keyboard);
        return;
      case MICROBIT:
        this.previosulySelectedDeviceType = IODeviceType.MICROBIT;
        break;
      case DISPLAY_ONLY_MICROBIT:
        this.previosulySelectedDeviceType = IODeviceType.DISPLAY_ONLY_MICROBIT;
        break;
    }
  }

  private Selection<Integer> createPlayerCountSelection() {
    List<Integer> options = Arrays.asList(new Integer[] { 1, 2, 3, 4 });
    return new Selection<>(
        createDisplayConfig(),
        "Select the Number of Players",
        SelectionType.PLAYER_COUNT,
        options);
  }

  private Selection<IODeviceType> createIoDeviceTypeSelection() {
    List<IODeviceType> options = new ArrayList<>();
    if (keyboardHasNotBeenSelected()) {
      options.add(IODeviceType.KEYBOARD);
    }
    options.addAll(Arrays.asList(new IODeviceType[] {
        IODeviceType.MICROBIT, IODeviceType.DISPLAY_ONLY_MICROBIT,
    }));
    String playerName = "Player " + (playerIoDevices.size() + 1);
    return new Selection<>(
        createDisplayConfig(),
        "Select Device for " + playerName,
        SelectionType.IO_DEVICE_TYPE,
        options);
  }

  private boolean keyboardHasNotBeenSelected() {
    for (IODevice d : playerIoDevices) {
      if (d.equals(keyboard)) {
        return false;
      }
    }
    return true;
  }

  private Selection<String> createSerialPortSelection() {
    String playerName = "Player " + (playerIoDevices.size() + 1);
    return new Selection<String>(
        createDisplayConfig(),
        "Select Serial Port for " + playerName,
        SelectionType.SERIAL_PORT,
        getAvailableSerialPorts());
  }

  private List<String> getAvailableSerialPorts() {
    List<String> ports = new ArrayList<>();
    ports.addAll(Arrays.asList(Serial.list()));

    for (SerialDevice d : getPlayerSerialDevices()) {
      ports.remove(d.getSerialPortName());
    }
    return ports;
  }

  private List<SerialDevice> getPlayerSerialDevices() {
    List<SerialDevice> devices = new ArrayList<>();
    for (IODevice d : playerIoDevices) {
      if (d instanceof SerialDevice) {
        devices.add((SerialDevice) d);
      }
    }
    return devices;
  }

  private Selection<MicrobitColor> createPlayerColorSelection() {
    List<MicrobitColor> options = getAvailableColors();
    String playerName = "Player " + (playerColors.size() + 1);
    return new Selection<MicrobitColor>(
        createDisplayConfig(),
        "Select a Colour for " + playerName,
        SelectionType.COLOR,
        options);
  }

  private List<MicrobitColor> getAvailableColors() {
    List<MicrobitColor> colors = new ArrayList<>();
    colors.addAll(Arrays.asList(new MicrobitColor[] {
        MicrobitColor.BLUE, MicrobitColor.YELLOW, MicrobitColor.GREEN, MicrobitColor.RED,
    }));
    colors.removeAll(playerColors);
    return colors;
  }

  private DisplayConfig createDisplayConfig() {
    DisplayConfig config = new DisplayConfig();
    config.height = this.height;
    config.width = this.width;
    config.backgroundColor = new Color(255);
    config.defaultTextColor = new Color(40);
    config.bigTextSize = 100;
    config.smallTextSize = 32;
    return config;
  }

  private Game createGame() {
    return new Game(createGameConfig(), createDisplayConfig());
  }

  private GameConfig createGameConfig() {
    GameConfig config = new GameConfig();
    config.microbitDefence = creatMicrobitDefenceConfig();
    config.players = createPlayers();
    config.scoreToWin = 3;
    config.roundEndScreenDuration = 3000;
    return config;
  }

  private List<Player> createPlayers() {
    List<Player> players = new ArrayList<>();
    for (int i = 0; i < playerCount; i += 1) {
      String name = "Player " + (i + 1);
      IODevice ioDevice = playerIoDevices.get(i);
      MicrobitColor color = playerColors.get(i);
      Player p = new Player(name, ioDevice, color);
      players.add(p);
    }
    return players;
  }

  private MicrobitDefenceConfig creatMicrobitDefenceConfig() {
    MicrobitDefenceConfig config = new MicrobitDefenceConfig();
    config.maxPlayers = 4;
    config.rampUpTime = 15000;

    config.attackers = new AttackersConfig();
    config.attackers.smallAttacker = new SingleAttackerConfig();
    config.attackers.smallAttacker.defaultFillColor = new Color(250);
    config.attackers.smallAttacker.defaultTextColor = new Color(40);
    config.attackers.smallAttacker.damagedFillColor = new Color(255, 0, 0);
    config.attackers.smallAttacker.damagedTextColor = new Color(40);
    config.attackers.smallAttacker.health = 1;
    config.attackers.smallAttacker.size = 100;
    config.attackers.smallAttacker.speed = 3;
    config.attackers.smallAttacker.damageDealt = DamageType.SMALL;
    config.attackers.smallAttacker.damagedDisplayDuration = 200;
    config.attackers.smallAttacker.textSize = 30;
    config.attackers.smallAttacker.textLeading = 25;
    config.attackers.smallAttacker.minSpawnFrequency = 0.002f;
    config.attackers.smallAttacker.maxSpawnFrequency = 0.1f;

    config.attackers.bigAttacker = new SingleAttackerConfig();
    config.attackers.bigAttacker.defaultFillColor = new Color(40);
    config.attackers.bigAttacker.defaultTextColor = new Color(250);
    config.attackers.bigAttacker.damagedFillColor = new Color(255, 0, 0);
    config.attackers.bigAttacker.damagedTextColor = new Color(40);
    config.attackers.bigAttacker.health = 3;
    config.attackers.bigAttacker.size = 260;
    config.attackers.bigAttacker.speed = 1.5f;
    config.attackers.bigAttacker.damageDealt = DamageType.BIG;
    config.attackers.bigAttacker.damagedDisplayDuration = 200;
    config.attackers.bigAttacker.textSize = 30;
    config.attackers.bigAttacker.textLeading = 25;
    config.attackers.bigAttacker.minSpawnFrequency = 0.0001f;
    config.attackers.bigAttacker.maxSpawnFrequency = 0.05f;

    config.defenceTarget = new DefenceTargetConfig();
    config.defenceTarget.damagedDisplayDuration = 400;
    config.defenceTarget.noImageDefaultColor = new Color(100, 255, 100);
    config.defenceTarget.noImageDamagedColor = new Color(255, 50, 50);
    config.defenceTarget.noImageHeight = 300;
    config.defenceTarget.noImageWidth = 300;
    config.defenceTarget.initialHealth = 100;

    return config;
  }

}
