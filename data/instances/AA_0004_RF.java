package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for methods of {@link StringUtils}
 * which been moved to their own test classes.
 */
class AA_0004_RF {
  static final String WHITESPACE;
  static final String NON_WHITESPACE;
  static final String HARD_SPACE;
  static final String TRIMMABLE;
  static final String NON_TRIMMABLE;

  static {
    final StringBuilder ws = new StringBuilder();
    final StringBuilder nws = new StringBuilder();
    final String hs = String.valueOf((char) 160);
    final StringBuilder tr = new StringBuilder();
    final StringBuilder ntr = new StringBuilder();
    for (int i = 0; i < Character.MAX_VALUE; i++) {
      if (Character.isWhitespace((char) i)) {
        ws.append((char) i);
        if (i > 32) {
          ntr.append((char) i);
        }
      } else if (i < 40) {
        nws.append((char) i);
      }
    }
    for (int i = 0; i <= 32; i++) {
      tr.append((char) i);
    }
    WHITESPACE = ws.toString();
    NON_WHITESPACE = nws.toString();
    HARD_SPACE = hs;
    TRIMMABLE = tr.toString();
    NON_TRIMMABLE = ntr.toString();
  }

  private static final String[] ARRAY_LIST = { "foo", "bar", "baz" };
  private static final String[] EMPTY_ARRAY_LIST = {};
  private static final String[] NULL_ARRAY_LIST = { null };
  private static final Object[] NULL_TO_STRING_LIST = { new Object() {
    @Override
    public String toString() {
      return null;
    }
  } };
  private static final String[] MIXED_ARRAY_LIST = { null, "", "foo" };
  private static final Object[] MIXED_TYPE_LIST = { "foo", Long.valueOf(2L) };
  private static final long[] LONG_PRIM_LIST = { 1, 2 };
  private static final int[] INT_PRIM_LIST = { 1, 2 };
  private static final byte[] BYTE_PRIM_LIST = { 1, 2 };
  private static final short[] SHORT_PRIM_LIST = { 1, 2 };
  private static final char[] CHAR_PRIM_LIST = { '1', '2' };
  private static final float[] FLOAT_PRIM_LIST = { 1, 2 };
  private static final double[] DOUBLE_PRIM_LIST = { 1, 2 };
  private static final List<String> MIXED_STRING_LIST = Arrays.asList(null, "", "foo");
  private static final List<Object> MIXED_TYPE_OBJECT_LIST = Arrays.<Object>asList("foo", Long.valueOf(2L));
  private static final List<String> STRING_LIST = Arrays.asList("foo", "bar", "baz");
  private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
  private static final List<String> NULL_STRING_LIST = Collections.singletonList(null);

  private static final String SEPARATOR = ",";
  private static final char SEPARATOR_CHAR = ';';
  private static final char COMMA_SEPARATOR_CHAR = ',';

  private static final String TEXT_LIST = "foo,bar,baz";
  private static final String TEXT_LIST_CHAR = "foo;bar;baz";
  private static final String TEXT_LIST_NOSEP = "foobarbaz";

  private static final String FOO_UNCAP = "foo";
  private static final String FOO_CAP = "Foo";

  private static final String SENTENCE_UNCAP = "foo bar baz";
  private static final String SENTENCE_CAP = "Foo Bar Baz";

  private static final boolean[] EMPTY = {};
  private static final boolean[] ARRAY_FALSE_FALSE = { false, false };
  private static final boolean[] ARRAY_FALSE_TRUE = { false, true };
  private static final boolean[] ARRAY_FALSE_TRUE_FALSE = { false, true, false };

  @Test
  void testCapitalize() {
    assertNull(StringUtils.capitalize(null));

    assertEquals("", StringUtils.capitalize(""), "capitalize(empty-string) failed");
    assertEquals("X", StringUtils.capitalize("x"), "capitalize(single-char-string) failed");
    assertEquals(FOO_CAP, StringUtils.capitalize(FOO_CAP), "capitalize(String) failed");
    assertEquals(FOO_CAP, StringUtils.capitalize(FOO_UNCAP), "capitalize(string) failed");

    assertEquals("\u01C8", StringUtils.capitalize("\u01C9"), "capitalize(String) is not using TitleCase");

    // Javadoc examples
    assertNull(StringUtils.capitalize(null));
    assertEquals("", StringUtils.capitalize(""));
    assertEquals("Cat", StringUtils.capitalize("cat"));
    assertEquals("CAt", StringUtils.capitalize("cAt"));
    assertEquals("'cat'", StringUtils.capitalize("'cat'"));
  }
}
