package com.job.jobhunter.controller;


import com.job.jobhunter.domain.response.file.ResUploadFileDTO;
import com.job.jobhunter.service.FileService;
import com.job.jobhunter.util.annotation.ApiMessage;
import com.job.jobhunter.util.error.StorageUploadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private  final FileService fileService;

        @Value("${jobhuntor.upload-file.base-uri}")
        private String baseUri;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("upload-file success")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam(value  = "file", required = false) MultipartFile file,
                                                       @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageUploadFileException {
        // Validate file
        if (file == null || file.isEmpty()) {
            throw new StorageUploadFileException("File is empty");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        Boolean isValidExtension = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValidExtension) {
            throw new StorageUploadFileException("File type is not supported" + allowedExtensions.toString());
        }
        // Logic to handle file upload
        this.fileService.createUploadFolder(baseUri + folder);
        String finalName = this.fileService.store(file, folder);
        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(finalName,Instant.now());
        return ResponseEntity.ok().body(resUploadFileDTO);
    }

    @GetMapping("/files")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageUploadFileException, URISyntaxException, FileNotFoundException {
        if (fileName == null || folder == null) {
            throw new StorageUploadFileException("Missing required params : (fileName or folder) in query params.");
        }

        // check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageUploadFileException("File with name = " + fileName + " not found.");
        }

        // download a file
        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
