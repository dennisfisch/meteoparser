package de.dennisfisch.meteoparser.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageStoreService {

  private final File storageFolder;

  private final DateTimeFormatter filenameFormatter;

  public ImageStoreService(
      final @Value("${meteoparser.imagestore.folder}") String folder,
      final @Value("${meteoparser.imagestore.filepattern}") String filenamePattern) {
    storageFolder = new File(folder);
    filenameFormatter = DateTimeFormatter.ofPattern(filenamePattern);
  }

  public void storeImage(final MBFImage image) throws IOException {
    if (!storageFolder.exists()) {
      if (!storageFolder.mkdirs()) {
        log.warn("Unable to create image store folder {}", storageFolder);
        return;
      }

      log.info("Created image store folder {}", storageFolder);
    }

    final String filename = LocalDateTime.now().format(filenameFormatter);
    final File imageFile = storageFolder.toPath().resolve(filename).toFile();
    if (imageFile.exists()) {
      log.warn("Overwriting existing image {}", filename);
    }

    ImageUtilities.write(image, imageFile);
    log.debug("Stored image {}", filename);
  }
}