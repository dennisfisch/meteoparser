package de.dennisfisch.meteoparser.service;

import de.dennisfisch.meteoparser.model.Forecast;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeteoService {

  private final SunshineParserService sunshineParserService;

  private final TesseractService tesseractService;

  public Forecast provideForecast(final URL sourceUrl) throws IOException {
    final FImage image = ImageUtilities.readMBF(sourceUrl).flatten();

    final String text = tesseractService.provideText(ImageUtilities.createBufferedImage(image));

    final Map<Integer, BigDecimal> sunshineMinutesByHour = sunshineParserService.parseImage(image);
    final int sunshineMinutes = sunshineMinutesByHour.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add).intValue();

    return Forecast.builder()
        .date(new Date())
        .sunshineMinutes(sunshineMinutes)
        .sunshineByHour(sunshineMinutesByHour)
        .build();
  }
}