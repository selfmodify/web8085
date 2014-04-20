package com.shastram.web8085.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean loginRequired = false;
    private boolean wouldHaveOverrittenFile = false;
    private String msg;
    private List<FileInfo> fileList = new ArrayList<>();
    private FileInfo savedFileData;

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

    public boolean wouldHaveOverrittenFile() {
        return wouldHaveOverrittenFile;
    }

    public void setWouldHaveOverrittenFile(boolean wouldHaveOverrittenFile) {
        this.wouldHaveOverrittenFile = wouldHaveOverrittenFile;
    }

    //
    // Object Generators
    //

    public static ServiceResponse aboutToOverwrite(Date created) {
        ServiceResponse resp = new ServiceResponse();
        resp.setMsg("File with this name already exists.\nCreated on " + created.toString());
        resp.setWouldHaveOverrittenFile(true);
        return resp;
    }

    public FileInfo getSavedFileData() {
        return savedFileData;
    }

    public void setSavedFileData(FileInfo savedFileData) {
        this.savedFileData = savedFileData;
    }

    public static ServiceResponse fileSaved(String id, String fileName, Date created, Date updated) {
        ServiceResponse resp = new ServiceResponse("Saved file " + fileName);
        resp.setSavedFileData(new FileInfo(id, fileName, created, updated));
        return resp;
    }
}
