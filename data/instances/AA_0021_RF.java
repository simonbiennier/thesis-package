package org.joda.time.format;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

public class AA_0021_RF extends TestCase {
  private static final Locale UK = Locale.UK;
  private static final Locale US = Locale.US;
  private static final Locale FRANCE = Locale.FRANCE;
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
    return new TestSuite(AA_0021_RF.class);
  }

  public AA_0021_RF(String name) {
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
    Locale.setDefault(UK);
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

  public void testForStyle_printingByStyle() {
    Object[][] styleCases = new Object[][] {
        { "S-", DateFormat.SHORT, -1 },
        { "-S", -1, DateFormat.SHORT },
        { "SS", DateFormat.SHORT, DateFormat.SHORT },
        { "M-", DateFormat.MEDIUM, -1 },
        { "-M", -1, DateFormat.MEDIUM },
        { "MM", DateFormat.MEDIUM, DateFormat.MEDIUM },
        { "L-", DateFormat.LONG, -1 },
        { "-L", -1, DateFormat.LONG },
        { "LL", DateFormat.LONG, DateFormat.LONG },
        { "F-", DateFormat.FULL, -1 },
        { "-F", -1, DateFormat.FULL },
        { "FF", DateFormat.FULL, DateFormat.FULL },
        { "SM", DateFormat.SHORT, DateFormat.MEDIUM },
        { "SL", DateFormat.SHORT, DateFormat.LONG },
        { "SF", DateFormat.SHORT, DateFormat.FULL },
        { "MS", DateFormat.MEDIUM, DateFormat.SHORT },
        { "ML", DateFormat.MEDIUM, DateFormat.LONG },
        { "MF", DateFormat.MEDIUM, DateFormat.FULL }
    };
    for (int i = 0; i < styleCases.length; i++) {
      String style = (String) styleCases[i][0];
      int dateStyle = (int) styleCases[i][1];
      int timeStyle = (int) styleCases[i][2];
      DateTimeFormatter f = DateTimeFormat.forStyle(style);
      DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
      String expect = createDateFormat(dateStyle, timeStyle, UK).format(dt.toDate());
      assertEquals(expect, f.print(dt));
      expect = createDateFormat(dateStyle, timeStyle, US).format(dt.toDate());
      assertEquals(expect, f.withLocale(US).print(dt));
      expect = createDateFormat(dateStyle, timeStyle, FRANCE).format(dt.toDate());
      assertEquals(expect, f.withLocale(FRANCE).print(dt));
    }
  }

  private DateFormat createDateFormat(int dateStyle, int timeStyle, Locale locale) {
    if (dateStyle >= 0 && timeStyle >= 0) {
      return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    }
    if (dateStyle >= 0) {
      return DateFormat.getDateInstance(dateStyle, locale);
    }
    return DateFormat.getTimeInstance(timeStyle, locale);
  }
}
