package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AA_0019_RF extends TestCase {
  private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
  private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
  private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
  private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
  private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

  long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
      365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
      366 + 365;
  // 2002-06-09
  private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * MILLIS_PER_DAY;

  private DateTimeZone originalDateTimeZone = null;
  private TimeZone originalTimeZone = null;
  private Locale originalLocale = null;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0019_RF.class);
  }

  public AA_0019_RF(String name) {
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

  public void testDurationFields_Names() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals("eras", coptic.eras().getName());
    assertEquals("centuries", coptic.centuries().getName());
    assertEquals("years", coptic.years().getName());
    assertEquals("weekyears", coptic.weekyears().getName());
    assertEquals("months", coptic.months().getName());
    assertEquals("weeks", coptic.weeks().getName());
    assertEquals("days", coptic.days().getName());
    assertEquals("halfdays", coptic.halfdays().getName());
    assertEquals("hours", coptic.hours().getName());
    assertEquals("minutes", coptic.minutes().getName());
    assertEquals("seconds", coptic.seconds().getName());
    assertEquals("millis", coptic.millis().getName());
  }

  public void testDurationFields_IsSupported() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals(false, coptic.eras().isSupported());
    assertEquals(true, coptic.centuries().isSupported());
    assertEquals(true, coptic.years().isSupported());
    assertEquals(true, coptic.weekyears().isSupported());
    assertEquals(true, coptic.months().isSupported());
    assertEquals(true, coptic.weeks().isSupported());
    assertEquals(true, coptic.days().isSupported());
    assertEquals(true, coptic.halfdays().isSupported());
    assertEquals(true, coptic.hours().isSupported());
    assertEquals(true, coptic.minutes().isSupported());
    assertEquals(true, coptic.seconds().isSupported());
    assertEquals(true, coptic.millis().isSupported());
  }

  public void testDurationFields_IsPrecise_DefaultZone() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals(false, coptic.centuries().isPrecise());
    assertEquals(false, coptic.years().isPrecise());
    assertEquals(false, coptic.weekyears().isPrecise());
    assertEquals(false, coptic.months().isPrecise());
    assertEquals(false, coptic.weeks().isPrecise());
    assertEquals(false, coptic.days().isPrecise());
    assertEquals(false, coptic.halfdays().isPrecise());
    assertEquals(true, coptic.hours().isPrecise());
    assertEquals(true, coptic.minutes().isPrecise());
    assertEquals(true, coptic.seconds().isPrecise());
    assertEquals(true, coptic.millis().isPrecise());
  }

  public void testDurationFields_IsPrecise_Utc() {
    final CopticChronology copticUTC = CopticChronology.getInstanceUTC();
    assertEquals(false, copticUTC.centuries().isPrecise());
    assertEquals(false, copticUTC.years().isPrecise());
    assertEquals(false, copticUTC.weekyears().isPrecise());
    assertEquals(false, copticUTC.months().isPrecise());
    assertEquals(true, copticUTC.weeks().isPrecise());
    assertEquals(true, copticUTC.days().isPrecise());
    assertEquals(true, copticUTC.halfdays().isPrecise());
    assertEquals(true, copticUTC.hours().isPrecise());
    assertEquals(true, copticUTC.minutes().isPrecise());
    assertEquals(true, copticUTC.seconds().isPrecise());
    assertEquals(true, copticUTC.millis().isPrecise());
  }

  public void testDurationFields_IsPrecise_Gmt() {
    final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
    final CopticChronology copticGMT = CopticChronology.getInstance(gmt);
    assertEquals(false, copticGMT.centuries().isPrecise());
    assertEquals(false, copticGMT.years().isPrecise());
    assertEquals(false, copticGMT.weekyears().isPrecise());
    assertEquals(false, copticGMT.months().isPrecise());
    assertEquals(true, copticGMT.weeks().isPrecise());
    assertEquals(true, copticGMT.days().isPrecise());
    assertEquals(true, copticGMT.halfdays().isPrecise());
    assertEquals(true, copticGMT.hours().isPrecise());
    assertEquals(true, copticGMT.minutes().isPrecise());
    assertEquals(true, copticGMT.seconds().isPrecise());
    assertEquals(true, copticGMT.millis().isPrecise());
  }

  public void testDateFields_Names() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals("era", coptic.era().getName());
    assertEquals("centuryOfEra", coptic.centuryOfEra().getName());
    assertEquals("yearOfCentury", coptic.yearOfCentury().getName());
    assertEquals("yearOfEra", coptic.yearOfEra().getName());
    assertEquals("year", coptic.year().getName());
    assertEquals("monthOfYear", coptic.monthOfYear().getName());
    assertEquals("weekyearOfCentury", coptic.weekyearOfCentury().getName());
    assertEquals("weekyear", coptic.weekyear().getName());
    assertEquals("weekOfWeekyear", coptic.weekOfWeekyear().getName());
    assertEquals("dayOfYear", coptic.dayOfYear().getName());
    assertEquals("dayOfMonth", coptic.dayOfMonth().getName());
    assertEquals("dayOfWeek", coptic.dayOfWeek().getName());
  }

  public void testDateFields_IsSupported() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals(true, coptic.era().isSupported());
    assertEquals(true, coptic.centuryOfEra().isSupported());
    assertEquals(true, coptic.yearOfCentury().isSupported());
    assertEquals(true, coptic.yearOfEra().isSupported());
    assertEquals(true, coptic.year().isSupported());
    assertEquals(true, coptic.monthOfYear().isSupported());
    assertEquals(true, coptic.weekyearOfCentury().isSupported());
    assertEquals(true, coptic.weekyear().isSupported());
    assertEquals(true, coptic.weekOfWeekyear().isSupported());
    assertEquals(true, coptic.dayOfYear().isSupported());
    assertEquals(true, coptic.dayOfMonth().isSupported());
    assertEquals(true, coptic.dayOfWeek().isSupported());
  }

  public void testDateFields_DurationFieldMapping() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals(coptic.eras(), coptic.era().getDurationField());
    assertEquals(coptic.centuries(), coptic.centuryOfEra().getDurationField());
    assertEquals(coptic.years(), coptic.yearOfCentury().getDurationField());
    assertEquals(coptic.years(), coptic.yearOfEra().getDurationField());
    assertEquals(coptic.years(), coptic.year().getDurationField());
    assertEquals(coptic.months(), coptic.monthOfYear().getDurationField());
    assertEquals(coptic.weekyears(), coptic.weekyearOfCentury().getDurationField());
    assertEquals(coptic.weekyears(), coptic.weekyear().getDurationField());
    assertEquals(coptic.weeks(), coptic.weekOfWeekyear().getDurationField());
    assertEquals(coptic.days(), coptic.dayOfYear().getDurationField());
    assertEquals(coptic.days(), coptic.dayOfMonth().getDurationField());
    assertEquals(coptic.days(), coptic.dayOfWeek().getDurationField());
  }

  public void testDateFields_RangeDurationFieldMapping() {
    final CopticChronology coptic = CopticChronology.getInstance();
    assertEquals(null, coptic.era().getRangeDurationField());
    assertEquals(coptic.eras(), coptic.centuryOfEra().getRangeDurationField());
    assertEquals(coptic.centuries(), coptic.yearOfCentury().getRangeDurationField());
    assertEquals(coptic.eras(), coptic.yearOfEra().getRangeDurationField());
    assertEquals(null, coptic.year().getRangeDurationField());
    assertEquals(coptic.years(), coptic.monthOfYear().getRangeDurationField());
    assertEquals(coptic.centuries(), coptic.weekyearOfCentury().getRangeDurationField());
    assertEquals(null, coptic.weekyear().getRangeDurationField());
    assertEquals(coptic.weekyears(), coptic.weekOfWeekyear().getRangeDurationField());
    assertEquals(coptic.years(), coptic.dayOfYear().getRangeDurationField());
    assertEquals(coptic.months(), coptic.dayOfMonth().getRangeDurationField());
    assertEquals(coptic.weeks(), coptic.dayOfWeek().getRangeDurationField());
  }

  public void testSampleDate_BaseFields() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(CopticChronology.AM, dt.getEra());
    assertEquals(18, dt.getCenturyOfEra()); // TODO confirm
    assertEquals(20, dt.getYearOfCentury());
    assertEquals(1720, dt.getYearOfEra());
  }

  public void testSampleDate_YearProperty() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(1720, dt.getYear());
    Property fld = dt.year();
    assertEquals(false, fld.isLeap());
    assertEquals(0, fld.getLeapAmount());
    assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
    assertEquals(new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));
  }

  public void testSampleDate_MonthProperty() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(10, dt.getMonthOfYear());
    Property fld = dt.monthOfYear();
    assertEquals(false, fld.isLeap());
    assertEquals(0, fld.getLeapAmount());
    assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
    assertEquals(1, fld.getMinimumValue());
    assertEquals(1, fld.getMinimumValueOverall());
    assertEquals(13, fld.getMaximumValue());
    assertEquals(13, fld.getMaximumValueOverall());
    assertEquals(new DateTime(1721, 1, 2, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(4));
    assertEquals(new DateTime(1720, 1, 2, 0, 0, 0, 0, COPTIC_UTC), fld.addWrapFieldToCopy(4));
  }

  public void testSampleDate_DayOfMonthProperty() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(2, dt.getDayOfMonth());
    Property fld = dt.dayOfMonth();
    assertEquals(false, fld.isLeap());
    assertEquals(0, fld.getLeapAmount());
    assertEquals(null, fld.getLeapDurationField());
    assertEquals(1, fld.getMinimumValue());
    assertEquals(1, fld.getMinimumValueOverall());
    assertEquals(30, fld.getMaximumValue());
    assertEquals(30, fld.getMaximumValueOverall());
    assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));
  }

  public void testSampleDate_DayOfWeekProperty() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
    Property fld = dt.dayOfWeek();
    assertEquals(false, fld.isLeap());
    assertEquals(0, fld.getLeapAmount());
    assertEquals(null, fld.getLeapDurationField());
    assertEquals(1, fld.getMinimumValue());
    assertEquals(1, fld.getMinimumValueOverall());
    assertEquals(7, fld.getMaximumValue());
    assertEquals(7, fld.getMaximumValueOverall());
    assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));
  }

  public void testSampleDate_DayOfYearProperty() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(9 * 30 + 2, dt.getDayOfYear());
    Property fld = dt.dayOfYear();
    assertEquals(false, fld.isLeap());
    assertEquals(0, fld.getLeapAmount());
    assertEquals(null, fld.getLeapDurationField());
    assertEquals(1, fld.getMinimumValue());
    assertEquals(1, fld.getMinimumValueOverall());
    assertEquals(365, fld.getMaximumValue());
    assertEquals(366, fld.getMaximumValueOverall());
    assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));
  }

  public void testSampleDate_TimeFields() {
    DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
    assertEquals(0, dt.getHourOfDay());
    assertEquals(0, dt.getMinuteOfHour());
    assertEquals(0, dt.getSecondOfMinute());
    assertEquals(0, dt.getMillisOfSecond());
  }

  public void testSampleDateWithZone() {
    DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(COPTIC_UTC);
    assertEquals(CopticChronology.AM, dt.getEra());
    assertEquals(1720, dt.getYear());
    assertEquals(1720, dt.getYearOfEra());
    assertEquals(10, dt.getMonthOfYear());
    assertEquals(2, dt.getDayOfMonth());
    assertEquals(10, dt.getHourOfDay()); // PARIS is UTC+2 in summer (12-2=10)
    assertEquals(0, dt.getMinuteOfHour());
    assertEquals(0, dt.getSecondOfMinute());
    assertEquals(0, dt.getMillisOfSecond());
  }

  public void testDurationYear_GetMillis_Int() {
    // Leap 1723
    DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt20.year().getDurationField();
    assertEquals(COPTIC_UTC.years(), fld);
    assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1, dt20.getMillis()));
    assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2, dt20.getMillis()));
    assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3, dt20.getMillis()));
    assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4, dt20.getMillis()));

    assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1));
    assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2));
  }

  public void testDurationYear_GetMillis_Long() {
    DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt20.year().getDurationField();
    assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1L, dt20.getMillis()));
    assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2L, dt20.getMillis()));
    assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3L, dt20.getMillis()));
    assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4L, dt20.getMillis()));

    assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1L));
    assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2L));

    assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getUnitMillis());
  }

  public void testDurationYear_GetValue() {
    DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt20.year().getDurationField();
    assertEquals(0, fld.getValue(1L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
    assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
    assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
    assertEquals(1, fld.getValue(2L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
    assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
    assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
    assertEquals(2, fld.getValue(3L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
    assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
    assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
    assertEquals(3, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY - 1L, dt20.getMillis()));
    assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY, dt20.getMillis()));
    assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY + 1L, dt20.getMillis()));
  }

  public void testDurationYear_Add() {
    DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt21 = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt22 = new DateTime(1722, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt23 = new DateTime(1723, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt24 = new DateTime(1724, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt20.year().getDurationField();
    assertEquals(dt21.getMillis(), fld.add(dt20.getMillis(), 1));
    assertEquals(dt22.getMillis(), fld.add(dt20.getMillis(), 2));
    assertEquals(dt23.getMillis(), fld.add(dt20.getMillis(), 3));
    assertEquals(dt24.getMillis(), fld.add(dt20.getMillis(), 4));

    assertEquals(dt21.getMillis(), fld.add(dt20.getMillis(), 1L));
    assertEquals(dt22.getMillis(), fld.add(dt20.getMillis(), 2L));
    assertEquals(dt23.getMillis(), fld.add(dt20.getMillis(), 3L));
    assertEquals(dt24.getMillis(), fld.add(dt20.getMillis(), 4L));
  }

  public void testDurationMonth_GetMillis_Int() {
    // Leap 1723
    DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt11.monthOfYear().getDurationField();
    assertEquals(COPTIC_UTC.months(), fld);
    assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1, dt11.getMillis()));
    assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2, dt11.getMillis()));
    assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3, dt11.getMillis()));
    assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4, dt11.getMillis()));

    assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1));
    assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2));
    assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13));
  }

  public void testDurationMonth_GetMillis_Long() {
    DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt11.monthOfYear().getDurationField();
    assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L, dt11.getMillis()));
    assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L, dt11.getMillis()));
    assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3L, dt11.getMillis()));
    assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4L, dt11.getMillis()));

    assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L));
    assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L));
    assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13L));
  }

  public void testDurationMonth_GetValue() {
    DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt11.monthOfYear().getDurationField();
    assertEquals(0, fld.getValue(1L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
    assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
    assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
    assertEquals(1, fld.getValue(2L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
    assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
    assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
    assertEquals(2, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
    assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
    assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
    assertEquals(3, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
    assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
    assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
  }

  public void testDurationMonth_Add() {
    DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt12 = new DateTime(1723, 12, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt13 = new DateTime(1723, 13, 2, 0, 0, 0, 0, COPTIC_UTC);
    DateTime dt01 = new DateTime(1724, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
    DurationField fld = dt11.monthOfYear().getDurationField();
    assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1));
    assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2));
    assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3));

    assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1L));
    assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2L));
    assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3L));
  }

  public void testLeap_5_13() {
    Chronology chrono = CopticChronology.getInstance();
    DateTime dt = new DateTime(3, 13, 5, 0, 0, chrono);
    assertEquals(true, dt.year().isLeap());
    assertEquals(true, dt.monthOfYear().isLeap());
    assertEquals(false, dt.dayOfMonth().isLeap());
    assertEquals(false, dt.dayOfYear().isLeap());
  }

  public void testLeap_6_13() {
    Chronology chrono = CopticChronology.getInstance();
    DateTime dt = new DateTime(3, 13, 6, 0, 0, chrono);
    assertEquals(true, dt.year().isLeap());
    assertEquals(true, dt.monthOfYear().isLeap());
    assertEquals(true, dt.dayOfMonth().isLeap());
    assertEquals(true, dt.dayOfYear().isLeap());
  }

}
