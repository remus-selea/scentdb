package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.exception.FileStorageException;
import com.github.remusselea.scentdb.exception.ImageFileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

  @Value("${perfumes.images.dir:${user.home}}")
  public String perfumeImagesDir;

  @Value("${notes.images.dir:${user.home}}")
  public String noteImagesDir;

  @Value("${perfumers.images.dir:${user.home}}")
  public String perfumerImagesDir;

  @Value("${companies.images.dir:${user.home}}")
  public String companiesImagesDir;


  public Resource loadPerfumeImageFileAsResource(String fileName) {
    return getResource(fileName, perfumeImagesDir);
  }

  public Resource loadNoteImageFileAsResource(String fileName) {
    return getResource(fileName, noteImagesDir);
  }

  public Resource loadPerfumerImageFileAsResource(String fileName) {
    return getResource(fileName, perfumerImagesDir);
  }

  public Resource loadCompanyImageFileAsResource(String fileName) {
    return getResource(fileName, companiesImagesDir);
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


  public String storeImage(MultipartFile file, String uploadDir) {
    // Normalize file name
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    try {
      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
      Path copyLocation = targetLocation.resolve(fileName);
      Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + fileName + ". Please try again!",
          ex);
    }
    return fileName;
  }



}
