package de.dennisfisch.meteoparser.controller;

import de.dennisfisch.meteoparser.model.Forecast;
import de.dennisfisch.meteoparser.service.MeteoService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeteoController {

  private final MeteoService meteoService;

  private final URL meteoUrl;

  public MeteoController(final MeteoService meteoService,
      final @Value("${meteoparser.url}") URL meteoUrl) {
    this.meteoService = meteoService;
    this.meteoUrl = meteoUrl;
  }

  @GetMapping("/forecast")
  @ResponseBody
  public ResponseEntity<List<Forecast>> provideForecast() throws IOException {
    return ResponseEntity.ok(meteoService.provideForecast(meteoUrl));
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity handleIOError() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}