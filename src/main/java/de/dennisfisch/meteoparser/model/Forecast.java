package de.dennisfisch.meteoparser.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Forecast {

  private final long date;

  private final Map<Long, BigDecimal> temperatureByHour;

  private final int sunshineMinutes;

  private final Map<Long, BigDecimal> sunshineByHour;

  private final Map<Long, BigDecimal> precipitationByHour;

}