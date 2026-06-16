package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test case.
 *
 * @author Stephen Colebourne
 */
public class AA_0013_OR extends TestCase {
  /**
   * The main method for this test program.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  /**
   * TestSuite is a junit required method.
   */
  public static TestSuite suite() {
    return new TestSuite(AA_0013_OR.class);
  }

  /**
   * TestDateTimeComparator constructor.
   *
   * @param name
   */
  public AA_0013_OR(String name) {
    super(name);
  }

  public void testConstructor() {
    DateTimeConstants c = new DateTimeConstants() {
    };
    c.toString();
  }
}
