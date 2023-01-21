package com.anatorini.lab06.FleetCommand.GUI;

import com.anatorini.lab06.FleetCommand.Core.NetworkManager;
import com.anatorini.lab06.FleetCommand.FleetCommand;

import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class FleetCommandControlPanel extends JPanel {
    private JButton connectButton;
    private JButton autoConnectButton;
    private JTextField ipField;
    private JTextField portField;

    private FleetStatusPanel fleetStatusPanel;
    public FleetCommandControlPanel() {
        super();
        setBackground(new Color(240 , 240 , 240));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;

        JLabel ipLabel = new JLabel("IP Address:");
        add(ipLabel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        ipField = new JTextField("127.0.0.1");
        ipField.setColumns(15);
        add(ipField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.4;

        JLabel portLabel = new JLabel("Port:");
        add(portLabel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.6;
        portField = new JTextField("6000");
        portField.setColumns(15);
        add(portField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> {
            //FleetCommand.networkManager = new NetworkManager();
            try {
                String port = portField.getText();
                String ip = ipField.getText();
                if(FleetCommand.networkManager.oceanHandshake(ip, Integer.parseInt(port))){
                    System.out.println("Connected to Ocean at " + ip + ":" + port);
                    System.out.println("Ocean Size: " + FleetCommand.oceanWidth + "x" + FleetCommand.oceanHeight);
                    System.out.println("Running FleetCommand Server at " + FleetCommand.networkManager.getFleetCommandPort());
                    System.out.println("Waiting for Buoys to connect...");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Connection failed. ");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(connectButton,gbc);

        autoConnectButton = new JButton("Auto Configure (localhost only)");
        autoConnectButton.addActionListener((e)->{
            //FleetCommand.networkManager = new NetworkManager();
            for(int i = 1024; i < 65535; i++){
               // System.out.println("Scanning port " + i);
                try{
                    Socket s = new Socket();
                    SocketAddress sa = new InetSocketAddress("localhost", i);
                    s.connect(sa, 1);
                    System.out.println("Port " + i + " is open");
                    System.out.println("Testing for OceanServer");
                    if(FleetCommand.networkManager.oceanHandshake("localhost", i)){
                        System.out.println("Connected to Ocean at 127.0.0.1:" + i);
                        System.out.println("Ocean Size: " + FleetCommand.oceanWidth + "x" + FleetCommand.oceanHeight);
                        System.out.println("Running FleetCommand Server at " + FleetCommand.networkManager.getFleetCommandPort());
                        System.out.println("Waiting for Buoys to connect...");
                        break;
                    }
                }catch(Exception ex){
                    //System.out.println("Port " + i + " is closed");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        add(autoConnectButton,gbc);

        fleetStatusPanel = new FleetStatusPanel();
        fleetStatusPanel.getUpdater().start();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(fleetStatusPanel,gbc);




    }
}
