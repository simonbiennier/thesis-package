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

public class AA_0021_OR extends TestCase {
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
    return new TestSuite(AA_0021_OR.class);
  }

  public AA_0021_OR(String name) {
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

  public void testForStyle_shortDate() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("S-");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateInstance(DateFormat.SHORT, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateInstance(DateFormat.SHORT, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateInstance(DateFormat.SHORT, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_shortTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("-S");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getTimeInstance(DateFormat.SHORT, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.SHORT, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.SHORT, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_shortDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("SS");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_mediumDate() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("M-");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateInstance(DateFormat.MEDIUM, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateInstance(DateFormat.MEDIUM, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateInstance(DateFormat.MEDIUM, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_mediumTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("-M");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getTimeInstance(DateFormat.MEDIUM, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.MEDIUM, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.MEDIUM, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_mediumDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("MM");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_longDate() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("L-");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateInstance(DateFormat.LONG, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateInstance(DateFormat.LONG, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateInstance(DateFormat.LONG, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_longTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("-L");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getTimeInstance(DateFormat.LONG, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.LONG, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.LONG, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_longDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("LL");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_fullDate() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("F-");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateInstance(DateFormat.FULL, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateInstance(DateFormat.FULL, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateInstance(DateFormat.FULL, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_fullTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("-F");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getTimeInstance(DateFormat.FULL, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.FULL, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getTimeInstance(DateFormat.FULL, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_fullDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("FF");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_shortMediumDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("SM");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_shortLongDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("SL");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_shortFullDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("SF");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_mediumShortDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("MS");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_mediumLongDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("ML");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }

  public void testForStyle_mediumFullDateTime() throws Exception {
    DateTimeFormatter f = DateTimeFormat.forStyle("MF");
    DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 0);
    String expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL, UK).format(dt.toDate());
    assertEquals(expect, f.print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL, US).format(dt.toDate());
    assertEquals(expect, f.withLocale(US).print(dt));
    expect = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL, FRANCE).format(dt.toDate());
    assertEquals(expect, f.withLocale(FRANCE).print(dt));
  }
}
