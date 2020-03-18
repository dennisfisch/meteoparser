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

  public Map<Integer, BigDecimal> provideSunshineByHourOfDay(final FImage weatherImage, final Pixel leftBottomCorner) {
    Pixel currentHourPixel = new Pixel(leftBottomCorner.x + 1, leftBottomCorner.y);
    final Map<Integer, BigDecimal> sunhourMap = new LinkedHashMap<>();

    for (int hour = 0; hour < 24; hour++) {
      final Pixel firstPixelOfHourBar = currentHourPixel.clone();
      firstPixelOfHourBar.translate(1, 0);

      final float hourColor = weatherImage.getPixel(firstPixelOfHourBar);
      if (hourColor == 0.654902f) {
        final Optional<Pixel> optionalHourBarTopPixel = ImageNavigator.nearestLine(firstPixelOfHourBar, weatherImage,ImageNavigator::upwards);
        final BigDecimal minutes = optionalHourBarTopPixel
            .map(hourBarTopPixel -> calculateMinutesFromBarHeight(firstPixelOfHourBar.y - hourBarTopPixel.y))
            .orElse(BigDecimal.ZERO);

        sunhourMap.put(hour, minutes);
      } else {
        sunhourMap.put(hour, BigDecimal.ZERO);
      }

      final Optional<Pixel> optionalNextHour = ImageNavigator.nearestLine(firstPixelOfHourBar, weatherImage,ImageNavigator::right);
      if (!optionalNextHour.isPresent()) {
        break;
      }

      currentHourPixel = optionalNextHour.get();
      currentHourPixel.x++;
    }
    return sunhourMap;
  }

  private BigDecimal calculateMinutesFromBarHeight(final int barHeight) {
    if (barHeight <= 12) {
      return BigDecimal.valueOf(RangeMapper.mapRange(barHeight, 0, 11, 0, 20));
    } else if (barHeight <= 23) {
      return BigDecimal.valueOf(RangeMapper.mapRange(barHeight, 12, 23, 21.81818181818182, 40));
    } else if (barHeight <= 34) {
      return BigDecimal.valueOf(RangeMapper.mapRange(barHeight, 24, 34, 41.81818181818182, 60));
    } else {
      return BigDecimal.ZERO;
    }
  }
}