package com.shastram.web8085.server.db;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.shastram.web8085.client.FileData;
import com.shastram.web8085.client.FileInfo;

@Entity
public class ServerFileData {
    @Id 
    private String id;
    private String fileName;
    private String data;
    private Date created;
    private Date lastModified;
    @Index String owner;

    public ServerFileData() {
        super();
        setDates();
    }

    private void setDates() {
        created = lastModified = new Date();
    }

    public ServerFileData(String currentUser, FileData clientData) {
        super();
        setDates();
        this.owner = currentUser;
        FileInfo fileInfo = clientData.getFileInfo();
        fileName = fileInfo.getFileName();
        id = createId(currentUser, fileInfo);
        data = clientData.getSourceCode();
    }

    public String getId() {
        return id;
    }

    public String getFileContent() {
        return data;
    }

    public void updateLastModified() {
        this.lastModified = new Date();
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public static String createId(String currentUser, FileInfo fileInfo) {
        return currentUser + "::" + fileInfo.getFileName();
    }
}
