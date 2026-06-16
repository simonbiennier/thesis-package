package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

public class AA_0040_OR extends TestCase {
  private static final DateTimeZone UTC = DateTimeZone.UTC;
  private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
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
    return new TestSuite(AA_0040_OR.class);
  }

  public AA_0040_OR(String name) {
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

  public void testFormat_year_twoDigit() {
    DateTimeFormatter f = new DateTimeFormatterBuilder().appendTwoDigitYear(2000, true).toFormatter();
    f = f.withZoneUTC();
    expect = new DateTime(2004, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("04"));
    expect = new DateTime(4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("+04"));
    expect = new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-04"));
    expect = new DateTime(4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("4"));
    expect = new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-4"));
    expect = new DateTime(4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("004"));
    expect = new DateTime(4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("+004"));
    expect = new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-004"));
    expect = new DateTime(3004, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("3004"));
    expect = new DateTime(3004, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("+3004"));
    expect = new DateTime(-3004, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-3004"));
  }

  public void testFormat_weekyearOfEra_twoDigit() {
    DateTimeFormatter f = new DateTimeFormatterBuilder().appendTwoDigitWeekyear(2000, true).toFormatter();
    f = f.withZoneUTC();
    expect = new DateTime(2003, 12, 29, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("04"));
    expect = new DateTime(3, 12, 29, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("+04"));
    expect = new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-04"));
    expect = new DateTime(3, 12, 29, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("4"));
    expect = new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-4"));
    expect = new DateTime(3, 12, 29, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("004"));
    expect = new DateTime(3, 12, 29, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("+004"));
    expect = new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-004"));
    expect = new DateTime(3004, 1, 2, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("3004"));
    expect = new DateTime(3004, 1, 2, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("+3004"));
    expect = new DateTime(-3004, 1, 4, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("-3004"));
  }
}
