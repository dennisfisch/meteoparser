package de.dennisfisch.meteoparser.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.dennisfisch.meteoparser.model.Forecast;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeteoServiceTest {

  @Autowired
  private MeteoService meteoService;

  @Test
  public void testImageProcess1() throws IOException {
    final List<Forecast> forecasts = meteoService.provideForecast(FileUtils.getFile("src", "test", "resources", "20200312_21_104120.png").toURI().toURL());
    assertNotNull(forecasts);
  }

  @Test
  public void testImageProcess2() throws IOException {
    final List<Forecast> forecasts = meteoService.provideForecast(FileUtils.getFile("src", "test", "resources", "20200313_00_104120.png").toURI().toURL());
    assertNotNull(forecasts);
  }
}