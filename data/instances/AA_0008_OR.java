package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

enum Enum64_OR {
  A00, A01, A02, A03, A04, A05, A06, A07, A08, A09, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22,
  A23, A24, A25, A26, A27, A28, A29, A30, A31, A32, A33, A34, A35, A36, A37, A38, A39, A40, A41, A42, A43, A44, A45,
  A46, A47, A48, A49, A50, A51, A52, A53, A54, A55, A56, A57, A58, A59, A60, A61, A62, A63
}

class AA_0008_OR {
  private void arrayEquals(final long[] actual, final long... expected) {
    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void testConstructable() {
    // enforce public constructor
    new EnumUtils();
  }

  @Disabled
  @Test
  void testGenerateBitVector() {
    assertEquals(0L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.noneOf(Traffic_OR.class)));
    assertEquals(1L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.RED)));
    assertEquals(2L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.AMBER)));
    assertEquals(4L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.GREEN)));
    assertEquals(3L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER)));
    assertEquals(5L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.RED, Traffic_OR.GREEN)));
    assertEquals(6L, EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.AMBER, Traffic_OR.GREEN)));
    assertEquals(7L,
        EnumUtils.generateBitVector(Traffic_OR.class, EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN)));

    // 64 values Enum (to test whether no int<->long jdk conversion issue exists)
    assertEquals(1L << 31, EnumUtils.generateBitVector(Enum64_OR.class, EnumSet.of(Enum64_OR.A31)));
    assertEquals(1L << 32, EnumUtils.generateBitVector(Enum64_OR.class, EnumSet.of(Enum64_OR.A32)));
    assertEquals(1L << 63, EnumUtils.generateBitVector(Enum64_OR.class, EnumSet.of(Enum64_OR.A63)));
    assertEquals(Long.MIN_VALUE, EnumUtils.generateBitVector(Enum64_OR.class, EnumSet.of(Enum64_OR.A63)));
  }

  @Disabled
  @Test
  void testGenerateBitVector_longClass() {
    assertIllegalArgumentException(
        () -> EnumUtils.generateBitVector(TooMany_OR.class, EnumSet.of(TooMany_OR.A1)));
  }

  @Disabled
  @Test
  void testGenerateBitVector_longClassWithArray() {
    assertIllegalArgumentException(() -> EnumUtils.generateBitVector(TooMany_OR.class, TooMany_OR.A1));
  }

  @SuppressWarnings("unchecked")
  @Disabled
  @Test
  void testGenerateBitVector_nonEnumClass() {
    @SuppressWarnings("rawtypes")
    final Class rawType = Object.class;
    @SuppressWarnings("rawtypes")
    final List rawList = new ArrayList();
    assertIllegalArgumentException(() -> EnumUtils.generateBitVector(rawType, rawList));
  }

  @SuppressWarnings("unchecked")
  @Disabled
  @Test
  void testGenerateBitVector_nonEnumClassWithArray() {
    @SuppressWarnings("rawtypes")
    final Class rawType = Object.class;
    assertIllegalArgumentException(() -> EnumUtils.generateBitVector(rawType));
  }

  @Disabled
  @Test
  void testGenerateBitVector_nullArray() {
    assertNullPointerException(() -> EnumUtils.generateBitVector(Traffic_OR.class, (Traffic_OR[]) null));
  }

  @Disabled
  @Test
  void testGenerateBitVector_nullArrayElement() {
    assertIllegalArgumentException(
        () -> EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.RED, null));
  }

  @Disabled
  @Test
  void testGenerateBitVector_nullClass() {
    assertNullPointerException(() -> EnumUtils.generateBitVector(null, EnumSet.of(Traffic_OR.RED)));
  }

  @Disabled
  @Test
  void testGenerateBitVector_nullClassWithArray() {
    assertNullPointerException(() -> EnumUtils.generateBitVector(null, Traffic_OR.RED));
  }

  @Disabled
  @Test
  void testGenerateBitVector_nullElement() {
    assertNullPointerException(
        () -> EnumUtils.generateBitVector(Traffic_OR.class, Arrays.asList(Traffic_OR.RED, null)));
  }

  @Disabled
  @Test
  void testGenerateBitVector_nullIterable() {
    assertNullPointerException(
        () -> EnumUtils.generateBitVector(Traffic_OR.class, (Iterable<Traffic_OR>) null));
  }

  @Disabled
  @Test
  void testGenerateBitVectorFromArray() {
    assertEquals(0L, EnumUtils.generateBitVector(Traffic_OR.class));
    assertEquals(1L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.RED));
    assertEquals(2L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.AMBER));
    assertEquals(4L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.GREEN));
    assertEquals(3L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.AMBER));
    assertEquals(5L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.GREEN));
    assertEquals(6L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.AMBER, Traffic_OR.GREEN));
    assertEquals(7L, EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN));
    // gracefully handles duplicates:
    assertEquals(7L,
        EnumUtils.generateBitVector(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN, Traffic_OR.GREEN));

    // 64 values Enum (to test whether no int<->long jdk conversion issue exists)
    assertEquals(1L << 31, EnumUtils.generateBitVector(Enum64_OR.class, Enum64_OR.A31));
    assertEquals(1L << 32, EnumUtils.generateBitVector(Enum64_OR.class, Enum64_OR.A32));
    assertEquals(1L << 63, EnumUtils.generateBitVector(Enum64_OR.class, Enum64_OR.A63));
    assertEquals(Long.MIN_VALUE, EnumUtils.generateBitVector(Enum64_OR.class, Enum64_OR.A63));
  }

  @Disabled
  @Test
  void testGenerateBitVectors() {
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.noneOf(Traffic_OR.class)), 0L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.RED)), 1L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.AMBER)), 2L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.GREEN)), 4L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER)), 3L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.RED, Traffic_OR.GREEN)), 5L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.AMBER, Traffic_OR.GREEN)), 6L);
    arrayEquals(
        EnumUtils.generateBitVectors(Traffic_OR.class, EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN)), 7L);

    // 64 values Enum (to test whether no int<->long jdk conversion issue exists)
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, EnumSet.of(Enum64_OR.A31)), 1L << 31);
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, EnumSet.of(Enum64_OR.A32)), 1L << 32);
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, EnumSet.of(Enum64_OR.A63)), 1L << 63);
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, EnumSet.of(Enum64_OR.A63)), Long.MIN_VALUE);

    // More than 64 values Enum
    arrayEquals(EnumUtils.generateBitVectors(TooMany_OR.class, EnumSet.of(TooMany_OR.M2)), 1L, 0L);
    arrayEquals(EnumUtils.generateBitVectors(TooMany_OR.class, EnumSet.of(TooMany_OR.L2, TooMany_OR.M2)), 1L,
        1L << 63);
  }

  @SuppressWarnings("unchecked")
  @Disabled
  @Test
  void testGenerateBitVectors_nonEnumClass() {
    @SuppressWarnings("rawtypes")
    final Class rawType = Object.class;
    @SuppressWarnings("rawtypes")
    final List rawList = new ArrayList();
    assertIllegalArgumentException(() -> EnumUtils.generateBitVectors(rawType, rawList));
  }

  @SuppressWarnings("unchecked")
  @Disabled
  @Test
  void testGenerateBitVectors_nonEnumClassWithArray() {
    @SuppressWarnings("rawtypes")
    final Class rawType = Object.class;
    assertIllegalArgumentException(() -> EnumUtils.generateBitVectors(rawType));
  }

  @Disabled
  @Test
  void testGenerateBitVectors_nullArray() {
    assertNullPointerException(() -> EnumUtils.generateBitVectors(Traffic_OR.class, (Traffic_OR[]) null));
  }

  @Disabled
  @Test
  void testGenerateBitVectors_nullArrayElement() {
    assertIllegalArgumentException(
        () -> EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.RED, null));
  }

  @Disabled
  @Test
  void testGenerateBitVectors_nullClass() {
    assertNullPointerException(() -> EnumUtils.generateBitVectors(null, EnumSet.of(Traffic_OR.RED)));
  }

  @Disabled
  @Test
  void testGenerateBitVectors_nullClassWithArray() {
    assertNullPointerException(() -> EnumUtils.generateBitVectors(null, Traffic_OR.RED));
  }

  @Disabled
  @Test
  void testGenerateBitVectors_nullElement() {
    assertNullPointerException(
        () -> EnumUtils.generateBitVectors(Traffic_OR.class, Arrays.asList(Traffic_OR.RED, null)));
  }

  @Disabled
  @Test
  void testGenerateBitVectors_nullIterable() {
    assertNullPointerException(() -> EnumUtils.generateBitVectors(null, (Iterable<Traffic_OR>) null));
  }

  @Disabled
  @Test
  void testGenerateBitVectorsFromArray() {
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class), 0L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.RED), 1L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.AMBER), 2L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.GREEN), 4L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.AMBER), 3L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.GREEN), 5L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.AMBER, Traffic_OR.GREEN), 6L);
    arrayEquals(EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN), 7L);
    // gracefully handles duplicates:
    arrayEquals(
        EnumUtils.generateBitVectors(Traffic_OR.class, Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN, Traffic_OR.GREEN), 7L);

    // 64 values Enum (to test whether no int<->long jdk conversion issue exists)
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, Enum64_OR.A31), 1L << 31);
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, Enum64_OR.A32), 1L << 32);
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, Enum64_OR.A63), 1L << 63);
    arrayEquals(EnumUtils.generateBitVectors(Enum64_OR.class, Enum64_OR.A63), Long.MIN_VALUE);

    // More than 64 values Enum
    arrayEquals(EnumUtils.generateBitVectors(TooMany_OR.class, TooMany_OR.M2), 1L, 0L);
    arrayEquals(EnumUtils.generateBitVectors(TooMany_OR.class, TooMany_OR.L2, TooMany_OR.M2), 1L, 1L << 63);

  }

  @Disabled
  @Test
  void testGetEnum() {
    assertEquals(Traffic_OR.RED, EnumUtils.getEnum(Traffic_OR.class, "RED"));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnum(Traffic_OR.class, "AMBER"));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnum(Traffic_OR.class, "GREEN"));
    assertNull(EnumUtils.getEnum(Traffic_OR.class, "PURPLE"));
    assertNull(EnumUtils.getEnum(Traffic_OR.class, null));
  }

  @Disabled
  @Test
  void testGetEnum_defaultEnum() {
    assertEquals(Traffic_OR.RED, EnumUtils.getEnum(Traffic_OR.class, "RED", Traffic_OR.AMBER));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnum(Traffic_OR.class, "AMBER", Traffic_OR.GREEN));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnum(Traffic_OR.class, "GREEN", Traffic_OR.RED));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnum(Traffic_OR.class, "PURPLE", Traffic_OR.AMBER));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnum(Traffic_OR.class, "PURPLE", Traffic_OR.GREEN));
    assertEquals(Traffic_OR.RED, EnumUtils.getEnum(Traffic_OR.class, "PURPLE", Traffic_OR.RED));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnum(Traffic_OR.class, null, Traffic_OR.AMBER));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnum(Traffic_OR.class, null, Traffic_OR.GREEN));
    assertEquals(Traffic_OR.RED, EnumUtils.getEnum(Traffic_OR.class, null, Traffic_OR.RED));
    assertNull(EnumUtils.getEnum(Traffic_OR.class, "PURPLE", null));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnum(null, "RED", Traffic_OR.AMBER));
  }

  /**
   * Tests raw type.
   */
  @SuppressWarnings("unchecked")
  @Disabled
  @Test
  void testGetEnum_nonEnumClass() {
    @SuppressWarnings("rawtypes")
    final Class rawType = Object.class;
    assertNull(EnumUtils.getEnum(rawType, "rawType"));
  }

  @Disabled
  @Test
  void testGetEnum_nullClass() {
    assertNull(EnumUtils.getEnum((Class<Traffic_OR>) null, "PURPLE"));
  }

  @Disabled
  @Test
  void testGetEnumIgnoreCase() {
    assertEquals(Traffic_OR.RED, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "red"));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "Amber"));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "grEEn"));
    assertNull(EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "purple"));
    assertNull(EnumUtils.getEnumIgnoreCase(Traffic_OR.class, null));
  }

  @Disabled
  @Test
  void testGetEnumIgnoreCase_defaultEnum() {
    assertEquals(Traffic_OR.RED, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "red", Traffic_OR.AMBER));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "Amber", Traffic_OR.GREEN));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "grEEn", Traffic_OR.RED));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "PURPLE", Traffic_OR.AMBER));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "purple", Traffic_OR.GREEN));
    assertEquals(Traffic_OR.RED, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "pUrPlE", Traffic_OR.RED));
    assertEquals(Traffic_OR.AMBER, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, null, Traffic_OR.AMBER));
    assertEquals(Traffic_OR.GREEN, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, null, Traffic_OR.GREEN));
    assertEquals(Traffic_OR.RED, EnumUtils.getEnumIgnoreCase(Traffic_OR.class, null, Traffic_OR.RED));
    assertNull(EnumUtils.getEnumIgnoreCase(Traffic_OR.class, "PURPLE", null));
    assertNull(EnumUtils.getEnumIgnoreCase(null, "PURPLE", null));
  }

  /**
   * Tests raw type.
   */
  @SuppressWarnings("unchecked")
  @Disabled
  @Test
  void testGetEnumIgnoreCase_nonEnumClass() {
    @SuppressWarnings("rawtypes")
    final Class rawType = Object.class;
    assertNull(EnumUtils.getEnumIgnoreCase(rawType, "rawType"));
  }

  @Disabled
  @Test
  void testGetEnumIgnoreCase_nullClass() {
    assertNull(EnumUtils.getEnumIgnoreCase((Class<Traffic_OR>) null, "PURPLE"));
  }

  @Disabled
  @Test
  void testGetEnumList() {
    final List<Traffic_OR> test = EnumUtils.getEnumList(Traffic_OR.class);
    assertEquals(3, test.size());
    assertEquals(Traffic_OR.RED, test.get(0));
    assertEquals(Traffic_OR.AMBER, test.get(1));
    assertEquals(Traffic_OR.GREEN, test.get(2));
  }

  @Disabled
  @Test
  void testGetEnumMap() {
    final Map<String, Traffic_OR> test = EnumUtils.getEnumMap(Traffic_OR.class);
    final Map<String, Traffic_OR> expected = new HashMap<>();
    expected.put("RED", Traffic_OR.RED);
    expected.put("AMBER", Traffic_OR.AMBER);
    expected.put("GREEN", Traffic_OR.GREEN);
    assertEquals(expected, test, "getEnumMap not created correctly");
    assertEquals(3, test.size());
    assertTrue(test.containsKey("RED"));
    assertEquals(Traffic_OR.RED, test.get("RED"));
    assertTrue(test.containsKey("AMBER"));
    assertEquals(Traffic_OR.AMBER, test.get("AMBER"));
    assertTrue(test.containsKey("GREEN"));
    assertEquals(Traffic_OR.GREEN, test.get("GREEN"));
    assertFalse(test.containsKey("PURPLE"));
  }

  @Disabled
  @Test
  void testGetEnumMap_keyFunction() {
    final Map<Integer, Month_OR> test = EnumUtils.getEnumMap(Month_OR.class, Month_OR::getId);
    final Map<Integer, Month_OR> expected = new HashMap<>();
    expected.put(1, Month_OR.JAN);
    expected.put(2, Month_OR.FEB);
    expected.put(3, Month_OR.MAR);
    expected.put(4, Month_OR.APR);
    expected.put(5, Month_OR.MAY);
    expected.put(6, Month_OR.JUN);
    expected.put(7, Month_OR.JUL);
    expected.put(8, Month_OR.AUG);
    expected.put(9, Month_OR.SEP);
    expected.put(10, Month_OR.OCT);
    expected.put(11, Month_OR.NOV);
    expected.put(12, Month_OR.DEC);
    assertEquals(expected, test, "getEnumMap not created correctly");
    assertEquals(12, test.size());
    assertFalse(test.containsKey(0));
    assertTrue(test.containsKey(1));
    assertEquals(Month_OR.JAN, test.get(1));
    assertTrue(test.containsKey(2));
    assertEquals(Month_OR.FEB, test.get(2));
    assertTrue(test.containsKey(3));
    assertEquals(Month_OR.MAR, test.get(3));
    assertTrue(test.containsKey(4));
    assertEquals(Month_OR.APR, test.get(4));
    assertTrue(test.containsKey(5));
    assertEquals(Month_OR.MAY, test.get(5));
    assertTrue(test.containsKey(6));
    assertEquals(Month_OR.JUN, test.get(6));
    assertTrue(test.containsKey(7));
    assertEquals(Month_OR.JUL, test.get(7));
    assertTrue(test.containsKey(8));
    assertEquals(Month_OR.AUG, test.get(8));
    assertTrue(test.containsKey(9));
    assertEquals(Month_OR.SEP, test.get(9));
    assertTrue(test.containsKey(10));
    assertEquals(Month_OR.OCT, test.get(10));
    assertTrue(test.containsKey(11));
    assertEquals(Month_OR.NOV, test.get(11));
    assertTrue(test.containsKey(12));
    assertEquals(Month_OR.DEC, test.get(12));
    assertFalse(test.containsKey(13));
  }

  @Disabled
  @Test
  void testGetEnumSystemProperty() {
    final String key = getClass().getName();
    System.setProperty(key, Traffic_OR.RED.toString());
    try {
      assertEquals(Traffic_OR.RED, EnumUtils.getEnumSystemProperty(Traffic_OR.class, key, null));
      assertEquals(Traffic_OR.RED, EnumUtils.getEnumSystemProperty(Traffic_OR.class, "?", Traffic_OR.RED));
      assertEquals(Traffic_OR.RED, EnumUtils.getEnumSystemProperty(null, null, Traffic_OR.RED));
      assertEquals(Traffic_OR.RED, EnumUtils.getEnumSystemProperty(null, "?", Traffic_OR.RED));
      assertEquals(Traffic_OR.RED, EnumUtils.getEnumSystemProperty(Traffic_OR.class, null, Traffic_OR.RED));
    } finally {
      System.getProperties().remove(key);
    }
  }

  @Disabled
  @Test
  void testGetFirstEnumIgnoreCase_defaultEnum() {
    final Function<Traffic2_OR, String> f = Traffic2_OR::getLabel;
    assertEquals(Traffic2_OR.RED, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "***red***", f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "**Amber**", f, Traffic2_OR.GREEN));
    assertEquals(Traffic2_OR.GREEN, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "*grEEn*", f, Traffic2_OR.RED));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "PURPLE", f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.GREEN, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "purple", f, Traffic2_OR.GREEN));
    assertEquals(Traffic2_OR.RED, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "pUrPlE", f, Traffic2_OR.RED));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, null, f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.GREEN, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, null, f, Traffic2_OR.GREEN));
    assertEquals(Traffic2_OR.RED, EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, null, f, Traffic2_OR.RED));
    assertNull(EnumUtils.getFirstEnumIgnoreCase(Traffic2_OR.class, "PURPLE", f, null));
    assertNull(EnumUtils.getFirstEnumIgnoreCase(null, "PURPLE", f, null));
  }

  @Disabled
  @Test
  void testGetFirstEnumToIntFunction() {
    final ToIntFunction<Traffic2_OR> f = Traffic2_OR::getValue;
    assertEquals(Traffic2_OR.RED, EnumUtils.getFirstEnum(Traffic2_OR.class, 1, f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnum(Traffic2_OR.class, 2, f, Traffic2_OR.GREEN));
    assertEquals(Traffic2_OR.GREEN, EnumUtils.getFirstEnum(Traffic2_OR.class, 3, f, Traffic2_OR.RED));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnum(Traffic2_OR.class, 4, f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.GREEN, EnumUtils.getFirstEnum(Traffic2_OR.class, 5, f, Traffic2_OR.GREEN));
    assertEquals(Traffic2_OR.RED, EnumUtils.getFirstEnum(Traffic2_OR.class, 6, f, Traffic2_OR.RED));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnum(Traffic2_OR.class, 0, f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.GREEN, EnumUtils.getFirstEnum(Traffic2_OR.class, -1, f, Traffic2_OR.GREEN));
    assertEquals(Traffic2_OR.RED, EnumUtils.getFirstEnum(Traffic2_OR.class, 0, f, Traffic2_OR.RED));
    assertNull(EnumUtils.getFirstEnum(Traffic2_OR.class, 7, f, null));
    // Edge cases for 1st argument
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnum(null, 1, f, Traffic2_OR.AMBER));
    assertEquals(Traffic2_OR.AMBER, EnumUtils.getFirstEnum((Class) String.class, 1, f, Traffic2_OR.AMBER));
  }

  @Disabled
  @Test
  void testIsValidEnum() {
    assertTrue(EnumUtils.isValidEnum(Traffic_OR.class, "RED"));
    assertTrue(EnumUtils.isValidEnum(Traffic_OR.class, "AMBER"));
    assertTrue(EnumUtils.isValidEnum(Traffic_OR.class, "GREEN"));
    assertFalse(EnumUtils.isValidEnum(Traffic_OR.class, "PURPLE"));
    assertFalse(EnumUtils.isValidEnum(Traffic_OR.class, null));
  }

  @Disabled
  @Test
  void testIsValidEnum_nullClass() {
    assertFalse(EnumUtils.isValidEnum(null, "PURPLE"));
  }

  @Disabled
  @Test
  void testIsValidEnumIgnoreCase() {
    assertTrue(EnumUtils.isValidEnumIgnoreCase(Traffic_OR.class, "red"));
    assertTrue(EnumUtils.isValidEnumIgnoreCase(Traffic_OR.class, "Amber"));
    assertTrue(EnumUtils.isValidEnumIgnoreCase(Traffic_OR.class, "grEEn"));
    assertFalse(EnumUtils.isValidEnumIgnoreCase(Traffic_OR.class, "purple"));
    assertFalse(EnumUtils.isValidEnumIgnoreCase(Traffic_OR.class, null));
  }

  @Disabled
  @Test
  void testIsValidEnumIgnoreCase_nullClass() {
    assertFalse(EnumUtils.isValidEnumIgnoreCase(null, "PURPLE"));
  }

  @Disabled
  @Test
  void testProcessBitVector() {
    assertEquals(EnumSet.noneOf(Traffic_OR.class), EnumUtils.processBitVector(Traffic_OR.class, 0L));
    assertEquals(EnumSet.of(Traffic_OR.RED), EnumUtils.processBitVector(Traffic_OR.class, 1L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER), EnumUtils.processBitVector(Traffic_OR.class, 2L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER), EnumUtils.processBitVector(Traffic_OR.class, 3L));
    assertEquals(EnumSet.of(Traffic_OR.GREEN), EnumUtils.processBitVector(Traffic_OR.class, 4L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.GREEN), EnumUtils.processBitVector(Traffic_OR.class, 5L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER, Traffic_OR.GREEN), EnumUtils.processBitVector(Traffic_OR.class, 6L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN),
        EnumUtils.processBitVector(Traffic_OR.class, 7L));

    // 64 values Enum (to test whether no int<->long jdk conversion issue exists)
    assertEquals(EnumSet.of(Enum64_OR.A31), EnumUtils.processBitVector(Enum64_OR.class, 1L << 31));
    assertEquals(EnumSet.of(Enum64_OR.A32), EnumUtils.processBitVector(Enum64_OR.class, 1L << 32));
    assertEquals(EnumSet.of(Enum64_OR.A63), EnumUtils.processBitVector(Enum64_OR.class, 1L << 63));
    assertEquals(EnumSet.of(Enum64_OR.A63), EnumUtils.processBitVector(Enum64_OR.class, Long.MIN_VALUE));
  }

  @Disabled
  @Test
  void testProcessBitVector_longClass() {
    assertIllegalArgumentException(() -> EnumUtils.processBitVector(TooMany_OR.class, 0L));
  }

  @Disabled
  @Test
  void testProcessBitVector_nullClass() {
    final Class<Traffic_OR> empty = null;
    assertNullPointerException(() -> EnumUtils.processBitVector(empty, 0L));
  }

  @Disabled
  @Test
  void testProcessBitVectors() {
    assertEquals(EnumSet.noneOf(Traffic_OR.class), EnumUtils.processBitVectors(Traffic_OR.class, 0L));
    assertEquals(EnumSet.of(Traffic_OR.RED), EnumUtils.processBitVectors(Traffic_OR.class, 1L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER), EnumUtils.processBitVectors(Traffic_OR.class, 2L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER), EnumUtils.processBitVectors(Traffic_OR.class, 3L));
    assertEquals(EnumSet.of(Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 4L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 5L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER, Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 6L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN),
        EnumUtils.processBitVectors(Traffic_OR.class, 7L));

    assertEquals(EnumSet.noneOf(Traffic_OR.class), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 0L));
    assertEquals(EnumSet.of(Traffic_OR.RED), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 1L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 2L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 3L));
    assertEquals(EnumSet.of(Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 4L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 5L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER, Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 0L, 6L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN),
        EnumUtils.processBitVectors(Traffic_OR.class, 0L, 7L));

    // demonstrate tolerance of irrelevant high-order digits:
    assertEquals(EnumSet.noneOf(Traffic_OR.class), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 0L));
    assertEquals(EnumSet.of(Traffic_OR.RED), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 1L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 2L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 3L));
    assertEquals(EnumSet.of(Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 4L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 5L));
    assertEquals(EnumSet.of(Traffic_OR.AMBER, Traffic_OR.GREEN), EnumUtils.processBitVectors(Traffic_OR.class, 666L, 6L));
    assertEquals(EnumSet.of(Traffic_OR.RED, Traffic_OR.AMBER, Traffic_OR.GREEN),
        EnumUtils.processBitVectors(Traffic_OR.class, 666L, 7L));

    // 64 values Enum (to test whether no int<->long jdk conversion issue exists)
    assertEquals(EnumSet.of(Enum64_OR.A31), EnumUtils.processBitVectors(Enum64_OR.class, 1L << 31));
    assertEquals(EnumSet.of(Enum64_OR.A32), EnumUtils.processBitVectors(Enum64_OR.class, 1L << 32));
    assertEquals(EnumSet.of(Enum64_OR.A63), EnumUtils.processBitVectors(Enum64_OR.class, 1L << 63));
    assertEquals(EnumSet.of(Enum64_OR.A63), EnumUtils.processBitVectors(Enum64_OR.class, Long.MIN_VALUE));
  }

  @Disabled
  @Test
  void testProcessBitVectors_longClass() {
    assertEquals(EnumSet.noneOf(TooMany_OR.class), EnumUtils.processBitVectors(TooMany_OR.class, 0L));
    assertEquals(EnumSet.of(TooMany_OR.A), EnumUtils.processBitVectors(TooMany_OR.class, 1L));
    assertEquals(EnumSet.of(TooMany_OR.B), EnumUtils.processBitVectors(TooMany_OR.class, 2L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B), EnumUtils.processBitVectors(TooMany_OR.class, 3L));
    assertEquals(EnumSet.of(TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 4L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 5L));
    assertEquals(EnumSet.of(TooMany_OR.B, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 6L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 7L));

    assertEquals(EnumSet.noneOf(TooMany_OR.class), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 0L));
    assertEquals(EnumSet.of(TooMany_OR.A), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 1L));
    assertEquals(EnumSet.of(TooMany_OR.B), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 2L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 3L));
    assertEquals(EnumSet.of(TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 4L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 5L));
    assertEquals(EnumSet.of(TooMany_OR.B, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 6L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 7L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C), EnumUtils.processBitVectors(TooMany_OR.class, 0L, 7L));

    assertEquals(EnumSet.of(TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 0L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 1L));
    assertEquals(EnumSet.of(TooMany_OR.B, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 2L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 3L));
    assertEquals(EnumSet.of(TooMany_OR.C, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 4L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.C, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 5L));
    assertEquals(EnumSet.of(TooMany_OR.B, TooMany_OR.C, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 1L, 6L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C, TooMany_OR.M2),
        EnumUtils.processBitVectors(TooMany_OR.class, 1L, 7L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C, TooMany_OR.M2),
        EnumUtils.processBitVectors(TooMany_OR.class, 1L, 7L));

    // demonstrate tolerance of irrelevant high-order digits:
    assertEquals(EnumSet.of(TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 0L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 1L));
    assertEquals(EnumSet.of(TooMany_OR.B, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 2L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 3L));
    assertEquals(EnumSet.of(TooMany_OR.C, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 4L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.C, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 5L));
    assertEquals(EnumSet.of(TooMany_OR.B, TooMany_OR.C, TooMany_OR.M2), EnumUtils.processBitVectors(TooMany_OR.class, 9L, 6L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C, TooMany_OR.M2),
        EnumUtils.processBitVectors(TooMany_OR.class, 9L, 7L));
    assertEquals(EnumSet.of(TooMany_OR.A, TooMany_OR.B, TooMany_OR.C, TooMany_OR.M2),
        EnumUtils.processBitVectors(TooMany_OR.class, 9L, 7L));
  }

  @Disabled
  @Test
  void testProcessBitVectors_nullClass() {
    final Class<Traffic_OR> empty = null;
    assertNullPointerException(() -> EnumUtils.processBitVectors(empty, 0L));
  }

  @Disabled
  @Test
  void testStream() {
    assertEquals(7, EnumUtils.stream(TimeUnit.class).count());
    Assertions.assertArrayEquals(TimeUnit.values(), EnumUtils.stream(TimeUnit.class).toArray(TimeUnit[]::new));
    assertEquals(0, EnumUtils.stream(Object.class).count());
    assertEquals(0, EnumUtils.stream(null).count());
  }
}

enum Month_OR {
  JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

  private final int id;

  Month_OR(final int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }
}

enum TooMany_OR {
  A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1,
  J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2,
  M2
}

enum Traffic_OR {
  RED, AMBER, GREEN
}

enum Traffic2_OR {

  RED("***Red***", 1), AMBER("**Amber**", 2), GREEN("*green*", 3);

  final String label;
  final int value;

  Traffic2_OR(final String label, final int value) {
    this.label = label;
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public int getValue() {
    return value;
  }
}
