package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AA_0015_OR extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0015_OR.class);
  }

  public AA_0015_OR(String name) {
    super(name);
  }
  // -----------------------------------------------------------------------
  // ------------------------ Bug [1710316] --------------------------------
  // -----------------------------------------------------------------------
  // The behaviour of getOffsetFromLocal is defined in its javadoc
  // However, this definition doesn't work for all DateTimeField operations

  /** Mock zone simulating Asia/Gaza cutover at midnight 2007-04-01 */
  private static long CUTOVER_GAZA = 1175378400000L;
  private static int OFFSET_GAZA = 7200000; // +02:00
  private static final DateTimeZone MOCK_GAZA = new MockZone(CUTOVER_GAZA, OFFSET_GAZA, 3600);

  public void test_DateTime_newValid_Gaza() {
    new DateTime(2007, 3, 31, 19, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 3, 31, 20, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 3, 31, 21, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 3, 31, 22, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 3, 31, 23, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 4, 1, 1, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 4, 1, 2, 0, 0, 0, MOCK_GAZA);
    new DateTime(2007, 4, 1, 3, 0, 0, 0, MOCK_GAZA);
  }

  // -----------------------------------------------------------------------
  // ------------------------ Bug [1710316] --------------------------------
  // -----------------------------------------------------------------------
  /** Mock zone simulating America/Grand_Turk cutover at midnight 2007-04-01 */
  private static long CUTOVER_TURK = 1175403600000L;
  private static int OFFSET_TURK = -18000000; // -05:00
  private static final DateTimeZone MOCK_TURK = new MockZone(CUTOVER_TURK, OFFSET_TURK, 3600);

  public void test_DateTime_newValid_Turk() {
    new DateTime(2007, 3, 31, 23, 0, 0, 0, MOCK_TURK);
    new DateTime(2007, 4, 1, 1, 0, 0, 0, MOCK_TURK);
    new DateTime(2007, 4, 1, 2, 0, 0, 0, MOCK_TURK);
    new DateTime(2007, 4, 1, 3, 0, 0, 0, MOCK_TURK);
    new DateTime(2007, 4, 1, 4, 0, 0, 0, MOCK_TURK);
    new DateTime(2007, 4, 1, 5, 0, 0, 0, MOCK_TURK);
    new DateTime(2007, 4, 1, 6, 0, 0, 0, MOCK_TURK);
  }
}
