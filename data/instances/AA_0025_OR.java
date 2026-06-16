package org.apache.commons.math4.legacy.analysis.solvers;

import org.apache.commons.math4.legacy.analysis.QuinticFunction;
import org.apache.commons.math4.legacy.analysis.UnivariateFunction;
import org.apache.commons.math4.legacy.analysis.function.Sin;
import org.apache.commons.math4.legacy.exception.MathIllegalArgumentException;
import org.apache.commons.math4.legacy.exception.NoBracketingException;
import org.apache.commons.math4.legacy.exception.NullArgumentException;
import org.apache.commons.math4.core.jdkmath.JdkMath;
import org.junit.Assert;
import org.junit.Test;

public class AA_0025_OR {
    protected UnivariateFunction sin = new Sin();

    @Test(expected=NullArgumentException.class)
    public void testSolveNull() {
        double x = UnivariateSolverUtils.solve(null, 0.0, 4.0);
        System.out.println("x=" + x);
    }

    @Test(expected=MathIllegalArgumentException.class)
    public void testSolveBadEndpoints() {
      double x = UnivariateSolverUtils.solve(sin, 4.0, -0.1, 1e-6);
      System.out.println("x=" + x);
    }

    @Test
    public void testSolveSin() {
        double x = UnivariateSolverUtils.solve(sin, 1.0, 4.0);
        System.out.println("x=" + x);
        Assert.assertEquals(JdkMath.PI, x, 1.0e-4);
    }

    @Test(expected=NullArgumentException.class)
    public void testSolveAccuracyNull()  {
        double accuracy = 1.0e-6;
        double x = UnivariateSolverUtils.solve(null, 0.0, 4.0, accuracy);
        System.out.println("x=" + x);
    }

    @Test
    public void testSolveAccuracySin() {
        double accuracy = 1.0e-6;
        double x = UnivariateSolverUtils.solve(sin, 1.0,
                4.0, accuracy);
        System.out.println("x=" + x);
        Assert.assertEquals(JdkMath.PI, x, accuracy);
    }

    @Test(expected=MathIllegalArgumentException.class)
    public void testSolveNoRoot() {
        double x = UnivariateSolverUtils.solve(sin, 1.0, 1.5);
        System.out.println("x=" + x);
    }

    @Test
    public void testBracketSin() {
        double[] result = UnivariateSolverUtils.bracket(sin,
                0.0, -2.0, 2.0);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
        Assert.assertTrue(sin.value(result[0]) < 0);
        Assert.assertTrue(sin.value(result[1]) > 0);
    }

    @Test
    public void testBracketCentered() {
        double initial = 0.1;
        double[] result = UnivariateSolverUtils.bracket(sin, initial, -2.0, 2.0, 0.2, 1.0, 100);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
        Assert.assertTrue(result[0] < initial);
        Assert.assertTrue(result[1] > initial);
        Assert.assertTrue(sin.value(result[0]) < 0);
        Assert.assertTrue(sin.value(result[1]) > 0);
    }

    @Test
    public void testBracketLow() {
        double initial = 0.5;
        double[] result = UnivariateSolverUtils.bracket(sin, initial, -2.0, 2.0, 0.2, 1.0, 100);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
        Assert.assertTrue(result[0] < initial);
        Assert.assertTrue(result[1] < initial);
        Assert.assertTrue(sin.value(result[0]) < 0);
        Assert.assertTrue(sin.value(result[1]) > 0);
    }

    @Test
    public void testBracketHigh(){
        double initial = -0.5;
        double[] result = UnivariateSolverUtils.bracket(sin, initial, -2.0, 2.0, 0.2, 1.0, 100);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
        Assert.assertTrue(result[0] > initial);
        Assert.assertTrue(result[1] > initial);
        Assert.assertTrue(sin.value(result[0]) < 0);
        Assert.assertTrue(sin.value(result[1]) > 0);
    }

    @Test
    public void testBracketEndpointRoot() {
        double[] result = UnivariateSolverUtils.bracket(sin, 1.5, 0, 2.0);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
        Assert.assertEquals(0.0, sin.value(result[0]), 1.0e-15);
        Assert.assertTrue(sin.value(result[1]) > 0);
    }

    @Test(expected=NullArgumentException.class)
    public void testNullFunction() {
        double[] result = UnivariateSolverUtils.bracket(null, 1.5, 0, 2.0);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
    }

    @Test(expected=MathIllegalArgumentException.class)
    public void testBadInitial() {
        double[] result = UnivariateSolverUtils.bracket(sin, 2.5, 0, 2.0);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
    }

    @Test(expected=MathIllegalArgumentException.class)
    public void testBadAdditive() {
        double[] result = UnivariateSolverUtils.bracket(sin, 1.0, -2.0, 3.0, -1.0, 1.0, 100);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
    }

    @Test(expected=NoBracketingException.class)
    public void testIterationExceeded() {
        double[] result = UnivariateSolverUtils.bracket(sin, 1.0, -2.0, 3.0, 1.0e-5, 1.0, 100);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
    }

    /** check the search continues when a = lowerBound and b < upperBound. */
    @Test
    public void testBracketLoopConditionForB() {
        double[] result = UnivariateSolverUtils.bracket(sin, -0.9, -1, 1, 0.1, 1, 100);
        System.out.println("result[0]=" + result[0] + ", result[1]=" + result[1]);
        Assert.assertTrue(result[0] <= 0);
        Assert.assertTrue(result[1] >= 0);
    }

    @Test
    public void testMisc() {
        UnivariateFunction f = new QuinticFunction();
        double result;
        // Static solve method
        result = UnivariateSolverUtils.solve(f, -0.2, 0.2);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 0, 1E-8);
        result = UnivariateSolverUtils.solve(f, -0.1, 0.3);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 0, 1E-8);
        result = UnivariateSolverUtils.solve(f, -0.3, 0.45);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 0, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.3, 0.7);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 0.5, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.2, 0.6);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 0.5, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.05, 0.95);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 0.5, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.85, 1.25);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 1.0, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.8, 1.2);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 1.0, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.85, 1.75);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 1.0, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.55, 1.45);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 1.0, 1E-6);
        result = UnivariateSolverUtils.solve(f, 0.85, 5);
        System.out.println("x=" + result);
        Assert.assertEquals(result, 1.0, 1E-6);
    }
}
