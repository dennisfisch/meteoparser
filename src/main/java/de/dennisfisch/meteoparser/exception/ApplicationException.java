package de.dennisfisch.meteoparser.exception;

import net.sourceforge.tess4j.TesseractException;

public class ApplicationException extends RuntimeException {

  public ApplicationException(final TesseractException e) {
    super(e);
  }
}