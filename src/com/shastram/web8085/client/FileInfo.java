package com.shastram.web8085.client;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fileName;
    String id;
    private Date dateCreated;
    private Date dateUpdated;

    public FileInfo() {
    }

    public FileInfo(String id, String name, Date dateCreated, Date dateUpdated) {
        this.id = id;
        this.fileName = name;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public FileInfo(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}