package de.dennisfisch.meteoparser.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Forecast {

  private final Date date;

  private final Map<Integer, BigDecimal> temperatureByHour;

  private final int sunshineMinutes;

  private final Map<Integer, BigDecimal> sunshineByHour;

  private final Map<Integer, BigDecimal> precipitationByHour;

}