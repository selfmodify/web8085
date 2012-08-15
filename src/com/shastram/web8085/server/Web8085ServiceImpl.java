package com.shastram.web8085.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.shastram.web8085.client.Web8085Service;
import com.shastram.web8085.client.rpc.SaveFileData;
import com.shastram.web8085.server.BoxNetService.BoxNetTicketResponse;

public class Web8085ServiceImpl extends RemoteServiceServlet implements
        Web8085Service {

    private static Logger logger = Logger.getLogger(Web8085Service.class.getName());

    private static final long serialVersionUID = 6343983694137057114L;

    private static BoxNetService boxNetService = new BoxNetService();
    private static ObjectMapper mapper = new ObjectMapper();
    
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<String> getExampleNames() {
        List<String> list = Arrays.asList(Test.getTestNames());
        return list;
    }

    @Override
    public String getExampleSourceCode(String name) {
        // InputStream systemResourceAsStream =
        // ClassLoader.getSystemResourceAsStream(name);
        return "";
    }

    @Override
    public String saveFile(SaveFileData saveFileData) {
        try {
            BoxNetTicketResponse newTicket = boxNetService.getNewTicket();
            boxNetService.getAuthToken(newTicket);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Saving file to external service failed. ", e);
        }
        return null;
    }

}
