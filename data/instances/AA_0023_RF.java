package org.joda.time.format;

import java.util.Locale;

import org.joda.time.Period;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AA_0023_RF extends TestCase {
  private static final Locale DE = new Locale("de");
  private static final Locale PL = new Locale("pl");
  private static final Locale RU = new Locale("ru");
  private Locale originalLocale = null;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static TestSuite suite() {
    return new TestSuite(AA_0023_RF.class);
  }

  public AA_0023_RF(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    originalLocale = Locale.getDefault();
    Locale.setDefault(DE);
  }

  @Override
  protected void tearDown() throws Exception {
    Locale.setDefault(originalLocale);
    originalLocale = null;
  }

  private static interface PeriodFactory {
    Period create(int value);
  }

  private void assertWordForms(PeriodFormatter pf, PeriodFactory factory, Object[][] wordForms) {
    for (int i = 0; i < wordForms.length; i++) {
      String expectedText = (String) wordForms[i][0];
      Integer value = (Integer) wordForms[i][1];
      assertEquals(expectedText, pf.print(factory.create(value.intValue())));
    }
  }

  public void test_wordBased_pl_regEx_years() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 rok", 1 },
        { "2 lata", 2 },
        { "5 lat", 5 },
        { "12 lat", 12 },
        { "15 lat", 15 },
        { "1112 lat", 1112 },
        { "1115 lat", 1115 },
        { "2112 lat", 2112 },
        { "2115 lat", 2115 },
        { "2212 lat", 2212 },
        { "2215 lat", 2215 },
        { "22 lata", 22 },
        { "25 lat", 25 },
        { "1122 lata", 1122 },
        { "1125 lat", 1125 },
        { "2122 lata", 2122 },
        { "2125 lat", 2125 },
        { "2222 lata", 2222 },
        { "2225 lat", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.years(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_months() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 miesi\u0105c", 1 },
        { "2 miesi\u0105ce", 2 },
        { "5 miesi\u0119cy", 5 },
        { "12 miesi\u0119cy", 12 },
        { "15 miesi\u0119cy", 15 },
        { "1112 miesi\u0119cy", 1112 },
        { "1115 miesi\u0119cy", 1115 },
        { "2112 miesi\u0119cy", 2112 },
        { "2115 miesi\u0119cy", 2115 },
        { "2212 miesi\u0119cy", 2212 },
        { "2215 miesi\u0119cy", 2215 },
        { "22 miesi\u0105ce", 22 },
        { "25 miesi\u0119cy", 25 },
        { "1122 miesi\u0105ce", 1122 },
        { "1125 miesi\u0119cy", 1125 },
        { "2122 miesi\u0105ce", 2122 },
        { "2125 miesi\u0119cy", 2125 },
        { "2222 miesi\u0105ce", 2222 },
        { "2225 miesi\u0119cy", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.months(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_weeks() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 tydzie\u0144", 1 },
        { "2 tygodnie", 2 },
        { "5 tygodni", 5 },
        { "12 tygodni", 12 },
        { "15 tygodni", 15 },
        { "1112 tygodni", 1112 },
        { "1115 tygodni", 1115 },
        { "2112 tygodni", 2112 },
        { "2115 tygodni", 2115 },
        { "2212 tygodni", 2212 },
        { "2215 tygodni", 2215 },
        { "22 tygodnie", 22 },
        { "25 tygodni", 25 },
        { "1122 tygodnie", 1122 },
        { "1125 tygodni", 1125 },
        { "2122 tygodnie", 2122 },
        { "2125 tygodni", 2125 },
        { "2222 tygodnie", 2222 },
        { "2225 tygodni", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.weeks(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_days() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 dzie\u0144", 1 },
        { "2 dni", 2 },
        { "5 dni", 5 },
        { "12 dni", 12 },
        { "15 dni", 15 },
        { "22 dni", 22 },
        { "25 dni", 25 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.days(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_hours() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 godzina", 1 },
        { "2 godziny", 2 },
        { "5 godzin", 5 },
        { "12 godzin", 12 },
        { "15 godzin", 15 },
        { "1112 godzin", 1112 },
        { "1115 godzin", 1115 },
        { "2112 godzin", 2112 },
        { "2115 godzin", 2115 },
        { "2212 godzin", 2212 },
        { "2215 godzin", 2215 },
        { "22 godziny", 22 },
        { "25 godzin", 25 },
        { "1122 godziny", 1122 },
        { "1125 godzin", 1125 },
        { "2122 godziny", 2122 },
        { "2125 godzin", 2125 },
        { "2222 godziny", 2222 },
        { "2225 godzin", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.hours(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_minutes() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 minuta", 1 },
        { "2 minuty", 2 },
        { "5 minut", 5 },
        { "12 minut", 12 },
        { "15 minut", 15 },
        { "1112 minut", 1112 },
        { "1115 minut", 1115 },
        { "2112 minut", 2112 },
        { "2115 minut", 2115 },
        { "2212 minut", 2212 },
        { "2215 minut", 2215 },
        { "22 minuty", 22 },
        { "25 minut", 25 },
        { "1122 minuty", 1122 },
        { "1125 minut", 1125 },
        { "2122 minuty", 2122 },
        { "2125 minut", 2125 },
        { "2222 minuty", 2222 },
        { "2225 minut", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.minutes(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_seconds() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 sekunda", 1 },
        { "2 sekundy", 2 },
        { "5 sekund", 5 },
        { "12 sekund", 12 },
        { "15 sekund", 15 },
        { "1112 sekund", 1112 },
        { "1115 sekund", 1115 },
        { "2112 sekund", 2112 },
        { "2115 sekund", 2115 },
        { "2212 sekund", 2212 },
        { "2215 sekund", 2215 },
        { "22 sekundy", 22 },
        { "25 sekund", 25 },
        { "1122 sekundy", 1122 },
        { "1125 sekund", 1125 },
        { "2122 sekundy", 2122 },
        { "2125 sekund", 2125 },
        { "2222 sekundy", 2222 },
        { "2225 sekund", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.seconds(value);
      }
    }, wordForms);
  }

  public void test_wordBased_pl_regEx_millis() {
    PeriodFormatter pf = PeriodFormat.wordBased(PL);
    Object[][] wordForms = new Object[][] {
        { "1 milisekunda", 1 },
        { "2 milisekundy", 2 },
        { "5 milisekund", 5 },
        { "12 milisekund", 12 },
        { "15 milisekund", 15 },
        { "1112 milisekund", 1112 },
        { "1115 milisekund", 1115 },
        { "2112 milisekund", 2112 },
        { "2115 milisekund", 2115 },
        { "2212 milisekund", 2212 },
        { "2215 milisekund", 2215 },
        { "22 milisekundy", 22 },
        { "25 milisekund", 25 },
        { "1122 milisekundy", 1122 },
        { "1125 milisekund", 1125 },
        { "2122 milisekundy", 2122 },
        { "2125 milisekund", 2125 },
        { "2222 milisekundy", 2222 },
        { "2225 milisekund", 2225 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.millis(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_years() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 год", 1 },
        { "11 лет", 11 },
        { "21 год", 21 },
        { "101 год", 101 },
        { "111 лет", 111 },
        { "121 год", 121 },
        { "2001 год", 2001 },
        { "2 года", 2 },
        { "3 года", 3 },
        { "4 года", 4 },
        { "12 лет", 12 },
        { "13 лет", 13 },
        { "14 лет", 14 },
        { "22 года", 22 },
        { "23 года", 23 },
        { "24 года", 24 },
        { "102 года", 102 },
        { "112 лет", 112 },
        { "124 года", 124 },
        { "5 лет", 5 },
        { "15 лет", 15 },
        { "25 лет", 25 },
        { "105 лет", 105 },
        { "1005 лет", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.years(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_months() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 месяц", 1 },
        { "11 месяцев", 11 },
        { "21 месяц", 21 },
        { "101 месяц", 101 },
        { "111 месяцев", 111 },
        { "121 месяц", 121 },
        { "2001 месяц", 2001 },
        { "2 месяца", 2 },
        { "3 месяца", 3 },
        { "4 месяца", 4 },
        { "12 месяцев", 12 },
        { "13 месяцев", 13 },
        { "14 месяцев", 14 },
        { "22 месяца", 22 },
        { "23 месяца", 23 },
        { "24 месяца", 24 },
        { "102 месяца", 102 },
        { "112 месяцев", 112 },
        { "124 месяца", 124 },
        { "5 месяцев", 5 },
        { "15 месяцев", 15 },
        { "25 месяцев", 25 },
        { "105 месяцев", 105 },
        { "1005 месяцев", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.months(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_weeks() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 неделя", 1 },
        { "11 недель", 11 },
        { "21 неделя", 21 },
        { "101 неделя", 101 },
        { "111 недель", 111 },
        { "121 неделя", 121 },
        { "2001 неделя", 2001 },
        { "2 недели", 2 },
        { "3 недели", 3 },
        { "4 недели", 4 },
        { "12 недель", 12 },
        { "13 недель", 13 },
        { "14 недель", 14 },
        { "22 недели", 22 },
        { "23 недели", 23 },
        { "24 недели", 24 },
        { "102 недели", 102 },
        { "112 недель", 112 },
        { "124 недели", 124 },
        { "5 недель", 5 },
        { "15 недель", 15 },
        { "25 недель", 25 },
        { "105 недель", 105 },
        { "1005 недель", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.weeks(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_days() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 день", 1 },
        { "11 дней", 11 },
        { "21 день", 21 },
        { "101 день", 101 },
        { "111 дней", 111 },
        { "121 день", 121 },
        { "2001 день", 2001 },
        { "2 дня", 2 },
        { "3 дня", 3 },
        { "4 дня", 4 },
        { "12 дней", 12 },
        { "13 дней", 13 },
        { "14 дней", 14 },
        { "22 дня", 22 },
        { "23 дня", 23 },
        { "24 дня", 24 },
        { "102 дня", 102 },
        { "112 дней", 112 },
        { "124 дня", 124 },
        { "5 дней", 5 },
        { "15 дней", 15 },
        { "25 дней", 25 },
        { "105 дней", 105 },
        { "1005 дней", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.days(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_hours() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 час", 1 },
        { "11 часов", 11 },
        { "21 час", 21 },
        { "101 час", 101 },
        { "111 часов", 111 },
        { "121 час", 121 },
        { "2001 час", 2001 },
        { "2 часа", 2 },
        { "3 часа", 3 },
        { "4 часа", 4 },
        { "12 часов", 12 },
        { "13 часов", 13 },
        { "14 часов", 14 },
        { "22 часа", 22 },
        { "23 часа", 23 },
        { "24 часа", 24 },
        { "102 часа", 102 },
        { "112 часов", 112 },
        { "124 часа", 124 },
        { "5 часов", 5 },
        { "15 часов", 15 },
        { "25 часов", 25 },
        { "105 часов", 105 },
        { "1005 часов", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.hours(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_minutes() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 минута", 1 },
        { "11 минут", 11 },
        { "21 минута", 21 },
        { "101 минута", 101 },
        { "111 минут", 111 },
        { "121 минута", 121 },
        { "2001 минута", 2001 },
        { "2 минуты", 2 },
        { "3 минуты", 3 },
        { "4 минуты", 4 },
        { "12 минут", 12 },
        { "13 минут", 13 },
        { "14 минут", 14 },
        { "22 минуты", 22 },
        { "23 минуты", 23 },
        { "24 минуты", 24 },
        { "102 минуты", 102 },
        { "112 минут", 112 },
        { "124 минуты", 124 },
        { "5 минут", 5 },
        { "15 минут", 15 },
        { "25 минут", 25 },
        { "105 минут", 105 },
        { "1005 минут", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.minutes(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_seconds() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 секунда", 1 },
        { "11 секунд", 11 },
        { "21 секунда", 21 },
        { "101 секунда", 101 },
        { "111 секунд", 111 },
        { "121 секунда", 121 },
        { "2001 секунда", 2001 },
        { "2 секунды", 2 },
        { "3 секунды", 3 },
        { "4 секунды", 4 },
        { "12 секунд", 12 },
        { "13 секунд", 13 },
        { "14 секунд", 14 },
        { "22 секунды", 22 },
        { "23 секунды", 23 },
        { "24 секунды", 24 },
        { "102 секунды", 102 },
        { "112 секунд", 112 },
        { "124 секунды", 124 },
        { "5 секунд", 5 },
        { "15 секунд", 15 },
        { "25 секунд", 25 },
        { "105 секунд", 105 },
        { "1005 секунд", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.seconds(value);
      }
    }, wordForms);
  }

  public void test_wordBased_ru_regEx_millis() {
    PeriodFormatter pf = PeriodFormat.wordBased(RU);
    Object[][] wordForms = new Object[][] {
        { "1 миллисекунда", 1 },
        { "11 миллисекунд", 11 },
        { "21 миллисекунда", 21 },
        { "101 миллисекунда", 101 },
        { "111 миллисекунд", 111 },
        { "121 миллисекунда", 121 },
        { "2001 миллисекунда", 2001 },
        { "2 миллисекунды", 2 },
        { "3 миллисекунды", 3 },
        { "4 миллисекунды", 4 },
        { "12 миллисекунд", 12 },
        { "13 миллисекунд", 13 },
        { "14 миллисекунд", 14 },
        { "22 миллисекунды", 22 },
        { "23 миллисекунды", 23 },
        { "24 миллисекунды", 24 },
        { "102 миллисекунды", 102 },
        { "112 миллисекунд", 112 },
        { "124 миллисекунды", 124 },
        { "5 миллисекунд", 5 },
        { "15 миллисекунд", 15 },
        { "25 миллисекунд", 25 },
        { "105 миллисекунд", 105 },
        { "1005 миллисекунд", 1005 }
    };
    assertWordForms(pf, new PeriodFactory() {
      public Period create(int value) {
        return Period.millis(value);
      }
    }, wordForms);
  }
}
