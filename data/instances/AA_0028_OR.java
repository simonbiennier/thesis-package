package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;

/**
 * Functional tests for Json serialization and deserialization of arrays.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public class AA_0028_OR {
  @Test
  public void testArrayOfCollectionSerialization() {
    Gson gson = new Gson();
    StringBuilder sb = new StringBuilder("[");
    int arraySize = 3;

    Type typeToSerialize = new TypeToken<Collection<Integer>[]>() {
    }.getType();
    @SuppressWarnings({ "unchecked" })
    Collection<Integer>[] arrayOfCollection = new ArrayList[arraySize];
    for (int i = 0; i < arraySize; ++i) {
      int startValue = (3 * i) + 1;
      sb.append('[').append(startValue).append(',').append(startValue + 1).append(']');
      ArrayList<Integer> tmpList = new ArrayList<>();
      tmpList.add(startValue);
      tmpList.add(startValue + 1);
      arrayOfCollection[i] = tmpList;

      if (i < arraySize - 1) {
        sb.append(',');
      }
    }
    sb.append(']');

    String json = gson.toJson(arrayOfCollection, typeToSerialize);
    assertThat(json).isEqualTo(sb.toString());
  }
}
