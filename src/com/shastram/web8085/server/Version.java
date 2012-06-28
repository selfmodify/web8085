package com.shastram.web8085.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/version")
public class Version {

    @GET
    @Produces("text/plain")
    public String getVersion() {
        return "1.0";
    }
}
