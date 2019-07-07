package edu.nyu.pqs.stopwatch.api;

import java.util.List;

/**
 *
 * A thread-safe object that can be used for timing laps.  The stopwatch
 * objects are created in the StopwatchFactory.  Different threads can
 * share a single stopwatch object and safely call any of the stopwatch methods.
 *
 */
public interface Stopwatch {

  /**
   * Returns the Id of this stopwatch
   * @return the Id of this stopwatch.  Will never be empty or null.
   */
  public String getId();

  /**
   * Starts the stopwatch.
   * @throws IllegalStateException thrown when the stopwatch is already running
   */
  public void start();

  /**
   * Stores the time elapsed since the last time lap() was called
   * or since start() was called if this is the first lap.
   * @throws IllegalStateException thrown when the stopwatch isn't running
   */
  public void lap();

  /**
   * Stops the stopwatch (and records one final lap).
   * @throws IllegalStateException thrown when the stopwatch isn't running
   */
  public void stop();

  /**
   * Resets the stopwatch.  If the stopwatch is running, this method stops the
   * watch and resets it.  This also clears all recorded laps.
   */
  public void reset();

  /**
   * Returns a list of lap times (in milliseconds).  This method can be called at
   * any time and will not throw an exception.
   * @return a list of recorded lap times or an empty list.
   */
  public List<Long> getLapTimes();
}
