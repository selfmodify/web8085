package com.shastram.web8085.server.db;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.shastram.web8085.client.FileData;

@Entity
public class ServerFileData {
    @Id String id;
    private String fileName;
    String data;
    Date created;
    private Date lastModified;
    @Parent Key<UserData> owner;

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
        this.owner = Key.create(UserData.class, currentUser);
        fileName = clientData.getFilename();
        id = currentUser + ":" + clientData.getFilename();
        data = clientData.getFiledata();
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
}
