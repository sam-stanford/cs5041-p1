package io.events.output;

import java.util.LinkedList;
import java.util.Queue;

public class SingleOutputEventQueue implements OutputEventQueue {

  private Queue<OutputEvent> internalQueue;

  public SingleOutputEventQueue() {
    internalQueue = new LinkedList<>();
  }

  @Override
  public OutputEvent peek() {
    return internalQueue.peek();
  }

  @Override
  public OutputEvent poll() {
    return internalQueue.poll();
  }

  @Override
  public boolean isEmpty() {
    return internalQueue.isEmpty();
  }

  @Override
  public void add(OutputEvent event) {
    internalQueue.add(event);
  }
}
