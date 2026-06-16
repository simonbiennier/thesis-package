package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;

/**
 * Unit test for the {@link JsonPrimitive} class.
 *
 * @author Joel Leitch
 */
public class AA_0030_RF {
  private static void assertEqualsAndHashCodeForNumericPair(Number a, Number b) {
    JsonPrimitive p1 = new JsonPrimitive(a);
    JsonPrimitive p2 = new JsonPrimitive(b);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testNumericEqualsAndHashCodeAcrossTypes() {
    assertEqualsAndHashCodeForNumericPair((byte) 10, (short) 10);
    assertEqualsAndHashCodeForNumericPair((byte) 10, 10);
    assertEqualsAndHashCodeForNumericPair((byte) 10, 10L);
    assertEqualsAndHashCodeForNumericPair((byte) 10, new BigInteger("10"));
    assertEqualsAndHashCodeForNumericPair((short) 10, 10);
    assertEqualsAndHashCodeForNumericPair((short) 10, 10L);
    assertEqualsAndHashCodeForNumericPair((short) 10, new BigInteger("10"));
    assertEqualsAndHashCodeForNumericPair(10, 10L);
    assertEqualsAndHashCodeForNumericPair(10, new BigInteger("10"));
    assertEqualsAndHashCodeForNumericPair(10L, new BigInteger("10"));
    assertEqualsAndHashCodeForNumericPair(10.25F, 10.25D);
    assertEqualsAndHashCodeForNumericPair(10.25F, new BigDecimal("10.25"));
    assertEqualsAndHashCodeForNumericPair(10.25D, new BigDecimal("10.25"));
  }
}
