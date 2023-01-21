package com.anatorini.lab06.Ocean.GUI;

import com.anatorini.lab06.Ocean.Ocean;

import javax.swing.*;
import java.awt.*;


public class OceanStatusPanel extends JPanel {
    private JLabel oceanStatus;
    private JLabel fleetCommandStatus;
    private JLabel shipCount;
    private JLabel buoyCount;
    private JLabel oceanStatusLabel;
    private JLabel fleetCommandStatusLabel;
    private JLabel shipCountLabel;
    private JLabel buoyCountLabel;
    public OceanStatusPanel() {
        super();
        this.fleetCommandStatus = new JLabel("FleetCommand Status:e");
        this.oceanStatus = new JLabel("Ocean Status:");
        this.shipCount = new JLabel("Ship Count:");
        this.buoyCount = new JLabel("Buoy Count:");
        this.fleetCommandStatusLabel = new JLabel("Offline");
        this.oceanStatusLabel = new JLabel("Offline");
        this.shipCountLabel = new JLabel("0");
        this.buoyCountLabel = new JLabel("0");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.anchor = GridBagConstraints.WEST;
        add(oceanStatus,gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.EAST;
        add(oceanStatusLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.anchor = GridBagConstraints.WEST;
        add(fleetCommandStatus,gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.EAST;
        add(fleetCommandStatusLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.4;
        gbc.anchor = GridBagConstraints.WEST;
        add(shipCount,gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.EAST;
        add(shipCountLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        gbc.anchor = GridBagConstraints.WEST;
        add(buoyCount,gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.EAST;
        add(buoyCountLabel,gbc);
    }
    public void update(){
        if(Ocean.serverSocket != null && Ocean.serverSocket.isBound() && !Ocean.serverSocket.isClosed()){
            oceanStatusLabel.setForeground(new Color(0, 128, 0));
            oceanStatusLabel.setText("Online at " + Ocean.serverSocket.getInetAddress().getHostAddress() + ":" + Ocean.serverSocket.getLocalPort());
        } else {
            oceanStatusLabel.setForeground(new Color(255, 0, 0));
            oceanStatusLabel.setText("Offline");
        }
        if(Ocean.fleetCommandHost != null && Ocean.fleetCommandPort != 0){
            fleetCommandStatusLabel.setForeground(new Color(0, 128, 0));
            fleetCommandStatusLabel.setText("Online at " + Ocean.fleetCommandHost + ":" + Ocean.fleetCommandPort);
        } else {
            fleetCommandStatusLabel.setForeground(new Color(255, 0, 0));
            fleetCommandStatusLabel.setText("Offline");
        }
        if(Ocean.ships != null){
            int cnt =0;
            for(int i = 0; i < Ocean.ships.size(); i++){
                if(Ocean.ships.get(i) != null){
                    cnt++;
                }
            }
            shipCountLabel.setText(cnt + "");
        } else {
            shipCountLabel.setText("0");
        }
        if(Ocean.buoys != null){
            int count =0;
            for(int i=0;i<Ocean.buoys.length;i++){
                for(int j=0;j<Ocean.buoys[i].length;j++){
                    if(Ocean.buoys[i][j] != null){
                        count++;
                    }
                }
            }
            buoyCountLabel.setText(count + "");
        } else {
            buoyCountLabel.setText("0");
        }
    }
    public Updater getUpdater(){
        return new Updater();
    }

    public class Updater extends Thread{
        public void run(){
            while(true){
                update();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
