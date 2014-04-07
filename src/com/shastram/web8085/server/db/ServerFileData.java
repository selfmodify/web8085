package com.shastram.web8085.server.db;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.shastram.web8085.client.FileData;

@Entity
public class ServerFileData {
    @Id String id;
    String fileName;
    String data;
    Date created;
    Date lastModified;

    public ServerFileData() {
        super();
        setDates();
    }

    private void setDates() {
        created = lastModified = new Date();
    }

    public ServerFileData(String id, String fileName, String data) {
        super();
        this.id = id;
        this.fileName = fileName;
        this.data = data;
        setDates();
    }

    public ServerFileData(String currentUser, FileData clientData) {
        super();
        setDates();
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
}
