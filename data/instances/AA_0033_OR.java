package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class AA_0033_OR extends AbstractLangTest {
  @Test
  void testCloneBoolean() {
    assertNull(ArrayUtils.clone((boolean[]) null));
    final boolean[] original = { true, false };
    final boolean[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneByte() {
    assertNull(ArrayUtils.clone((byte[]) null));
    final byte[] original = { 1, 6 };
    final byte[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneChar() {
    assertNull(ArrayUtils.clone((char[]) null));
    final char[] original = { 'a', '4' };
    final char[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneDouble() {
    assertNull(ArrayUtils.clone((double[]) null));
    final double[] original = { 2.4d, 5.7d };
    final double[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneFloat() {
    assertNull(ArrayUtils.clone((float[]) null));
    final float[] original = { 2.6f, 6.4f };
    final float[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneInt() {
    assertNull(ArrayUtils.clone((int[]) null));
    final int[] original = { 5, 8 };
    final int[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneLong() {
    assertNull(ArrayUtils.clone((long[]) null));
    final long[] original = { 0L, 1L };
    final long[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }

  @Test
  void testCloneShort() {
    assertNull(ArrayUtils.clone((short[]) null));
    final short[] original = { 1, 4 };
    final short[] cloned = ArrayUtils.clone(original);
    assertArrayEquals(original, cloned);
    assertNotSame(original, cloned);
  }
}
