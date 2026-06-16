package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AA_0037_RF extends AbstractLangTest {
  @ParameterizedTest
  @MethodSource("reverseCases")
  void testReverse(final String name, final Class<?> arrayType, final Object array, final Object expected) {
    reverse(arrayType, array);
    assertArrayState(expected, array);
  }

  @ParameterizedTest
  @MethodSource("reverseRangeCases")
  void testReverseRange(final String name, final Class<?> arrayType, final Object array, final int start,
      final int end, final Object expected) {
    reverse(arrayType, array, start, end);
    assertArrayState(expected, array);
  }

  private static Stream<Arguments> reverseCases() {
    final StringBuffer str1 = new StringBuffer("pick");
    final String str2 = "a";
    final String[] str3 = { "stick" };
    final String str4 = "up";
    return Stream.of(
        Arguments.of("Object[] length 3", Object[].class, new Object[] { str1, str2, str3 },
            new Object[] { str3, str2, str1 }),
        Arguments.of("Object[] length 4", Object[].class, new Object[] { str1, str2, str3, str4 },
            new Object[] { str4, str3, str2, str1 }),
        Arguments.of("Object[] null", Object[].class, null, null),
        Arguments.of("boolean[] reverse", boolean[].class, new boolean[] { false, false, true },
            new boolean[] { true, false, false }),
        Arguments.of("boolean[] null", boolean[].class, null, null),
        Arguments.of("byte[] reverse", byte[].class, new byte[] { 2, 3, 4 }, new byte[] { 4, 3, 2 }),
        Arguments.of("byte[] null", byte[].class, null, null),
        Arguments.of("char[] reverse", char[].class, new char[] { 'a', 'f', 'C' }, new char[] { 'C', 'f', 'a' }),
        Arguments.of("char[] null", char[].class, null, null),
        Arguments.of("double[] reverse", double[].class, new double[] { 0.3d, 0.4d, 0.5d },
            new double[] { 0.5d, 0.4d, 0.3d }),
        Arguments.of("double[] null", double[].class, null, null),
        Arguments.of("float[] reverse", float[].class, new float[] { 0.3f, 0.4f, 0.5f },
            new float[] { 0.5f, 0.4f, 0.3f }),
        Arguments.of("float[] null", float[].class, null, null),
        Arguments.of("int[] reverse", int[].class, new int[] { 1, 2, 3 }, new int[] { 3, 2, 1 }),
        Arguments.of("int[] null", int[].class, null, null),
        Arguments.of("long[] reverse", long[].class, new long[] { 1L, 2L, 3L }, new long[] { 3L, 2L, 1L }),
        Arguments.of("long[] null", long[].class, null, null),
        Arguments.of("short[] reverse", short[].class, new short[] { 1, 2, 3 }, new short[] { 3, 2, 1 }),
        Arguments.of("short[] null", short[].class, null, null));
  }

  private static Stream<Arguments> reverseRangeCases() {
    return Stream.of(
        Arguments.of("boolean[] whole", boolean[].class, new boolean[] { false, false, true }, 0, 3,
            new boolean[] { true, false, false }),
        Arguments.of("boolean[] range", boolean[].class, new boolean[] { false, false, true }, 0, 2,
            new boolean[] { false, false, true }),
        Arguments.of("boolean[] negative start", boolean[].class, new boolean[] { false, false, true }, -1, 3,
            new boolean[] { true, false, false }),
        Arguments.of("boolean[] large end", boolean[].class, new boolean[] { false, false, true }, -1, 1003,
            new boolean[] { true, false, false }),
        Arguments.of("boolean[] null", boolean[].class, null, 0, 3, null),
        Arguments.of("byte[] whole", byte[].class, new byte[] { 1, 2, 3 }, 0, 3, new byte[] { 3, 2, 1 }),
        Arguments.of("byte[] range", byte[].class, new byte[] { 1, 2, 3 }, 0, 2, new byte[] { 2, 1, 3 }),
        Arguments.of("byte[] negative start", byte[].class, new byte[] { 1, 2, 3 }, -1, 3, new byte[] { 3, 2, 1 }),
        Arguments.of("byte[] large end", byte[].class, new byte[] { 1, 2, 3 }, -1, 1003, new byte[] { 3, 2, 1 }),
        Arguments.of("byte[] null", byte[].class, null, 0, 3, null),
        Arguments.of("char[] whole", char[].class, new char[] { 1, 2, 3 }, 0, 3, new char[] { 3, 2, 1 }),
        Arguments.of("char[] range", char[].class, new char[] { 1, 2, 3 }, 0, 2, new char[] { 2, 1, 3 }),
        Arguments.of("char[] negative start", char[].class, new char[] { 1, 2, 3 }, -1, 3, new char[] { 3, 2, 1 }),
        Arguments.of("char[] large end", char[].class, new char[] { 1, 2, 3 }, -1, 1003, new char[] { 3, 2, 1 }),
        Arguments.of("char[] null", char[].class, null, 0, 3, null),
        Arguments.of("double[] whole", double[].class, new double[] { 1, 2, 3 }, 0, 3, new double[] { 3, 2, 1 }),
        Arguments.of("double[] range", double[].class, new double[] { 1, 2, 3 }, 0, 2, new double[] { 2, 1, 3 }),
        Arguments.of("double[] negative start", double[].class, new double[] { 1, 2, 3 }, -1, 3,
            new double[] { 3, 2, 1 }),
        Arguments.of("double[] large end", double[].class, new double[] { 1, 2, 3 }, -1, 1003,
            new double[] { 3, 2, 1 }),
        Arguments.of("double[] null", double[].class, null, 0, 3, null),
        Arguments.of("float[] whole", float[].class, new float[] { 1, 2, 3 }, 0, 3, new float[] { 3, 2, 1 }),
        Arguments.of("float[] range", float[].class, new float[] { 1, 2, 3 }, 0, 2, new float[] { 2, 1, 3 }),
        Arguments.of("float[] negative start", float[].class, new float[] { 1, 2, 3 }, -1, 3, new float[] { 3, 2, 1 }),
        Arguments.of("float[] large end", float[].class, new float[] { 1, 2, 3 }, -1, 1003, new float[] { 3, 2, 1 }),
        Arguments.of("float[] null", float[].class, null, 0, 3, null),
        Arguments.of("int[] whole", int[].class, new int[] { 1, 2, 3 }, 0, 3, new int[] { 3, 2, 1 }),
        Arguments.of("int[] range", int[].class, new int[] { 1, 2, 3 }, 0, 2, new int[] { 2, 1, 3 }),
        Arguments.of("int[] negative start", int[].class, new int[] { 1, 2, 3 }, -1, 3, new int[] { 3, 2, 1 }),
        Arguments.of("int[] large end", int[].class, new int[] { 1, 2, 3 }, -1, 1003, new int[] { 3, 2, 1 }),
        Arguments.of("int[] null", int[].class, null, 0, 3, null),
        Arguments.of("long[] whole", long[].class, new long[] { 1, 2, 3 }, 0, 3, new long[] { 3, 2, 1 }),
        Arguments.of("long[] range", long[].class, new long[] { 1, 2, 3 }, 0, 2, new long[] { 2, 1, 3 }),
        Arguments.of("long[] negative start", long[].class, new long[] { 1, 2, 3 }, -1, 3, new long[] { 3, 2, 1 }),
        Arguments.of("long[] large end", long[].class, new long[] { 1, 2, 3 }, -1, 1003, new long[] { 3, 2, 1 }),
        Arguments.of("long[] null", long[].class, null, 0, 3, null),
        Arguments.of("Object[] whole", Object[].class, new String[] { "1", "2", "3" }, 0, 3,
            new String[] { "3", "2", "1" }),
        Arguments.of("Object[] range", Object[].class, new String[] { "1", "2", "3" }, 0, 2,
            new String[] { "2", "1", "3" }),
        Arguments.of("Object[] negative start", Object[].class, new String[] { "1", "2", "3" }, -1, 3,
            new String[] { "3", "2", "1" }),
        Arguments.of("Object[] large end", Object[].class, new String[] { "1", "2", "3" }, -1, 1003,
            new String[] { "3", "2", "1" }),
        Arguments.of("Object[] null", Object[].class, null, 0, 3, null),
        Arguments.of("short[] whole", short[].class, new short[] { 1, 2, 3 }, 0, 3, new short[] { 3, 2, 1 }),
        Arguments.of("short[] range", short[].class, new short[] { 1, 2, 3 }, 0, 2, new short[] { 2, 1, 3 }),
        Arguments.of("short[] negative start", short[].class, new short[] { 1, 2, 3 }, -1, 3, new short[] { 3, 2, 1 }),
        Arguments.of("short[] large end", short[].class, new short[] { 1, 2, 3 }, -1, 1003, new short[] { 3, 2, 1 }),
        Arguments.of("short[] null", short[].class, null, 0, 3, null));
  }

  private static void reverse(final Class<?> arrayType, final Object array) {
    try {
      final Method method = ArrayUtils.class.getMethod("reverse", arrayType);
      method.invoke(null, array);
    } catch (final ReflectiveOperationException e) {
      throw new AssertionError("reverse invocation failed for " + arrayType, e);
    }
  }

  private static void reverse(final Class<?> arrayType, final Object array, final int start, final int end) {
    try {
      final Method method = ArrayUtils.class.getMethod("reverse", arrayType, int.class, int.class);
      method.invoke(null, array, start, end);
    } catch (final ReflectiveOperationException e) {
      throw new AssertionError("reverse(range) invocation failed for " + arrayType, e);
    }
  }

  private static void assertArrayState(final Object expected, final Object actual) {
    if (expected == null) {
      assertNull(actual);
      return;
    }
    assertTrue(Arrays.deepEquals(new Object[] { expected }, new Object[] { actual }));
  }
}
