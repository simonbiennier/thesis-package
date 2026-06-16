package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Function;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link org.apache.commons.lang3.math.NumberUtils}.
 */
class AA_0003_OR extends AbstractLangTest {
  private boolean checkCreateNumber(final String val) {
    try {
      final Object obj = NumberUtils.createNumber(val);
      return obj != null;
    } catch (final NumberFormatException e) {
      return false;
    }
  }

  private void compareIsCreatableWithCreateNumber(final String val, final boolean expected) {
    final boolean isValid = NumberUtils.isCreatable(val);
    final boolean canCreate = checkCreateNumber(val);
    assertTrue(isValid == expected && canCreate == expected, "Expecting " + expected
        + " for isCreatable/createNumber using \"" + val + "\" but got " + isValid + " and " + canCreate);
  }

  @SuppressWarnings("deprecation")
  private void compareIsNumberWithCreateNumber(final String val, final boolean expected) {
    final boolean isValid = NumberUtils.isNumber(val);
    final boolean canCreate = checkCreateNumber(val);
    assertTrue(isValid == expected && canCreate == expected, "Expecting " + expected
        + " for isNumber/createNumber using \"" + val + "\" but got " + isValid + " and " + canCreate);
  }

  private boolean isApplyNonNull(final String s, final Function<String, ?> function) {
    try {
      assertNotNull(function.apply(s));
      return true;
    } catch (final Exception e) {
      if (!s.matches(".*\\s.*")) {
        e.printStackTrace();
      }
      return false;
    }
  }

  private boolean isNumberFormatParsable(final String s) {
    final NumberFormat instance = NumberFormat.getInstance();
    try {
      // Stops parsing when a space is found, then returns an object.
      assertNotNull(instance.parse(s));
      return true;
    } catch (final ParseException e) {
      return false;
    }
  }

  private boolean isNumberIntegerOnlyFormatParsable(final String s) {
    final NumberFormat instance = NumberFormat.getInstance();
    instance.setParseIntegerOnly(true);
    try {
      // Stops parsing when a space is found, then returns an object.
      assertNotNull(instance.parse(s));
      return true;
    } catch (final ParseException e) {
      return false;
    }
  }

  private boolean isParsableByte(final String s) {
    final boolean parsable = NumberUtils.isParsable(s);
    assertTrue(isNumberFormatParsable(s), s);
    assertTrue(isNumberIntegerOnlyFormatParsable(s), s);
    assertEquals(parsable, isApplyNonNull(s, Byte::parseByte), s);
    return parsable;
  }

  private boolean isParsableDouble(final String s) {
    final boolean parsable = NumberUtils.isParsable(s);
    assertTrue(isNumberFormatParsable(s), s);
    assertTrue(isNumberIntegerOnlyFormatParsable(s), s);
    assertEquals(parsable, isApplyNonNull(s, Double::parseDouble), s);
    return parsable;
  }

  private boolean isParsableFloat(final String s) {
    final boolean parsable = NumberUtils.isParsable(s);
    assertTrue(isNumberFormatParsable(s), s);
    assertTrue(isNumberIntegerOnlyFormatParsable(s), s);
    assertEquals(parsable, isApplyNonNull(s, Float::parseFloat), s);
    return parsable;
  }

  private boolean isParsableInteger(final String s) {
    final boolean parsable = NumberUtils.isParsable(s);
    assertTrue(isNumberFormatParsable(s), s);
    assertTrue(isNumberIntegerOnlyFormatParsable(s), s);
    assertEquals(parsable, isApplyNonNull(s, Integer::parseInt), s);
    return parsable;
  }

  private boolean isParsableLong(final String s) {
    final boolean parsable = NumberUtils.isParsable(s);
    assertTrue(isNumberFormatParsable(s), s);
    assertTrue(isNumberIntegerOnlyFormatParsable(s), s);
    assertEquals(parsable, isApplyNonNull(s, Long::parseLong), s);
    return parsable;
  }

  private boolean isParsableShort(final String s) {
    final boolean parsable = NumberUtils.isParsable(s);
    assertTrue(isNumberFormatParsable(s), s);
    assertTrue(isNumberIntegerOnlyFormatParsable(s), s);
    assertEquals(parsable, isApplyNonNull(s, Short::parseShort), s);
    return parsable;
  }

