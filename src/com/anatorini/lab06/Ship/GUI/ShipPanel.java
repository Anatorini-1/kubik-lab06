package com.anatorini.lab06.Ship.GUI;

import javax.swing.*;
import java.awt.*;

public class ShipPanel extends JPanel {
    private ShipStatusPanel shipStatusPanel;
    private ShipControlPanel shipControlPanel;
    public ShipPanel(){
        super();
        setLayout(new GridBagLayout());
        shipControlPanel = new ShipControlPanel();
        shipStatusPanel = new ShipStatusPanel();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;
        add(shipStatusPanel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;
        add(shipControlPanel, c);

    }
}
