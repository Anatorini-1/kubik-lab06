package com.anatorini.lab06.Ship.GUI;

import com.anatorini.lab06.Ship.Core.CommandsProvider;
import com.anatorini.lab06.Ship.Core.NetworkManager;
import com.anatorini.lab06.Ship.Ship;

import javax.swing.*;
import java.awt.*;
public class ShipControlPanel extends JPanel {
    private JButton connectButton;
    private JTextField hostField;
    private JTextField portField;
    private JTextField xField;
    private JTextField yField;
    private JButton moveButton;
    private JButton scanButton;
    public ShipControlPanel(){
        super();

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        add(new JLabel("Host:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.8;


        hostField = new JTextField("127.0.0.1");
        add(hostField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.2;

        add(new JLabel("Port:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.8;

        portField = new JTextField("6000");
        add(portField, c);
        xField = new JTextField("20");
        yField = new JTextField("20");
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.2;
        add(new JLabel("X:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.8;
        add(xField, c);
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.2;
        add(new JLabel("Y:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0.8;
        add(yField, c);
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 2;
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> {
            connect();
        });
        add(connectButton, c);
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 2;
        moveButton = new JButton("Move");
        moveButton.addActionListener(e -> {
            new Captain().start();
        });
        add(moveButton, c);
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 1;
        c.gridwidth = 2;
        scanButton = new JButton("Scan");
        scanButton.addActionListener(e -> {
            CommandsProvider.scan();
        });
        add(scanButton, c);
        new Updater(this).start();
    }
    private void connect(){
        System.out.println("Attempting ocean connection at " + hostField.getText() + ":" + portField.getText());
        Ship.x = Integer.parseInt(xField.getText());
        Ship.y = Integer.parseInt(yField.getText());
        try {
            int port = Integer.parseInt(portField.getText());
            if (port < 0 || port > 65535)
                throw new NumberFormatException();
            NetworkManager.oceanHandshake(hostField.getText(), port);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void update(){
        if(NetworkManager.socket != null && NetworkManager.socket.isConnected()){
            moveButton.setEnabled(true);
            scanButton.setEnabled(true);

        }else{
            moveButton.setEnabled(false);
            scanButton.setEnabled(false);
        }
    }
    private static class Updater extends Thread{
        private ShipControlPanel panel;
        public Updater(ShipControlPanel panel){
            super();
            this.panel = panel;
        }
        public void run(){
            while(true){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                panel.update();
            }
        }
    }

    private static class Captain extends Thread{
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(NetworkManager.socket != null && NetworkManager.socket.isConnected()){
                    CommandsProvider.move();
                }
            }
        }
    }
}
