package de.dennisfisch.meteoparser.service;

import de.dennisfisch.meteoparser.util.ImageNavigator;
import de.dennisfisch.meteoparser.util.RangeMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.openimaj.image.FImage;
import org.openimaj.image.pixel.Pixel;
import org.springframework.stereotype.Component;

@Component
public class SunshineParserService {

  public Map<Integer, BigDecimal> parseImage(final FImage image) {
    final Pixel initialPixel = new Pixel(183, 475);

    final Optional<Pixel> optionalBottomLine = ImageNavigator.nearestLine(initialPixel, image, ImageNavigator::upwards);
    if (!optionalBottomLine.isPresent()) {
      return Collections.emptyMap();
    }
    final Pixel bottomLine = optionalBottomLine.get();

    final Optional<Pixel> optionalSunHoursBottomLine = ImageNavigator.nearestLine(new Pixel(bottomLine.x, bottomLine.y - 1), image,ImageNavigator::upwards);
    if (!optionalSunHoursBottomLine.isPresent()) {
      return Collections.emptyMap();
    }
    final Pixel sunHoursBottomLine = optionalSunHoursBottomLine.get();

    final Optional<Pixel> optionalBottomCornerLeft = ImageNavigator.nearestLine(new Pixel(sunHoursBottomLine.x, sunHoursBottomLine.y - 1), image,ImageNavigator::left);
    if (!optionalBottomCornerLeft.isPresent()) {
      return Collections.emptyMap();
    }
    final Pixel leftBottomCorner = optionalBottomCornerLeft.get();

    return provideSunshineMinutesByHour(image, leftBottomCorner);
  }

  private Map<Integer, BigDecimal> provideSunshineMinutesByHour(final FImage img, final Pixel leftBottomCorner) {
    Pixel currentHourPixel = new Pixel(leftBottomCorner.x + 1, leftBottomCorner.y);
    final Map<Integer, BigDecimal> sunhourMap = new LinkedHashMap<>();

    for (int hour = 0; hour < 24; hour++) {
      final Pixel firstPixelOfHour = currentHourPixel.clone();
      firstPixelOfHour.translate(1, 0);

      final float hourColor = img.getPixel(firstPixelOfHour);
      if (hourColor == 0.654902f) {
        final Optional<Pixel> optionalHourTopPixel = ImageNavigator.nearestLine(firstPixelOfHour, img,ImageNavigator::upwards);
        final BigDecimal minutes = optionalHourTopPixel
            .map(hourTopPixel -> provideSunshineMinutes(firstPixelOfHour.y - hourTopPixel.y))
            .orElse(BigDecimal.ZERO);

        sunhourMap.put(hour, minutes);
      } else {
        sunhourMap.put(hour, BigDecimal.ZERO);
      }

      final Optional<Pixel> optionalNextHour = ImageNavigator.nearestLine(firstPixelOfHour, img,ImageNavigator::right);
      if (!optionalNextHour.isPresent()) {
        break;
      }

      currentHourPixel = optionalNextHour.get();
      currentHourPixel.x++;
    }
    return sunhourMap;
  }

  private BigDecimal provideSunshineMinutes(final int pixelDifference) {
    if (pixelDifference <= 12) {
      return BigDecimal.valueOf(RangeMapper.mapRange(pixelDifference, 0, 11, 0, 20));
    } else if (pixelDifference <= 23) {
      return BigDecimal.valueOf(RangeMapper.mapRange(pixelDifference, 12, 23, 21.81818181818182, 40));
    } else if (pixelDifference <= 34) {
      return BigDecimal.valueOf(RangeMapper.mapRange(pixelDifference, 24, 34, 41.81818181818182, 60));
    } else {
      return BigDecimal.ZERO;
    }
  }
}