package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class AA_0032_OR {
  @Test
  public void testTopLevelListOfIntegerCollectionsDeserialization() {
    Gson gson = new Gson();
    String json = "[[1,2,3],[4,5,6],[7,8,9]]";
    Type collectionType = new TypeToken<Collection<Collection<Integer>>>() {
    }.getType();
    List<Collection<Integer>> target = gson.fromJson(json, collectionType);

    int[][] expected = new int[3][3];
    for (int i = 0; i < 3; ++i) {
      int start = (3 * i) + 1;
      for (int j = 0; j < 3; ++j) {
        expected[i][j] = start + j;
      }
    }

    for (int i = 0; i < 3; i++) {
      assertThat(toIntArray(target.get(i))).isEqualTo(expected[i]);
    }
  }

  private static int[] toIntArray(Collection<?> collection) {
    int[] ints = new int[collection.size()];
    int i = 0;
    for (Iterator<?> iterator = collection.iterator(); iterator.hasNext(); ++i) {
      Object obj = iterator.next();
      if (obj instanceof Integer) {
        ints[i] = (Integer) obj;
      } else if (obj instanceof Long) {
        ints[i] = ((Long) obj).intValue();
      }
    }
    return ints;
  }
}
