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
public class AA_0030_OR {
  @Test
  public void testByteEqualsShort() {
    JsonPrimitive p1 = new JsonPrimitive((byte) 10);
    JsonPrimitive p2 = new JsonPrimitive((short) 10);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testByteEqualsInteger() {
    JsonPrimitive p1 = new JsonPrimitive((byte) 10);
    JsonPrimitive p2 = new JsonPrimitive(10);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testByteEqualsLong() {
    JsonPrimitive p1 = new JsonPrimitive((byte) 10);
    JsonPrimitive p2 = new JsonPrimitive(10L);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testByteEqualsBigInteger() {
    JsonPrimitive p1 = new JsonPrimitive((byte) 10);
    JsonPrimitive p2 = new JsonPrimitive(new BigInteger("10"));
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testShortEqualsInteger() {
    JsonPrimitive p1 = new JsonPrimitive((short) 10);
    JsonPrimitive p2 = new JsonPrimitive(10);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testShortEqualsLong() {
    JsonPrimitive p1 = new JsonPrimitive((short) 10);
    JsonPrimitive p2 = new JsonPrimitive(10L);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testShortEqualsBigInteger() {
    JsonPrimitive p1 = new JsonPrimitive((short) 10);
    JsonPrimitive p2 = new JsonPrimitive(new BigInteger("10"));
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testIntegerEqualsLong() {
    JsonPrimitive p1 = new JsonPrimitive(10);
    JsonPrimitive p2 = new JsonPrimitive(10L);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testIntegerEqualsBigInteger() {
    JsonPrimitive p1 = new JsonPrimitive(10);
    JsonPrimitive p2 = new JsonPrimitive(new BigInteger("10"));
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testLongEqualsBigInteger() {
    JsonPrimitive p1 = new JsonPrimitive(10L);
    JsonPrimitive p2 = new JsonPrimitive(new BigInteger("10"));
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testFloatEqualsDouble() {
    JsonPrimitive p1 = new JsonPrimitive(10.25F);
    JsonPrimitive p2 = new JsonPrimitive(10.25D);
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testFloatEqualsBigDecimal() {
    JsonPrimitive p1 = new JsonPrimitive(10.25F);
    JsonPrimitive p2 = new JsonPrimitive(new BigDecimal("10.25"));
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  public void testDoubleEqualsBigDecimal() {
    JsonPrimitive p1 = new JsonPrimitive(10.25D);
    JsonPrimitive p2 = new JsonPrimitive(new BigDecimal("10.25"));
    assertThat(p1).isEqualTo(p2);
    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }
}
