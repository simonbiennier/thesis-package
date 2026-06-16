package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;

enum Enum64_RF {
  A00, A01, A02, A03, A04, A05, A06, A07, A08, A09, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22,
  A23, A24, A25, A26, A27, A28, A29, A30, A31, A32, A33, A34, A35, A36, A37, A38, A39, A40, A41, A42, A43, A44, A45,
  A46, A47, A48, A49, A50, A51, A52, A53, A54, A55, A56, A57, A58, A59, A60, A61, A62, A63
}

class AA_0008_RF {
  @Test
  void testConstructable() {
    // enforce public constructor
    new EnumUtils();
  }
}

enum Month_RF {
  JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

  private final int id;

  Month_RF(final int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }
}

enum TooMany_RF {
  A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1,
  J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, X1, Y1, Z1, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2,
  M2
}

enum Traffic_RF {
  RED, AMBER, GREEN
}

enum Traffic2_RF {

  RED("***Red***", 1), AMBER("**Amber**", 2), GREEN("*green*", 3);

  final String label;
  final int value;

  Traffic2_RF(final String label, final int value) {
    this.label = label;
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public int getValue() {
    return value;
  }
}
