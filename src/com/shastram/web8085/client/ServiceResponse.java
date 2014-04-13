package com.shastram.web8085.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceResponse implements Serializable {

    public static class FileInfo implements Serializable {
        String name;
        String id;
        String dateUpdated;
        public FileInfo () {}

        public FileInfo(String id, String name, String dateUpdated) {
            this.id = id;
            this.name = name;
            this.dateUpdated = dateUpdated;
        }
    }
    private static final long serialVersionUID = 1L;
    private boolean loginRequired = false;
    private String msg;
    private List<FileInfo> fileList = new ArrayList<>();

    public ServiceResponse() {
    }

    public ServiceResponse(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public ServiceResponse(String msg) {
        this.msg = msg;
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void addFileToFileList(FileInfo info) {
        fileList.add(info);
    }

    public List<FileInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }
}
