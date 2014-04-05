package com.shastram.web8085.client;

import java.io.Serializable;

public class FileData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String filename;
    private String filedata;

    public FileData() {
    }

    public FileData(String filename, String filedata) {
        this.filename = filename;
        this.filedata = filedata;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiledata() {
        return filedata;
    }
}
