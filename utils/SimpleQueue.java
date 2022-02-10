package utils;

public interface SimpleQueue<T> {

  public void add(T t);

  public T peek();

  public T poll();

  public boolean isEmpty();
}
