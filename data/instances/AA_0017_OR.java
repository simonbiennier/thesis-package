package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

public class AA_0017_OR extends TestCase {
  private static final DateTimeZone UTC = DateTimeZone.UTC;
  private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
  private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
  private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
  private static final DateTimeZone NEWYORK = DateTimeZone.forID("America/New_York");
  long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
      365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365;
  // 2002-06-09
  private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;
  private DateTimeZone originalDateTimeZone = null;
  private TimeZone originalTimeZone = null;
  private Locale originalLocale = null;
  DateTime expect;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0017_OR.class);
  }

  public AA_0017_OR(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
    originalDateTimeZone = DateTimeZone.getDefault();
    originalTimeZone = TimeZone.getDefault();
    originalLocale = Locale.getDefault();
    DateTimeZone.setDefault(LONDON);
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Locale.setDefault(Locale.UK);
  }

  @Override
  protected void tearDown() throws Exception {
    DateTimeUtils.setCurrentMillisSystem();
    DateTimeZone.setDefault(originalDateTimeZone);
    TimeZone.setDefault(originalTimeZone);
    Locale.setDefault(originalLocale);
    originalDateTimeZone = null;
    originalTimeZone = null;
    originalLocale = null;
  }

  public void testFormat_era() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("G").withLocale(Locale.UK);
    assertEquals(dt.toString(), "AD", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "AD", f.print(dt));
    dt = dt.withZone(PARIS);
    assertEquals(dt.toString(), "AD", f.print(dt));
  }

  public void testFormat_centuryOfEra() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("C").withLocale(Locale.UK);
    assertEquals(dt.toString(), "20", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "20", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "20", f.print(dt));
  }

  public void testFormat_yearOfEra() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("Y").withLocale(Locale.UK);
    assertEquals(dt.toString(), "2004", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "2004", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "2004", f.print(dt));
  }

  public void testFormat_year() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("y").withLocale(Locale.UK);
    assertEquals(dt.toString(), "2004", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "2004", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "2004", f.print(dt));
  }

  public void testFormat_weekyear() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("x").withLocale(Locale.UK);
    assertEquals(dt.toString(), "2004", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "2004", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "2004", f.print(dt));
  }

  public void testFormat_weekOfWeekyear() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("w").withLocale(Locale.UK);
    assertEquals(dt.toString(), "24", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "24", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "24", f.print(dt));
  }

  public void testFormat_dayOfWeek() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("e").withLocale(Locale.UK);
    assertEquals(dt.toString(), "3", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "3", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "3", f.print(dt));
  }

  public void testFormat_dayOfWeekShortText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("E").withLocale(Locale.UK);
    assertEquals(dt.toString(), "Wed", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "Wed", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "Wed", f.print(dt));
  }

  public void testFormat_dayOfWeekText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("EEEE").withLocale(Locale.UK);
    assertEquals(dt.toString(), "Wednesday", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "Wednesday", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "Wednesday", f.print(dt));
  }

  public void testFormat_dayOfYearText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("D").withLocale(Locale.UK);
    assertEquals(dt.toString(), "161", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "161", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "161", f.print(dt));
  }

  public void testFormat_monthOfYear() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("M").withLocale(Locale.UK);
    assertEquals(dt.toString(), "6", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "6", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "6", f.print(dt));
  }

  public void testFormat_monthOfYearShortText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("MMM").withLocale(Locale.UK);
    assertEquals(dt.toString(), "Jun", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "Jun", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "Jun", f.print(dt));
  }

  public void testFormat_monthOfYearText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("MMMM").withLocale(Locale.UK);
    assertEquals(dt.toString(), "June", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "June", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "June", f.print(dt));
  }

  public void testFormat_dayOfMonth() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("d").withLocale(Locale.UK);
    assertEquals(dt.toString(), "9", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "9", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "9", f.print(dt));
  }

  public void testFormat_hourOfHalfday() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("K").withLocale(Locale.UK);
    assertEquals(dt.toString(), "10", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "6", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "7", f.print(dt));
  }

  public void testFormat_clockhourOfHalfday() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("h").withLocale(Locale.UK);
    assertEquals(dt.toString(), "10", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "6", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "7", f.print(dt));
  }

  public void testFormat_hourOfDay() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("H").withLocale(Locale.UK);
    assertEquals(dt.toString(), "10", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "6", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "19", f.print(dt));
  }

  public void testFormat_clockhourOfDay() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("k").withLocale(Locale.UK);
    assertEquals(dt.toString(), "10", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "6", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "19", f.print(dt));
  }

  public void testFormat_minute() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("m").withLocale(Locale.UK);
    assertEquals(dt.toString(), "20", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "20", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "20", f.print(dt));
  }

  public void testFormat_second() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("s").withLocale(Locale.UK);
    assertEquals(dt.toString(), "30", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "30", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "30", f.print(dt));
  }

  public void testFormat_fractionOfSecond() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("SSS").withLocale(Locale.UK);
    assertEquals(dt.toString(), "040", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "040", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "040", f.print(dt));
  }

  public void testFormat_fractionOfSecondLong() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("SSSSSS").withLocale(Locale.UK);
    assertEquals(dt.toString(), "040000", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "040000", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "040000", f.print(dt));
  }

  public void testFormat_zoneText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("z").withLocale(Locale.ENGLISH);
    assertEquals(dt.toString(), "UTC", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "EDT", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "JST", f.print(dt));
  }

  public void testFormat_zoneLongText() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("zzzz").withLocale(Locale.ENGLISH);
    assertEquals(dt.toString(), "Coordinated Universal Time", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "Eastern Daylight Time", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "Japan Standard Time", f.print(dt));
  }

  public void testFormat_zoneAmount() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("Z").withLocale(Locale.UK);
    assertEquals(dt.toString(), "+0000", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "-0400", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "+0900", f.print(dt));
  }

  public void testFormat_zoneAmountColon() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("ZZ").withLocale(Locale.UK);
    assertEquals(dt.toString(), "+00:00", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "-04:00", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "+09:00", f.print(dt));
  }

  public void testFormat_zoneAmountID() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("ZZZ").withLocale(Locale.UK);
    assertEquals(dt.toString(), "UTC", f.print(dt));
    dt = dt.withZone(NEWYORK);
    assertEquals(dt.toString(), "America/New_York", f.print(dt));
    dt = dt.withZone(TOKYO);
    assertEquals(dt.toString(), "Asia/Tokyo", f.print(dt));
  }
}
