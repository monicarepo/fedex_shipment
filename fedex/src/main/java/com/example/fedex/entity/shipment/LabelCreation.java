package com.example.fedex.entity.shipment;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "label_creation")
public class LabelCreation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    //Large Object type
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] pdfContent;
    private String fileName;
    private String fileType;
    private Long fileSize;

    private java.sql.Date createdDate;
    private java.sql.Date updatedDate;

    public LabelCreation() {
        this.fileType = "pdf";
        this.createdDate = new java.sql.Date(System.currentTimeMillis());
        this.updatedDate = new java.sql.Date(System.currentTimeMillis());
    }

    public LabelCreation(byte[] pdfContent, String fileName) {
        this();
        this.pdfContent = pdfContent;
        this.fileName = fileName;
        this.fileSize = (long) pdfContent.length;
    }
}
