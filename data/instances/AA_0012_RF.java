package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for DateTimeUtils.
 *
 * @author Stephen Colebourne
 */
public class AA_0012_RF extends TestCase {
  // Test in 2002/03 as time zones are more well known
  // (before the late 90's they were all over the place)
  long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
      365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365;
  long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
      365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365 + 365;

  // 2002-06-09
  private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

  // 2002-04-05
  private long TEST_TIME1 = (y2002days + 31L + 28L + 31L + 5L - 1L) * DateTimeConstants.MILLIS_PER_DAY
      + 12L * DateTimeConstants.MILLIS_PER_HOUR
      + 24L * DateTimeConstants.MILLIS_PER_MINUTE;

  // 2003-05-06
  private long TEST_TIME2 = (y2003days + 31L + 28L + 31L + 30L + 6L - 1L) * DateTimeConstants.MILLIS_PER_DAY
      + 14L * DateTimeConstants.MILLIS_PER_HOUR
      + 28L * DateTimeConstants.MILLIS_PER_MINUTE;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0012_RF.class);
  }

  public AA_0012_RF(String name) {
    super(name);
  }

  public void testTest() {
    assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
    assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
    assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
  }

  public void testMillisProvider_null() {
    try {
      DateTimeUtils.setCurrentMillisProvider(null);
    } catch (IllegalArgumentException ex) {
      // expected
    }
  }

  public void testGetPeriodType_PeriodType() {
    assertEquals(PeriodType.dayTime(), DateTimeUtils.getPeriodType(PeriodType.dayTime()));
    assertEquals(PeriodType.standard(), DateTimeUtils.getPeriodType(null));
  }

  public void testGetDurationMillis_RI() {
    Duration dur = new Duration(123L);
    assertEquals(123L, DateTimeUtils.getDurationMillis(dur));
    assertEquals(0L, DateTimeUtils.getDurationMillis(null));
  }
}
