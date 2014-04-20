package com.shastram.web8085.client;

import java.io.Serializable;
import java.util.Date;

public class FileData implements Serializable {
    private static final long serialVersionUID = 1L;
    private FileInfo fileInfo;
    private String sourceCode;
    private boolean overwriteExisting = false;

    public FileData() {
    }

    public FileData(String filename, String filedata) {
        this.fileInfo = new FileInfo(filename);
        this.sourceCode = filedata;
    }

    public FileData(String filename, String filedata, Date dateCreated) {
        fileInfo = new FileInfo(filename);
        fileInfo.setDateCreated(dateCreated);
        this.sourceCode = filedata;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public boolean isOverwriteExisting() {
        return overwriteExisting;
    }

    public void setOverwriteExisting(boolean overwriteExisting) {
        this.overwriteExisting = overwriteExisting;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
