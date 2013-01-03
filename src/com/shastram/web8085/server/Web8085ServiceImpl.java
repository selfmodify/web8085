package com.shastram.web8085.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.shastram.web8085.client.Web8085Service;
import com.shastram.web8085.client.rpc.SaveFileData;
import com.shastram.web8085.server.BoxNetData.BoxNetFileUploadResponse;

public class Web8085ServiceImpl extends RemoteServiceServlet implements
        Web8085Service {

    private static Logger logger = Logger.getLogger(Web8085Service.class.getName());

    private static final long serialVersionUID = 6343983694137057114L;

    private static BoxNetService boxNetService = new BoxNetService();

    @Override
    public List<String> getExampleNames() {
        List<String> list = Arrays.asList(TestInstructions.getTestNames());
        return list;
    }

    @Override
    public String getExampleSourceCode(String name) {
        // InputStream systemResourceAsStream =
        // ClassLoader.getSystemResourceAsStream(name);
        return "";
    }

    @Override
    public String getTicket() throws Exception {
        BoxNetData.BoxNetTicketResponse newTicket = boxNetService.getNewTicket();
        return newTicket.getTicket();
    }

    @Override
    public String saveFile(SaveFileData saveFileData) {
        BoxNetService service = new BoxNetService();
        BoxNetFileUploadResponse response = service.saveFileToBoxNet(saveFileData);
        String str = response.exception != null ?
                response.exception.getMessage() :
                    response.entries.get(0).message;
        return str;
    }
}
