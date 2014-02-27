package com.shastram.web8085.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/oauth2callback")
public class TestHandler {

    @GET
    public String get() {
        return "Hello World";
    }
}
