package com.example.fedex.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${label.storage.directory:generated-labels}")
    private String storageDirectory;

    @Value("${label.storage.auto-create:true}")
    private boolean autoCreateDirectory;

    @PostConstruct
    public void init() {
        if (autoCreateDirectory) {
            try {
                Path storagePath = Paths.get(storageDirectory);
                if (!Files.exists(storagePath)) {
                    Files.createDirectories(storagePath);
                    logger.info("Created label storage directory: {}", storagePath.toAbsolutePath());
                }
            } catch (IOException e) {
                logger.error("Failed to create storage directory: {}", storageDirectory, e);
            }
        }
    }

    public Path savePdfFile(byte[] pdfContent, String filename) throws IOException {
        Path filePath = getFilePath(filename);
        Files.write(filePath, pdfContent);
        logger.info("PDF file saved: {}", filePath.toAbsolutePath());
        return filePath;
    }

    public Path getFilePath(String filename) {
        return Paths.get(storageDirectory, filename);
    }

    public boolean fileExists(String filename) {
        return Files.exists(getFilePath(filename));
    }

    public byte[] readPdfFile(String filename) throws IOException {
        Path filePath = getFilePath(filename);
        return Files.readAllBytes(filePath);
    }

    public boolean deletePdfFile(String filename) throws IOException {
        Path filePath = getFilePath(filename);
        return Files.deleteIfExists(filePath);
    }

    public List<String> listAllFiles() throws IOException {
        return Files.list(Paths.get(storageDirectory))
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }
}
