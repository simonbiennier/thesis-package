package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AA_0036_RF extends AbstractLangTest {
  private boolean isSorted(final Class<?> arrayType, final Object array, final boolean expected) throws Exception {
    final Class<?> methodArrayType = arrayType.getComponentType().isPrimitive() ? arrayType : Comparable[].class;
    final Method method = ArrayUtils.class.getMethod("isSorted", methodArrayType);
    return (boolean) method.invoke(null, array);
  }

  @ParameterizedTest
  @MethodSource("isSortedCases")
  void testIsSorted(final String name, final Class<?> arrayType, final Object array, final boolean expected)
      throws Exception {
    final boolean isSorted = isSorted(arrayType, array, expected);
    assertEquals(expected, isSorted, name);
  }

  private static Stream<Arguments> isSortedCases() {
    return Stream.of(
        Arguments.of("Integer[] null", Integer[].class, null, true),
        Arguments.of("Integer[] single", Integer[].class, new Integer[] { 1 }, true),
        Arguments.of("Integer[] sorted", Integer[].class, new Integer[] { 1, 2, 3 }, true),
        Arguments.of("Integer[] unsorted", Integer[].class, new Integer[] { 1, 3, 2 }, false),
        Arguments.of("boolean[] null", boolean[].class, null, true),
        Arguments.of("boolean[] single", boolean[].class, new boolean[] { true }, true),
        Arguments.of("boolean[] sorted", boolean[].class, new boolean[] { false, true }, true),
        Arguments.of("boolean[] unsorted", boolean[].class, new boolean[] { true, false }, false),
        Arguments.of("byte[] null", byte[].class, null, true),
        Arguments.of("byte[] single", byte[].class, new byte[] { 0x10 }, true),
        Arguments.of("byte[] sorted", byte[].class, new byte[] { 0x10, 0x20, 0x30 }, true),
        Arguments.of("byte[] unsorted", byte[].class, new byte[] { 0x10, 0x30, 0x20 }, false),
        Arguments.of("char[] null", char[].class, null, true),
        Arguments.of("char[] single", char[].class, new char[] { 'a' }, true),
        Arguments.of("char[] sorted", char[].class, new char[] { 'a', 'b', 'c' }, true),
        Arguments.of("char[] unsorted", char[].class, new char[] { 'a', 'c', 'b' }, false),
        Arguments.of("double[] null", double[].class, null, true),
        Arguments.of("double[] single", double[].class, new double[] { 0.0 }, true),
        Arguments.of("double[] sorted", double[].class, new double[] { -1.0, 0.0, 0.1, 0.2 }, true),
        Arguments.of("double[] unsorted", double[].class, new double[] { -1.0, 0.2, 0.1, 0.0 }, false),
        Arguments.of("float[] null", float[].class, null, true),
        Arguments.of("float[] single", float[].class, new float[] { 0f }, true),
        Arguments.of("float[] sorted", float[].class, new float[] { -1f, 0f, 0.1f, 0.2f }, true),
        Arguments.of("float[] unsorted", float[].class, new float[] { -1f, 0.2f, 0.1f, 0f }, false),
        Arguments.of("int[] null", int[].class, null, true),
        Arguments.of("int[] single", int[].class, new int[] { 1 }, true),
        Arguments.of("int[] sorted", int[].class, new int[] { 1, 2, 3 }, true),
        Arguments.of("int[] unsorted", int[].class, new int[] { 1, 3, 2 }, false),
        Arguments.of("long[] null", long[].class, null, true),
        Arguments.of("long[] single", long[].class, new long[] { 0L }, true),
        Arguments.of("long[] sorted", long[].class, new long[] { -1L, 0L, 1L }, true),
        Arguments.of("long[] unsorted", long[].class, new long[] { -1L, 1L, 0L }, false),
        Arguments.of("short[] null", short[].class, null, true),
        Arguments.of("short[] single", short[].class, new short[] { 0 }, true),
        Arguments.of("short[] sorted", short[].class, new short[] { -1, 0, 1 }, true),
        Arguments.of("short[] unsorted", short[].class, new short[] { -1, 1, 0 }, false));
  }
}
