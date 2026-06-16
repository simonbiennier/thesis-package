package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ObjectUtils}.
 */
@SuppressWarnings("deprecation") // deliberate use of deprecated code
class AA_0005_RF {
  static final class CharSequenceComparator implements Comparator<CharSequence> {

    @Override
    public int compare(final CharSequence o1, final CharSequence o2) {
      return o1.toString().compareTo(o2.toString());
    }

  }

  /**
   * String that is cloneable.
   */
  static final class CloneableString extends MutableObject<String> implements Cloneable {
    private static final long serialVersionUID = 1L;

    CloneableString(final String s) {
      super(s);
    }

    @Override
    public CloneableString clone() throws CloneNotSupportedException {
      return (CloneableString) super.clone();
    }
  }

  static final class NonComparableCharSequence implements CharSequence {
    final String value;

    /**
     * Create a new NonComparableCharSequence instance.
     *
     * @param value the CharSequence value
     */
    NonComparableCharSequence(final String value) {
      Validate.notNull(value);
      this.value = value;
    }

    @Override
    public char charAt(final int arg0) {
      return value.charAt(arg0);
    }

    @Override
    public int length() {
      return value.length();
    }

    @Override
    public CharSequence subSequence(final int arg0, final int arg1) {
      return value.subSequence(arg0, arg1);
    }

    @Override
    public String toString() {
      return value;
    }
  }

  /**
   * String that is not cloneable.
   */
  static final class UncloneableString extends MutableObject<String> implements Cloneable {
    private static final long serialVersionUID = 1L;

    UncloneableString(final String s) {
      super(s);
    }
  }

  private static final Supplier<?> NULL_SUPPLIER = null;

  private static final String FOO = "foo";
  private static final String BAR = "bar";
  private static final String[] NON_EMPTY_ARRAY = { FOO, BAR, };

  private static final List<String> NON_EMPTY_LIST = Arrays.asList(NON_EMPTY_ARRAY);

  private static final Set<String> NON_EMPTY_SET = new HashSet<>(NON_EMPTY_LIST);

  private static final Map<String, String> NON_EMPTY_MAP = new HashMap<>();

  static {
    NON_EMPTY_MAP.put(FOO, BAR);
  }

  /**
   * Tests {@link ObjectUtils#clone(Object)} with an array of primitives.
   */
  @Test
  void testCloneOfPrimitiveArray() {
    assertArrayEquals(new int[] { 1 }, ObjectUtils.clone(new int[] { 1 }));
  }
}
