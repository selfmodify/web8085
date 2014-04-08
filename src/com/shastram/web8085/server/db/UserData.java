package com.shastram.web8085.server.db;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class UserData {
    @Id String id;
    String name;
    String emailId;
    Date created;

    public UserData() {
    }

    public UserData(String id, String name, String emailId) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;
        created = new Date();
    }

    public void setData(UserData user) {
        this.id = user.id;
        this.name = user.name;
        this.created = user.created;
        this.emailId = user.emailId;
    }
}
