package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a JUnit test for DateTimeZone.
 *
 * @author Stephen Colebourne
 */
public class AA_0014_OR extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0014_OR.class);
  }

  public AA_0014_OR(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
  }

  @Override
  protected void tearDown() throws Exception {
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

  // -----------------------------------------------------------------------
  public void test_MockGazaIsCorrect() {
    DateTime pre = new DateTime(CUTOVER_GAZA - 1L, MOCK_GAZA);
    assertEquals("2007-03-31T23:59:59.999+02:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_GAZA, MOCK_GAZA);
    assertEquals("2007-04-01T01:00:00.000+03:00", at.toString());
    DateTime post = new DateTime(CUTOVER_GAZA + 1L, MOCK_GAZA);
    assertEquals("2007-04-01T01:00:00.001+03:00", post.toString());
  }

  // -----------------------------------------------------------------------
  // ------------------------ Bug [1710316] --------------------------------
  // -----------------------------------------------------------------------
  /** Mock zone simulating America/Grand_Turk cutover at midnight 2007-04-01 */
  private static long CUTOVER_TURK = 1175403600000L;
  private static int OFFSET_TURK = -18000000; // -05:00
  private static final DateTimeZone MOCK_TURK = new MockZone(CUTOVER_TURK, OFFSET_TURK, 3600);

  // -----------------------------------------------------------------------
  public void test_MockTurkIsCorrect() {
    DateTime pre = new DateTime(CUTOVER_TURK - 1L, MOCK_TURK);
    assertEquals("2007-03-31T23:59:59.999-05:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_TURK, MOCK_TURK);
    assertEquals("2007-04-01T01:00:00.000-04:00", at.toString());
    DateTime post = new DateTime(CUTOVER_TURK + 1L, MOCK_TURK);
    assertEquals("2007-04-01T01:00:00.001-04:00", post.toString());
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  /** America/New_York cutover from 01:59 to 03:00 on 2007-03-11 */
  private static long CUTOVER_NEW_YORK_SPRING = 1173596400000L; // 2007-03-11T03:00:00.000-04:00
  private static final DateTimeZone ZONE_NEW_YORK = DateTimeZone.forID("America/New_York");
  // DateTime x = new DateTime(2007, 1, 1, 0, 0, 0, 0, ZONE_NEW_YORK);
  // System.out.println(ZONE_NEW_YORK.nextTransition(x.getMillis()));
  // DateTime y = new DateTime(ZONE_NEW_YORK.nextTransition(x.getMillis()),
  // ZONE_NEW_YORK);
  // System.out.println(y);

  // -----------------------------------------------------------------------
  public void test_NewYorkIsCorrect_Spring() {
    DateTime pre = new DateTime(CUTOVER_NEW_YORK_SPRING - 1L, ZONE_NEW_YORK);
    assertEquals("2007-03-11T01:59:59.999-05:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_NEW_YORK_SPRING, ZONE_NEW_YORK);
    assertEquals("2007-03-11T03:00:00.000-04:00", at.toString());
    DateTime post = new DateTime(CUTOVER_NEW_YORK_SPRING + 1L, ZONE_NEW_YORK);
    assertEquals("2007-03-11T03:00:00.001-04:00", post.toString());
  }

  // -----------------------------------------------------------------------
  /** America/New_York cutover from 01:59 to 01:00 on 2007-11-04 */
  private static long CUTOVER_NEW_YORK_AUTUMN = 1194156000000L; // 2007-11-04T01:00:00.000-05:00

  // -----------------------------------------------------------------------
  public void test_NewYorkIsCorrect_Autumn() {
    DateTime pre = new DateTime(CUTOVER_NEW_YORK_AUTUMN - 1L, ZONE_NEW_YORK);
    assertEquals("2007-11-04T01:59:59.999-04:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_NEW_YORK_AUTUMN, ZONE_NEW_YORK);
    assertEquals("2007-11-04T01:00:00.000-05:00", at.toString());
    DateTime post = new DateTime(CUTOVER_NEW_YORK_AUTUMN + 1L, ZONE_NEW_YORK);
    assertEquals("2007-11-04T01:00:00.001-05:00", post.toString());
  }

  // -----------------------------------------------------------------------
  /** Europe/Moscow cutover from 01:59 to 03:00 on 2007-03-25 */
  private static long CUTOVER_MOSCOW_SPRING = 1174777200000L; // 2007-03-25T03:00:00.000+04:00
  private static final DateTimeZone ZONE_MOSCOW = DateTimeZone.forID("Europe/Moscow");

  // -----------------------------------------------------------------------
  public void test_MoscowIsCorrect_Spring() {
    // DateTime x = new DateTime(2007, 7, 1, 0, 0, 0, 0, ZONE_MOSCOW);
    // System.out.println(ZONE_MOSCOW.nextTransition(x.getMillis()));
    // DateTime y = new DateTime(ZONE_MOSCOW.nextTransition(x.getMillis()),
    // ZONE_MOSCOW);
    // System.out.println(y);
    DateTime pre = new DateTime(CUTOVER_MOSCOW_SPRING - 1L, ZONE_MOSCOW);
    assertEquals("2007-03-25T01:59:59.999+03:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_MOSCOW_SPRING, ZONE_MOSCOW);
    assertEquals("2007-03-25T03:00:00.000+04:00", at.toString());
    DateTime post = new DateTime(CUTOVER_MOSCOW_SPRING + 1L, ZONE_MOSCOW);
    assertEquals("2007-03-25T03:00:00.001+04:00", post.toString());
  }

  // -----------------------------------------------------------------------
  /** America/New_York cutover from 02:59 to 02:00 on 2007-10-28 */
  private static long CUTOVER_MOSCOW_AUTUMN = 1193526000000L; // 2007-10-28T02:00:00.000+03:00

  // -----------------------------------------------------------------------
  public void test_MoscowIsCorrect_Autumn() {
    DateTime pre = new DateTime(CUTOVER_MOSCOW_AUTUMN - 1L, ZONE_MOSCOW);
    assertEquals("2007-10-28T02:59:59.999+04:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_MOSCOW_AUTUMN, ZONE_MOSCOW);
    assertEquals("2007-10-28T02:00:00.000+03:00", at.toString());
    DateTime post = new DateTime(CUTOVER_MOSCOW_AUTUMN + 1L, ZONE_MOSCOW);
    assertEquals("2007-10-28T02:00:00.001+03:00", post.toString());
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  /** America/Guatemala cutover from 23:59 to 23:00 on 2006-09-30 */
  private static long CUTOVER_GUATEMALA_AUTUMN = 1159678800000L; // 2006-09-30T23:00:00.000-06:00
  private static final DateTimeZone ZONE_GUATEMALA = DateTimeZone.forID("America/Guatemala");

  // -----------------------------------------------------------------------
  public void test_GuatemataIsCorrect_Autumn() {
    DateTime pre = new DateTime(CUTOVER_GUATEMALA_AUTUMN - 1L, ZONE_GUATEMALA);
    assertEquals("2006-09-30T23:59:59.999-05:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_GUATEMALA_AUTUMN, ZONE_GUATEMALA);
    assertEquals("2006-09-30T23:00:00.000-06:00", at.toString());
    DateTime post = new DateTime(CUTOVER_GUATEMALA_AUTUMN + 1L, ZONE_GUATEMALA);
    assertEquals("2006-09-30T23:00:00.001-06:00", post.toString());
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  /**
   * America/Rio_Branco gap cutover from 2008-06-23T23:59-05:00 to
   * 2008-06-24T01:00-04:00
   */
  private static long CUTOVER_RIO_BRANCO_AUTUMN = 1214283600000L; // 2008-06-24T01:00:00.000-04:00
  private static final DateTimeZone ZONE_RIO_BRANCO = DateTimeZone.forID("America/Rio_Branco");

  // -----------------------------------------------------------------------
  public void test_RioBrancoIsCorrect_Spring() {
    DateTime pre = new DateTime(CUTOVER_RIO_BRANCO_AUTUMN - 1L, ZONE_RIO_BRANCO);
    assertEquals("2008-06-23T23:59:59.999-05:00", pre.toString());
    DateTime at = new DateTime(CUTOVER_RIO_BRANCO_AUTUMN, ZONE_RIO_BRANCO);
    assertEquals("2008-06-24T01:00:00.000-04:00", at.toString());
    DateTime post = new DateTime(CUTOVER_RIO_BRANCO_AUTUMN + 1L, ZONE_RIO_BRANCO);
    assertEquals("2008-06-24T01:00:00.001-04:00", post.toString());
  }
}
