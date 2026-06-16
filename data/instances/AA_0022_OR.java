package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

public class AA_0022_OR extends TestCase {
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

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0022_OR.class);
  }

  public AA_0022_OR(String name) {
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

  public void testFormat_yearOfEra_twoDigit() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK);
    assertEquals(dt.toString(), "04", f.print(dt));
    dt = new DateTime(-123, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "23", f.print(dt));
    // current time set to 2002-06-09
    f = f.withZoneUTC();
    DateTime expect = null;
    expect = new DateTime(2004, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("04"));
    expect = new DateTime(1922, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("22"));
    expect = new DateTime(2021, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("21"));
    // Added tests to ensure single sign digit parse fails properly
    try {
      f.parseDateTime("-");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    // Added tests for pivot year setting
    f = f.withPivotYear(new Integer(2050));
    expect = new DateTime(2000, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("00"));
    expect = new DateTime(2099, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("99"));
    // Added tests to ensure two digit parsing is lenient for DateTimeFormat
    f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK);
    f = f.withZoneUTC();
    f.parseDateTime("5");
    f.parseDateTime("005");
    f.parseDateTime("+50");
    f.parseDateTime("-50");
  }

  public void testFormat_year_twoDigit() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK);
    assertEquals(dt.toString(), "04", f.print(dt));
    dt = new DateTime(-123, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "23", f.print(dt));
    // current time set to 2002-06-09
    f = f.withZoneUTC();
    DateTime expect = null;
    expect = new DateTime(2004, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("04"));
    expect = new DateTime(1922, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("22"));
    expect = new DateTime(2021, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("21"));
    // Added tests to ensure single sign digit parse fails properly
    try {
      f.parseDateTime("-");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    // Added tests for pivot year setting
    f = f.withPivotYear(new Integer(2050));
    expect = new DateTime(2000, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("00"));
    expect = new DateTime(2099, 1, 1, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("99"));
    // Added tests to ensure two digit parsing is strict by default for
    // DateTimeFormatterBuilder
    f = new DateTimeFormatterBuilder().appendTwoDigitYear(2000).toFormatter();
    f = f.withZoneUTC();
    try {
      f.parseDateTime("5");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("005");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+50");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("-50");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    // Added tests to ensure two digit parsing is lenient for DateTimeFormat
    f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK);
    f = f.withZoneUTC();
    f.parseDateTime("5");
    f.parseDateTime("005");
    f.parseDateTime("+50");
    f.parseDateTime("-50");
    // Added tests for lenient two digit parsing
    f = new DateTimeFormatterBuilder().appendTwoDigitYear(2000, true).toFormatter();
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
    try {
      f.parseDateTime("-");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+");
      fail();
    } catch (IllegalArgumentException ex) {
    }
  }

  public void testFormat_weekyearOfEra_twoDigit() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeFormatter f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK);
    assertEquals(dt.toString(), "04", f.print(dt));
    dt = new DateTime(-123, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "23", f.print(dt));
    // current time set to 2002-06-09
    f = f.withZoneUTC();
    DateTime expect = null;
    expect = new DateTime(2003, 12, 29, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("04"));
    expect = new DateTime(1922, 1, 2, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("22"));
    expect = new DateTime(2021, 1, 4, 0, 0, 0, 0, UTC);
    assertEquals(expect, f.parseDateTime("21"));
    // Added tests to ensure single sign digit parse fails properly
    try {
      f.parseDateTime("-");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    // Added tests for pivot year setting
    f = f.withPivotYear(new Integer(2050));
    expect = new DateTime(2000, 1, 3, 0, 0, 0, 0, DateTimeZone.UTC);
    assertEquals(expect, f.parseDateTime("00"));
    expect = new DateTime(2098, 12, 29, 0, 0, 0, 0, DateTimeZone.UTC);
    assertEquals(expect, f.parseDateTime("99"));
    // Added tests to ensure two digit parsing is strict by default for
    // DateTimeFormatterBuilder
    f = new DateTimeFormatterBuilder().appendTwoDigitWeekyear(2000).toFormatter();
    f = f.withZoneUTC();
    try {
      f.parseDateTime("5");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("005");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+50");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("-50");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    // Added tests to ensure two digit parsing is lenient for DateTimeFormat
    f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK);
    f = f.withZoneUTC();
    f.parseDateTime("5");
    f.parseDateTime("005");
    f.parseDateTime("+50");
    f.parseDateTime("-50");
    // Added tests for lenient two digit parsing
    f = new DateTimeFormatterBuilder().appendTwoDigitWeekyear(2000, true).toFormatter();
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
    try {
      f.parseDateTime("-");
      fail();
    } catch (IllegalArgumentException ex) {
    }
    try {
      f.parseDateTime("+");
      fail();
    } catch (IllegalArgumentException ex) {
    }
  }
}
