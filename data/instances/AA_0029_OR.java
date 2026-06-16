package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import java.util.Random;
import org.junit.Test;

public final class AA_0029_OR {
  @Test
  public void testLargeSetOfRandomKeys() {
    Random random = new Random(1367593214724L);
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    String[] keys = new String[1000];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
      map.put(keys[i], String.valueOf(i));
    }

    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      assertThat(map.containsKey(key)).isTrue();
      assertThat(map.get(key)).isEqualTo(String.valueOf(i));
    }
  }
}
