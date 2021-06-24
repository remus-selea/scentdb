package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.service.ImageService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scentdb/v1")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class ImageController {

  private ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  /**
   * Get the image of a perfume by filename.
   *
   * @param fileName the image filename used to obtain the image.
   * @param request  containing the file mime type.
   * @return the URL of the Image file.
   */
  @GetMapping("/images/perfumes/{fileName:.+}")
  public ResponseEntity<Resource> getPerfumeImageFile(@PathVariable String fileName,
      HttpServletRequest request) {
    // Load image file as Resource
    Resource resource = imageService.loadPerfumeImageFileAsResource(fileName);
    return createResourceResponseEntity(request, resource);

  }

  /**
   * Get the image of a note by filename.
   *
   * @param fileName the image filename used to obtain the image.
   * @param request  containing the file mime type.
   * @return the URL of the Image file.
   */
    @GetMapping("/images/notes/{fileName:.+}")
  public ResponseEntity<Resource> getNoteImageFile(@PathVariable String fileName,
      HttpServletRequest request) {
    // Load image file as Resource
    Resource resource = imageService.loadNoteImageFileAsResource(fileName);

    return createResourceResponseEntity(request, resource);
  }

  /**
   * Get the image of a perfumer by filename.
   *
   * @param fileName the image filename used to obtain the image.
   * @param request  containing the file mime type.
   * @return the URL of the Image file.
   */
  @GetMapping("/images/perfumers/{fileName:.+}")
  public ResponseEntity<Resource> getPerfumerImageFile(@PathVariable String fileName,
      HttpServletRequest request) {
    // Load image file as Resource
    Resource resource = imageService.loadPerfumerImageFileAsResource(fileName);

    return createResourceResponseEntity(request, resource);
  }


  /**
   * Get the image of a company by filename.
   *
   * @param fileName the image filename used to obtain the image.
   * @param request  containing the file mime type.
   * @return the URL of the Image file.
   */
  @GetMapping("/images/companies/{fileName:.+}")
  public ResponseEntity<Resource> getCompanyImageFile(@PathVariable String fileName,
      HttpServletRequest request) {
    // Load image file as Resource
    Resource resource = imageService.loadCompanyImageFileAsResource(fileName);

    return createResourceResponseEntity(request, resource);
  }


  private ResponseEntity<Resource> createResourceResponseEntity(HttpServletRequest request,
      Resource resource) {
    // Try to determine file's content type
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {
      log.info("Could not determine file type.");
    }

    // Fallback to the default content type if type could not be determined
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

}
