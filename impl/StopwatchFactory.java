package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The StopwatchFactory is a thread-safe factory class for Stopwatch objects. It
 * maintains references to all created Stopwatch objects and provides a
 * convenient method for getting a list of those objects.
 *
 */
public class StopwatchFactory {

  private static Map<String, Stopwatch> allStopwatches = new ConcurrentHashMap<String, Stopwatch>();
  private static final Object allStopwatchesLock = new Object();

  private StopwatchFactory() {
  }

  /**
   * Creates and returns a new Stopwatch object
   * 
   * @param id
   *          The identifier of the new object
   * @return The new Stopwatch object
   * @throws IllegalArgumentException
   *           if <code>id</code> is empty, null, or already taken.
   */
  public static Stopwatch getStopwatch(String id) {
    synchronized (allStopwatchesLock) {
      if (id == null) {
        throw new IllegalStateException("id cannot be null");
      }
      if (id.isEmpty()) {
        throw new IllegalStateException("id cannot be empty");
      }
      if (allStopwatches.containsKey(id)) {
        throw new IllegalStateException("id already exists");
      }
      Stopwatch newStopwatch = new ThreadSafeStopwatch(id);
      allStopwatches.put(id, newStopwatch);
      return newStopwatch;
    }
  }

  /**
   * Returns a list of all created stopwatches
   * 
   * @throws NullPointerException 
   *         if listCopyOfStopwatches is ever null
   * @return a List of all created Stopwatch objects. Returns an empty list if no
   *         Stopwatches have been created. returned list is unmodifiable, will
   *         throw UnsupportedOperationException if attempted to modify.
   */
  public static List<Stopwatch> getStopwatches() {
    synchronized (allStopwatchesLock) {
      List<Stopwatch> listCopyOfStopwatches = new ArrayList<Stopwatch>(allStopwatches.values());
      List<Stopwatch> unmodifiableCopyStopwatches = Collections
          .unmodifiableList(listCopyOfStopwatches);
      return unmodifiableCopyStopwatches;
    }
  }

}
