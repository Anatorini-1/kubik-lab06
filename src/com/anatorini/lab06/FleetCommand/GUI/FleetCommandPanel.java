package com.anatorini.lab06.FleetCommand.GUI;

import com.anatorini.lab06.FleetCommand.FleetCommand;

import javax.swing.*;
import java.awt.*;

public class FleetCommandPanel extends JPanel {
    private double width;
    private double height;
    private FleetCommandControlPanel fccp;
    private FleetCommandGridPanel fcgp;
    public FleetCommandPanel(double width,double height){
        super();
        this.width=width;
        this.height=height;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.fccp = new FleetCommandControlPanel();
        this.fcgp = new FleetCommandGridPanel();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        add(fccp,gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        add(fcgp,gbc);


    }

}
