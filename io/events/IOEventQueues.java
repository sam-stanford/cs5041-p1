package io.events;

import java.util.Queue;

public class IOEventQueues {
  public Queue<InputEvent> inputEventQueue;
  public Queue<OutputEvent> outputEventQueue;

  public IOEventQueues(Queue<InputEvent> inputEventQueue, Queue<OutputEvent> outputEventQueue) {
    this.inputEventQueue = inputEventQueue;
    this.outputEventQueue = outputEventQueue;
  }
}
