package com.shastram.web8085.client.rpc;

import java.io.Serializable;

public class SaveFileData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private String data;
    private String authToken;
    private String fileId;

    public SaveFileData() {
        
    }

    public SaveFileData(String authToken, String fileName,  String fileId, String data) {
        this.setAuthToken(authToken);
        this.fileName = fileName;
        this.data = data;
        this.setFileId(fileId);
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    public String toString() {
        return "fileId=" + fileId + " authToken=" + authToken + " fileName=" + fileName;
    }
}