  @Test
  void testCompareDouble() {
    assertEquals(0, Double.compare(Double.NaN, Double.NaN));
    assertEquals(Double.compare(Double.NaN, Double.POSITIVE_INFINITY), +1);
    assertEquals(Double.compare(Double.NaN, Double.MAX_VALUE), +1);
    assertEquals(Double.compare(Double.NaN, 1.2d), +1);
    assertEquals(Double.compare(Double.NaN, 0.0d), +1);
    assertEquals(Double.compare(Double.NaN, -0.0d), +1);
    assertEquals(Double.compare(Double.NaN, -1.2d), +1);
    assertEquals(Double.compare(Double.NaN, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(Double.NaN, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, Double.NaN), -1);
    assertEquals(0, Double.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, Double.MAX_VALUE), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, 1.2d), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, 0.0d), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, -0.0d), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, -1.2d), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(Double.MAX_VALUE, Double.NaN), -1);
    assertEquals(Double.compare(Double.MAX_VALUE, Double.POSITIVE_INFINITY), -1);
    assertEquals(0, Double.compare(Double.MAX_VALUE, Double.MAX_VALUE));
    assertEquals(Double.compare(Double.MAX_VALUE, 1.2d), +1);
    assertEquals(Double.compare(Double.MAX_VALUE, 0.0d), +1);
    assertEquals(Double.compare(Double.MAX_VALUE, -0.0d), +1);
    assertEquals(Double.compare(Double.MAX_VALUE, -1.2d), +1);
    assertEquals(Double.compare(Double.MAX_VALUE, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(Double.MAX_VALUE, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(1.2d, Double.NaN), -1);
    assertEquals(Double.compare(1.2d, Double.POSITIVE_INFINITY), -1);
    assertEquals(Double.compare(1.2d, Double.MAX_VALUE), -1);
    assertEquals(0, Double.compare(1.2d, 1.2d));
    assertEquals(Double.compare(1.2d, 0.0d), +1);
    assertEquals(Double.compare(1.2d, -0.0d), +1);
    assertEquals(Double.compare(1.2d, -1.2d), +1);
    assertEquals(Double.compare(1.2d, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(1.2d, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(0.0d, Double.NaN), -1);
    assertEquals(Double.compare(0.0d, Double.POSITIVE_INFINITY), -1);
    assertEquals(Double.compare(0.0d, Double.MAX_VALUE), -1);
    assertEquals(Double.compare(0.0d, 1.2d), -1);
    assertEquals(0, Double.compare(0.0d, 0.0d));
    assertEquals(Double.compare(0.0d, -0.0d), +1);
    assertEquals(Double.compare(0.0d, -1.2d), +1);
    assertEquals(Double.compare(0.0d, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(0.0d, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(-0.0d, Double.NaN), -1);
    assertEquals(Double.compare(-0.0d, Double.POSITIVE_INFINITY), -1);
    assertEquals(Double.compare(-0.0d, Double.MAX_VALUE), -1);
    assertEquals(Double.compare(-0.0d, 1.2d), -1);
    assertEquals(Double.compare(-0.0d, 0.0d), -1);
    assertEquals(0, Double.compare(-0.0d, -0.0d));
    assertEquals(Double.compare(-0.0d, -1.2d), +1);
    assertEquals(Double.compare(-0.0d, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(-0.0d, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(-1.2d, Double.NaN), -1);
    assertEquals(Double.compare(-1.2d, Double.POSITIVE_INFINITY), -1);
    assertEquals(Double.compare(-1.2d, Double.MAX_VALUE), -1);
    assertEquals(Double.compare(-1.2d, 1.2d), -1);
    assertEquals(Double.compare(-1.2d, 0.0d), -1);
    assertEquals(Double.compare(-1.2d, -0.0d), -1);
    assertEquals(0, Double.compare(-1.2d, -1.2d));
    assertEquals(Double.compare(-1.2d, -Double.MAX_VALUE), +1);
    assertEquals(Double.compare(-1.2d, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(-Double.MAX_VALUE, Double.NaN), -1);
    assertEquals(Double.compare(-Double.MAX_VALUE, Double.POSITIVE_INFINITY), -1);
    assertEquals(Double.compare(-Double.MAX_VALUE, Double.MAX_VALUE), -1);
    assertEquals(Double.compare(-Double.MAX_VALUE, 1.2d), -1);
    assertEquals(Double.compare(-Double.MAX_VALUE, 0.0d), -1);
    assertEquals(Double.compare(-Double.MAX_VALUE, -0.0d), -1);
    assertEquals(Double.compare(-Double.MAX_VALUE, -1.2d), -1);
    assertEquals(0, Double.compare(-Double.MAX_VALUE, -Double.MAX_VALUE));
    assertEquals(Double.compare(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY), +1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, Double.NaN), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, Double.MAX_VALUE), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, 1.2d), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, 0.0d), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, -0.0d), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, -1.2d), -1);
    assertEquals(Double.compare(Double.NEGATIVE_INFINITY, -Double.MAX_VALUE), -1);
    assertEquals(0, Double.compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
  }

  @Test
  void testCompareFloat() {
    assertEquals(0, Float.compare(Float.NaN, Float.NaN));
    assertEquals(Float.compare(Float.NaN, Float.POSITIVE_INFINITY), +1);
    assertEquals(Float.compare(Float.NaN, Float.MAX_VALUE), +1);
    assertEquals(Float.compare(Float.NaN, 1.2f), +1);
    assertEquals(Float.compare(Float.NaN, 0.0f), +1);
    assertEquals(Float.compare(Float.NaN, -0.0f), +1);
    assertEquals(Float.compare(Float.NaN, -1.2f), +1);
    assertEquals(Float.compare(Float.NaN, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(Float.NaN, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, Float.NaN), -1);
    assertEquals(0, Float.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, Float.MAX_VALUE), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, 1.2f), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, 0.0f), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, -0.0f), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, -1.2f), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(Float.MAX_VALUE, Float.NaN), -1);
    assertEquals(Float.compare(Float.MAX_VALUE, Float.POSITIVE_INFINITY), -1);
    assertEquals(0, Float.compare(Float.MAX_VALUE, Float.MAX_VALUE));
    assertEquals(Float.compare(Float.MAX_VALUE, 1.2f), +1);
    assertEquals(Float.compare(Float.MAX_VALUE, 0.0f), +1);
    assertEquals(Float.compare(Float.MAX_VALUE, -0.0f), +1);
    assertEquals(Float.compare(Float.MAX_VALUE, -1.2f), +1);
    assertEquals(Float.compare(Float.MAX_VALUE, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(Float.MAX_VALUE, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(1.2f, Float.NaN), -1);
    assertEquals(Float.compare(1.2f, Float.POSITIVE_INFINITY), -1);
    assertEquals(Float.compare(1.2f, Float.MAX_VALUE), -1);
    assertEquals(0, Float.compare(1.2f, 1.2f));
    assertEquals(Float.compare(1.2f, 0.0f), +1);
    assertEquals(Float.compare(1.2f, -0.0f), +1);
    assertEquals(Float.compare(1.2f, -1.2f), +1);
    assertEquals(Float.compare(1.2f, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(1.2f, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(0.0f, Float.NaN), -1);
    assertEquals(Float.compare(0.0f, Float.POSITIVE_INFINITY), -1);
    assertEquals(Float.compare(0.0f, Float.MAX_VALUE), -1);
    assertEquals(Float.compare(0.0f, 1.2f), -1);
    assertEquals(0, Float.compare(0.0f, 0.0f));
    assertEquals(Float.compare(0.0f, -0.0f), +1);
    assertEquals(Float.compare(0.0f, -1.2f), +1);
    assertEquals(Float.compare(0.0f, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(0.0f, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(-0.0f, Float.NaN), -1);
    assertEquals(Float.compare(-0.0f, Float.POSITIVE_INFINITY), -1);
    assertEquals(Float.compare(-0.0f, Float.MAX_VALUE), -1);
    assertEquals(Float.compare(-0.0f, 1.2f), -1);
    assertEquals(Float.compare(-0.0f, 0.0f), -1);
    assertEquals(0, Float.compare(-0.0f, -0.0f));
    assertEquals(Float.compare(-0.0f, -1.2f), +1);
    assertEquals(Float.compare(-0.0f, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(-0.0f, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(-1.2f, Float.NaN), -1);
    assertEquals(Float.compare(-1.2f, Float.POSITIVE_INFINITY), -1);
    assertEquals(Float.compare(-1.2f, Float.MAX_VALUE), -1);
    assertEquals(Float.compare(-1.2f, 1.2f), -1);
    assertEquals(Float.compare(-1.2f, 0.0f), -1);
    assertEquals(Float.compare(-1.2f, -0.0f), -1);
    assertEquals(0, Float.compare(-1.2f, -1.2f));
    assertEquals(Float.compare(-1.2f, -Float.MAX_VALUE), +1);
    assertEquals(Float.compare(-1.2f, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(-Float.MAX_VALUE, Float.NaN), -1);
    assertEquals(Float.compare(-Float.MAX_VALUE, Float.POSITIVE_INFINITY), -1);
    assertEquals(Float.compare(-Float.MAX_VALUE, Float.MAX_VALUE), -1);
    assertEquals(Float.compare(-Float.MAX_VALUE, 1.2f), -1);
    assertEquals(Float.compare(-Float.MAX_VALUE, 0.0f), -1);
    assertEquals(Float.compare(-Float.MAX_VALUE, -0.0f), -1);
    assertEquals(Float.compare(-Float.MAX_VALUE, -1.2f), -1);
    assertEquals(0, Float.compare(-Float.MAX_VALUE, -Float.MAX_VALUE));
    assertEquals(Float.compare(-Float.MAX_VALUE, Float.NEGATIVE_INFINITY), +1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, Float.NaN), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, Float.MAX_VALUE), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, 1.2f), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, 0.0f), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, -0.0f), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, -1.2f), -1);
    assertEquals(Float.compare(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE), -1);
    assertEquals(0, Float.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
  }

  @SuppressWarnings("cast") // suppress instanceof warning check
  @Test
  void testConstants() {
    assertInstanceOf(Long.class, NumberUtils.LONG_ZERO);
    assertInstanceOf(Long.class, NumberUtils.LONG_ONE);
    assertInstanceOf(Long.class, NumberUtils.LONG_MINUS_ONE);
    assertInstanceOf(Integer.class, NumberUtils.INTEGER_ZERO);
    assertInstanceOf(Integer.class, NumberUtils.INTEGER_ONE);
    assertInstanceOf(Integer.class, NumberUtils.INTEGER_MINUS_ONE);
    assertInstanceOf(Short.class, NumberUtils.SHORT_ZERO);
    assertInstanceOf(Short.class, NumberUtils.SHORT_ONE);
    assertInstanceOf(Short.class, NumberUtils.SHORT_MINUS_ONE);
    assertInstanceOf(Byte.class, NumberUtils.BYTE_ZERO);
    assertInstanceOf(Byte.class, NumberUtils.BYTE_ONE);
    assertInstanceOf(Byte.class, NumberUtils.BYTE_MINUS_ONE);
    assertInstanceOf(Double.class, NumberUtils.DOUBLE_ZERO);
    assertInstanceOf(Double.class, NumberUtils.DOUBLE_ONE);
    assertInstanceOf(Double.class, NumberUtils.DOUBLE_MINUS_ONE);
    assertInstanceOf(Float.class, NumberUtils.FLOAT_ZERO);
    assertInstanceOf(Float.class, NumberUtils.FLOAT_ONE);
    assertInstanceOf(Float.class, NumberUtils.FLOAT_MINUS_ONE);
    assertEquals(0, NumberUtils.LONG_ZERO.longValue());
    assertEquals(1, NumberUtils.LONG_ONE.longValue());
    assertEquals(NumberUtils.LONG_MINUS_ONE.longValue(), -1);
    assertEquals(0, NumberUtils.INTEGER_ZERO.intValue());
    assertEquals(1, NumberUtils.INTEGER_ONE.intValue());
    assertEquals(NumberUtils.INTEGER_MINUS_ONE.intValue(), -1);
    assertEquals(0, NumberUtils.SHORT_ZERO.shortValue());
    assertEquals(1, NumberUtils.SHORT_ONE.shortValue());
    assertEquals(NumberUtils.SHORT_MINUS_ONE.shortValue(), -1);
    assertEquals(0, NumberUtils.BYTE_ZERO.byteValue());
    assertEquals(1, NumberUtils.BYTE_ONE.byteValue());
    assertEquals(NumberUtils.BYTE_MINUS_ONE.byteValue(), -1);
    assertEquals(0.0d, NumberUtils.DOUBLE_ZERO.doubleValue());
    assertEquals(1.0d, NumberUtils.DOUBLE_ONE.doubleValue());
    assertEquals(NumberUtils.DOUBLE_MINUS_ONE.doubleValue(), -1.0d);
    assertEquals(0.0f, NumberUtils.FLOAT_ZERO.floatValue());
    assertEquals(1.0f, NumberUtils.FLOAT_ONE.floatValue());
    assertEquals(NumberUtils.FLOAT_MINUS_ONE.floatValue(), -1.0f);
  }

  @Test
  void testCreateNumber() {
    // a lot of things can go wrong
    assertEquals(Float.valueOf("1234.5"), NumberUtils.createNumber("1234.5"), "createNumber(String) 1 failed");
    assertEquals(Integer.valueOf("12345"), NumberUtils.createNumber("12345"), "createNumber(String) 2 failed");
    assertEquals(Double.valueOf("1234.5"), NumberUtils.createNumber("1234.5D"), "createNumber(String) 3 failed");
    assertEquals(Double.valueOf("1234.5"), NumberUtils.createNumber("1234.5d"), "createNumber(String) 3 failed");
    assertEquals(Float.valueOf("1234.5"), NumberUtils.createNumber("1234.5F"), "createNumber(String) 4 failed");
    assertEquals(Float.valueOf("1234.5"), NumberUtils.createNumber("1234.5f"), "createNumber(String) 4 failed");
    assertEquals(Long.valueOf(Integer.MAX_VALUE + 1L), NumberUtils.createNumber("" + (Integer.MAX_VALUE + 1L)),
        "createNumber(String) 5 failed");
    assertEquals(Long.valueOf(12345), NumberUtils.createNumber("12345L"), "createNumber(String) 6 failed");
    assertEquals(Long.valueOf(12345), NumberUtils.createNumber("12345l"), "createNumber(String) 6 failed");
    assertEquals(Float.valueOf("-1234.5"), NumberUtils.createNumber("-1234.5"), "createNumber(String) 7 failed");
    assertEquals(Integer.valueOf("-12345"), NumberUtils.createNumber("-12345"), "createNumber(String) 8 failed");
    assertEquals(0xFADE, NumberUtils.createNumber("0xFADE").intValue(), "createNumber(String) 9a failed");
    assertEquals(0xFADE, NumberUtils.createNumber("0Xfade").intValue(), "createNumber(String) 9b failed");
    assertEquals(-0xFADE, NumberUtils.createNumber("-0xFADE").intValue(), "createNumber(String) 10a failed");
    assertEquals(-0xFADE, NumberUtils.createNumber("-0Xfade").intValue(), "createNumber(String) 10b failed");
    assertEquals(Double.valueOf("1.1E200"), NumberUtils.createNumber("1.1E200"), "createNumber(String) 11 failed");
    assertEquals(Float.valueOf("1.1E20"), NumberUtils.createNumber("1.1E20"), "createNumber(String) 12 failed");
    assertEquals(Double.valueOf("-1.1E200"), NumberUtils.createNumber("-1.1E200"),
        "createNumber(String) 13 failed");
    assertEquals(Double.valueOf("1.1E-200"), NumberUtils.createNumber("1.1E-200"),
        "createNumber(String) 14 failed");
    assertNull(NumberUtils.createNumber(null), "createNumber(null) failed");
    assertEquals(new BigInteger("12345678901234567890"), NumberUtils.createNumber("12345678901234567890L"),
        "createNumber(String) failed");
    assertEquals(new BigDecimal("1.1E-700"), NumberUtils.createNumber("1.1E-700F"),
        "createNumber(String) 15 failed");
    assertEquals(Long.valueOf("10" + Integer.MAX_VALUE), NumberUtils.createNumber("10" + Integer.MAX_VALUE + "L"),
        "createNumber(String) 16 failed");
    assertEquals(Long.valueOf("10" + Integer.MAX_VALUE), NumberUtils.createNumber("10" + Integer.MAX_VALUE),
        "createNumber(String) 17 failed");
    assertEquals(new BigInteger("10" + Long.MAX_VALUE), NumberUtils.createNumber("10" + Long.MAX_VALUE),
        "createNumber(String) 18 failed");
    // LANG-521
    assertEquals(Float.valueOf("2."), NumberUtils.createNumber("2."), "createNumber(String) LANG-521 failed");
    // LANG-693
    assertEquals(Double.valueOf(Double.MAX_VALUE), NumberUtils.createNumber("" + Double.MAX_VALUE),
        "createNumber(String) LANG-693 failed");
    // LANG-1018
    assertEquals(Double.valueOf("-160952.54"), NumberUtils.createNumber("-160952.54"),
        "createNumber(String) LANG-1018 failed");
    // LANG-1187
    assertEquals(Double.valueOf("6264583.33"), NumberUtils.createNumber("6264583.33"),
        "createNumber(String) LANG-1187 failed");
    // LANG-1215
    assertEquals(Double.valueOf("193343.82"), NumberUtils.createNumber("193343.82"),
        "createNumber(String) LANG-1215 failed");
    // LANG-1060
    assertEquals(Double.valueOf("001234.5678"), NumberUtils.createNumber("001234.5678"),
        "createNumber(String) LANG-1060a failed");
    assertEquals(Double.valueOf("+001234.5678"), NumberUtils.createNumber("+001234.5678"),
        "createNumber(String) LANG-1060b failed");
    assertEquals(Double.valueOf("-001234.5678"), NumberUtils.createNumber("-001234.5678"),
        "createNumber(String) LANG-1060c failed");
    assertEquals(Double.valueOf("0000.00000"), NumberUtils.createNumber("0000.00000d"),
        "createNumber(String) LANG-1060d failed");
    assertEquals(Float.valueOf("001234.56"), NumberUtils.createNumber("001234.56"),
        "createNumber(String) LANG-1060e failed");
    assertEquals(Float.valueOf("+001234.56"), NumberUtils.createNumber("+001234.56"),
        "createNumber(String) LANG-1060f failed");
    assertEquals(Float.valueOf("-001234.56"), NumberUtils.createNumber("-001234.56"),
        "createNumber(String) LANG-1060g failed");
    assertEquals(Float.valueOf("0000.10"), NumberUtils.createNumber("0000.10"),
        "createNumber(String) LANG-1060h failed");
    assertEquals(Float.valueOf("001.1E20"), NumberUtils.createNumber("001.1E20"),
        "createNumber(String) LANG-1060i failed");
    assertEquals(Float.valueOf("+001.1E20"), NumberUtils.createNumber("+001.1E20"),
        "createNumber(String) LANG-1060j failed");
    assertEquals(Float.valueOf("-001.1E20"), NumberUtils.createNumber("-001.1E20"),
        "createNumber(String) LANG-1060k failed");
    assertEquals(Double.valueOf("001.1E200"), NumberUtils.createNumber("001.1E200"),
        "createNumber(String) LANG-1060l failed");
    assertEquals(Double.valueOf("+001.1E200"), NumberUtils.createNumber("+001.1E200"),
        "createNumber(String) LANG-1060m failed");
    assertEquals(Double.valueOf("-001.1E200"), NumberUtils.createNumber("-001.1E200"),
        "createNumber(String) LANG-1060n failed");
    // LANG-1645
    assertEquals(Integer.decode("+0xF"), NumberUtils.createNumber("+0xF"),
        "createNumber(String) LANG-1645a failed");
    assertEquals(Long.decode("+0xFFFFFFFF"), NumberUtils.createNumber("+0xFFFFFFFF"),
        "createNumber(String) LANG-1645b failed");
    assertEquals(new BigInteger("+FFFFFFFFFFFFFFFF", 16), NumberUtils.createNumber("+0xFFFFFFFFFFFFFFFF"),
        "createNumber(String) LANG-1645c failed");
  }

  @Test
  // Check that the code fails to create a valid number when preceded by -- rather
  // than -
  void testCreateNumberFailure_1() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("--1.1E-700F"));
  }

  @Test
  // Check that the code fails to create a valid number when both e and E are
  // present (with decimal)
  void testCreateNumberFailure_2() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("-1.1E+0-7e00"));
  }

  @Test
  // Check that the code fails to create a valid number when both e and E are
  // present (no decimal)
  void testCreateNumberFailure_3() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("-11E+0-7e00"));
  }

  @Test
  // Check that the code fails to create a valid number when both e and E are
  // present (no decimal)
  void testCreateNumberFailure_4() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("1eE+00001"));
  }

  @Test
  // Check that the code fails to create a valid number when there are multiple
  // trailing 'f' characters (LANG-1205)
  void testCreateNumberFailure_5() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("1234.5ff"));
  }

  @Test
  // Check that the code fails to create a valid number when there are multiple
  // trailing 'F' characters (LANG-1205)
  void testCreateNumberFailure_6() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("1234.5FF"));
  }

  @Test
  // Check that the code fails to create a valid number when there are multiple
  // trailing 'd' characters (LANG-1205)
  void testCreateNumberFailure_7() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("1234.5dd"));
  }

  @Test
  // Check that the code fails to create a valid number when there are multiple
  // trailing 'D' characters (LANG-1205)
  void testCreateNumberFailure_8() {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber("1234.5DD"));
  }

  /**
   * Tests isCreatable(String) and tests that createNumber(String) returns a valid
   * number iff isCreatable(String)
   * returns false.
   */
  @Test
  void testIsCreatable() {
    compareIsCreatableWithCreateNumber("12345", true);
    compareIsCreatableWithCreateNumber("1234.5", true);
    compareIsCreatableWithCreateNumber(".12345", true);
    compareIsCreatableWithCreateNumber("1234E5", true);
    compareIsCreatableWithCreateNumber("1234E+5", true);
    compareIsCreatableWithCreateNumber("1234E-5", true);
    compareIsCreatableWithCreateNumber("123.4E5", true);
    compareIsCreatableWithCreateNumber("-1234", true);
    compareIsCreatableWithCreateNumber("-1234.5", true);
    compareIsCreatableWithCreateNumber("-.12345", true);
    compareIsCreatableWithCreateNumber("-1234E5", true);
    compareIsCreatableWithCreateNumber("0", true);
    compareIsCreatableWithCreateNumber("0.1", true); // LANG-1216
    compareIsCreatableWithCreateNumber("-0", true);
    compareIsCreatableWithCreateNumber("01234", true);
    compareIsCreatableWithCreateNumber("-01234", true);
    compareIsCreatableWithCreateNumber("-0xABC123", true);
    compareIsCreatableWithCreateNumber("-0x0", true);
    compareIsCreatableWithCreateNumber("123.4E21D", true);
    compareIsCreatableWithCreateNumber("-221.23F", true);
    compareIsCreatableWithCreateNumber("22338L", true);
    compareIsCreatableWithCreateNumber(null, false);
    compareIsCreatableWithCreateNumber("", false);
    compareIsCreatableWithCreateNumber(" ", false);
    compareIsCreatableWithCreateNumber("\r\n\t", false);
    compareIsCreatableWithCreateNumber("--2.3", false);
    compareIsCreatableWithCreateNumber(".12.3", false);
    compareIsCreatableWithCreateNumber("-123E", false);
    compareIsCreatableWithCreateNumber("-123E+-212", false);
    compareIsCreatableWithCreateNumber("-123E2.12", false);
    compareIsCreatableWithCreateNumber("0xGF", false);
    compareIsCreatableWithCreateNumber("0xFAE-1", false);
    compareIsCreatableWithCreateNumber(".", false);
    compareIsCreatableWithCreateNumber("-0ABC123", false);
    compareIsCreatableWithCreateNumber("123.4E-D", false);
    compareIsCreatableWithCreateNumber("123.4ED", false);
    compareIsCreatableWithCreateNumber("1234E5l", false);
    compareIsCreatableWithCreateNumber("11a", false);
    compareIsCreatableWithCreateNumber("1a", false);
    compareIsCreatableWithCreateNumber("a", false);
    compareIsCreatableWithCreateNumber("11g", false);
    compareIsCreatableWithCreateNumber("11z", false);
    compareIsCreatableWithCreateNumber("11def", false);
    compareIsCreatableWithCreateNumber("11d11", false);
    compareIsCreatableWithCreateNumber("11 11", false);
    compareIsCreatableWithCreateNumber(" 1111", false);
    compareIsCreatableWithCreateNumber("1111 ", false);
    compareIsCreatableWithCreateNumber("2.", true); // LANG-521
    compareIsCreatableWithCreateNumber("1.1L", false); // LANG-664
    compareIsCreatableWithCreateNumber("+0xF", true); // LANG-1645
    compareIsCreatableWithCreateNumber("+0xFFFFFFFF", true); // LANG-1645
    compareIsCreatableWithCreateNumber("+0xFFFFFFFFFFFFFFFF", true); // LANG-1645
    compareIsCreatableWithCreateNumber(".0", true); // LANG-1646
    compareIsCreatableWithCreateNumber("0.", true); // LANG-1646
    compareIsCreatableWithCreateNumber("0.D", true); // LANG-1646
    compareIsCreatableWithCreateNumber("0e1", true); // LANG-1646
    compareIsCreatableWithCreateNumber("0e1D", true); // LANG-1646
    compareIsCreatableWithCreateNumber(".D", false); // LANG-1646
    compareIsCreatableWithCreateNumber(".e10", false); // LANG-1646
    compareIsCreatableWithCreateNumber(".e10D", false); // LANG-1646
  }

  @Test
  void testIsDigits() {
    assertFalse(NumberUtils.isDigits(null), "isDigits(null) failed");
    assertFalse(NumberUtils.isDigits(""), "isDigits('') failed");
    assertTrue(NumberUtils.isDigits("12345"), "isDigits(String) failed");
    assertFalse(NumberUtils.isDigits("1234.5"), "isDigits(String) neg 1 failed");
    assertFalse(NumberUtils.isDigits("1ab"), "isDigits(String) neg 3 failed");
    assertFalse(NumberUtils.isDigits("abc"), "isDigits(String) neg 4 failed");
  }

  /**
   * Tests isCreatable(String) and tests that createNumber(String) returns a valid
   * number iff isCreatable(String)
   * returns false.
   */
  @Test
  void testIsNumber() {
    compareIsNumberWithCreateNumber("12345", true);
    compareIsNumberWithCreateNumber("1234.5", true);
    compareIsNumberWithCreateNumber(".12345", true);
    compareIsNumberWithCreateNumber("1234E5", true);
    compareIsNumberWithCreateNumber("1234E+5", true);
    compareIsNumberWithCreateNumber("1234E-5", true);
    compareIsNumberWithCreateNumber("123.4E5", true);
    compareIsNumberWithCreateNumber("-1234", true);
    compareIsNumberWithCreateNumber("-1234.5", true);
    compareIsNumberWithCreateNumber("-.12345", true);
    compareIsNumberWithCreateNumber("-0001.12345", true);
    compareIsNumberWithCreateNumber("-000.12345", true);
    compareIsNumberWithCreateNumber("+00.12345", true);
    compareIsNumberWithCreateNumber("+0002.12345", true);
    compareIsNumberWithCreateNumber("-1234E5", true);
    compareIsNumberWithCreateNumber("0", true);
    compareIsNumberWithCreateNumber("-0", true);
    compareIsNumberWithCreateNumber("01234", true);
    compareIsNumberWithCreateNumber("-01234", true);
    compareIsNumberWithCreateNumber("-0xABC123", true);
    compareIsNumberWithCreateNumber("-0x0", true);
    compareIsNumberWithCreateNumber("123.4E21D", true);
    compareIsNumberWithCreateNumber("-221.23F", true);
    compareIsNumberWithCreateNumber("22338L", true);
    compareIsNumberWithCreateNumber(null, false);
    compareIsNumberWithCreateNumber("", false);
    compareIsNumberWithCreateNumber(" ", false);
    compareIsNumberWithCreateNumber("\r\n\t", false);
    compareIsNumberWithCreateNumber("--2.3", false);
    compareIsNumberWithCreateNumber(".12.3", false);
    compareIsNumberWithCreateNumber("-123E", false);
    compareIsNumberWithCreateNumber("-123E+-212", false);
    compareIsNumberWithCreateNumber("-123E2.12", false);
    compareIsNumberWithCreateNumber("0xGF", false);
    compareIsNumberWithCreateNumber("0xFAE-1", false);
    compareIsNumberWithCreateNumber(".", false);
    compareIsNumberWithCreateNumber("-0ABC123", false);
    compareIsNumberWithCreateNumber("123.4E-D", false);
    compareIsNumberWithCreateNumber("123.4ED", false);
    compareIsNumberWithCreateNumber("+000E.12345", false);
    compareIsNumberWithCreateNumber("-000E.12345", false);
    compareIsNumberWithCreateNumber("1234E5l", false);
    compareIsNumberWithCreateNumber("11a", false);
    compareIsNumberWithCreateNumber("1a", false);
    compareIsNumberWithCreateNumber("a", false);
    compareIsNumberWithCreateNumber("11g", false);
    compareIsNumberWithCreateNumber("11z", false);
    compareIsNumberWithCreateNumber("11def", false);
    compareIsNumberWithCreateNumber("11d11", false);
    compareIsNumberWithCreateNumber("11 11", false);
    compareIsNumberWithCreateNumber(" 1111", false);
    compareIsNumberWithCreateNumber("1111 ", false);
    compareIsNumberWithCreateNumber("2.", true); // LANG-521
    compareIsNumberWithCreateNumber("1.1L", false); // LANG-664
    compareIsNumberWithCreateNumber("+0xF", true); // LANG-1645
    compareIsNumberWithCreateNumber("+0xFFFFFFFF", true); // LANG-1645
    compareIsNumberWithCreateNumber("+0xFFFFFFFFFFFFFFFF", true); // LANG-1645
    compareIsNumberWithCreateNumber(".0", true); // LANG-1646
    compareIsNumberWithCreateNumber("0.", true); // LANG-1646
    compareIsNumberWithCreateNumber("0.D", true); // LANG-1646
    compareIsNumberWithCreateNumber("0e1", true); // LANG-1646
    compareIsNumberWithCreateNumber("0e1D", true); // LANG-1646
    compareIsNumberWithCreateNumber(".D", false); // LANG-1646
    compareIsNumberWithCreateNumber(".e10", false); // LANG-1646
    compareIsNumberWithCreateNumber(".e10D", false); // LANG-1646
  }

  @Test
  void testIsNumberLANG1252() {
    compareIsNumberWithCreateNumber("+2", true);
    compareIsNumberWithCreateNumber("+2.0", true);
  }

  @Test
  void testIsNumberLANG1385() {
    compareIsNumberWithCreateNumber("L", false);
  }

  @Test
  void testIsNumberLANG971() {
    compareIsNumberWithCreateNumber("0085", false);
    compareIsNumberWithCreateNumber("085", false);
    compareIsNumberWithCreateNumber("08", false);
    compareIsNumberWithCreateNumber("07", true);
    compareIsNumberWithCreateNumber("00", true);
  }

  @Test
  void testIsNumberLANG972() {
    compareIsNumberWithCreateNumber("0xABCD", true);
    compareIsNumberWithCreateNumber("0XABCD", true);
  }

  @Test
  void testIsNumberLANG992() {
    compareIsNumberWithCreateNumber("0.0", true);
    compareIsNumberWithCreateNumber("0.4790", true);
  }

  @Test
  void testIsParsable() {
    assertFalse(NumberUtils.isParsable(null));
    assertFalse(NumberUtils.isParsable(""));
    assertFalse(NumberUtils.isParsable("0xC1AB"));
    assertFalse(NumberUtils.isParsable("65CBA2"));
    assertFalse(NumberUtils.isParsable("pendro"));
    assertFalse(NumberUtils.isParsable("64, 2"));
    assertFalse(NumberUtils.isParsable("64.2.2"));
    assertFalse(NumberUtils.isParsable("64.."));
    assertTrue(NumberUtils.isParsable("64."));
    assertTrue(NumberUtils.isParsable("-64."));
    assertFalse(NumberUtils.isParsable("64L"));
    assertFalse(NumberUtils.isParsable("-"));
    assertFalse(NumberUtils.isParsable("--2"));
    assertTrue(NumberUtils.isParsable("64.2"));
    assertTrue(NumberUtils.isParsable("64"));
    assertTrue(NumberUtils.isParsable("018"));
    assertTrue(NumberUtils.isParsable(".18"));
    assertTrue(NumberUtils.isParsable("-65"));
    assertTrue(NumberUtils.isParsable("-018"));
    assertTrue(NumberUtils.isParsable("-018.2"));
    assertTrue(NumberUtils.isParsable("-.236"));
    assertTrue(NumberUtils.isParsable("2."));
  }

  @Test
  void testLang1729IsParsableByte() {
    assertTrue(isParsableByte("1"));
    assertFalse(isParsableByte("1 2 3"));
    assertTrue(isParsableByte("１２３"));
    assertFalse(isParsableByte("１ ２ ３"));
  }

  @Test
  void testLang1729IsParsableDouble() {
    assertTrue(isParsableDouble("1"));
    assertTrue(isParsableDouble("1."));
    assertTrue(isParsableDouble("1.0"));
    assertFalse(isParsableDouble("1.0."));
    assertFalse(isParsableDouble("1 2 3"));
    assertFalse(isParsableDouble("１ ２ ３"));
  }

  @Test
  void testLang1729IsParsableFloat() {
    assertTrue(isParsableFloat("1"));
    assertTrue(isParsableFloat("1."));
    // TODO assertTrue(isParsableFloat("1.f"));
    // TODO assertTrue(isParsableFloat("1.d"));
    assertTrue(isParsableFloat("1.0"));
    assertFalse(isParsableFloat("1.0."));
    assertFalse(isParsableFloat("1 2 3"));
    assertFalse(isParsableFloat("１ ２ ３"));
  }

  @Test
  void testLang1729IsParsableInteger() {
    assertTrue(isParsableInteger("1"));
    assertFalse(isParsableInteger("1 2 3"));
    assertTrue(isParsableInteger("１２３"));
    assertFalse(isParsableInteger("１ ２ ３"));
  }

  @Test
  void testLang1729IsParsableLong() {
    assertTrue(isParsableLong("1"));
    assertFalse(isParsableLong("1 2 3"));
    assertTrue(isParsableLong("１２３"));
    assertFalse(isParsableLong("１ ２ ３"));
  }

  @Test
  void testLang1729IsParsableShort() {
    assertTrue(isParsableShort("1"));
    assertFalse(isParsableShort("1 2 3"));
    assertTrue(isParsableShort("１２３"));
    assertFalse(isParsableShort("１ ２ ３"));
  }

  @Test
  public void TestLang747() {
    assertEquals(Integer.valueOf(0x8000), NumberUtils.createNumber("0x8000"));
    assertEquals(Integer.valueOf(0x80000), NumberUtils.createNumber("0x80000"));
    assertEquals(Integer.valueOf(0x800000), NumberUtils.createNumber("0x800000"));
    assertEquals(Integer.valueOf(0x8000000), NumberUtils.createNumber("0x8000000"));
    assertEquals(Integer.valueOf(0x7FFFFFFF), NumberUtils.createNumber("0x7FFFFFFF"));
    assertEquals(Long.valueOf(0x80000000L), NumberUtils.createNumber("0x80000000"));
    assertEquals(Long.valueOf(0xFFFFFFFFL), NumberUtils.createNumber("0xFFFFFFFF"));
    // Leading zero tests
    assertEquals(Integer.valueOf(0x8000000), NumberUtils.createNumber("0x08000000"));
    assertEquals(Integer.valueOf(0x7FFFFFFF), NumberUtils.createNumber("0x007FFFFFFF"));
    assertEquals(Long.valueOf(0x80000000L), NumberUtils.createNumber("0x080000000"));
    assertEquals(Long.valueOf(0xFFFFFFFFL), NumberUtils.createNumber("0x00FFFFFFFF"));
    assertEquals(Long.valueOf(0x800000000L), NumberUtils.createNumber("0x800000000"));
    assertEquals(Long.valueOf(0x8000000000L), NumberUtils.createNumber("0x8000000000"));
    assertEquals(Long.valueOf(0x80000000000L), NumberUtils.createNumber("0x80000000000"));
    assertEquals(Long.valueOf(0x800000000000L), NumberUtils.createNumber("0x800000000000"));
    assertEquals(Long.valueOf(0x8000000000000L), NumberUtils.createNumber("0x8000000000000"));
    assertEquals(Long.valueOf(0x80000000000000L), NumberUtils.createNumber("0x80000000000000"));
    assertEquals(Long.valueOf(0x800000000000000L), NumberUtils.createNumber("0x800000000000000"));
    assertEquals(Long.valueOf(0x7FFFFFFFFFFFFFFFL), NumberUtils.createNumber("0x7FFFFFFFFFFFFFFF"));
    // Cannot use a hex constant such as 0x8000000000000000L here as that is
    // interpreted as a negative long
    assertEquals(new BigInteger("8000000000000000", 16), NumberUtils.createNumber("0x8000000000000000"));
    assertEquals(new BigInteger("FFFFFFFFFFFFFFFF", 16), NumberUtils.createNumber("0xFFFFFFFFFFFFFFFF"));
    // Leading zero tests
    assertEquals(Long.valueOf(0x80000000000000L), NumberUtils.createNumber("0x00080000000000000"));
    assertEquals(Long.valueOf(0x800000000000000L), NumberUtils.createNumber("0x0800000000000000"));
    assertEquals(Long.valueOf(0x7FFFFFFFFFFFFFFFL), NumberUtils.createNumber("0x07FFFFFFFFFFFFFFF"));
    // Cannot use a hex constant such as 0x8000000000000000L here as that is
    // interpreted as a negative long
    assertEquals(new BigInteger("8000000000000000", 16), NumberUtils.createNumber("0x00008000000000000000"));
    assertEquals(new BigInteger("FFFFFFFFFFFFFFFF", 16), NumberUtils.createNumber("0x0FFFFFFFFFFFFFFFF"));
  }

  @Test
  void testLANG971() {
    compareIsCreatableWithCreateNumber("0085", false);
    compareIsCreatableWithCreateNumber("085", false);
    compareIsCreatableWithCreateNumber("08", false);
    compareIsCreatableWithCreateNumber("07", true);
    compareIsCreatableWithCreateNumber("00", true);
  }

  @Test
  void testLANG972() {
    compareIsCreatableWithCreateNumber("0xABCD", true);
    compareIsCreatableWithCreateNumber("0XABCD", true);
  }

  @Test
  void testLANG992() {
    compareIsCreatableWithCreateNumber("0.0", true);
    compareIsCreatableWithCreateNumber("0.4790", true);
  }

  @Test
  void testMaximumByte() {
    final byte low = 123;
    final byte mid = 123 + 1;
    final byte high = 123 + 2;
    assertEquals(high, NumberUtils.max(low, mid, high), "maximum(byte, byte, byte) 1 failed");
    assertEquals(high, NumberUtils.max(mid, low, high), "maximum(byte, byte, byte) 2 failed");
    assertEquals(high, NumberUtils.max(mid, high, low), "maximum(byte, byte, byte) 3 failed");
    assertEquals(high, NumberUtils.max(high, mid, high), "maximum(byte, byte, byte) 4 failed");
  }

  @Test
  void testMaximumDouble() {
    final double low = 12.3;
    final double mid = 12.3 + 1;
    final double high = 12.3 + 2;
    assertEquals(high, NumberUtils.max(low, mid, high), 0.0001);
    assertEquals(high, NumberUtils.max(mid, low, high), 0.0001);
    assertEquals(high, NumberUtils.max(mid, high, low), 0.0001);
    assertEquals(mid, NumberUtils.max(low, mid, low), 0.0001);
    assertEquals(high, NumberUtils.max(high, mid, high), 0.0001);
  }

  @Test
  void testMaximumFloat() {
    final float low = 12.3f;
    final float mid = 12.3f + 1;
    final float high = 12.3f + 2;
    assertEquals(high, NumberUtils.max(low, mid, high), 0.0001f);
    assertEquals(high, NumberUtils.max(mid, low, high), 0.0001f);
    assertEquals(high, NumberUtils.max(mid, high, low), 0.0001f);
    assertEquals(mid, NumberUtils.max(low, mid, low), 0.0001f);
    assertEquals(high, NumberUtils.max(high, mid, high), 0.0001f);
  }

  @Test
  void testMaximumInt() {
    assertEquals(12345, NumberUtils.max(12345, 12345 - 1, 12345 - 2), "maximum(int, int, int) 1 failed");
    assertEquals(12345, NumberUtils.max(12345 - 1, 12345, 12345 - 2), "maximum(int, int, int) 2 failed");
    assertEquals(12345, NumberUtils.max(12345 - 1, 12345 - 2, 12345), "maximum(int, int, int) 3 failed");
    assertEquals(12345, NumberUtils.max(12345 - 1, 12345, 12345), "maximum(int, int, int) 4 failed");
    assertEquals(12345, NumberUtils.max(12345, 12345, 12345), "maximum(int, int, int) 5 failed");
  }

  @Test
  void testMaximumLong() {
    assertEquals(12345L, NumberUtils.max(12345L, 12345L - 1L, 12345L - 2L), "maximum(long, long, long) 1 failed");
    assertEquals(12345L, NumberUtils.max(12345L - 1L, 12345L, 12345L - 2L), "maximum(long, long, long) 2 failed");
    assertEquals(12345L, NumberUtils.max(12345L - 1L, 12345L - 2L, 12345L), "maximum(long, long, long) 3 failed");
    assertEquals(12345L, NumberUtils.max(12345L - 1L, 12345L, 12345L), "maximum(long, long, long) 4 failed");
    assertEquals(12345L, NumberUtils.max(12345L, 12345L, 12345L), "maximum(long, long, long) 5 failed");
  }

  @Test
  void testMaximumShort() {
    final short low = 1234;
    final short mid = 1234 + 1;
    final short high = 1234 + 2;
    assertEquals(high, NumberUtils.max(low, mid, high), "maximum(short, short, short) 1 failed");
    assertEquals(high, NumberUtils.max(mid, low, high), "maximum(short, short, short) 2 failed");
    assertEquals(high, NumberUtils.max(mid, high, low), "maximum(short, short, short) 3 failed");
    assertEquals(high, NumberUtils.max(high, mid, high), "maximum(short, short, short) 4 failed");
  }

  @Test
  void testMinimumByte() {
    final byte low = 123;
    final byte mid = 123 + 1;
    final byte high = 123 + 2;
    assertEquals(low, NumberUtils.min(low, mid, high), "minimum(byte, byte, byte) 1 failed");
    assertEquals(low, NumberUtils.min(mid, low, high), "minimum(byte, byte, byte) 2 failed");
    assertEquals(low, NumberUtils.min(mid, high, low), "minimum(byte, byte, byte) 3 failed");
    assertEquals(low, NumberUtils.min(low, mid, low), "minimum(byte, byte, byte) 4 failed");
  }

  @Test
  void testMinimumDouble() {
    final double low = 12.3;
    final double mid = 12.3 + 1;
    final double high = 12.3 + 2;
    assertEquals(low, NumberUtils.min(low, mid, high), 0.0001);
    assertEquals(low, NumberUtils.min(mid, low, high), 0.0001);
    assertEquals(low, NumberUtils.min(mid, high, low), 0.0001);
    assertEquals(low, NumberUtils.min(low, mid, low), 0.0001);
    assertEquals(mid, NumberUtils.min(high, mid, high), 0.0001);
  }

  @Test
  void testMinimumFloat() {
    final float low = 12.3f;
    final float mid = 12.3f + 1;
    final float high = 12.3f + 2;
    assertEquals(low, NumberUtils.min(low, mid, high), 0.0001f);
    assertEquals(low, NumberUtils.min(mid, low, high), 0.0001f);
    assertEquals(low, NumberUtils.min(mid, high, low), 0.0001f);
    assertEquals(low, NumberUtils.min(low, mid, low), 0.0001f);
    assertEquals(mid, NumberUtils.min(high, mid, high), 0.0001f);
  }

  @Test
  void testMinimumInt() {
    assertEquals(12345, NumberUtils.min(12345, 12345 + 1, 12345 + 2), "minimum(int, int, int) 1 failed");
    assertEquals(12345, NumberUtils.min(12345 + 1, 12345, 12345 + 2), "minimum(int, int, int) 2 failed");
    assertEquals(12345, NumberUtils.min(12345 + 1, 12345 + 2, 12345), "minimum(int, int, int) 3 failed");
    assertEquals(12345, NumberUtils.min(12345 + 1, 12345, 12345), "minimum(int, int, int) 4 failed");
    assertEquals(12345, NumberUtils.min(12345, 12345, 12345), "minimum(int, int, int) 5 failed");
  }

  @Test
  void testMinimumLong() {
    assertEquals(12345L, NumberUtils.min(12345L, 12345L + 1L, 12345L + 2L), "minimum(long, long, long) 1 failed");
    assertEquals(12345L, NumberUtils.min(12345L + 1L, 12345L, 12345 + 2L), "minimum(long, long, long) 2 failed");
    assertEquals(12345L, NumberUtils.min(12345L + 1L, 12345L + 2L, 12345L), "minimum(long, long, long) 3 failed");
    assertEquals(12345L, NumberUtils.min(12345L + 1L, 12345L, 12345L), "minimum(long, long, long) 4 failed");
    assertEquals(12345L, NumberUtils.min(12345L, 12345L, 12345L), "minimum(long, long, long) 5 failed");
  }

  @Test
  void testMinimumShort() {
    final short low = 1234;
    final short mid = 1234 + 1;
    final short high = 1234 + 2;
    assertEquals(low, NumberUtils.min(low, mid, high), "minimum(short, short, short) 1 failed");
    assertEquals(low, NumberUtils.min(mid, low, high), "minimum(short, short, short) 2 failed");
    assertEquals(low, NumberUtils.min(mid, high, low), "minimum(short, short, short) 3 failed");
    assertEquals(low, NumberUtils.min(low, mid, low), "minimum(short, short, short) 4 failed");
  }

  /**
   * Test for {@link NumberUtils#createNumber(String)}
   */
  @Test
  void testStringCreateNumberEnsureNoPrecisionLoss() {
    assertInstanceOf(Float.class, NumberUtils.createNumber("1.23"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("3.40282354e+38"));
    assertInstanceOf(BigDecimal.class, NumberUtils.createNumber("1.797693134862315759e+308"));
    // LANG-1060
    assertInstanceOf(Float.class, NumberUtils.createNumber("001.12"));
    assertInstanceOf(Float.class, NumberUtils.createNumber("-001.12"));
    assertInstanceOf(Float.class, NumberUtils.createNumber("+001.12"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("003.40282354e+38"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("-003.40282354e+38"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("+003.40282354e+38"));
    assertInstanceOf(BigDecimal.class, NumberUtils.createNumber("0001.797693134862315759e+308"));
    assertInstanceOf(BigDecimal.class, NumberUtils.createNumber("-001.797693134862315759e+308"));
    assertInstanceOf(BigDecimal.class, NumberUtils.createNumber("+001.797693134862315759e+308"));
    // LANG-1613
    assertInstanceOf(Double.class, NumberUtils.createNumber("2.2250738585072014E-308"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("2.2250738585072014E-308D"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("2.2250738585072014E-308F"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("4.9E-324"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("4.9E-324D"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("4.9E-324F"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("1.7976931348623157E308"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("1.7976931348623157E308D"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("1.7976931348623157E308F"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("4.9e-324D"));
    assertInstanceOf(Double.class, NumberUtils.createNumber("4.9e-324F"));
  }

  /**
   * Test for {@link NumberUtils#toDouble(String)}.
   */
  @Test
  void testStringToDoubleString() {
    assertEquals(NumberUtils.toDouble("-1.2345"), -1.2345d, "toDouble(String) 1 failed");
    assertEquals(1.2345d, NumberUtils.toDouble("1.2345"), "toDouble(String) 2 failed");
    assertEquals(0.0d, NumberUtils.toDouble("abc"), "toDouble(String) 3 failed");
    // LANG-1060
    assertEquals(NumberUtils.toDouble("-001.2345"), -1.2345d, "toDouble(String) 4 failed");
    assertEquals(1.2345d, NumberUtils.toDouble("+001.2345"), "toDouble(String) 5 failed");
    assertEquals(1.2345d, NumberUtils.toDouble("001.2345"), "toDouble(String) 6 failed");
    assertEquals(0d, NumberUtils.toDouble("000.00000"), "toDouble(String) 7 failed");
    assertEquals(NumberUtils.toDouble(Double.MAX_VALUE + ""), Double.MAX_VALUE,
        "toDouble(Double.MAX_VALUE) failed");
    assertEquals(NumberUtils.toDouble(Double.MIN_VALUE + ""), Double.MIN_VALUE,
        "toDouble(Double.MIN_VALUE) failed");
    assertEquals(0.0d, NumberUtils.toDouble(""), "toDouble(empty) failed");
    assertEquals(0.0d, NumberUtils.toDouble((String) null), "toDouble(null) failed");
  }

  /**
   * Test for {@link NumberUtils#toDouble(String, double)}.
   */
  @Test
  void testStringToDoubleStringD() {
    assertEquals(1.2345d, NumberUtils.toDouble("1.2345", 5.1d), "toDouble(String, int) 1 failed");
    assertEquals(5.0d, NumberUtils.toDouble("a", 5.0d), "toDouble(String, int) 2 failed");
    // LANG-1060
    assertEquals(1.2345d, NumberUtils.toDouble("001.2345", 5.1d), "toDouble(String, int) 3 failed");
    assertEquals(NumberUtils.toDouble("-001.2345", 5.1d), -1.2345d, "toDouble(String, int) 4 failed");
    assertEquals(1.2345d, NumberUtils.toDouble("+001.2345", 5.1d), "toDouble(String, int) 5 failed");
    assertEquals(0d, NumberUtils.toDouble("000.00", 5.1d), "toDouble(String, int) 7 failed");
    assertEquals(5.1d, NumberUtils.toDouble("", 5.1d));
    assertEquals(5.1d, NumberUtils.toDouble((String) null, 5.1d));
  }

  /**
   * Test for {@link NumberUtils#toByte(String)}.
   */
  @Test
  void testToByteString() {
    assertEquals(123, NumberUtils.toByte("123"), "toByte(String) 1 failed");
    assertEquals(0, NumberUtils.toByte("abc"), "toByte(String) 2 failed");
    assertEquals(0, NumberUtils.toByte(""), "toByte(empty) failed");
    assertEquals(0, NumberUtils.toByte(null), "toByte(null) failed");
  }

  /**
   * Test for {@link NumberUtils#toByte(String, byte)}.
   */
  @Test
  void testToByteStringI() {
    assertEquals(123, NumberUtils.toByte("123", (byte) 5), "toByte(String, byte) 1 failed");
    assertEquals(5, NumberUtils.toByte("12.3", (byte) 5), "toByte(String, byte) 2 failed");
    assertEquals(5, NumberUtils.toByte("", (byte) 5));
    assertEquals(5, NumberUtils.toByte(null, (byte) 5));
  }

  /**
   * Test for {@link NumberUtils#toFloat(String)}.
   */
  @Test
  void testToFloatString() {
    assertEquals(NumberUtils.toFloat("-1.2345"), -1.2345f, "toFloat(String) 1 failed");
    assertEquals(1.2345f, NumberUtils.toFloat("1.2345"), "toFloat(String) 2 failed");
    assertEquals(0.0f, NumberUtils.toFloat("abc"), "toFloat(String) 3 failed");
    // LANG-1060
    assertEquals(NumberUtils.toFloat("-001.2345"), -1.2345f, "toFloat(String) 4 failed");
    assertEquals(1.2345f, NumberUtils.toFloat("+001.2345"), "toFloat(String) 5 failed");
    assertEquals(1.2345f, NumberUtils.toFloat("001.2345"), "toFloat(String) 6 failed");
    assertEquals(0f, NumberUtils.toFloat("000.00"), "toFloat(String) 7 failed");
    assertEquals(NumberUtils.toFloat(Float.MAX_VALUE + ""), Float.MAX_VALUE, "toFloat(Float.MAX_VALUE) failed");
    assertEquals(NumberUtils.toFloat(Float.MIN_VALUE + ""), Float.MIN_VALUE, "toFloat(Float.MIN_VALUE) failed");
    assertEquals(0.0f, NumberUtils.toFloat(""), "toFloat(empty) failed");
    assertEquals(0.0f, NumberUtils.toFloat(null), "toFloat(null) failed");
  }

  /**
   * Test for {@link NumberUtils#toFloat(String, float)}.
   */
  @Test
  void testToFloatStringF() {
    assertEquals(1.2345f, NumberUtils.toFloat("1.2345", 5.1f), "toFloat(String, int) 1 failed");
    assertEquals(5.0f, NumberUtils.toFloat("a", 5.0f), "toFloat(String, int) 2 failed");
    // LANG-1060
    assertEquals(5.0f, NumberUtils.toFloat("-001Z.2345", 5.0f), "toFloat(String, int) 3 failed");
    assertEquals(5.0f, NumberUtils.toFloat("+001AB.2345", 5.0f), "toFloat(String, int) 4 failed");
    assertEquals(5.0f, NumberUtils.toFloat("001Z.2345", 5.0f), "toFloat(String, int) 5 failed");
    assertEquals(5.0f, NumberUtils.toFloat("", 5.0f));
    assertEquals(5.0f, NumberUtils.toFloat(null, 5.0f));
  }

  /**
   * Test for {@link NumberUtils#toInt(String)}.
   */
  @Test
  void testToIntString() {
    assertEquals(12345, NumberUtils.toInt("12345"), "toInt(String) 1 failed");
    assertEquals(0, NumberUtils.toInt("abc"), "toInt(String) 2 failed");
    assertEquals(0, NumberUtils.toInt(""), "toInt(empty) failed");
    assertEquals(0, NumberUtils.toInt(null), "toInt(null) failed");
  }

  /**
   * Test for {@link NumberUtils#toInt(String, int)}.
   */
  @Test
  void testToIntStringI() {
    assertEquals(12345, NumberUtils.toInt("12345", 5), "toInt(String, int) 1 failed");
    assertEquals(5, NumberUtils.toInt("1234.5", 5), "toInt(String, int) 2 failed");
    assertEquals(5, NumberUtils.toInt("", 5));
    assertEquals(5, NumberUtils.toInt(null, 5));
  }

  /**
   * Test for {@link NumberUtils#toLong(String)}.
   */
  @Test
  void testToLongString() {
    assertEquals(12345L, NumberUtils.toLong("12345"), "toLong(String) 1 failed");
    assertEquals(0L, NumberUtils.toLong("abc"), "toLong(String) 2 failed");
    assertEquals(0L, NumberUtils.toLong("1L"), "toLong(String) 3 failed");
    assertEquals(0L, NumberUtils.toLong("1l"), "toLong(String) 4 failed");
    assertEquals(NumberUtils.toLong(Long.MAX_VALUE + ""), Long.MAX_VALUE, "toLong(Long.MAX_VALUE) failed");
    assertEquals(NumberUtils.toLong(Long.MIN_VALUE + ""), Long.MIN_VALUE, "toLong(Long.MIN_VALUE) failed");
    assertEquals(0L, NumberUtils.toLong(""), "toLong(empty) failed");
    assertEquals(0L, NumberUtils.toLong(null), "toLong(null) failed");
  }

  /**
   * Test for {@link NumberUtils#toLong(String, long)}.
   */
  @Test
  void testToLongStringL() {
    assertEquals(12345L, NumberUtils.toLong("12345", 5L), "toLong(String, long) 1 failed");
    assertEquals(5L, NumberUtils.toLong("1234.5", 5L), "toLong(String, long) 2 failed");
    assertEquals(5L, NumberUtils.toLong("", 5L));
    assertEquals(5L, NumberUtils.toLong(null, 5L));
  }
}
