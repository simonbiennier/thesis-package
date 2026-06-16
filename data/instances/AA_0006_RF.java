package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArrayUtils}.
 */
@SuppressWarnings("deprecation") // deliberate use of deprecated code
class AA_0006_RF {
  private static final class TestClass {
    // empty
  }

  /**
   * A predefined seed used to initialize {@link Random} in order to get
   * predictable results
   */
  private static final long SEED = 16111981L;

  private static Random seededRandom() {
    return new Random(SEED);
  }

  @SafeVarargs
  private static <T> T[] toArrayPropagatingType(final T... items) {
    return ArrayUtils.toArray(items);
  }

  private void assertIsEquals(final Object array1, final Object array2, final Object array3) {
    assertTrue(ArrayUtils.isEquals(array1, array1));
    assertTrue(ArrayUtils.isEquals(array2, array2));
    assertTrue(ArrayUtils.isEquals(array3, array3));
    assertFalse(ArrayUtils.isEquals(array1, array2));
    assertFalse(ArrayUtils.isEquals(array2, array1));
    assertFalse(ArrayUtils.isEquals(array1, array3));
    assertFalse(ArrayUtils.isEquals(array3, array1));
    assertFalse(ArrayUtils.isEquals(array1, array2));
    assertFalse(ArrayUtils.isEquals(array2, array1));
  }

  @Test
  void testArrayCreation() {
    final String[] array = ArrayUtils.toArray("foo", "bar");
    assertEquals(2, array.length);
    assertEquals("foo", array[0]);
    assertEquals("bar", array[1]);
  }
}