package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link BooleanUtils}.
 */
class AA_0007_RF {
  @Test
  void test_isTrue_Boolean() {
    assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
    assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    assertFalse(BooleanUtils.isTrue(null));
  }
}