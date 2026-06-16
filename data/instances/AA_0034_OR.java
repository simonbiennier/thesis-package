package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AA_0034_OR extends AbstractLangTest {
  @Test
  void testContainsBoolean() {
    boolean[] array = null;
    assertFalse(ArrayUtils.contains(array, true));
    array = new boolean[] { true, false, true };
    assertTrue(ArrayUtils.contains(array, true));
    assertTrue(ArrayUtils.contains(array, false));
    array = new boolean[] { true, true };
    assertTrue(ArrayUtils.contains(array, true));
    assertFalse(ArrayUtils.contains(array, false));
  }

  @Test
  void testContainsByte() {
    byte[] array = null;
    assertFalse(ArrayUtils.contains(array, (byte) 1));
    array = new byte[] { 0, 1, 2, 3, 0 };
    assertTrue(ArrayUtils.contains(array, (byte) 0));
    assertTrue(ArrayUtils.contains(array, (byte) 1));
    assertTrue(ArrayUtils.contains(array, (byte) 2));
    assertTrue(ArrayUtils.contains(array, (byte) 3));
    assertFalse(ArrayUtils.contains(array, (byte) 99));
  }

  @Test
  void testContainsChar() {
    char[] array = null;
    assertFalse(ArrayUtils.contains(array, 'b'));
    array = new char[] { 'a', 'b', 'c', 'd', 'a' };
    assertTrue(ArrayUtils.contains(array, 'a'));
    assertTrue(ArrayUtils.contains(array, 'b'));
    assertTrue(ArrayUtils.contains(array, 'c'));
    assertTrue(ArrayUtils.contains(array, 'd'));
    assertFalse(ArrayUtils.contains(array, 'e'));
  }

  @Test
  void testContainsDouble() {
    double[] array = null;
    assertFalse(ArrayUtils.contains(array, 1));
    array = new double[] { 0, 1, 2, 3, 0 };
    assertTrue(ArrayUtils.contains(array, 0));
    assertTrue(ArrayUtils.contains(array, 1));
    assertTrue(ArrayUtils.contains(array, 2));
    assertTrue(ArrayUtils.contains(array, 3));
    assertFalse(ArrayUtils.contains(array, 99));
  }

  @Test
  void testContainsDoubleNaN() {
    final double[] a = { Double.NEGATIVE_INFINITY, Double.NaN, Double.POSITIVE_INFINITY };
    assertTrue(ArrayUtils.contains(a, Double.POSITIVE_INFINITY));
    assertTrue(ArrayUtils.contains(a, Double.NEGATIVE_INFINITY));
    assertTrue(ArrayUtils.contains(a, Double.NaN));
  }

  @Test
  void testContainsFloat() {
    float[] array = null;
    assertFalse(ArrayUtils.contains(array, 1));
    array = new float[] { 0, 1, 2, 3, 0 };
    assertTrue(ArrayUtils.contains(array, 0));
    assertTrue(ArrayUtils.contains(array, 1));
    assertTrue(ArrayUtils.contains(array, 2));
    assertTrue(ArrayUtils.contains(array, 3));
    assertFalse(ArrayUtils.contains(array, 99));
  }

  @Test
  void testContainsFloatNaN() {
    final float[] array = { Float.NEGATIVE_INFINITY, Float.NaN, Float.POSITIVE_INFINITY };
    assertTrue(ArrayUtils.contains(array, Float.POSITIVE_INFINITY));
    assertTrue(ArrayUtils.contains(array, Float.NEGATIVE_INFINITY));
    assertTrue(ArrayUtils.contains(array, Float.NaN));
  }

  @Test
  void testContainsInt() {
    int[] array = null;
    assertFalse(ArrayUtils.contains(array, 1));
    array = new int[] { 0, 1, 2, 3, 0 };
    assertTrue(ArrayUtils.contains(array, 0));
    assertTrue(ArrayUtils.contains(array, 1));
    assertTrue(ArrayUtils.contains(array, 2));
    assertTrue(ArrayUtils.contains(array, 3));
    assertFalse(ArrayUtils.contains(array, 99));
  }

  @Test
  void testContainsLong() {
    long[] array = null;
    assertFalse(ArrayUtils.contains(array, 1));
    array = new long[] { 0, 1, 2, 3, 0 };
    assertTrue(ArrayUtils.contains(array, 0));
    assertTrue(ArrayUtils.contains(array, 1));
    assertTrue(ArrayUtils.contains(array, 2));
    assertTrue(ArrayUtils.contains(array, 3));
    assertFalse(ArrayUtils.contains(array, 99));
  }

  @Test
  void testContainsShort() {
    short[] array = null;
    assertFalse(ArrayUtils.contains(array, (short) 1));
    array = new short[] { 0, 1, 2, 3, 0 };
    assertTrue(ArrayUtils.contains(array, (short) 0));
    assertTrue(ArrayUtils.contains(array, (short) 1));
    assertTrue(ArrayUtils.contains(array, (short) 2));
    assertTrue(ArrayUtils.contains(array, (short) 3));
    assertFalse(ArrayUtils.contains(array, (short) 99));
  }
}
