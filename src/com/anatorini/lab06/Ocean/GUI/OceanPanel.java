package com.anatorini.lab06.Ocean.GUI;

import javax.swing.*;
import java.awt.*;

public class OceanPanel extends JPanel {
    private OceanConfigPanel oceanConfigPanel;
    private OceanGridPanel oceanGridPanel;

    public OceanPanel(){
        super();
        oceanConfigPanel = new OceanConfigPanel();
        oceanGridPanel = new OceanGridPanel();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.15;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
       // gbc.anchor = GridBagConstraints.NORTHWEST;
        add(oceanConfigPanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.85;
        add(oceanGridPanel, gbc);
    }

}
