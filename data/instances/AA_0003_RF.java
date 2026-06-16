package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.stream.Stream;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link org.apache.commons.lang3.math.NumberUtils}.
 */
class AA_0003_RF extends AbstractLangTest {
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

  @ParameterizedTest
  @MethodSource("provideDoubleCompareTestCases")
  void testCompareDouble(double a, double b, int expected) {
    assertEquals(expected, Double.compare(a, b));
  }

  private static Stream<Arguments> provideDoubleCompareTestCases() {
    return Stream.of(
        Arguments.of(Double.NaN, Double.NaN, 0),
        Arguments.of(Double.NaN, Double.POSITIVE_INFINITY, +1),
        Arguments.of(Double.NaN, Double.MAX_VALUE, +1),
        Arguments.of(Double.NaN, 1.2d, +1),
        Arguments.of(Double.NaN, 0.0d, +1),
        Arguments.of(Double.NaN, -0.0d, +1),
        Arguments.of(Double.NaN, -1.2d, +1),
        Arguments.of(Double.NaN, -Double.MAX_VALUE, +1),
        Arguments.of(Double.NaN, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(Double.POSITIVE_INFINITY, Double.NaN, -1),
        Arguments.of(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0),
        Arguments.of(Double.POSITIVE_INFINITY, Double.MAX_VALUE, +1),
        Arguments.of(Double.POSITIVE_INFINITY, 1.2d, +1),
        Arguments.of(Double.POSITIVE_INFINITY, 0.0d, +1),
        Arguments.of(Double.POSITIVE_INFINITY, -0.0d, +1),
        Arguments.of(Double.POSITIVE_INFINITY, -1.2d, +1),
        Arguments.of(Double.POSITIVE_INFINITY, -Double.MAX_VALUE, +1),
        Arguments.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(Double.MAX_VALUE, Double.NaN, -1),
        Arguments.of(Double.MAX_VALUE, Double.POSITIVE_INFINITY, -1),
        Arguments.of(Double.MAX_VALUE, Double.MAX_VALUE, 0),
        Arguments.of(Double.MAX_VALUE, 1.2d, +1),
        Arguments.of(Double.MAX_VALUE, 0.0d, +1),
        Arguments.of(Double.MAX_VALUE, -0.0d, +1),
        Arguments.of(Double.MAX_VALUE, -1.2d, +1),
        Arguments.of(Double.MAX_VALUE, -Double.MAX_VALUE, +1),
        Arguments.of(Double.MAX_VALUE, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(1.2d, Double.NaN, -1),
        Arguments.of(1.2d, Double.POSITIVE_INFINITY, -1),
        Arguments.of(1.2d, Double.MAX_VALUE, -1),
        Arguments.of(1.2d, 1.2d, 0),
        Arguments.of(1.2d, 0.0d, +1),
        Arguments.of(1.2d, -0.0d, +1),
        Arguments.of(1.2d, -1.2d, +1),
        Arguments.of(1.2d, -Double.MAX_VALUE, +1),
        Arguments.of(1.2d, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(0.0d, Double.NaN, -1),
        Arguments.of(0.0d, Double.POSITIVE_INFINITY, -1),
        Arguments.of(0.0d, Double.MAX_VALUE, -1),
        Arguments.of(0.0d, 1.2d, -1),
        Arguments.of(0.0d, 0.0d, 0),
        Arguments.of(0.0d, -0.0d, +1),
        Arguments.of(0.0d, -1.2d, +1),
        Arguments.of(0.0d, -Double.MAX_VALUE, +1),
        Arguments.of(0.0d, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(-0.0d, Double.NaN, -1),
        Arguments.of(-0.0d, Double.POSITIVE_INFINITY, -1),
        Arguments.of(-0.0d, Double.MAX_VALUE, -1),
        Arguments.of(-0.0d, 1.2d, -1),
        Arguments.of(-0.0d, 0.0d, -1),
        Arguments.of(-0.0d, -0.0d, 0),
        Arguments.of(-0.0d, -1.2d, +1),
        Arguments.of(-0.0d, -Double.MAX_VALUE, +1),
        Arguments.of(-0.0d, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(-1.2d, Double.NaN, -1),
        Arguments.of(-1.2d, Double.POSITIVE_INFINITY, -1),
        Arguments.of(-1.2d, Double.MAX_VALUE, -1),
        Arguments.of(-1.2d, 1.2d, -1),
        Arguments.of(-1.2d, 0.0d, -1),
        Arguments.of(-1.2d, -0.0d, -1),
        Arguments.of(-1.2d, -1.2d, 0),
        Arguments.of(-1.2d, -Double.MAX_VALUE, +1),
        Arguments.of(-1.2d, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(-Double.MAX_VALUE, Double.NaN, -1),
        Arguments.of(-Double.MAX_VALUE, Double.POSITIVE_INFINITY, -1),
        Arguments.of(-Double.MAX_VALUE, Double.MAX_VALUE, -1),
        Arguments.of(-Double.MAX_VALUE, 1.2d, -1),
        Arguments.of(-Double.MAX_VALUE, 0.0d, -1),
        Arguments.of(-Double.MAX_VALUE, -0.0d, -1),
        Arguments.of(-Double.MAX_VALUE, -1.2d, -1),
        Arguments.of(-Double.MAX_VALUE, -Double.MAX_VALUE, 0),
        Arguments.of(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY, +1),
        Arguments.of(Double.NEGATIVE_INFINITY, Double.NaN, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, Double.MAX_VALUE, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, 1.2d, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, 0.0d, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, -0.0d, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, -1.2d, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, -1),
        Arguments.of(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0));
  }

  @ParameterizedTest
  @MethodSource("provideFloatCompareTestCases")
  void testCompareFloat(float a, float b, int expected) {
    assertEquals(expected, Float.compare(a, b));
  }

  private static Stream<Arguments> provideFloatCompareTestCases() {
    return Stream.of(
        Arguments.of(Float.NaN, Float.NaN, 0),
        Arguments.of(Float.NaN, Float.POSITIVE_INFINITY, +1),
        Arguments.of(Float.NaN, Float.MAX_VALUE, +1),
        Arguments.of(Float.NaN, 1.2f, +1),
        Arguments.of(Float.NaN, 0.0f, +1),
        Arguments.of(Float.NaN, -0.0f, +1),
        Arguments.of(Float.NaN, -1.2f, +1),
        Arguments.of(Float.NaN, -Float.MAX_VALUE, +1),
        Arguments.of(Float.NaN, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(Float.POSITIVE_INFINITY, Float.NaN, -1),
        Arguments.of(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0),
        Arguments.of(Float.POSITIVE_INFINITY, Float.MAX_VALUE, +1),
        Arguments.of(Float.POSITIVE_INFINITY, 1.2f, +1),
        Arguments.of(Float.POSITIVE_INFINITY, 0.0f, +1),
        Arguments.of(Float.POSITIVE_INFINITY, -0.0f, +1),
        Arguments.of(Float.POSITIVE_INFINITY, -1.2f, +1),
        Arguments.of(Float.POSITIVE_INFINITY, -Float.MAX_VALUE, +1),
        Arguments.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(Float.MAX_VALUE, Float.NaN, -1),
        Arguments.of(Float.MAX_VALUE, Float.POSITIVE_INFINITY, -1),
        Arguments.of(Float.MAX_VALUE, Float.MAX_VALUE, 0),
        Arguments.of(Float.MAX_VALUE, 1.2f, +1),
        Arguments.of(Float.MAX_VALUE, 0.0f, +1),
        Arguments.of(Float.MAX_VALUE, -0.0f, +1),
        Arguments.of(Float.MAX_VALUE, -1.2f, +1),
        Arguments.of(Float.MAX_VALUE, -Float.MAX_VALUE, +1),
        Arguments.of(Float.MAX_VALUE, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(1.2f, Float.NaN, -1),
        Arguments.of(1.2f, Float.POSITIVE_INFINITY, -1),
        Arguments.of(1.2f, Float.MAX_VALUE, -1),
        Arguments.of(1.2f, 1.2f, 0),
        Arguments.of(1.2f, 0.0f, +1),
        Arguments.of(1.2f, -0.0f, +1),
        Arguments.of(1.2f, -1.2f, +1),
        Arguments.of(1.2f, -Float.MAX_VALUE, +1),
        Arguments.of(1.2f, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(0.0f, Float.NaN, -1),
        Arguments.of(0.0f, Float.POSITIVE_INFINITY, -1),
        Arguments.of(0.0f, Float.MAX_VALUE, -1),
        Arguments.of(0.0f, 1.2f, -1),
        Arguments.of(0.0f, 0.0f, 0),
        Arguments.of(0.0f, -0.0f, +1),
        Arguments.of(0.0f, -1.2f, +1),
        Arguments.of(0.0f, -Float.MAX_VALUE, +1),
        Arguments.of(0.0f, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(-0.0f, Float.NaN, -1),
        Arguments.of(-0.0f, Float.POSITIVE_INFINITY, -1),
        Arguments.of(-0.0f, Float.MAX_VALUE, -1),
        Arguments.of(-0.0f, 1.2f, -1),
        Arguments.of(-0.0f, 0.0f, -1),
        Arguments.of(-0.0f, -0.0f, 0),
        Arguments.of(-0.0f, -1.2f, +1),
        Arguments.of(-0.0f, -Float.MAX_VALUE, +1),
        Arguments.of(-0.0f, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(-1.2f, Float.NaN, -1),
        Arguments.of(-1.2f, Float.POSITIVE_INFINITY, -1),
        Arguments.of(-1.2f, Float.MAX_VALUE, -1),
        Arguments.of(-1.2f, 1.2f, -1),
        Arguments.of(-1.2f, 0.0f, -1),
        Arguments.of(-1.2f, -0.0f, -1),
        Arguments.of(-1.2f, -1.2f, 0),
        Arguments.of(-1.2f, -Float.MAX_VALUE, +1),
        Arguments.of(-1.2f, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(-Float.MAX_VALUE, Float.NaN, -1),
        Arguments.of(-Float.MAX_VALUE, Float.POSITIVE_INFINITY, -1),
        Arguments.of(-Float.MAX_VALUE, Float.MAX_VALUE, -1),
        Arguments.of(-Float.MAX_VALUE, 1.2f, -1),
        Arguments.of(-Float.MAX_VALUE, 0.0f, -1),
        Arguments.of(-Float.MAX_VALUE, -0.0f, -1),
        Arguments.of(-Float.MAX_VALUE, -1.2f, -1),
        Arguments.of(-Float.MAX_VALUE, -Float.MAX_VALUE, 0),
        Arguments.of(-Float.MAX_VALUE, Float.NEGATIVE_INFINITY, +1),
        Arguments.of(Float.NEGATIVE_INFINITY, Float.NaN, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, Float.MAX_VALUE, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, 1.2f, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, 0.0f, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, -0.0f, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, -1.2f, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE, -1),
        Arguments.of(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, 0));
  }

  @SuppressWarnings("cast") // suppress instanceof warning check
  @ParameterizedTest
  @MethodSource("provideConstantsTypeTestCases")
  void testConstantsType(Class<?> expectedType, Object constant) {
    assertInstanceOf(expectedType, constant);
  }

  private static Stream<Arguments> provideConstantsTypeTestCases() {
    return Stream.of(
        Arguments.of(Long.class, NumberUtils.LONG_ZERO),
        Arguments.of(Long.class, NumberUtils.LONG_ONE),
        Arguments.of(Long.class, NumberUtils.LONG_MINUS_ONE),
        Arguments.of(Integer.class, NumberUtils.INTEGER_ZERO),
        Arguments.of(Integer.class, NumberUtils.INTEGER_ONE),
        Arguments.of(Integer.class, NumberUtils.INTEGER_MINUS_ONE),
        Arguments.of(Short.class, NumberUtils.SHORT_ZERO),
        Arguments.of(Short.class, NumberUtils.SHORT_ONE),
        Arguments.of(Short.class, NumberUtils.SHORT_MINUS_ONE),
        Arguments.of(Byte.class, NumberUtils.BYTE_ZERO),
        Arguments.of(Byte.class, NumberUtils.BYTE_ONE),
        Arguments.of(Byte.class, NumberUtils.BYTE_MINUS_ONE),
        Arguments.of(Double.class, NumberUtils.DOUBLE_ZERO),
        Arguments.of(Double.class, NumberUtils.DOUBLE_ONE),
        Arguments.of(Double.class, NumberUtils.DOUBLE_MINUS_ONE),
        Arguments.of(Float.class, NumberUtils.FLOAT_ZERO),
        Arguments.of(Float.class, NumberUtils.FLOAT_ONE),
        Arguments.of(Float.class, NumberUtils.FLOAT_MINUS_ONE));
  }

  @ParameterizedTest
  @MethodSource("provideConstantsValueTestCases")
  void testConstantsValue(Number expected, Number constant) {
    assertEquals(expected, constant);
  }

  private static Stream<Arguments> provideConstantsValueTestCases() {
    return Stream.of(
        Arguments.of(0L, NumberUtils.LONG_ZERO.longValue()),
        Arguments.of(1L, NumberUtils.LONG_ONE.longValue()),
        Arguments.of(-1L, NumberUtils.LONG_MINUS_ONE.longValue()),
        Arguments.of(0, NumberUtils.INTEGER_ZERO.intValue()),
        Arguments.of(1, NumberUtils.INTEGER_ONE.intValue()),
        Arguments.of(-1, NumberUtils.INTEGER_MINUS_ONE.intValue()),
        Arguments.of((short) 0, NumberUtils.SHORT_ZERO.shortValue()),
        Arguments.of((short) 1, NumberUtils.SHORT_ONE.shortValue()),
        Arguments.of((short) -1, NumberUtils.SHORT_MINUS_ONE.shortValue()),
        Arguments.of((byte) 0, NumberUtils.BYTE_ZERO.byteValue()),
        Arguments.of((byte) 1, NumberUtils.BYTE_ONE.byteValue()),
        Arguments.of((byte) -1, NumberUtils.BYTE_MINUS_ONE.byteValue()),
        Arguments.of(0.0d, NumberUtils.DOUBLE_ZERO.doubleValue()),
        Arguments.of(1.0d, NumberUtils.DOUBLE_ONE.doubleValue()),
        Arguments.of(-1.0d, NumberUtils.DOUBLE_MINUS_ONE.doubleValue()),
        Arguments.of(0.0f, NumberUtils.FLOAT_ZERO.floatValue()),
        Arguments.of(1.0f, NumberUtils.FLOAT_ONE.floatValue()),
        Arguments.of(-1.0f, NumberUtils.FLOAT_MINUS_ONE.floatValue()));
  }

  @ParameterizedTest
  @MethodSource("provideCreateNumberTestCases")
  void testCreateNumber(String input, Object expected, String description) {
    if (expected == null) {
      assertNull(NumberUtils.createNumber(input), description);
    } else {
      assertEquals(expected, NumberUtils.createNumber(input), description);
    }
  }

  private static Stream<Arguments> provideCreateNumberTestCases() {
    return Stream.of(
        // a lot of things can go wrong
        Arguments.of("1234.5", Float.valueOf("1234.5"), "createNumber(String) 1 failed"),
        Arguments.of("12345", Integer.valueOf("12345"), "createNumber(String) 2 failed"),
        Arguments.of("1234.5D", Double.valueOf("1234.5"), "createNumber(String) 3 failed"),
        Arguments.of("1234.5d", Double.valueOf("1234.5"), "createNumber(String) 3 failed"),
        Arguments.of("1234.5F", Float.valueOf("1234.5"), "createNumber(String) 4 failed"),
        Arguments.of("1234.5f", Float.valueOf("1234.5"), "createNumber(String) 4 failed"),
        Arguments.of("" + (Integer.MAX_VALUE + 1L), Long.valueOf(Integer.MAX_VALUE + 1L),
            "createNumber(String) 5 failed"),
        Arguments.of("12345L", Long.valueOf(12345), "createNumber(String) 6 failed"),
        Arguments.of("12345l", Long.valueOf(12345), "createNumber(String) 6 failed"),
        Arguments.of("-1234.5", Float.valueOf("-1234.5"), "createNumber(String) 7 failed"),
        Arguments.of("-12345", Integer.valueOf("-12345"), "createNumber(String) 8 failed"),
        Arguments.of("1.1E200", Double.valueOf("1.1E200"), "createNumber(String) 11 failed"),
        Arguments.of("1.1E20", Float.valueOf("1.1E20"), "createNumber(String) 12 failed"),
        Arguments.of("-1.1E200", Double.valueOf("-1.1E200"), "createNumber(String) 13 failed"),
        Arguments.of("1.1E-200", Double.valueOf("1.1E-200"), "createNumber(String) 14 failed"),
        Arguments.of(null, null, "createNumber(null) failed"),
        Arguments.of("12345678901234567890L", new BigInteger("12345678901234567890"), "createNumber(String) failed"),
        Arguments.of("1.1E-700F", new BigDecimal("1.1E-700"), "createNumber(String) 15 failed"),
        Arguments.of("10" + Integer.MAX_VALUE + "L", Long.valueOf("10" + Integer.MAX_VALUE),
            "createNumber(String) 16 failed"),
        Arguments.of("10" + Integer.MAX_VALUE, Long.valueOf("10" + Integer.MAX_VALUE),
            "createNumber(String) 17 failed"),
        Arguments.of("10" + Long.MAX_VALUE, new BigInteger("10" + Long.MAX_VALUE), "createNumber(String) 18 failed"),
        // LANG-521
        Arguments.of("2.", Float.valueOf("2."), "createNumber(String) LANG-521 failed"),
        // LANG-693
        Arguments.of("" + Double.MAX_VALUE, Double.valueOf(Double.MAX_VALUE), "createNumber(String) LANG-693 failed"),
        // LANG-1018
        Arguments.of("-160952.54", Double.valueOf("-160952.54"), "createNumber(String) LANG-1018 failed"),
        // LANG-1187
        Arguments.of("6264583.33", Double.valueOf("6264583.33"), "createNumber(String) LANG-1187 failed"),
        // LANG-1215
        Arguments.of("193343.82", Double.valueOf("193343.82"), "createNumber(String) LANG-1215 failed"),
        // LANG-1060
        Arguments.of("001234.5678", Double.valueOf("001234.5678"), "createNumber(String) LANG-1060a failed"),
        Arguments.of("+001234.5678", Double.valueOf("+001234.5678"), "createNumber(String) LANG-1060b failed"),
        Arguments.of("-001234.5678", Double.valueOf("-001234.5678"), "createNumber(String) LANG-1060c failed"),
        Arguments.of("0000.00000d", Double.valueOf("0000.00000"), "createNumber(String) LANG-1060d failed"),
        Arguments.of("001234.56", Float.valueOf("001234.56"), "createNumber(String) LANG-1060e failed"),
        Arguments.of("+001234.56", Float.valueOf("+001234.56"), "createNumber(String) LANG-1060f failed"),
        Arguments.of("-001234.56", Float.valueOf("-001234.56"), "createNumber(String) LANG-1060g failed"),
        Arguments.of("0000.10", Float.valueOf("0000.10"), "createNumber(String) LANG-1060h failed"),
        Arguments.of("001.1E20", Float.valueOf("001.1E20"), "createNumber(String) LANG-1060i failed"),
        Arguments.of("+001.1E20", Float.valueOf("+001.1E20"), "createNumber(String) LANG-1060j failed"),
        Arguments.of("-001.1E20", Float.valueOf("-001.1E20"), "createNumber(String) LANG-1060k failed"),
        Arguments.of("001.1E200", Double.valueOf("001.1E200"), "createNumber(String) LANG-1060l failed"),
        Arguments.of("+001.1E200", Double.valueOf("+001.1E200"), "createNumber(String) LANG-1060m failed"),
        Arguments.of("-001.1E200", Double.valueOf("-001.1E200"), "createNumber(String) LANG-1060n failed"),
        // LANG-1645
        Arguments.of("+0xF", Integer.decode("+0xF"), "createNumber(String) LANG-1645a failed"),
        Arguments.of("+0xFFFFFFFF", Long.decode("+0xFFFFFFFF"), "createNumber(String) LANG-1645b failed"),
        Arguments.of("+0xFFFFFFFFFFFFFFFF", new BigInteger("+FFFFFFFFFFFFFFFF", 16),
            "createNumber(String) LANG-1645c failed"));
  }

  @ParameterizedTest
  @MethodSource("provideCreateNumberIntValueTestCases")
  void testCreateNumberIntValue(String input, int expected, String description) {
    assertEquals(expected, NumberUtils.createNumber(input).intValue(), description);
  }

  private static Stream<Arguments> provideCreateNumberIntValueTestCases() {
    return Stream.of(
        Arguments.of("0xFADE", 0xFADE, "createNumber(String) 9a failed"),
        Arguments.of("0Xfade", 0xFADE, "createNumber(String) 9b failed"),
        Arguments.of("-0xFADE", -0xFADE, "createNumber(String) 10a failed"),
        Arguments.of("-0Xfade", -0xFADE, "createNumber(String) 10b failed"));
  }

  @ParameterizedTest
  @MethodSource("provideCreateNumberFailureInputs")
  void testCreateNumberFailure(String input) {
    assertThrows(NumberFormatException.class, () -> NumberUtils.createNumber(input));
  }

  private static Stream<String> provideCreateNumberFailureInputs() {
    return Stream.of(
        "--1.1E-700F", // Check that the code fails to create a valid number when preceded by -- rather
                       // than -
        "-1.1E+0-7e00", // Check that the code fails to create a valid number when both e and E are
                        // present (with decimal)
        "-11E+0-7e00", // Check that the code fails to create a valid number when both e and E are
                       // present (no decimal)
        "1eE+00001", // Check that the code fails to create a valid number when both e and E are
                     // present (no decimal)
        "1234.5ff", // Check that the code fails to create a valid number when there are multiple
                    // trailing 'f' characters (LANG-1205)
        "1234.5FF", // Check that the code fails to create a valid number when there are multiple
                    // trailing 'F' characters (LANG-1205)
        "1234.5dd", // Check that the code fails to create a valid number when there are multiple
                    // trailing 'd' characters (LANG-1205)
        "1234.5DD" // Check that the code fails to create a valid number when there are multiple
                   // trailing 'D' characters (LANG-1205)
    );
  }

  /**
   * Tests isCreatable(String) and tests that createNumber(String) returns a valid
   * number iff isCreatable(String)
   * returns false.
   */
  @ParameterizedTest
  @MethodSource("provideIsCreatableTestCases")
  void testIsCreatable(String input, boolean expected) {
    compareIsCreatableWithCreateNumber(input, expected);
  }

  private static Stream<Arguments> provideIsCreatableTestCases() {
    return Stream.of(
        Arguments.of("12345", true),
        Arguments.of("1234.5", true),
        Arguments.of(".12345", true),
        Arguments.of("1234E5", true),
        Arguments.of("1234E+5", true),
        Arguments.of("1234E-5", true),
        Arguments.of("123.4E5", true),
        Arguments.of("-1234", true),
        Arguments.of("-1234.5", true),
        Arguments.of("-.12345", true),
        Arguments.of("-1234E5", true),
        Arguments.of("0", true),
        Arguments.of("0.1", true), // LANG-1216
        Arguments.of("-0", true),
        Arguments.of("01234", true),
        Arguments.of("-01234", true),
        Arguments.of("-0xABC123", true),
        Arguments.of("-0x0", true),
        Arguments.of("123.4E21D", true),
        Arguments.of("-221.23F", true),
        Arguments.of("22338L", true),
        Arguments.of(null, false),
        Arguments.of("", false),
        Arguments.of(" ", false),
        Arguments.of("\r\n\t", false),
        Arguments.of("--2.3", false),
        Arguments.of(".12.3", false),
        Arguments.of("-123E", false),
        Arguments.of("-123E+-212", false),
        Arguments.of("-123E2.12", false),
        Arguments.of("0xGF", false),
        Arguments.of("0xFAE-1", false),
        Arguments.of(".", false),
        Arguments.of("-0ABC123", false),
        Arguments.of("123.4E-D", false),
        Arguments.of("123.4ED", false),
        Arguments.of("1234E5l", false),
        Arguments.of("11a", false),
        Arguments.of("1a", false),
        Arguments.of("a", false),
        Arguments.of("11g", false),
        Arguments.of("11z", false),
        Arguments.of("11def", false),
        Arguments.of("11d11", false),
        Arguments.of("11 11", false),
        Arguments.of(" 1111", false),
        Arguments.of("1111 ", false),
        Arguments.of("2.", true), // LANG-521
        Arguments.of("1.1L", false), // LANG-664
        Arguments.of("+0xF", true), // LANG-1645
        Arguments.of("+0xFFFFFFFF", true), // LANG-1645
        Arguments.of("+0xFFFFFFFFFFFFFFFF", true), // LANG-1645
        Arguments.of(".0", true), // LANG-1646
        Arguments.of("0.", true), // LANG-1646
        Arguments.of("0.D", true), // LANG-1646
        Arguments.of("0e1", true), // LANG-1646
        Arguments.of("0e1D", true), // LANG-1646
        Arguments.of(".D", false), // LANG-1646
        Arguments.of(".e10", false), // LANG-1646
        Arguments.of(".e10D", false) // LANG-1646
    );
  }

  @ParameterizedTest
  @MethodSource("provideIsDigitsTestCases")
  void testIsDigits(String input, boolean expected, String description) {
    assertEquals(expected, NumberUtils.isDigits(input), description);
  }

  private static Stream<Arguments> provideIsDigitsTestCases() {
    return Stream.of(
        Arguments.of(null, false, "isDigits(null) failed"),
        Arguments.of("", false, "isDigits('') failed"),
        Arguments.of("12345", true, "isDigits(String) failed"),
        Arguments.of("1234.5", false, "isDigits(String) neg 1 failed"),
        Arguments.of("1ab", false, "isDigits(String) neg 3 failed"),
        Arguments.of("abc", false, "isDigits(String) neg 4 failed"));
  }

  /**
   * Tests isCreatable(String) and tests that createNumber(String) returns a valid
   * number iff isCreatable(String)
   * returns false.
   */
  @ParameterizedTest
  @MethodSource("provideIsNumberTestCases")
  void testIsNumber(String input, boolean expected) {
    compareIsNumberWithCreateNumber(input, expected);
  }

  private static Stream<Arguments> provideIsNumberTestCases() {
    return Stream.of(
        Arguments.of("12345", true),
        Arguments.of("1234.5", true),
        Arguments.of(".12345", true),
        Arguments.of("1234E5", true),
        Arguments.of("1234E+5", true),
        Arguments.of("1234E-5", true),
        Arguments.of("123.4E5", true),
        Arguments.of("-1234", true),
        Arguments.of("-1234.5", true),
        Arguments.of("-.12345", true),
        Arguments.of("-0001.12345", true),
        Arguments.of("-000.12345", true),
        Arguments.of("+00.12345", true),
        Arguments.of("+0002.12345", true),
        Arguments.of("-1234E5", true),
        Arguments.of("0", true),
        Arguments.of("-0", true),
        Arguments.of("01234", true),
        Arguments.of("-01234", true),
        Arguments.of("-0xABC123", true),
        Arguments.of("-0x0", true),
        Arguments.of("123.4E21D", true),
        Arguments.of("-221.23F", true),
        Arguments.of("22338L", true),
        Arguments.of(null, false),
        Arguments.of("", false),
        Arguments.of(" ", false),
        Arguments.of("\r\n\t", false),
        Arguments.of("--2.3", false),
        Arguments.of(".12.3", false),
        Arguments.of("-123E", false),
        Arguments.of("-123E+-212", false),
        Arguments.of("-123E2.12", false),
        Arguments.of("0xGF", false),
        Arguments.of("0xFAE-1", false),
        Arguments.of(".", false),
        Arguments.of("-0ABC123", false),
        Arguments.of("123.4E-D", false),
        Arguments.of("123.4ED", false),
        Arguments.of("+000E.12345", false),
        Arguments.of("-000E.12345", false),
        Arguments.of("1234E5l", false),
        Arguments.of("11a", false),
        Arguments.of("1a", false),
        Arguments.of("a", false),
        Arguments.of("11g", false),
        Arguments.of("11z", false),
        Arguments.of("11def", false),
        Arguments.of("11d11", false),
        Arguments.of("11 11", false),
        Arguments.of(" 1111", false),
        Arguments.of("1111 ", false),
        Arguments.of("2.", true), // LANG-521
        Arguments.of("1.1L", false), // LANG-664
        Arguments.of("+0xF", true), // LANG-1645
        Arguments.of("+0xFFFFFFFF", true), // LANG-1645
        Arguments.of("+0xFFFFFFFFFFFFFFFF", true), // LANG-1645
        Arguments.of(".0", true), // LANG-1646
        Arguments.of("0.", true), // LANG-1646
        Arguments.of("0.D", true), // LANG-1646
        Arguments.of("0e1", true), // LANG-1646
        Arguments.of("0e1D", true), // LANG-1646
        Arguments.of(".D", false), // LANG-1646
        Arguments.of(".e10", false), // LANG-1646
        Arguments.of(".e10D", false), // LANG-1646
        Arguments.of("+2", true), // LANG-1252
        Arguments.of("+2.0", true), // LANG-1252
        Arguments.of("L", false), // LANG-1385
        Arguments.of("0085", false), // LANG-971
        Arguments.of("085", false), // LANG-971
        Arguments.of("08", false), // LANG-971
        Arguments.of("07", true), // LANG-971
        Arguments.of("00", true), // LANG-971
        Arguments.of("0xABCD", true), // LANG-972
        Arguments.of("0.0", true), // LANG-992
        Arguments.of("0.4790", true) // LANG-992
    );
  }

  @ParameterizedTest
  @MethodSource("provideIsParsableTestCases")
  void testIsParsable(String input, boolean expected) {
    assertEquals(expected, NumberUtils.isParsable(input));
  }

  private static Stream<Arguments> provideIsParsableTestCases() {
    return Stream.of(
        Arguments.of(null, false),
        Arguments.of("", false),
        Arguments.of("0xC1AB", false),
        Arguments.of("65CBA2", false),
        Arguments.of("pendro", false),
        Arguments.of("64, 2", false),
        Arguments.of("64.2.2", false),
        Arguments.of("64..", false),
        Arguments.of("64.", true),
        Arguments.of("-64.", true),
        Arguments.of("64L", false),
        Arguments.of("-", false),
        Arguments.of("--2", false),
        Arguments.of("64.2", true),
        Arguments.of("64", true),
        Arguments.of("018", true),
        Arguments.of(".18", true),
        Arguments.of("-65", true),
        Arguments.of("-018", true),
        Arguments.of("-018.2", true),
        Arguments.of("-.236", true),
        Arguments.of("2.", true));
  }

  private static Stream<Arguments> provideTestLang1729IsParsableByteTestCases() {
    return Stream.of(
        Arguments.of("1", true),
        Arguments.of("1 2 3", false),
        Arguments.of("１２３", true),
        Arguments.of("１ ２ ３", false));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang1729IsParsableByteTestCases")
  void testLang1729IsParsableByte(String input, boolean expected) {
    assertEquals(expected, isParsableByte(input));
  }

  private static Stream<Arguments> provideTestLang1729IsParsableDoubleTestCases() {
    return Stream.of(
        Arguments.of("1", true),
        Arguments.of("1.", true),
        Arguments.of("1.0", true),
        Arguments.of("1.0.", false),
        Arguments.of("1 2 3", false),
        Arguments.of("１ ２ ３", false));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang1729IsParsableDoubleTestCases")
  void testLang1729IsParsableDouble(String input, boolean expected) {
    assertEquals(expected, isParsableDouble(input));
  }

  private static Stream<Arguments> provideTestLang1729IsParsableFloatTestCases() {
    return Stream.of(
        Arguments.of("1", true),
        Arguments.of("1.", true),
        Arguments.of("1.0", true),
        Arguments.of("1.0.", false),
        Arguments.of("1 2 3", false),
        Arguments.of("１ ２ ３", false));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang1729IsParsableFloatTestCases")
  void testLang1729IsParsableFloat(String input, boolean expected) {
    assertEquals(expected, isParsableFloat(input));
  }

  private static Stream<Arguments> provideTestLang1729IsParsableIntegerTestCases() {
    return Stream.of(
        Arguments.of("1", true),
        Arguments.of("1 2 3", false),
        Arguments.of("１２３", true),
        Arguments.of("１ ２ ３", false));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang1729IsParsableIntegerTestCases")
  void testLang1729IsParsableInteger(String input, boolean expected) {
    assertEquals(expected, isParsableInteger(input));
  }

  private static Stream<Arguments> provideTestLang1729IsParsableLongTestCases() {
    return Stream.of(
        Arguments.of("1", true),
        Arguments.of("1 2 3", false),
        Arguments.of("１２３", true),
        Arguments.of("１ ２ ３", false));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang1729IsParsableLongTestCases")
  void testLang1729IsParsableLong(String input, boolean expected) {
    assertEquals(expected, isParsableLong(input));
  }

  private static Stream<Arguments> provideTestLang1729IsParsableShortTestCases() {
    return Stream.of(
        Arguments.of("1", true),
        Arguments.of("1 2 3", false),
        Arguments.of("１２３", true),
        Arguments.of("１ ２ ３", false));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang1729IsParsableShortTestCases")
  void testLang1729IsParsableShort(String input, boolean expected) {
    assertEquals(expected, isParsableShort(input));
  }

  private static Stream<Arguments> provideTestLang747Cases() {
    return Stream.of(
        Arguments.of("0x8000", Integer.valueOf(0x8000)),
        Arguments.of("0x80000", Integer.valueOf(0x80000)),
        Arguments.of("0x800000", Integer.valueOf(0x800000)),
        Arguments.of("0x8000000", Integer.valueOf(0x8000000)),
        Arguments.of("0x7FFFFFFF", Integer.valueOf(0x7FFFFFFF)),
        Arguments.of("0x80000000", Long.valueOf(0x80000000L)),
        Arguments.of("0xFFFFFFFF", Long.valueOf(0xFFFFFFFFL)),
        // Leading zero tests
        Arguments.of("0x08000000", Integer.valueOf(0x8000000)),
        Arguments.of("0x007FFFFFFF", Integer.valueOf(0x7FFFFFFF)),
        Arguments.of("0x080000000", Long.valueOf(0x80000000L)),
        Arguments.of("0x00FFFFFFFF", Long.valueOf(0xFFFFFFFFL)),
        Arguments.of("0x800000000", Long.valueOf(0x800000000L)),
        Arguments.of("0x8000000000", Long.valueOf(0x8000000000L)),
        Arguments.of("0x80000000000", Long.valueOf(0x80000000000L)),
        Arguments.of("0x800000000000", Long.valueOf(0x800000000000L)),
        Arguments.of("0x8000000000000", Long.valueOf(0x8000000000000L)),
        Arguments.of("0x80000000000000", Long.valueOf(0x80000000000000L)),
        Arguments.of("0x800000000000000", Long.valueOf(0x800000000000000L)),
        Arguments.of("0x7FFFFFFFFFFFFFFF", Long.valueOf(0x7FFFFFFFFFFFFFFFL)),
        // Cannot use a hex constant such as 0x8000000000000000L here as that is
        // interpreted as a negative long
        Arguments.of("0x8000000000000000", new BigInteger("8000000000000000", 16)),
        Arguments.of("0xFFFFFFFFFFFFFFFF", new BigInteger("FFFFFFFFFFFFFFFF", 16)),
        // Leading zero tests
        Arguments.of("0x00080000000000000", Long.valueOf(0x80000000000000L)),
        Arguments.of("0x0800000000000000", Long.valueOf(0x800000000000000L)),
        Arguments.of("0x07FFFFFFFFFFFFFFF", Long.valueOf(0x7FFFFFFFFFFFFFFFL)),
        // Cannot use a hex constant such as 0x8000000000000000L here as that is
        // interpreted as a negative long
        Arguments.of("0x00008000000000000000", new BigInteger("8000000000000000", 16)),
        Arguments.of("0x0FFFFFFFFFFFFFFFF", new BigInteger("FFFFFFFFFFFFFFFF", 16)));
  }

  @ParameterizedTest
  @MethodSource("provideTestLang747Cases")
  public void TestLang747(String input, Number expected) {
    assertEquals(expected, NumberUtils.createNumber(input));
  }

  private static Stream<Arguments> provideTestLANG971Cases() {
    return Stream.of(
        Arguments.of("0085", false),
        Arguments.of("085", false),
        Arguments.of("08", false),
        Arguments.of("07", true),
        Arguments.of("00", true));
  }

  @ParameterizedTest
  @MethodSource("provideTestLANG971Cases")
  void testLANG971(String input, boolean expected) {
    compareIsCreatableWithCreateNumber(input, expected);
  }

  private static Stream<Arguments> provideTestLANG972Cases() {
    return Stream.of(
        Arguments.of("0xABCD", true),
        Arguments.of("0XABCD", true));
  }

  @ParameterizedTest
  @MethodSource("provideTestLANG972Cases")
  void testLANG972(String input, boolean expected) {
    compareIsCreatableWithCreateNumber(input, expected);
  }

  private static Stream<Arguments> provideTestLANG992Cases() {
    return Stream.of(
        Arguments.of("0.0", true),
        Arguments.of("0.4790", true));
  }

  @ParameterizedTest
  @MethodSource("provideTestLANG992Cases")
  void testLANG992(String input, boolean expected) {
    compareIsCreatableWithCreateNumber(input, expected);
  }

  @ParameterizedTest
  @MethodSource("provideMaximumByteTestCases")
  void testMaximumByte(byte a, byte b, byte c, byte expected, String description) {
    assertEquals(expected, NumberUtils.max(a, b, c), description);
  }

  private static Stream<Arguments> provideMaximumByteTestCases() {
    final byte low = 123;
    final byte mid = 123 + 1;
    final byte high = 123 + 2;
    return Stream.of(
        Arguments.of(low, mid, high, high, "maximum(byte, byte, byte) 1 failed"),
        Arguments.of(mid, low, high, high, "maximum(byte, byte, byte) 2 failed"),
        Arguments.of(mid, high, low, high, "maximum(byte, byte, byte) 3 failed"),
        Arguments.of(high, mid, high, high, "maximum(byte, byte, byte) 4 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMaximumDoubleTestCases")
  void testMaximumDouble(double a, double b, double c, double expected, double delta) {
    assertEquals(expected, NumberUtils.max(a, b, c), delta);
  }

  private static Stream<Arguments> provideMaximumDoubleTestCases() {
    final double low = 12.3;
    final double mid = 12.3 + 1;
    final double high = 12.3 + 2;
    return Stream.of(
        Arguments.of(low, mid, high, high, 0.0001),
        Arguments.of(mid, low, high, high, 0.0001),
        Arguments.of(mid, high, low, high, 0.0001),
        Arguments.of(low, mid, low, mid, 0.0001),
        Arguments.of(high, mid, high, high, 0.0001));
  }

  @ParameterizedTest
  @MethodSource("provideMaximumFloatTestCases")
  void testMaximumFloat(float a, float b, float c, float expected, float delta) {
    assertEquals(expected, NumberUtils.max(a, b, c), delta);
  }

  private static Stream<Arguments> provideMaximumFloatTestCases() {
    final float low = 12.3f;
    final float mid = 12.3f + 1;
    final float high = 12.3f + 2;
    return Stream.of(
        Arguments.of(low, mid, high, high, 0.0001f),
        Arguments.of(mid, low, high, high, 0.0001f),
        Arguments.of(mid, high, low, high, 0.0001f),
        Arguments.of(low, mid, low, mid, 0.0001f),
        Arguments.of(high, mid, high, high, 0.0001f));
  }

  @ParameterizedTest
  @MethodSource("provideMaximumIntTestCases")
  void testMaximumInt(int a, int b, int c, int expected, String description) {
    assertEquals(expected, NumberUtils.max(a, b, c), description);
  }

  private static Stream<Arguments> provideMaximumIntTestCases() {
    return Stream.of(
        Arguments.of(12345, 12345 - 1, 12345 - 2, 12345, "maximum(int, int, int) 1 failed"),
        Arguments.of(12345 - 1, 12345, 12345 - 2, 12345, "maximum(int, int, int) 2 failed"),
        Arguments.of(12345 - 1, 12345 - 2, 12345, 12345, "maximum(int, int, int) 3 failed"),
        Arguments.of(12345 - 1, 12345, 12345, 12345, "maximum(int, int, int) 4 failed"),
        Arguments.of(12345, 12345, 12345, 12345, "maximum(int, int, int) 5 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMaximumLongTestCases")
  void testMaximumLong(long a, long b, long c, long expected, String description) {
    assertEquals(expected, NumberUtils.max(a, b, c), description);
  }

  private static Stream<Arguments> provideMaximumLongTestCases() {
    return Stream.of(
        Arguments.of(12345L, 12345L - 1L, 12345L - 2L, 12345L, "maximum(long, long, long) 1 failed"),
        Arguments.of(12345L - 1L, 12345L, 12345L - 2L, 12345L, "maximum(long, long, long) 2 failed"),
        Arguments.of(12345L - 1L, 12345L - 2L, 12345L, 12345L, "maximum(long, long, long) 3 failed"),
        Arguments.of(12345L - 1L, 12345L, 12345L, 12345L, "maximum(long, long, long) 4 failed"),
        Arguments.of(12345L, 12345L, 12345L, 12345L, "maximum(long, long, long) 5 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMaximumShortTestCases")
  void testMaximumShort(short a, short b, short c, short expected, String description) {
    assertEquals(expected, NumberUtils.max(a, b, c), description);
  }

  private static Stream<Arguments> provideMaximumShortTestCases() {
    final short low = 1234;
    final short mid = 1234 + 1;
    final short high = 1234 + 2;
    return Stream.of(
        Arguments.of(low, mid, high, high, "maximum(short, short, short) 1 failed"),
        Arguments.of(mid, low, high, high, "maximum(short, short, short) 2 failed"),
        Arguments.of(mid, high, low, high, "maximum(short, short, short) 3 failed"),
        Arguments.of(high, mid, high, high, "maximum(short, short, short) 4 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMinimumByteTestCases")
  void testMinimumByte(byte a, byte b, byte c, byte expected, String description) {
    assertEquals(expected, NumberUtils.min(a, b, c), description);
  }

  private static Stream<Arguments> provideMinimumByteTestCases() {
    final byte low = 123;
    final byte mid = 123 + 1;
    final byte high = 123 + 2;
    return Stream.of(
        Arguments.of(low, mid, high, low, "minimum(byte, byte, byte) 1 failed"),
        Arguments.of(mid, low, high, low, "minimum(byte, byte, byte) 2 failed"),
        Arguments.of(mid, high, low, low, "minimum(byte, byte, byte) 3 failed"),
        Arguments.of(low, mid, low, low, "minimum(byte, byte, byte) 4 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMinimumDoubleTestCases")
  void testMinimumDouble(double a, double b, double c, double expected, double delta) {
    assertEquals(expected, NumberUtils.min(a, b, c), delta);
  }

  private static Stream<Arguments> provideMinimumDoubleTestCases() {
    final double low = 12.3;
    final double mid = 12.3 + 1;
    final double high = 12.3 + 2;
    return Stream.of(
        Arguments.of(low, mid, high, low, 0.0001),
        Arguments.of(mid, low, high, low, 0.0001),
        Arguments.of(mid, high, low, low, 0.0001),
        Arguments.of(low, mid, low, low, 0.0001),
        Arguments.of(high, mid, high, mid, 0.0001));
  }

  @ParameterizedTest
  @MethodSource("provideMinimumFloatTestCases")
  void testMinimumFloat(float a, float b, float c, float expected, float delta) {
    assertEquals(expected, NumberUtils.min(a, b, c), delta);
  }

  private static Stream<Arguments> provideMinimumFloatTestCases() {
    final float low = 12.3f;
    final float mid = 12.3f + 1;
    final float high = 12.3f + 2;
    return Stream.of(
        Arguments.of(low, mid, high, low, 0.0001f),
        Arguments.of(mid, low, high, low, 0.0001f),
        Arguments.of(mid, high, low, low, 0.0001f),
        Arguments.of(low, mid, low, low, 0.0001f),
        Arguments.of(high, mid, high, mid, 0.0001f));
  }

  @ParameterizedTest
  @MethodSource("provideMinimumIntTestCases")
  void testMinimumInt(int a, int b, int c, int expected, String description) {
    assertEquals(expected, NumberUtils.min(a, b, c), description);
  }

  private static Stream<Arguments> provideMinimumIntTestCases() {
    return Stream.of(
        Arguments.of(12345, 12345 + 1, 12345 + 2, 12345, "minimum(int, int, int) 1 failed"),
        Arguments.of(12345 + 1, 12345, 12345 + 2, 12345, "minimum(int, int, int) 2 failed"),
        Arguments.of(12345 + 1, 12345 + 2, 12345, 12345, "minimum(int, int, int) 3 failed"),
        Arguments.of(12345 + 1, 12345, 12345, 12345, "minimum(int, int, int) 4 failed"),
        Arguments.of(12345, 12345, 12345, 12345, "minimum(int, int, int) 5 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMinimumLongTestCases")
  void testMinimumLong(long a, long b, long c, long expected, String description) {
    assertEquals(expected, NumberUtils.min(a, b, c), description);
  }

  private static Stream<Arguments> provideMinimumLongTestCases() {
    return Stream.of(
        Arguments.of(12345L, 12345L + 1L, 12345L + 2L, 12345L, "minimum(long, long, long) 1 failed"),
        Arguments.of(12345L + 1L, 12345L, 12345L + 2L, 12345L, "minimum(long, long, long) 2 failed"),
        Arguments.of(12345L + 1L, 12345L + 2L, 12345L, 12345L, "minimum(long, long, long) 3 failed"),
        Arguments.of(12345L + 1L, 12345L, 12345L, 12345L, "minimum(long, long, long) 4 failed"),
        Arguments.of(12345L, 12345L, 12345L, 12345L, "minimum(long, long, long) 5 failed"));
  }

  @ParameterizedTest
  @MethodSource("provideMinimumShortTestCases")
  void testMinimumShort(short a, short b, short c, short expected, String description) {
    assertEquals(expected, NumberUtils.min(a, b, c), description);
  }

  private static Stream<Arguments> provideMinimumShortTestCases() {
    final short low = 1234;
    final short mid = 1234 + 1;
    final short high = 1234 + 2;
    return Stream.of(
        Arguments.of(low, mid, high, low, "minimum(short, short, short) 1 failed"),
        Arguments.of(mid, low, high, low, "minimum(short, short, short) 2 failed"),
        Arguments.of(mid, high, low, low, "minimum(short, short, short) 3 failed"),
        Arguments.of(low, mid, low, low, "minimum(short, short, short) 4 failed"));
  }

  /**
   * Test for {@link NumberUtils#createNumber(String)}
   */
  @ParameterizedTest
  @MethodSource("provideStringCreateNumberEnsureNoPrecisionLossTestCases")
  void testStringCreateNumberEnsureNoPrecisionLoss(String input, Class<?> expectedType) {
    assertInstanceOf(expectedType, NumberUtils.createNumber(input));
  }

  private static Stream<Arguments> provideStringCreateNumberEnsureNoPrecisionLossTestCases() {
    return Stream.of(
        Arguments.of("1.23", Float.class),
        Arguments.of("3.40282354e+38", Double.class),
        Arguments.of("1.797693134862315759e+308", BigDecimal.class),
        // LANG-1060
        Arguments.of("001.12", Float.class),
        Arguments.of("-001.12", Float.class),
        Arguments.of("+001.12", Float.class),
        Arguments.of("003.40282354e+38", Double.class),
        Arguments.of("-003.40282354e+38", Double.class),
        Arguments.of("+003.40282354e+38", Double.class),
        Arguments.of("0001.797693134862315759e+308", BigDecimal.class),
        Arguments.of("-001.797693134862315759e+308", BigDecimal.class),
        Arguments.of("+001.797693134862315759e+308", BigDecimal.class),
        // LANG-1613
        Arguments.of("2.2250738585072014E-308", Double.class),
        Arguments.of("2.2250738585072014E-308D", Double.class),
        Arguments.of("2.2250738585072014E-308F", Double.class),
        Arguments.of("4.9E-324", Double.class),
        Arguments.of("4.9E-324D", Double.class),
        Arguments.of("4.9E-324F", Double.class),
        Arguments.of("1.7976931348623157E308", Double.class),
        Arguments.of("1.7976931348623157E308D", Double.class),
        Arguments.of("1.7976931348623157E308F", Double.class),
        Arguments.of("4.9e-324D", Double.class),
        Arguments.of("4.9e-324F", Double.class));
  }

  /**
   * Test for {@link NumberUtils#toDouble(String)}.
   */
  @ParameterizedTest
  @MethodSource("provideStringToDoubleStringTestCases")
  void testStringToDoubleString(String input, double expected, String description) {
    assertEquals(expected, NumberUtils.toDouble(input), description);
  }

  private static Stream<Arguments> provideStringToDoubleStringTestCases() {
    return Stream.of(
        Arguments.of("-1.2345", -1.2345d, "toDouble(String) 1 failed"),
        Arguments.of("1.2345", 1.2345d, "toDouble(String) 2 failed"),
        Arguments.of("abc", 0.0d, "toDouble(String) 3 failed"),
        // LANG-1060
        Arguments.of("-001.2345", -1.2345d, "toDouble(String) 4 failed"),
        Arguments.of("+001.2345", 1.2345d, "toDouble(String) 5 failed"),
        Arguments.of("001.2345", 1.2345d, "toDouble(String) 6 failed"),
        Arguments.of("000.00000", 0d, "toDouble(String) 7 failed"),
        Arguments.of(Double.MAX_VALUE + "", Double.MAX_VALUE, "toDouble(Double.MAX_VALUE) failed"),
        Arguments.of(Double.MIN_VALUE + "", Double.MIN_VALUE, "toDouble(Double.MIN_VALUE) failed"),
        Arguments.of("", 0.0d, "toDouble(empty) failed"),
        Arguments.of(null, 0.0d, "toDouble(null) failed"));
  }

  /**
   * Test for {@link NumberUtils#toDouble(String, double)}.
   */
  @ParameterizedTest
  @MethodSource("provideStringToDoubleStringDTestCases")
  void testStringToDoubleStringD(String input, double defaultValue, double expected, String description) {
    assertEquals(expected, NumberUtils.toDouble(input, defaultValue), description);
  }

  private static Stream<Arguments> provideStringToDoubleStringDTestCases() {
    return Stream.of(
        Arguments.of("1.2345", 5.1d, 1.2345d, "toDouble(String, int) 1 failed"),
        Arguments.of("a", 5.0d, 5.0d, "toDouble(String, int) 2 failed"),
        // LANG-1060
        Arguments.of("001.2345", 5.1d, 1.2345d, "toDouble(String, int) 3 failed"),
        Arguments.of("-001.2345", 5.1d, -1.2345d, "toDouble(String, int) 4 failed"),
        Arguments.of("+001.2345", 5.1d, 1.2345d, "toDouble(String, int) 5 failed"),
        Arguments.of("000.00", 5.1d, 0d, "toDouble(String, int) 7 failed"),
        Arguments.of("", 5.1d, 5.1d, ""),
        Arguments.of(null, 5.1d, 5.1d, ""));
  }

  /**
   * Test for {@link NumberUtils#toByte(String)}.
   */
  @ParameterizedTest
  @MethodSource("provideToByteStringTestCases")
  void testToByteString(String input, byte expected, String description) {
    assertEquals(expected, NumberUtils.toByte(input), description);
  }

  private static Stream<Arguments> provideToByteStringTestCases() {
    return Stream.of(
        Arguments.of("123", (byte) 123, "toByte(String) 1 failed"),
        Arguments.of("abc", (byte) 0, "toByte(String) 2 failed"),
        Arguments.of("", (byte) 0, "toByte(empty) failed"),
        Arguments.of(null, (byte) 0, "toByte(null) failed"));
  }

  /**
   * Test for {@link NumberUtils#toByte(String, byte)}.
   */
  @ParameterizedTest
  @MethodSource("provideToByteStringITestCases")
  void testToByteStringI(String input, byte defaultValue, byte expected, String description) {
    assertEquals(expected, NumberUtils.toByte(input, defaultValue), description);
  }

  private static Stream<Arguments> provideToByteStringITestCases() {
    return Stream.of(
        Arguments.of("123", (byte) 5, (byte) 123, "toByte(String, byte) 1 failed"),
        Arguments.of("12.3", (byte) 5, (byte) 5, "toByte(String, byte) 2 failed"),
        Arguments.of("", (byte) 5, (byte) 5, ""),
        Arguments.of(null, (byte) 5, (byte) 5, ""));
  }

  /**
   * Test for {@link NumberUtils#toFloat(String)}.
   */
  @ParameterizedTest
  @MethodSource("provideToFloatStringTestCases")
  void testToFloatString(String input, float expected, String description) {
    assertEquals(expected, NumberUtils.toFloat(input), description);
  }

  private static Stream<Arguments> provideToFloatStringTestCases() {
    return Stream.of(
        Arguments.of("-1.2345", -1.2345f, "toFloat(String) 1 failed"),
        Arguments.of("1.2345", 1.2345f, "toFloat(String) 2 failed"),
        Arguments.of("abc", 0.0f, "toFloat(String) 3 failed"),
        // LANG-1060
        Arguments.of("-001.2345", -1.2345f, "toFloat(String) 4 failed"),
        Arguments.of("+001.2345", 1.2345f, "toFloat(String) 5 failed"),
        Arguments.of("001.2345", 1.2345f, "toFloat(String) 6 failed"),
        Arguments.of("000.00", 0f, "toFloat(String) 7 failed"),
        Arguments.of(Float.MAX_VALUE + "", Float.MAX_VALUE, "toFloat(Float.MAX_VALUE) failed"),
        Arguments.of(Float.MIN_VALUE + "", Float.MIN_VALUE, "toFloat(Float.MIN_VALUE) failed"),
        Arguments.of("", 0.0f, "toFloat(empty) failed"),
        Arguments.of(null, 0.0f, "toFloat(null) failed"));
  }

  /**
   * Test for {@link NumberUtils#toFloat(String, float)}.
   */
  @ParameterizedTest
  @MethodSource("provideToFloatStringFTestCases")
  void testToFloatStringF(String input, float defaultValue, float expected, String description) {
    assertEquals(expected, NumberUtils.toFloat(input, defaultValue), description);
  }

  private static Stream<Arguments> provideToFloatStringFTestCases() {
    return Stream.of(
        Arguments.of("1.2345", 5.1f, 1.2345f, "toFloat(String, int) 1 failed"),
        Arguments.of("a", 5.0f, 5.0f, "toFloat(String, int) 2 failed"),
        // LANG-1060
        Arguments.of("-001Z.2345", 5.0f, 5.0f, "toFloat(String, int) 3 failed"),
        Arguments.of("+001AB.2345", 5.0f, 5.0f, "toFloat(String, int) 4 failed"),
        Arguments.of("001Z.2345", 5.0f, 5.0f, "toFloat(String, int) 5 failed"),
        Arguments.of("", 5.0f, 5.0f, ""),
        Arguments.of(null, 5.0f, 5.0f, ""));
  }

  /**
   * Test for {@link NumberUtils#toInt(String)}.
   */
  @ParameterizedTest
  @MethodSource("provideToIntStringTestCases")
  void testToIntString(String input, int expected, String description) {
    assertEquals(expected, NumberUtils.toInt(input), description);
  }

  private static Stream<Arguments> provideToIntStringTestCases() {
    return Stream.of(
        Arguments.of("12345", 12345, "toInt(String) 1 failed"),
        Arguments.of("abc", 0, "toInt(String) 2 failed"),
        Arguments.of("", 0, "toInt(empty) failed"),
        Arguments.of(null, 0, "toInt(null) failed"));
  }

  /**
   * Test for {@link NumberUtils#toInt(String, int)}.
   */
  @ParameterizedTest
  @MethodSource("provideToIntStringITestCases")
  void testToIntStringI(String input, int defaultValue, int expected, String description) {
    assertEquals(expected, NumberUtils.toInt(input, defaultValue), description);
  }

  private static Stream<Arguments> provideToIntStringITestCases() {
    return Stream.of(
        Arguments.of("12345", 5, 12345, "toInt(String, int) 1 failed"),
        Arguments.of("1234.5", 5, 5, "toInt(String, int) 2 failed"),
        Arguments.of("", 5, 5, ""),
        Arguments.of(null, 5, 5, ""));
  }

  /**
   * Test for {@link NumberUtils#toLong(String)}.
   */
  @ParameterizedTest
  @MethodSource("provideToLongStringTestCases")
  void testToLongString(String input, long expected, String description) {
    assertEquals(expected, NumberUtils.toLong(input), description);
  }

  private static Stream<Arguments> provideToLongStringTestCases() {
    return Stream.of(
        Arguments.of("12345", 12345L, "toLong(String) 1 failed"),
        Arguments.of("abc", 0L, "toLong(String) 2 failed"),
        Arguments.of("1L", 0L, "toLong(String) 3 failed"),
        Arguments.of("1l", 0L, "toLong(String) 4 failed"),
        Arguments.of(Long.MAX_VALUE + "", Long.MAX_VALUE, "toLong(Long.MAX_VALUE) failed"),
        Arguments.of(Long.MIN_VALUE + "", Long.MIN_VALUE, "toLong(Long.MIN_VALUE) failed"),
        Arguments.of("", 0L, "toLong(empty) failed"),
        Arguments.of(null, 0L, "toLong(null) failed"));
  }

  /**
   * Test for {@link NumberUtils#toLong(String, long)}.
   */
  @ParameterizedTest
  @MethodSource("provideToLongStringLTestCases")
  void testToLongStringL(String input, long defaultValue, long expected, String description) {
    assertEquals(expected, NumberUtils.toLong(input, defaultValue), description);
  }

  private static Stream<Arguments> provideToLongStringLTestCases() {
    return Stream.of(
        Arguments.of("12345", 5L, 12345L, "toLong(String, long) 1 failed"),
        Arguments.of("1234.5", 5L, 5L, "toLong(String, long) 2 failed"),
        Arguments.of("", 5L, 5L, ""),
        Arguments.of(null, 5L, 5L, ""));
  }
}
