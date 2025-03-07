package com.fileutils.service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileService {
    
    public byte[] compressImage(MultipartFile file, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .scale(1.0)
                .outputQuality(quality)
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    public byte[] mergePDFs(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files provided for merging");
        }

        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            merger.setDestinationStream(outputStream);

            for (MultipartFile file : files) {
                if (!file.getContentType().equals("application/pdf")) {
                    throw new IllegalArgumentException("Invalid file type. Only PDF files are allowed");
                }
                merger.addSource(file.getInputStream());
            }

            merger.mergeDocuments(null);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error merging PDF files: " + e.getMessage(), e);
        }
    }
    public byte[] unlockPDF(MultipartFile file, String password) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream(), password);
        document.setAllSecurityToBeRemoved(true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    public byte[] convertImageToPDF(MultipartFile file) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDImageXObject image = PDImageXObject.createFromByteArray(document, file.getBytes(), file.getOriginalFilename());
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float scale = Math.min(page.getMediaBox().getWidth() / image.getWidth(),
                             page.getMediaBox().getHeight() / image.getHeight());
        
        contentStream.drawImage(image, 0, 0, 
                              image.getWidth() * scale, 
                              image.getHeight() * scale);
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream.toByteArray();
    }
}