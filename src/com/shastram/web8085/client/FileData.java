package com.shastram.web8085.client;

import java.io.Serializable;
import java.util.Date;

public class FileData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String filename;
    private String filedata;
    private Date dateCreated;
    private boolean overwriteExisting = false;

    public FileData() {
    }

    public FileData(String filename, String filedata) {
        this.filename = filename;
        this.filedata = filedata;
    }

    public FileData(String filename, String filedata, Date dateCrated) {
        this.filename = filename;
        this.filedata = filedata;
        dateCreated = dateCrated;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiledata() {
        return filedata;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public boolean isOverwriteExisting() {
        return overwriteExisting;
    }

    public void setOverwriteExisting(boolean overwriteExisting) {
        this.overwriteExisting = overwriteExisting;
    }
}
