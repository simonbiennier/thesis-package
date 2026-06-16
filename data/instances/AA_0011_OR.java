package org.joda.time;

import org.joda.time.chrono.ISOChronology;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for DurationField.
 *
 * @author Stephen Colebourne
 */
public class AA_0011_OR extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0011_OR.class);
  }

  public AA_0011_OR(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
  }

  @Override
  protected void tearDown() throws Exception {
  }

  public void test_subtract() throws Exception {
    DurationField fld = ISOChronology.getInstanceUTC().millis();
    assertEquals(900, fld.subtract(1000L, 100));
    assertEquals(900L, fld.subtract(1000L, 100L));
    assertEquals((1000L - Integer.MAX_VALUE), fld.subtract(1000L, Integer.MAX_VALUE));
    assertEquals((1000L - Integer.MIN_VALUE), fld.subtract(1000L, Integer.MIN_VALUE));
    assertEquals((1000L - Long.MAX_VALUE), fld.subtract(1000L, Long.MAX_VALUE));
    try {
      fld.subtract(-1000L, Long.MIN_VALUE);
      fail();
    } catch (ArithmeticException ex) {
    }
  }

}
