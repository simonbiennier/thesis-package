package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AA_0037_OR extends AbstractLangTest {
  @Test
  void testReverse() {
    final StringBuffer str1 = new StringBuffer("pick");
    final String str2 = "a";
    final String[] str3 = { "stick" };
    final String str4 = "up";
    Object[] array = { str1, str2, str3 };
    ArrayUtils.reverse(array);
    assertEquals(array[0], str3);
    assertEquals(array[1], str2);
    assertEquals(array[2], str1);
    array = new Object[] { str1, str2, str3, str4 };
    ArrayUtils.reverse(array);
    assertEquals(array[0], str4);
    assertEquals(array[1], str3);
    assertEquals(array[2], str2);
    assertEquals(array[3], str1);
    array = null;
    ArrayUtils.reverse(array);
    assertArrayEquals(null, array);
  }

  @Test
  void testReverseBoolean() {
    boolean[] array = { false, false, true };
    ArrayUtils.reverse(array);
    assertTrue(array[0]);
    assertFalse(array[1]);
    assertFalse(array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseBooleanRange() {
    boolean[] array = { false, false, true };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertTrue(array[0]);
    assertFalse(array[1]);
    assertFalse(array[2]);
    // a range
    array = new boolean[] { false, false, true };
    ArrayUtils.reverse(array, 0, 2);
    assertFalse(array[0]);
    assertFalse(array[1]);
    assertTrue(array[2]);
    // a range with a negative start
    array = new boolean[] { false, false, true };
    ArrayUtils.reverse(array, -1, 3);
    assertTrue(array[0]);
    assertFalse(array[1]);
    assertFalse(array[2]);
    // a range with a large stop index
    array = new boolean[] { false, false, true };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertTrue(array[0]);
    assertFalse(array[1]);
    assertFalse(array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseByte() {
    byte[] array = { 2, 3, 4 };
    ArrayUtils.reverse(array);
    assertEquals(4, array[0]);
    assertEquals(3, array[1]);
    assertEquals(2, array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseByteRange() {
    byte[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new byte[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new byte[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new byte[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseChar() {
    char[] array = { 'a', 'f', 'C' };
    ArrayUtils.reverse(array);
    assertEquals('C', array[0]);
    assertEquals('f', array[1]);
    assertEquals('a', array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseCharRange() {
    char[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new char[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new char[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new char[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseDouble() {
    double[] array = { 0.3d, 0.4d, 0.5d };
    ArrayUtils.reverse(array);
    assertEquals(0.5d, array[0]);
    assertEquals(0.4d, array[1]);
    assertEquals(0.3d, array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseDoubleRange() {
    double[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new double[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new double[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new double[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseFloat() {
    float[] array = { 0.3f, 0.4f, 0.5f };
    ArrayUtils.reverse(array);
    assertEquals(0.5f, array[0]);
    assertEquals(0.4f, array[1]);
    assertEquals(0.3f, array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseFloatRange() {
    float[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new float[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new float[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new float[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseInt() {
    int[] array = { 1, 2, 3 };
    ArrayUtils.reverse(array);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseIntRange() {
    int[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new int[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new int[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new int[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseLong() {
    long[] array = { 1L, 2L, 3L };
    ArrayUtils.reverse(array);
    assertEquals(3L, array[0]);
    assertEquals(2L, array[1]);
    assertEquals(1L, array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseLongRange() {
    long[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new long[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new long[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new long[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseObjectRange() {
    String[] array = { "1", "2", "3" };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals("3", array[0]);
    assertEquals("2", array[1]);
    assertEquals("1", array[2]);
    // a range
    array = new String[] { "1", "2", "3" };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals("2", array[0]);
    assertEquals("1", array[1]);
    assertEquals("3", array[2]);
    // a range with a negative start
    array = new String[] { "1", "2", "3" };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals("3", array[0]);
    assertEquals("2", array[1]);
    assertEquals("1", array[2]);
    // a range with a large stop index
    array = new String[] { "1", "2", "3" };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals("3", array[0]);
    assertEquals("2", array[1]);
    assertEquals("1", array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }

  @Test
  void testReverseShort() {
    short[] array = { 1, 2, 3 };
    ArrayUtils.reverse(array);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    array = null;
    ArrayUtils.reverse(array);
    assertNull(array);
  }

  @Test
  void testReverseShortRange() {
    short[] array = { 1, 2, 3 };
    // The whole array
    ArrayUtils.reverse(array, 0, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range
    array = new short[] { 1, 2, 3 };
    ArrayUtils.reverse(array, 0, 2);
    assertEquals(2, array[0]);
    assertEquals(1, array[1]);
    assertEquals(3, array[2]);
    // a range with a negative start
    array = new short[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, 3);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // a range with a large stop index
    array = new short[] { 1, 2, 3 };
    ArrayUtils.reverse(array, -1, array.length + 1000);
    assertEquals(3, array[0]);
    assertEquals(2, array[1]);
    assertEquals(1, array[2]);
    // null
    array = null;
    ArrayUtils.reverse(array, 0, 3);
    assertNull(array);
  }
}
