package com.anatorini.lab06.Ocean.GUI;

import com.anatorini.lab06.Ocean.Ocean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OceanConfigPanel extends JPanel {
    private JTextField portField;
    private JTextField sizeField;
    private JButton initButton;

    private OceanStatusPanel statusPanel;
    public OceanConfigPanel(){
        super();
        setBackground(new Color(245, 245, 220));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        portField = new JTextField("6000");
        sizeField = new JTextField("8");
        statusPanel = new OceanStatusPanel();
        portField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                portField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(portField.getText().equals("")){
                    portField.setText("6000");
                }
            }
        });
        sizeField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sizeField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(sizeField.getText().equals("")){
                    sizeField.setText("8");
                }
            }
        });

        portField.setHorizontalAlignment(JTextField.CENTER);
        sizeField.setHorizontalAlignment(JTextField.CENTER);
        initButton = new JButton("Init");
        initButton.addActionListener(e -> {
            try{
                int port = Integer.parseInt(portField.getText());
                int size = Integer.parseInt(sizeField.getText());
                if(port < 1024 || port > 65535){
                    throw new NumberFormatException();
                }
                if(size < 1 || size > 100){
                    throw new NumberFormatException();
                }
                Ocean.initNetwork(port,size);
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Invalid port or size");
            }

        });
        portField.setColumns(8);
        sizeField.setColumns(8);

        c.insets = new Insets(5,5,5,5);
        add(new JLabel("Port"),c);
        c.gridx = 1;
        add(portField,c);
        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Size"),c);
        c.gridx = 1;
        add(sizeField,c);
        c.gridy=2;
        c.gridx=0;
        c.gridwidth=2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(initButton,c);
        c.gridy=3;
        c.gridwidth=2;
        add(statusPanel,c);
        statusPanel.getUpdater().start();
    }
}
