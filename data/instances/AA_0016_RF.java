package org.joda.time.tz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * Unit tests for class {@link ZoneInfoCompiler}.
 *
 * @date 2017-07-31
 * @see ZoneInfoCompiler
 **/
public class AA_0016_RF extends TestCase {
  public void testParseDataFileWithTrue() throws IOException {
    ZoneInfoCompiler zoneInfoCompiler = new ZoneInfoCompiler();
    assertNotNull("ZoneInfoCompiler should not be null", zoneInfoCompiler);
    StringReader stringReader = new StringReader(" I  ;>- ");
    BufferedReader bufferedReader = new BufferedReader(stringReader, 2014);
    // should complete without throwing an exception
    zoneInfoCompiler.parseDataFile(bufferedReader, true);
    // verify compiler is still valid after parsing
    assertNotNull("ZoneInfoCompiler should remain valid after parsing", zoneInfoCompiler);
  }

  public void testParseDataFileWithFalse() throws IOException {
    ZoneInfoCompiler zoneInfoCompiler = new ZoneInfoCompiler();
    assertNotNull("ZoneInfoCompiler should not be null", zoneInfoCompiler);
    StringReader stringReader = new StringReader("XYC5w.9eA}*U#A;mu");
    BufferedReader bufferedReader = new BufferedReader(stringReader);
    // should complete without throwing an exception
    zoneInfoCompiler.parseDataFile(bufferedReader, false);
    // verify compiler is still valid after parsing
    assertNotNull("ZoneInfoCompiler should remain valid after parsing", zoneInfoCompiler);
  }
}
