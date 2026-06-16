package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AA_0036_OR extends AbstractLangTest {
  @Test
  void testIsSorted() {
    Integer[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new Integer[] { 1 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new Integer[] { 1, 2, 3 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new Integer[] { 1, 3, 2 };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedBool() {
    boolean[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new boolean[] { true };
    assertTrue(ArrayUtils.isSorted(array));
    array = new boolean[] { false, true };
    assertTrue(ArrayUtils.isSorted(array));
    array = new boolean[] { true, false };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedByte() {
    byte[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new byte[] { 0x10 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new byte[] { 0x10, 0x20, 0x30 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new byte[] { 0x10, 0x30, 0x20 };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedChar() {
    char[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new char[] { 'a' };
    assertTrue(ArrayUtils.isSorted(array));
    array = new char[] { 'a', 'b', 'c' };
    assertTrue(ArrayUtils.isSorted(array));
    array = new char[] { 'a', 'c', 'b' };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedDouble() {
    double[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new double[] { 0.0 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new double[] { -1.0, 0.0, 0.1, 0.2 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new double[] { -1.0, 0.2, 0.1, 0.0 };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedFloat() {
    float[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new float[] { 0f };
    assertTrue(ArrayUtils.isSorted(array));
    array = new float[] { -1f, 0f, 0.1f, 0.2f };
    assertTrue(ArrayUtils.isSorted(array));
    array = new float[] { -1f, 0.2f, 0.1f, 0f };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedInt() {
    int[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new int[] { 1 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new int[] { 1, 2, 3 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new int[] { 1, 3, 2 };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedLong() {
    long[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new long[] { 0L };
    assertTrue(ArrayUtils.isSorted(array));
    array = new long[] { -1L, 0L, 1L };
    assertTrue(ArrayUtils.isSorted(array));
    array = new long[] { -1L, 1L, 0L };
    assertFalse(ArrayUtils.isSorted(array));
  }

  @Test
  void testIsSortedShort() {
    short[] array = null;
    assertTrue(ArrayUtils.isSorted(array));
    array = new short[] { 0 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new short[] { -1, 0, 1 };
    assertTrue(ArrayUtils.isSorted(array));
    array = new short[] { -1, 1, 0 };
    assertFalse(ArrayUtils.isSorted(array));
  }
}
