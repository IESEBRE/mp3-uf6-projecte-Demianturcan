package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PeliView extends JFrame {
    private JTabbedPane pestanyes;
    private JButton insertaButton;
    private JButton modificaButton;
    private JButton borraButton;
    private JTextField campTitol;
    private JTextField campNota;

    private JTable taulaFitxa;
    private JTable taula;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JScrollPane JScrollPane2;
    private JComboBox comboCaract;
    private JTextField campValor;
    private JComboBox comboEstat;
    private JButton desferButton;

    public JButton getDesferButton() {
        return desferButton;
    }

    public JTextField getCampNota() {
        return campNota;
    }

    public JTable getTaulaFitxa() {
        return taulaFitxa;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTabbedPane getTabbedPane() {
        return pestanyes;
    }

    public JButton getInsertaButton() {
        return insertaButton;
    }

    public JButton getModificaButton() {
        return modificaButton;
    }

    public JButton getBorraButton() {
        return borraButton;
    }

    public JTextField getCampTitol() {
        return campTitol;
    }

    public JTextField getCampValor() {
        return campValor;
    }



    public JTable getTaulaPeli() {
        return taula;
    }

    public JComboBox getComboCaract() {
        return comboCaract;
    }

    public JComboBox getComboEstat() {
        return comboEstat;
    }

    public PeliView() {


        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane.setViewportView(taula);

    }

}
