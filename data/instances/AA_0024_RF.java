package org.apache.commons.math4.legacy.stat.correlation;

import org.junit.Assert;
import org.junit.Test;

public class AA_0024_RF {
  @Test
  public void testConstant() {
    double[] noVariance = new double[] { 1, 1, 1, 1 };
    double[] values = new double[] { 1, 2, 3, 4 };
    Assert.assertTrue(Double.isNaN(new SpearmansCorrelation().correlation(noVariance, values)));
  }
}
