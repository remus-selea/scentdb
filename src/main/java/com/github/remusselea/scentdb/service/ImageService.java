package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.exception.ImageFileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  @Value("${perfumes.images.dir:${user.home}}")
  public String perfumeImagesDir;

  @Value("${notes.images.dir:${user.home}}")
  public String noteImagesDir;

  public Resource loadPerfumeImageFileAsResource(String fileName) {
    return getResource(fileName, perfumeImagesDir);
  }

  public Resource loadNoteImageFileAsResource(String fileName) {
    return getResource(fileName, noteImagesDir);
  }

  private Resource getResource(String fileName, String imagesDir) {
    try {
      Path targetLocation = Paths.get(imagesDir).toAbsolutePath().normalize();
      Path filePath = targetLocation.resolve(fileName);
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new ImageFileNotFoundException("File not found " + fileName);
      }

    } catch (MalformedURLException ex) {
      throw new ImageFileNotFoundException("File not found " + fileName, ex);
    }
  }


}
