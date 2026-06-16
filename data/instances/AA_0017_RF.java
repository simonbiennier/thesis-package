package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

public class AA_0017_RF extends TestCase {
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

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0017_RF.class);
  }

  public AA_0017_RF(String name) {
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

  public void testFormat_printingByPattern() {
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
    DateTimeZone[] zonesUtcNewYorkParis = new DateTimeZone[] { UTC, NEWYORK, PARIS };
    DateTimeZone[] zonesUtcNewYorkTokyo = new DateTimeZone[] { UTC, NEWYORK, TOKYO };
    Object[][] formatCases = new Object[][] {
        { "G", Locale.UK, zonesUtcNewYorkParis, new String[] { "AD", "AD", "AD" } },
        { "C", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "20", "20", "20" } },
        { "Y", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "2004", "2004", "2004" } },
        { "y", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "2004", "2004", "2004" } },
        { "x", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "2004", "2004", "2004" } },
        { "w", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "24", "24", "24" } },
        { "e", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "3", "3", "3" } },
        { "E", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "Wed", "Wed", "Wed" } },
        { "EEEE", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "Wednesday", "Wednesday", "Wednesday" } },
        { "D", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "161", "161", "161" } },
        { "M", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "6", "6", "6" } },
        { "MMM", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "Jun", "Jun", "Jun" } },
        { "MMMM", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "June", "June", "June" } },
        { "d", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "9", "9", "9" } },
        { "K", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "10", "6", "7" } },
        { "h", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "10", "6", "7" } },
        { "H", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "10", "6", "19" } },
        { "k", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "10", "6", "19" } },
        { "m", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "20", "20", "20" } },
        { "s", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "30", "30", "30" } },
        { "SSS", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "040", "040", "040" } },
        { "SSSSSS", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "040000", "040000", "040000" } },
        { "z", Locale.ENGLISH, zonesUtcNewYorkTokyo, new String[] { "UTC", "EDT", "JST" } },
        { "zzzz", Locale.ENGLISH, zonesUtcNewYorkTokyo,
            new String[] { "Coordinated Universal Time", "Eastern Daylight Time", "Japan Standard Time" } },
        { "Z", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "+0000", "-0400", "+0900" } },
        { "ZZ", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "+00:00", "-04:00", "+09:00" } },
        { "ZZZ", Locale.UK, zonesUtcNewYorkTokyo, new String[] { "UTC", "America/New_York", "Asia/Tokyo" } }
    };
    for (int i = 0; i < formatCases.length; i++) {
      String pattern = (String) formatCases[i][0];
      Locale locale = (Locale) formatCases[i][1];
      DateTimeZone[] zones = (DateTimeZone[]) formatCases[i][2];
      String[] expected = (String[]) formatCases[i][3];
      assertPatternPrintsForZones(dt, pattern, locale, zones, expected);
    }
  }

  private void assertPatternPrintsForZones(DateTime dt, String pattern, Locale locale, DateTimeZone[] zones,
      String[] expected) {
    DateTimeFormatter f = DateTimeFormat.forPattern(pattern).withLocale(locale);
    for (int i = 0; i < zones.length; i++) {
      DateTime zoned = dt.withZone(zones[i]);
      String printed = f.print(zoned);
      assertEquals(zoned.toString(), expected[i], printed);
    }
  }
}
