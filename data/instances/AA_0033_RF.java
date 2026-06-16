package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AA_0033_RF extends AbstractLangTest {
  private Object clone(final Object original, final Object arg) throws Exception {
    return ArrayUtils.class.getMethod("clone", original.getClass()).invoke(null, arg);
  }

  @ParameterizedTest
  @MethodSource("arrays")
  void testClone(final String name, final Object original) throws Exception {
    assertNull(clone(original, null));
    final Object cloned = clone(original, original);
    assertTrue(Arrays.deepEquals(new Object[] { original }, new Object[] { cloned }), name);
    assertNotSame(original, cloned, name);
  }

  private static Stream<Arguments> arrays() {
    return Stream.of(
        Arguments.of("boolean[]", new boolean[] { true, false }),
        Arguments.of("byte[]", new byte[] { 1, 6 }),
        Arguments.of("char[]", new char[] { 'a', '4' }),
        Arguments.of("double[]", new double[] { 2.4d, 5.7d }),
        Arguments.of("float[]", new float[] { 2.6f, 6.4f }),
        Arguments.of("int[]", new int[] { 5, 8 }),
        Arguments.of("long[]", new long[] { 0L, 1L }),
        Arguments.of("short[]", new short[] { 1, 4 }));
  }
}
