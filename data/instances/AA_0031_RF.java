package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Test;

public class AA_0041_RF {
  @Test
  public void testEntrySetInitiallyEmpty() {
    JsonObject o = new JsonObject();
    assertThat(o.entrySet()).hasSize(0);
  }

  @Test
  public void testEntrySetPreservesInsertionOrder() {
    JsonObject o = new JsonObject();
    o.addProperty("b", true);
    Set<?> expectedEntries = Collections.singleton(new SimpleEntry<>("b", new JsonPrimitive(true)));
    assertThat(o.entrySet()).isEqualTo(expectedEntries);
    assertThat(o.entrySet()).hasSize(1);
    o.addProperty("a", false);
    // Insertion order should be preserved by entrySet()
    List<?> expectedEntriesList = Arrays.asList(
        new SimpleEntry<>("b", new JsonPrimitive(true)),
        new SimpleEntry<>("a", new JsonPrimitive(false)));
    assertThat(new ArrayList<>(o.entrySet())).isEqualTo(expectedEntriesList);
  }

  @Test
  public void testEntrySetEntrySetValueUpdatesBackingObject() {
    JsonObject o = createObjectWithBaseEntries();
    Iterator<Entry<String, JsonElement>> iterator = o.entrySet().iterator();
    // Test behavior of Entry.setValue
    for (int i = 0; i < o.size(); i++) {
      Entry<String, JsonElement> entry = iterator.next();
      entry.setValue(new JsonPrimitive(i));
      assertThat(entry.getValue()).isEqualTo(new JsonPrimitive(i));
    }
    List<?> expectedEntriesList = Arrays.asList(
        new SimpleEntry<>("b", new JsonPrimitive(0)),
        new SimpleEntry<>("a", new JsonPrimitive(1)));
    assertThat(new ArrayList<>(o.entrySet())).isEqualTo(expectedEntriesList);
  }

  @Test
  public void testEntrySetEntrySetValueRejectsNull() {
    JsonObject o = createObjectWithBaseEntries();
    Entry<String, JsonElement> entry = o.entrySet().iterator().next();
    // null value is not permitted, only JsonNull is supported
    // This intentionally deviates from the behavior of the other JsonObject methods which
    // implicitly convert null -> JsonNull, to match more closely the contract of Map.Entry
    NullPointerException e = assertThrows(NullPointerException.class, () -> entry.setValue(null));
    assertThat(e).hasMessageThat().isEqualTo("value == null");
    assertThat(entry.getValue()).isNotNull();
  }

  @Test
  public void testEntrySetIteratorRemoveUpdatesObjectAndIterationOrder() {
    JsonObject o = new JsonObject();
    o.addProperty("b", 0);
    o.addProperty("a", 1);
    o.addProperty("key1", 1);
    o.addProperty("key2", 2);
    Deque<?> expectedEntriesQueue = new ArrayDeque<>(
        Arrays.asList(
            new SimpleEntry<>("b", new JsonPrimitive(0)),
            new SimpleEntry<>("a", new JsonPrimitive(1)),
            new SimpleEntry<>("key1", new JsonPrimitive(1)),
            new SimpleEntry<>("key2", new JsonPrimitive(2))));
    // Note: Must wrap in ArrayList because Deque implementations do not implement `equals`
    assertThat(new ArrayList<>(o.entrySet())).isEqualTo(new ArrayList<>(expectedEntriesQueue));
    Iterator<Entry<String, JsonElement>> iterator = o.entrySet().iterator();
    // Remove entries one by one
    for (int i = o.size(); i >= 1; i--) {
      assertThat(iterator.hasNext()).isTrue();
      assertThat(iterator.next()).isEqualTo(expectedEntriesQueue.getFirst());
      iterator.remove();
      expectedEntriesQueue.removeFirst();
      assertThat(o.size()).isEqualTo(i - 1);
      assertThat(new ArrayList<>(o.entrySet())).isEqualTo(new ArrayList<>(expectedEntriesQueue));
    }
  }

  private static JsonObject createObjectWithBaseEntries() {
    JsonObject o = new JsonObject();
    o.addProperty("b", true);
    o.addProperty("a", false);
    return o;
  }
}
