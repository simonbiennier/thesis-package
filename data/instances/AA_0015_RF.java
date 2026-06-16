package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AA_0015_RF extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0015_RF.class);
  }

  public AA_0015_RF(String name) {
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
    DateTime dt1 = new DateTime(2007, 3, 31, 19, 0, 0, 0, MOCK_GAZA);
    assertEquals(2007, dt1.getYear());
    assertEquals(3, dt1.getMonthOfYear());
    assertEquals(31, dt1.getDayOfMonth());
    assertEquals(19, dt1.getHourOfDay());
    DateTime dt2 = new DateTime(2007, 3, 31, 20, 0, 0, 0, MOCK_GAZA);
    assertEquals(20, dt2.getHourOfDay());
    DateTime dt3 = new DateTime(2007, 3, 31, 21, 0, 0, 0, MOCK_GAZA);
    assertEquals(21, dt3.getHourOfDay());
    DateTime dt4 = new DateTime(2007, 3, 31, 22, 0, 0, 0, MOCK_GAZA);
    assertEquals(22, dt4.getHourOfDay());
    DateTime dt5 = new DateTime(2007, 3, 31, 23, 0, 0, 0, MOCK_GAZA);
    assertEquals(23, dt5.getHourOfDay());
    DateTime dt6 = new DateTime(2007, 4, 1, 1, 0, 0, 0, MOCK_GAZA);
    assertEquals(2007, dt6.getYear());
    assertEquals(4, dt6.getMonthOfYear());
    assertEquals(1, dt6.getDayOfMonth());
    assertEquals(1, dt6.getHourOfDay());
    DateTime dt7 = new DateTime(2007, 4, 1, 2, 0, 0, 0, MOCK_GAZA);
    assertEquals(2, dt7.getHourOfDay());
    DateTime dt8 = new DateTime(2007, 4, 1, 3, 0, 0, 0, MOCK_GAZA);
    assertEquals(3, dt8.getHourOfDay());
  }

  // -----------------------------------------------------------------------
  // ------------------------ Bug [1710316] --------------------------------
  // -----------------------------------------------------------------------
  /** Mock zone simulating America/Grand_Turk cutover at midnight 2007-04-01 */
  private static long CUTOVER_TURK = 1175403600000L;
  private static int OFFSET_TURK = -18000000; // -05:00
  private static final DateTimeZone MOCK_TURK = new MockZone(CUTOVER_TURK, OFFSET_TURK, 3600);

  public void test_DateTime_newValid_Turk() {
    DateTime dt1 = new DateTime(2007, 3, 31, 23, 0, 0, 0, MOCK_TURK);
    assertEquals(2007, dt1.getYear());
    assertEquals(3, dt1.getMonthOfYear());
    assertEquals(31, dt1.getDayOfMonth());
    assertEquals(23, dt1.getHourOfDay());
    DateTime dt2 = new DateTime(2007, 4, 1, 1, 0, 0, 0, MOCK_TURK);
    assertEquals(2007, dt2.getYear());
    assertEquals(4, dt2.getMonthOfYear());
    assertEquals(1, dt2.getDayOfMonth());
    assertEquals(1, dt2.getHourOfDay());
    DateTime dt3 = new DateTime(2007, 4, 1, 2, 0, 0, 0, MOCK_TURK);
    assertEquals(2, dt3.getHourOfDay());
    DateTime dt4 = new DateTime(2007, 4, 1, 3, 0, 0, 0, MOCK_TURK);
    assertEquals(3, dt4.getHourOfDay());
    DateTime dt5 = new DateTime(2007, 4, 1, 4, 0, 0, 0, MOCK_TURK);
    assertEquals(4, dt5.getHourOfDay());
    DateTime dt6 = new DateTime(2007, 4, 1, 5, 0, 0, 0, MOCK_TURK);
    assertEquals(5, dt6.getHourOfDay());
    DateTime dt7 = new DateTime(2007, 4, 1, 6, 0, 0, 0, MOCK_TURK);
    assertEquals(6, dt7.getHourOfDay());
  }
}
