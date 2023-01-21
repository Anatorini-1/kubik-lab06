package com.anatorini.lab06.Ocean.Core;

import java.net.ServerSocket;

public class NetworkManager {
    private int oceanPort;
    private String oceanHost;
    private int fleetCommandPort;
    private String fleetCommandHost;
    private ServerSocket serverSocket;

    public NetworkManager() {
        oceanPort = 0;
        oceanHost = "";
        fleetCommandPort = 0;
        fleetCommandHost = "";
        serverSocket = null;
    }


}
