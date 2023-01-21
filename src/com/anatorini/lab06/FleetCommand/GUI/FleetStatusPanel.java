package com.anatorini.lab06.FleetCommand.GUI;

import com.anatorini.lab06.FleetCommand.FleetCommand;

import javax.swing.*;

public class FleetStatusPanel extends JPanel {
    private JLabel fleetStatus;
    private JLabel oceanStatusLabel;
    private JLabel buoyCountLabel;
    public FleetStatusPanel() {
        super();
        this.buoyCountLabel = new JLabel("Buoy Count: 0");
        this.oceanStatusLabel = new JLabel("Ocean Status: ");
        this.fleetStatus = new JLabel("Fleet Status: ");
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(this.fleetStatus);
        add(this.oceanStatusLabel);
        add(this.buoyCountLabel);

    }
    public void update(){
        this.fleetStatus.setText("Fleet Status: " + (FleetCommand.networkManager.getFleetCommandStatus().equals("DISCONNECTED") ? "Offline" : "Online at "+ FleetCommand.networkManager.getFleetCommandHost()+ ":" + FleetCommand.networkManager.getFleetCommandPort()));
        this.oceanStatusLabel.setText("Ocean Status: " +  (FleetCommand.networkManager.getOceanStatus().equals("DISCONNECTED") ? "Offline" : "Online at " + FleetCommand.networkManager.getOceanHost() + ":" + FleetCommand.networkManager.getOceanPort()));
        this.buoyCountLabel.setText("Buoy Count: " + FleetCommand.networkManager.getBuoyCount());
    }
    public Updater getUpdater(){
        return new Updater();
    }
    public class Updater extends Thread{
        @Override
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
