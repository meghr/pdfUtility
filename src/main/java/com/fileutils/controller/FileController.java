package com.fileutils.controller;

import com.fileutils.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/compress")
    public ResponseEntity<byte[]> compressImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("quality") float quality) throws IOException {
        byte[] compressed = fileService.compressImage(file, quality);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"compressed_" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(compressed);
    }

    @PostMapping("/merge-pdf")
    public ResponseEntity<byte[]> mergePDFs(@RequestParam("files") List<MultipartFile> files) throws IOException {
        if (files == null || files.size() < 2) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            byte[] merged = fileService.mergePDFs(files);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"merged.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(merged);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/unlock-pdf")
    public ResponseEntity<byte[]> unlockPDF(@RequestParam("file") MultipartFile file,
                                          @RequestParam("password") String password) throws IOException {
        byte[] unlocked = fileService.unlockPDF(file, password);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"unlocked_" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(unlocked);
    }

    @PostMapping("/image-to-pdf")
    public ResponseEntity<byte[]> imageToPDF(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] pdf = fileService.convertImageToPDF(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    @GetMapping("/")
public String index() {
    return "index";
}
}