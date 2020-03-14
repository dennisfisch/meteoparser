package de.dennisfisch.meteoparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RangeMapper {

  private static final double EPSILON = 1e-12;

  public static double mapRange(final double valueCoord1,
      final double startCoord1, final double endCoord1,
      final double startCoord2, final double endCoord2) {

    if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
      throw new ArithmeticException("/ 0");
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    final double offset = startCoord2;

    final double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
    return ratio * (valueCoord1 - startCoord1) + offset;
  }
}