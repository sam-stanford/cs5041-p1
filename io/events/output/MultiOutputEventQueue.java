package io.events.output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiOutputEventQueue implements OutputEventQueue {

  private List<OutputEventQueue> queues;
  private Map<String, OutputEventQueue> queueMap;

  public MultiOutputEventQueue(String[] queueKeys) {
    queues = new ArrayList<>();
    queueMap = new HashMap<>();
    for (String key : queueKeys) {
      OutputEventQueue q = new SingleOutputEventQueue();
      queues.add(q);
      queueMap.put(key, q);
    }
  }

  @Override
  public OutputEvent peek() {
    for (OutputEventQueue q : queues) {
      OutputEvent e = q.peek();
      if (e != null) {
        return e;
      }
    }
    return null;
  }

  public OutputEvent peek(String queueKey) {
    if (!queueMap.containsKey(queueKey)) {
      return null;
    }
    return queueMap.get(queueKey).peek();
  }

  @Override
  public OutputEvent poll() {
    for (OutputEventQueue q : queues) {
      OutputEvent e = q.poll();
      if (e != null) {
        return e;
      }
    }
    return null;
  }

  public OutputEvent poll(String queueKey) {
    if (!queueMap.containsKey(queueKey)) {
      return null;
    }
    return queueMap.get(queueKey).poll();
  }

  @Override
  public boolean isEmpty() {
    for (OutputEventQueue q : queues) {
      if (!q.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public boolean isEmpty(String queueKey) {
    if (!queueMap.containsKey(queueKey)) {
      return true;
    }
    return queueMap.get(queueKey).isEmpty();
  }

  @Override
  public void add(OutputEvent event) {
    for (OutputEventQueue q : queues) {
      q.add(event);
    }
  }
}
