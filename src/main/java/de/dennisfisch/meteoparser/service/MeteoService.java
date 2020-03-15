package de.dennisfisch.meteoparser.service;

import de.dennisfisch.meteoparser.model.Forecast;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.pixel.Pixel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeteoService {

  private static final Pixel DAY_1_INITIAL_PIXEL = new Pixel(183, 475);

  private static final float OFFSET_BETWEEN_DAYS = 162f;

  private static final int DAYS_PER_IMAGE = 4;

  private final ImageStoreService imageStoreService;

  private final SunshineParserService sunshineParserService;

  public List<Forecast> provideForecast(final URL sourceUrl) throws IOException {
    final MBFImage originalImage = ImageUtilities.readMBF(sourceUrl);
    imageStoreService.storeImage(originalImage);

    return parseForecasts(originalImage.flatten());
  }

  private List<Forecast> parseForecasts(final FImage image) {
    final List<Forecast> forecasts = new ArrayList<>();

    for (int day = 0; day < DAYS_PER_IMAGE; day++) {
      final Pixel initialPixel = DAY_1_INITIAL_PIXEL.clone();
      initialPixel.translate(day * OFFSET_BETWEEN_DAYS, 0);

      final Map<Integer, BigDecimal> sunshineMinutesByHour = sunshineParserService.provideSunshineByHourOfDay(image, initialPixel);

      final Map<Long, BigDecimal> sunshineMinutesByTimestamp = new LinkedHashMap<>();
      for (final Entry<Integer, BigDecimal> minutesByHour : sunshineMinutesByHour.entrySet()) {
        final LocalDateTime hourTimestamp = new LocalDate()
            .plusDays(day)
            .toLocalDateTime(LocalTime.MIDNIGHT)
            .plus(Hours.hours(minutesByHour.getKey()));

        sunshineMinutesByTimestamp.put(hourTimestamp.toDate().getTime() * 1000000, minutesByHour.getValue());
      }

      final int sunshineMinutes = sunshineMinutesByHour.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add).intValue();

      final Forecast forecast = Forecast.builder()
          .date(new LocalDate().plusDays(day).toDate().getTime() * 1000000)
          .sunshineMinutes(sunshineMinutes)
          .sunshineByHour(sunshineMinutesByTimestamp)
          .build();
      forecasts.add(forecast);
    }

    return forecasts;
  }
}