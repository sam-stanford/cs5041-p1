package io.events.input;

import java.util.LinkedList;
import java.util.Queue;

public class SingleInputEventQueue implements InputEventQueue {

  private Queue<InputEvent> internalQueue;

  public SingleInputEventQueue() {
    internalQueue = new LinkedList<>();
  }

  @Override
  public InputEvent peek() {
    return internalQueue.peek();
  }

  @Override
  public InputEvent poll() {
    return internalQueue.poll();
  }

  @Override
  public boolean isEmpty() {
    return internalQueue.isEmpty();
  }

  @Override
  public void add(InputEvent event) {
    internalQueue.add(event);
  }
}
