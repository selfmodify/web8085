package com.shastram.web8085.client.rpc;

import java.io.Serializable;

public class SaveFileData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private String data;

    public SaveFileData() {
        
    }

    public SaveFileData(String fileName, String data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
