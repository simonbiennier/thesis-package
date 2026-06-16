package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * This class is a Junit unit test for DateTime Formating.
 *
 * @author Stephen Colebourne
 * @author Fredrik Borgh
 */
public class AA_0022_RF extends TestCase {
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
    return new TestSuite(AA_0022_RF.class);
  }

  public AA_0022_RF(String name) {
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

  private void assertParseFails(DateTimeFormatter f, String input) {
    try {
      f.parseDateTime(input);
      fail();
    } catch (IllegalArgumentException ex) {
    }
  }

  public void testFormat_yearOfEra_twoDigit_print() {
    DateTimeFormatter f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK);
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "04", f.print(dt));

    dt = new DateTime(-123, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "23", f.print(dt));
  }

  public void testFormat_yearOfEra_twoDigit_parse_usesDefaultPivot() {
    DateTimeFormatter f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK).withZoneUTC();
    assertEquals(new DateTime(2004, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("04"));
    assertEquals(new DateTime(1922, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("22"));
    assertEquals(new DateTime(2021, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("21"));
  }

  public void testFormat_yearOfEra_twoDigit_parse_rejectsSignOnly() {
    DateTimeFormatter f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK).withZoneUTC();
    assertParseFails(f, "-");
    assertParseFails(f, "+");
  }

  public void testFormat_yearOfEra_twoDigit_parse_withCustomPivot() {
    DateTimeFormatter f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK).withZoneUTC();
    f = f.withPivotYear(new Integer(2050));
    assertEquals(new DateTime(2000, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("00"));
    assertEquals(new DateTime(2099, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("99"));
  }

  public void testFormat_yearOfEra_twoDigit_parse_isLenientForDateTimeFormat() {
    DateTimeFormatter f = DateTimeFormat.forPattern("YY").withLocale(Locale.UK).withZoneUTC();
    f.parseDateTime("5");
    f.parseDateTime("005");
    f.parseDateTime("+50");
    f.parseDateTime("-50");
  }

  public void testFormat_year_twoDigit_print() {
    DateTimeFormatter f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK);
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "04", f.print(dt));

    dt = new DateTime(-123, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "23", f.print(dt));
  }

  public void testFormat_year_twoDigit_parse_usesDefaultPivot() {
    DateTimeFormatter f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK).withZoneUTC();
    assertEquals(new DateTime(2004, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("04"));
    assertEquals(new DateTime(1922, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("22"));
    assertEquals(new DateTime(2021, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("21"));
  }

  public void testFormat_year_twoDigit_parse_rejectsSignOnly() {
    DateTimeFormatter f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK).withZoneUTC();
    assertParseFails(f, "-");
    assertParseFails(f, "+");
  }

  public void testFormat_year_twoDigit_parse_withCustomPivot() {
    DateTimeFormatter f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK).withZoneUTC();
    f = f.withPivotYear(new Integer(2050));
    assertEquals(new DateTime(2000, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("00"));
    assertEquals(new DateTime(2099, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("99"));
  }

  public void testFormat_year_twoDigit_parse_isStrictByDefaultForBuilder() {
    DateTimeFormatter f = new DateTimeFormatterBuilder().appendTwoDigitYear(2000).toFormatter().withZoneUTC();
    assertParseFails(f, "5");
    assertParseFails(f, "005");
    assertParseFails(f, "+50");
    assertParseFails(f, "-50");
  }

  public void testFormat_year_twoDigit_parse_isLenientForDateTimeFormat() {
    DateTimeFormatter f = DateTimeFormat.forPattern("yy").withLocale(Locale.UK).withZoneUTC();
    f.parseDateTime("5");
    f.parseDateTime("005");
    f.parseDateTime("+50");
    f.parseDateTime("-50");
  }

  public void testFormat_year_twoDigit_parse_lenientBuilderAcceptsVariableLengthAndSign() {
    DateTimeFormatter f = new DateTimeFormatterBuilder().appendTwoDigitYear(2000, true).toFormatter().withZoneUTC();
    assertEquals(new DateTime(2004, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("04"));
    assertEquals(new DateTime(4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("+04"));
    assertEquals(new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-04"));
    assertEquals(new DateTime(4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("4"));
    assertEquals(new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-4"));
    assertEquals(new DateTime(4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("004"));
    assertEquals(new DateTime(4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("+004"));
    assertEquals(new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-004"));
    assertEquals(new DateTime(3004, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("3004"));
    assertEquals(new DateTime(3004, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("+3004"));
    assertEquals(new DateTime(-3004, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-3004"));
    assertParseFails(f, "-");
    assertParseFails(f, "+");
  }

  public void testFormat_weekyearOfEra_twoDigit_print() {
    DateTimeFormatter f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK);
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "04", f.print(dt));

    dt = new DateTime(-123, 6, 9, 10, 20, 30, 40, UTC);
    assertEquals(dt.toString(), "23", f.print(dt));
  }

  public void testFormat_weekyearOfEra_twoDigit_parse_usesDefaultPivot() {
    DateTimeFormatter f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK).withZoneUTC();
    assertEquals(new DateTime(2003, 12, 29, 0, 0, 0, 0, UTC), f.parseDateTime("04"));
    assertEquals(new DateTime(1922, 1, 2, 0, 0, 0, 0, UTC), f.parseDateTime("22"));
    assertEquals(new DateTime(2021, 1, 4, 0, 0, 0, 0, UTC), f.parseDateTime("21"));
  }

  public void testFormat_weekyearOfEra_twoDigit_parse_rejectsSignOnly() {
    DateTimeFormatter f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK).withZoneUTC();
    assertParseFails(f, "-");
    assertParseFails(f, "+");
  }

  public void testFormat_weekyearOfEra_twoDigit_parse_withCustomPivot() {
    DateTimeFormatter f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK).withZoneUTC();
    f = f.withPivotYear(new Integer(2050));
    assertEquals(new DateTime(2000, 1, 3, 0, 0, 0, 0, DateTimeZone.UTC), f.parseDateTime("00"));
    assertEquals(new DateTime(2098, 12, 29, 0, 0, 0, 0, DateTimeZone.UTC), f.parseDateTime("99"));
  }

  public void testFormat_weekyearOfEra_twoDigit_parse_isStrictByDefaultForBuilder() {
    DateTimeFormatter f = new DateTimeFormatterBuilder().appendTwoDigitWeekyear(2000).toFormatter().withZoneUTC();
    assertParseFails(f, "5");
    assertParseFails(f, "005");
    assertParseFails(f, "+50");
    assertParseFails(f, "-50");
  }

  public void testFormat_weekyearOfEra_twoDigit_parse_isLenientForDateTimeFormat() {
    DateTimeFormatter f = DateTimeFormat.forPattern("xx").withLocale(Locale.UK).withZoneUTC();
    f.parseDateTime("5");
    f.parseDateTime("005");
    f.parseDateTime("+50");
    f.parseDateTime("-50");
  }

  public void testFormat_weekyearOfEra_twoDigit_parse_lenientBuilderAcceptsVariableLengthAndSign() {
    DateTimeFormatter f = new DateTimeFormatterBuilder().appendTwoDigitWeekyear(2000, true).toFormatter().withZoneUTC();
    assertEquals(new DateTime(2003, 12, 29, 0, 0, 0, 0, UTC), f.parseDateTime("04"));
    assertEquals(new DateTime(3, 12, 29, 0, 0, 0, 0, UTC), f.parseDateTime("+04"));
    assertEquals(new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-04"));
    assertEquals(new DateTime(3, 12, 29, 0, 0, 0, 0, UTC), f.parseDateTime("4"));
    assertEquals(new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-4"));
    assertEquals(new DateTime(3, 12, 29, 0, 0, 0, 0, UTC), f.parseDateTime("004"));
    assertEquals(new DateTime(3, 12, 29, 0, 0, 0, 0, UTC), f.parseDateTime("+004"));
    assertEquals(new DateTime(-4, 1, 1, 0, 0, 0, 0, UTC), f.parseDateTime("-004"));
    assertEquals(new DateTime(3004, 1, 2, 0, 0, 0, 0, UTC), f.parseDateTime("3004"));
    assertEquals(new DateTime(3004, 1, 2, 0, 0, 0, 0, UTC), f.parseDateTime("+3004"));
    assertEquals(new DateTime(-3004, 1, 4, 0, 0, 0, 0, UTC), f.parseDateTime("-3004"));
    assertParseFails(f, "-");
    assertParseFails(f, "+");
  }
}
