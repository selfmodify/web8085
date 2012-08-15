package com.shastram.web8085.client.rpc;

import java.io.Serializable;

public class SaveFileData implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object fileName;
    private String data;

    public SaveFileData(String fileName, String data) {
        this.fileName = fileName;
        this.data = data;
    }

    public Object getFileName() {
        return fileName;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
