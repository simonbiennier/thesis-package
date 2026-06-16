package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;

/**
 * Functional tests for Json serialization and deserialization of arrays.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public class AA_0028_RF {
  @Test
  public void testArrayOfCollectionSerialization() {
    Gson gson = new Gson();

    Type typeToSerialize = new TypeToken<Collection<Integer>[]>() {
    }.getType();
    @SuppressWarnings("unchecked")
    Collection<Integer>[] arrayOfCollection = (Collection<Integer>[]) new Collection<?>[] {
        Arrays.asList(1, 2), Arrays.asList(4, 5), Arrays.asList(7, 8)
    };

    String json = gson.toJson(arrayOfCollection, typeToSerialize);
    assertThat(json).isEqualTo("[[1,2],[4,5],[7,8]]");
  }
}
