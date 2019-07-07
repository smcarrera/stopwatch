package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implements Stopwatch interface, A thread-safe object that can be used for
 * timing laps
 * 
 * All Stopwatch objects should be created via the StopwatchFactory and not this
 * class directly. Instantiating a stopwatch object directly from this class
 * will not include: validation checks for the stopwatch object or ability to
 * add to the list of all created Stopwatch objects.
 * 
 * @author sc2936@nyu.edu
 *
 */
class ThreadSafeStopwatch implements Stopwatch {

  private final Object stopwatchLock;
  private final String id;
  private final List<Long> lapTimes;
  private long startTime;
  private long prevLapTime;
  private State state;

  private enum State {
    STOPPED, RUNNING
  }

  /**
   * StopwatchImplemented constructor. Stopwatch state can be only stoppedState or
   * runningState. Thread safe. Does not provide duplicate, empty, or null id
   * validation outside of StopwatchFactory. The stopwatch objects are created
   * with the StopwatchFactory.
   * 
   * @param id
   */
  ThreadSafeStopwatch(String id) {
    this.id = id;
    state = State.STOPPED;
    startTime = 0L;
    prevLapTime = 0L;
    lapTimes = new ArrayList<Long>();
    stopwatchLock = new Object();
  }
  
  /**
   * @inheritDoc
   */
  public String getId() {
      return this.id;
  }

  /**
   * @inheritDoc
   * sequentially stop() then start() function as a pause within a lap time.
   */
  public void start() {
    synchronized (stopwatchLock) {
      if (state == State.RUNNING) {
        throw new IllegalStateException("Already running");
      }

      state = State.RUNNING;
      if (lapTimes.isEmpty()) {
        startTime = System.currentTimeMillis();
        prevLapTime = startTime;
      }
      else {
        prevLapTime = System.currentTimeMillis() - lapTimes.remove(lapTimes.size() - 1);
      }
    }
  }

  /**
   * @inheritDoc
   */
  public void lap() {
    synchronized (stopwatchLock) {
      if (state == State.STOPPED) {
        throw new IllegalStateException("Must be running to call lap");
      }
      long currentTime = System.currentTimeMillis();
      lapTimes.add(new Long(currentTime - prevLapTime));
      prevLapTime = currentTime;
    }
  }

  /**
   * @inhertiDoc
   */
  public void stop() {
    synchronized (stopwatchLock) {
      if (state == State.STOPPED) {
        throw new IllegalStateException("Must be running to call stop");
      }
      lap();
      state = State.STOPPED;
    }
  }

  /**
   * @inheritDoc
   */
  public void reset() {
    synchronized (stopwatchLock) {
      state = State.STOPPED;
      startTime = 0L;
      prevLapTime = 0L;
      lapTimes.clear();
    }
  }

  /**
   * Returns a list of lap times (in milliseconds). This method can be called at
   * any time and will not throw an exception. The returned list is a copy and any
   * changes will not modify the stopwatches's lap time. The returned list is
   * mutable though.
   * 
   * @return a list of recorded lap times or an empty list.
   */
  public List<Long> getLapTimes() {
    synchronized (stopwatchLock) {
      List<Long> copyOfLapTimes = new ArrayList<Long>();
      Iterator<Long> iter = lapTimes.iterator();
      while (iter.hasNext()) {
        copyOfLapTimes.add(iter.next());
      }
      return copyOfLapTimes;
    }
  }

  @Override
  public String toString() {
    synchronized (stopwatchLock) {
      String stopwatchEntry = "Stopwatch ID: " + this.id + " | laptimes: " + lapTimes;
      return stopwatchEntry;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof ThreadSafeStopwatch)) {
      return false;
    }
    Stopwatch sw = (ThreadSafeStopwatch) obj;
    return this.id.equals(sw.getId());
  }

  @Override
  public int hashCode() {
    return 31 + 17 * id.hashCode();
  }

}
