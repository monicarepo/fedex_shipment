package com.example.fedex.dto;


import com.example.fedex.entity.shipment.LabelCreation;
import lombok.Data;

@Data
public class LabelResponse {
    private Long labelId;
    private byte[] pdfContent;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private java.sql.Date createdDate;
    private java.sql.Date updatedDate;

    public LabelResponse(LabelCreation labelCreation) {
        this.labelId = labelCreation.getLabelId();
        this.pdfContent = labelCreation.getPdfContent();
        this.fileName = labelCreation.getFileName();
        this.fileType = labelCreation.getFileType();
        this.fileSize = labelCreation.getFileSize();
        this.createdDate = labelCreation.getCreatedDate();
        this.updatedDate = labelCreation.getUpdatedDate();
    }
}
