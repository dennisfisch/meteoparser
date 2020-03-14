package de.dennisfisch.meteoparser.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecognitionConfig {

  @Bean
  public Tesseract tesseract() {
    return new Tesseract();
  }
}