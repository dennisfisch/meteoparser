package de.dennisfisch.meteoparser.service;

import de.dennisfisch.meteoparser.exception.ApplicationException;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TesseractService {

  private final Tesseract tesseract;

  public String provideText(final BufferedImage image) {
    try {
      return tesseract.doOCR(image);
    } catch (TesseractException e) {
      throw new ApplicationException(e);
    }
  }
}