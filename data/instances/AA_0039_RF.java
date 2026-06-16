package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class AA_0039_RF extends AbstractLangTest {
  private static final List<Sample> samples = Arrays.asList(
      new Sample("obj-null", null),
      new Sample("obj-empty", new Object[] {}),
      new Sample("obj-one", new Object[] { "pick" }),
      new Sample("obj-two", new Object[] { "pick", "stick" }),
      new Sample("boolean-null", null),
      new Sample("boolean-empty", new boolean[] {}),
      new Sample("boolean-one", new boolean[] { true }),
      new Sample("boolean-two", new boolean[] { true, false }),
      new Sample("long-null", null),
      new Sample("long-empty", new long[] {}),
      new Sample("long-one", new long[] { 0L }),
      new Sample("long-two", new long[] { 0L, 76L }),
      new Sample("int-null", null),
      new Sample("int-empty", new int[] {}),
      new Sample("int-one", new int[] { 4 }),
      new Sample("int-two", new int[] { 5, 7 }),
      new Sample("short-null", null),
      new Sample("short-empty", new short[] {}),
      new Sample("short-one", new short[] { 4 }),
      new Sample("short-two", new short[] { 6, 8 }),
      new Sample("char-null", null),
      new Sample("char-empty", new char[] {}),
      new Sample("char-one", new char[] { 'f' }),
      new Sample("char-two", new char[] { 'd', 't' }),
      new Sample("byte-null", null),
      new Sample("byte-empty", new byte[] {}),
      new Sample("byte-one", new byte[] { 3 }),
      new Sample("byte-two", new byte[] { 4, 6 }),
      new Sample("double-null", null),
      new Sample("double-empty", new double[] {}),
      new Sample("double-one", new double[] { 1.3d }),
      new Sample("double-two", new double[] { 4.5d, 6.3d }),
      new Sample("float-null", null),
      new Sample("float-empty", new float[] {}),
      new Sample("float-one", new float[] { 2.5f }),
      new Sample("float-two", new float[] { 6.4f, 5.8f }));

  @Test
  void testSameLength() {
    for (final Sample left : samples) {
      for (final Sample right : samples) {
        final boolean expected = length(left.array) == length(right.array);
        assertEquals(expected, ArrayUtils.isSameLength(left.array, right.array), left.name + " vs " + right.name);
      }
    }
  }

  @Test
  void testSameLengthOverloads() throws Exception {
    assertOverloadCases("Object[]", Object[].class,
        new Object[] { null, new Object[] {}, new Object[] { "pick" }, new Object[] { "pick", "stick" } });
    assertOverloadCases("boolean[]", boolean[].class,
        new Object[] { null, new boolean[] {}, new boolean[] { true }, new boolean[] { true, false } });
    assertOverloadCases("byte[]", byte[].class,
        new Object[] { null, new byte[] {}, new byte[] { 3 }, new byte[] { 4, 6 } });
    assertOverloadCases("char[]", char[].class,
        new Object[] { null, new char[] {}, new char[] { 'f' }, new char[] { 'd', 't' } });
    assertOverloadCases("double[]", double[].class,
        new Object[] { null, new double[] {}, new double[] { 1.3d }, new double[] { 4.5d, 6.3d } });
    assertOverloadCases("float[]", float[].class,
        new Object[] { null, new float[] {}, new float[] { 2.5f }, new float[] { 6.4f, 5.8f } });
    assertOverloadCases("int[]", int[].class,
        new Object[] { null, new int[] {}, new int[] { 4 }, new int[] { 5, 7 } });
    assertOverloadCases("long[]", long[].class,
        new Object[] { null, new long[] {}, new long[] { 0L }, new long[] { 0L, 76L } });
    assertOverloadCases("short[]", short[].class,
        new Object[] { null, new short[] {}, new short[] { 4 }, new short[] { 6, 8 } });
  }

  private static void assertOverloadCases(final String typeName, final Class<?> arrayType, final Object[] values)
      throws Exception {
    final Method method = ArrayUtils.class.getMethod("isSameLength", arrayType, arrayType);
    for (int i = 0; i < values.length; i++) {
      for (int j = 0; j < values.length; j++) {
        final Object left = values[i];
        final Object right = values[j];
        final boolean expected = length(left) == length(right);
        assertEquals(expected, method.invoke(null, left, right), typeName + " case " + i + " vs " + j);
      }
    }
  }

  private static int length(final Object array) {
    return array == null ? 0 : Array.getLength(array);
  }

  private static final class Sample {
    private final String name;
    private final Object array;

    private Sample(final String name, final Object array) {
      this.name = name;
      this.array = array;
    }
  }
}