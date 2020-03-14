package de.dennisfisch.meteoparser.util;

import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openimaj.image.FImage;
import org.openimaj.image.pixel.Pixel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageNavigator {

  public static Optional<Pixel> nearestLine(final Pixel fromPixel, final FImage image, final UnaryOperator<Pixel> transformFunction) {
    final Pixel movablePixel = fromPixel.clone();

    while (movablePixel.y >= 0
        && movablePixel.x >= 0
        && movablePixel.y < image.getHeight()
        && movablePixel.x < image.getWidth()) {
      final Float pixelValue = image.getPixel(movablePixel);
      if (pixelValue == 0f) {
        return Optional.of(movablePixel);
      }

      transformFunction.apply(movablePixel);
    }

    return Optional.empty();
  }

  public static Pixel upwards(final Pixel pixel) {
    pixel.y--;
    return pixel;
  }

  public static Pixel downwards(final Pixel pixel) {
    pixel.y++;
    return pixel;
  }

  public static Pixel left(final Pixel pixel) {
    pixel.x--;
    return pixel;
  }

  public static Pixel right(final Pixel pixel) {
    pixel.x++;
    return pixel;
  }
}