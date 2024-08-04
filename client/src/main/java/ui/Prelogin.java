package ui;

import dataaccess.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;

public class Prelogin {

    private final ServerFacade server;
    private final String serverUrl;

    public Prelogin(String url) {
        serverUrl = url;
        server = new ServerFacade(serverUrl);
    }

}
