package com.shastram.web8085.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.shastram.web8085.client.Web8085Service;

public class Web8085ServiceImpl extends RemoteServiceServlet implements
        Web8085Service {

    private static Logger logger = Logger.getLogger(Web8085Service.class.getName());

    private static final long serialVersionUID = 6343983694137057114L;

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
}
