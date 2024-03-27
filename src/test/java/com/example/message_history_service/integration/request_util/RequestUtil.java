package com.example.message_history_service.integration.request_util;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestUtil {
    private int serverPort;
    private static String prefixHttp = "http://localhost:";
    private static String prefixServicesApiV1 = "/messageHistory/api/v1/history";

    public RequestUtil(int serverPort) {
        this.serverPort = serverPort;
    }

    public URI createRequestGetLastMessageWithUser() throws URISyntaxException {
        return new URI(prefixHttp + serverPort + prefixServicesApiV1 + "/getLastMessagesWithFriendForUser?idUser=");
    }

}
