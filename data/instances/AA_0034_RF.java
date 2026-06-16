package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AA_0034_RF extends AbstractLangTest {
  private Object contains(final Object array, final Object value) throws Exception {
    return ArrayUtils.class.getMethod("contains", array.getClass(), array.getClass().getComponentType()).invoke(null, array, value);
  }

  @ParameterizedTest
  @MethodSource("containsCases")
  void testContains(final String name, final Class<?> arrayType, final Object array, final Object value,
      final boolean expected) throws Exception {
    assertEquals(expected, contains(array, value), name);
  }

  private static Stream<Arguments> containsCases() {
    final double[] doubleNaN = { Double.NEGATIVE_INFINITY, Double.NaN, Double.POSITIVE_INFINITY };
    final float[] floatNaN = { Float.NEGATIVE_INFINITY, Float.NaN, Float.POSITIVE_INFINITY };

    return Stream.of(
        Arguments.of("boolean null", boolean[].class, null, true, false),
        Arguments.of("boolean true present", boolean[].class, new boolean[] { true, false, true }, true, true),
        Arguments.of("boolean false present", boolean[].class, new boolean[] { true, false, true }, false, true),
        Arguments.of("boolean false absent", boolean[].class, new boolean[] { true, true }, false, false),

        Arguments.of("byte null", byte[].class, null, (byte) 1, false),
        Arguments.of("byte 0 present", byte[].class, new byte[] { 0, 1, 2, 3, 0 }, (byte) 0, true),
        Arguments.of("byte 1 present", byte[].class, new byte[] { 0, 1, 2, 3, 0 }, (byte) 1, true),
        Arguments.of("byte 2 present", byte[].class, new byte[] { 0, 1, 2, 3, 0 }, (byte) 2, true),
        Arguments.of("byte 3 present", byte[].class, new byte[] { 0, 1, 2, 3, 0 }, (byte) 3, true),
        Arguments.of("byte 99 absent", byte[].class, new byte[] { 0, 1, 2, 3, 0 }, (byte) 99, false),

        Arguments.of("char null", char[].class, null, 'b', false),
        Arguments.of("char a present", char[].class, new char[] { 'a', 'b', 'c', 'd', 'a' }, 'a', true),
        Arguments.of("char b present", char[].class, new char[] { 'a', 'b', 'c', 'd', 'a' }, 'b', true),
        Arguments.of("char c present", char[].class, new char[] { 'a', 'b', 'c', 'd', 'a' }, 'c', true),
        Arguments.of("char d present", char[].class, new char[] { 'a', 'b', 'c', 'd', 'a' }, 'd', true),
        Arguments.of("char e absent", char[].class, new char[] { 'a', 'b', 'c', 'd', 'a' }, 'e', false),

        Arguments.of("double null", double[].class, null, 1d, false),
        Arguments.of("double 0 present", double[].class, new double[] { 0, 1, 2, 3, 0 }, 0d, true),
        Arguments.of("double 1 present", double[].class, new double[] { 0, 1, 2, 3, 0 }, 1d, true),
        Arguments.of("double 2 present", double[].class, new double[] { 0, 1, 2, 3, 0 }, 2d, true),
        Arguments.of("double 3 present", double[].class, new double[] { 0, 1, 2, 3, 0 }, 3d, true),
        Arguments.of("double 99 absent", double[].class, new double[] { 0, 1, 2, 3, 0 }, 99d, false),
        Arguments.of("double positive infinity", double[].class, doubleNaN, Double.POSITIVE_INFINITY, true),
        Arguments.of("double negative infinity", double[].class, doubleNaN, Double.NEGATIVE_INFINITY, true),
        Arguments.of("double NaN", double[].class, doubleNaN, Double.NaN, true),

        Arguments.of("float null", float[].class, null, 1f, false),
        Arguments.of("float 0 present", float[].class, new float[] { 0, 1, 2, 3, 0 }, 0f, true),
        Arguments.of("float 1 present", float[].class, new float[] { 0, 1, 2, 3, 0 }, 1f, true),
        Arguments.of("float 2 present", float[].class, new float[] { 0, 1, 2, 3, 0 }, 2f, true),
        Arguments.of("float 3 present", float[].class, new float[] { 0, 1, 2, 3, 0 }, 3f, true),
        Arguments.of("float 99 absent", float[].class, new float[] { 0, 1, 2, 3, 0 }, 99f, false),
        Arguments.of("float positive infinity", float[].class, floatNaN, Float.POSITIVE_INFINITY, true),
        Arguments.of("float negative infinity", float[].class, floatNaN, Float.NEGATIVE_INFINITY, true),
        Arguments.of("float NaN", float[].class, floatNaN, Float.NaN, true),

        Arguments.of("int null", int[].class, null, 1, false),
        Arguments.of("int 0 present", int[].class, new int[] { 0, 1, 2, 3, 0 }, 0, true),
        Arguments.of("int 1 present", int[].class, new int[] { 0, 1, 2, 3, 0 }, 1, true),
        Arguments.of("int 2 present", int[].class, new int[] { 0, 1, 2, 3, 0 }, 2, true),
        Arguments.of("int 3 present", int[].class, new int[] { 0, 1, 2, 3, 0 }, 3, true),
        Arguments.of("int 99 absent", int[].class, new int[] { 0, 1, 2, 3, 0 }, 99, false),

        Arguments.of("long null", long[].class, null, 1L, false),
        Arguments.of("long 0 present", long[].class, new long[] { 0, 1, 2, 3, 0 }, 0L, true),
        Arguments.of("long 1 present", long[].class, new long[] { 0, 1, 2, 3, 0 }, 1L, true),
        Arguments.of("long 2 present", long[].class, new long[] { 0, 1, 2, 3, 0 }, 2L, true),
        Arguments.of("long 3 present", long[].class, new long[] { 0, 1, 2, 3, 0 }, 3L, true),
        Arguments.of("long 99 absent", long[].class, new long[] { 0, 1, 2, 3, 0 }, 99L, false),

        Arguments.of("short null", short[].class, null, (short) 1, false),
        Arguments.of("short 0 present", short[].class, new short[] { 0, 1, 2, 3, 0 }, (short) 0, true),
        Arguments.of("short 1 present", short[].class, new short[] { 0, 1, 2, 3, 0 }, (short) 1, true),
        Arguments.of("short 2 present", short[].class, new short[] { 0, 1, 2, 3, 0 }, (short) 2, true),
        Arguments.of("short 3 present", short[].class, new short[] { 0, 1, 2, 3, 0 }, (short) 3, true),
        Arguments.of("short 99 absent", short[].class, new short[] { 0, 1, 2, 3, 0 }, (short) 99, false));
  }
}
