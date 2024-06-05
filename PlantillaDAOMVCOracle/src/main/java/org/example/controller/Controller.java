package org.example.controller;

import org.example.model.entities.Pelicula;
import org.example.model.entities.Pelicula.Fitxa;
import org.example.model.exceptions.DAOException;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.PeliDAOJDBCOracleImpl;
import org.example.view.PeliView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener


    private ModelComponentsVisuals modelComponentsVisuals =new ModelComponentsVisuals();
    private PeliDAOJDBCOracleImpl dadesPelicules;
    private PeliView view;

    public Controller(PeliDAOJDBCOracleImpl dadesPelicules, PeliView view) {
        this.dadesPelicules = dadesPelicules;


        this.view = view;

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);

        lligaVistaModel();

        afegirListeners();

        //Si no hem tingut cap poroblema amb la BD, mostrem la finestra
        view.setVisible(true);

    }

    private void lligaVistaModel() {

        //Carreguem la taula d'alumnes en les dades de la BD
        try {
            setModelTaulaPelicula(modelComponentsVisuals.getModelTaulaPeli(), dadesPelicules.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }

        try {
            dadesPelicules.creaTaula();
        } catch (DAOException e) {
            this.setExcepcio(e);
        }
            //Fixem el model de la taula dels alumnes
        JTable taulaPeli = view.getTaulaPeli();
        taulaPeli.setModel(this.modelComponentsVisuals.getModelTaulaPeli());
        //Amago la columna que conté l'objecte alumne
        taulaPeli.getColumnModel().getColumn(3).setMinWidth(0);
        taulaPeli.getColumnModel().getColumn(3).setMaxWidth(0);
        taulaPeli.getColumnModel().getColumn(3).setPreferredWidth(0);

        //Fixem el model de la taula de matrícules
        JTable taulaFitxa = view.getTaulaFitxa();
        taulaFitxa.setModel(this.modelComponentsVisuals.getModelTaulaFitxa());

        //Posem valor a el combo d'MPs
        view.getComboCaract().setModel(modelComponentsVisuals.getComboBoxModel());

        //Desactivem la pestanya de la fitxes
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Fitxa de ...");

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);
    }

    private void setModelTaulaPelicula(DefaultTableModel modelTaulaPelicula, List<Pelicula> all) {

        // Fill the table model with data from the collection
        for (Pelicula peli : all) {
            modelTaulaPelicula.addRow(new Object[]{peli.getTitol(), peli.getNota(), peli.getEstat(), peli});
        }
    }

    private void afegirListeners() {

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel modelPeli = modelo.getModelTaulaPeli();
        DefaultTableModel modelFitxa = modelo.getModelTaulaFitxa();
        JTable taulaPeli = view.getTaulaPeli();
        JTable taulaFitxa = view.getTaulaFitxa();
        JTextField campTitol = view.getCampTitol();
        JTextField campNota = view.getCampNota();
        JComboBox comboEstat = view.getComboEstat();
        JButton insertarButton = view.getInsertaButton();
        JButton modificarButton = view.getModificaButton();
        JButton borrarButton = view.getBorraButton();
        JTabbedPane pestanyes = view.getPestanyes();

        //Botó insertar
        insertarButton.addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (pestanyes.getSelectedIndex() == 0) {
                            try {
                            PeliDAOJDBCOracleImpl imp = new PeliDAOJDBCOracleImpl();
                                double nota =campNota.getText().isBlank() ? 0 : Double.parseDouble(campNota.getText().trim());
                                Pelicula peli = new Pelicula(imp.maxId()+1,campTitol.getText(), nota, comboEstat.getSelectedItem(), new TreeSet<Fitxa>());
                                imp.inserta(peli);
                                modelPeli.addRow(new Object[]{peli.getTitol(), peli.getNota()==0?" ":peli.getNota(), peli.getEstat(), peli});

                                campTitol.setText("");
                                campTitol.setSelectionStart(0);
                                campTitol.setSelectionEnd(campTitol.getText().length());

                                campNota.setText("");
                                campTitol.requestFocus();         //intentem que el foco vaigue al camp del titol

                            } catch (NumberFormatException ex) {
                                setExcepcio(new DAOException(3));
                            } catch (DAOException ex) {
                                setExcepcio(ex);
                            }


                        }else{
                            Pelicula peli = (Pelicula) modelPeli.getValueAt(taulaPeli.getSelectedRow(), 3);
                            Fitxa m = new Fitxa((Fitxa.Caracteristica) view.getComboCaract().getSelectedItem(), view.getCampValor().getText());
                            peli.getFitxes().add(m);
                            ompliFitxa(peli, modelFitxa);
                        }


                    }
                }
        );

        modificarButton.addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        int filaSel = taulaPeli.getSelectedRow();
                        int columnaSel = taulaPeli.getSelectedColumn();
                        PeliDAOJDBCOracleImpl imp = new PeliDAOJDBCOracleImpl();
                        Pelicula peli = (Pelicula) modelComponentsVisuals.getModelTaulaPeli().getValueAt(filaSel,3);

                        try {

                            double nota =campNota.getText().isBlank() ? 0 : Double.parseDouble(campNota.getText().trim());
                            //modelPeli.removeRow(filaSel);
                            //modelPeli.insertRow(filaSel, new Object[]{campTitol.getText(),  campNota.getText(), comboEstat.getSelectedItem()});
                            Pelicula peliNova = new Pelicula(peli.getId(), campTitol.getText(), nota, comboEstat.getSelectedItem(), new TreeSet<Fitxa>());
                            modelPeli.setValueAt(campTitol.getText(),filaSel,0);
                            modelPeli.setValueAt( nota==0?" ":nota,filaSel,1);
                            modelPeli.setValueAt(comboEstat.getSelectedItem(),filaSel,2);
                            modelPeli.setValueAt(peliNova,filaSel,3);
                            imp.update(peliNova);

                            campTitol.requestFocus();         //intentem que el foco vaigue al camp del titol


                        } catch (DAOException ex) {
                            throw new RuntimeException(ex);
                        }


                    }
                }
        );

        borrarButton.addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PeliDAOJDBCOracleImpl imp = new PeliDAOJDBCOracleImpl();
                        //Mirem si tenim una fila de la taula seleccionada
                        int filaSel = taulaPeli.getSelectedRow();

                        try {
                        if (filaSel != -1) {
                            Pelicula peli = (Pelicula) modelPeli.getValueAt(filaSel,3);
                            modelPeli.removeRow(filaSel);
                            imp.borra(peli);
                            //Posem els camps de text en blanc
                            campTitol.setText("");
                            campNota.setText("");
                        } else JOptionPane.showMessageDialog(null, "Per borrar una fila l'has de seleccionar a la taula");

                        } catch (DAOException ex) {
                            throw new RuntimeException(ex);
                        } catch (NumberFormatException b) {
                            setExcepcio(new DAOException(3));
                        }
                    }
                }
        );

        taulaPeli.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JTextField campTitol = view.getCampTitol();
                JTextField campNota = view.getCampNota();
                JComboBox comboEstat = view.getComboEstat();
                //Al seleccionar la taula omplim els camps de text en els valors de la fila seleccionada
                int filaSel = taulaPeli.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campTitol.setText(modelPeli.getValueAt(filaSel, 0).toString());
                    campNota.setText(String.valueOf(modelPeli.getValueAt(filaSel, 1)));
                    comboEstat.setSelectedItem(modelPeli.getValueAt(filaSel,2));
                    Pelicula peli = (Pelicula) modelComponentsVisuals.getModelTaulaPeli().getValueAt(filaSel,3);

                    System.out.println(peli.getId());
                    //Activem la pestanya de la matrícula de l'alumne seleccionat
                    view.getPestanyes().setEnabledAt(1, true);
                    view.getPestanyes().setTitleAt(1, "Fitxa de "+ campTitol.getText());

                    //Posem valor a el combo d'MPs
                    view.getComboCaract().setModel(modelo.getComboBoxModel());
                    ompliFitxa((Pelicula) modelPeli.getValueAt(filaSel, 3),modelFitxa);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campTitol.setText("");
                    campNota.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Fitxa de ...");
                }
            }
        });


        //throw new LaMeuaExcepcio(1,"Ha petat la base de dades");
    }



    private static void ompliFitxa(Pelicula peli, DefaultTableModel modelFitxa) {
        //Omplim el model de la taula de matrícula de l'alumne seleccionat
        modelFitxa.setRowCount(0);
        // Fill the table model with data from the collection
        for (Fitxa fitxa : peli.getFitxes()) {
            modelFitxa.addRow(new Object[]{fitxa.getCaract(), fitxa.getValor()});
        }
    }


    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";
    private DAOException excepcio;

    public DAOException getExcepcio() {
        return excepcio;
    }

    public void setExcepcio(DAOException excepcio) {
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DAOException rebuda=(DAOException)evt.getNewValue();

        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getTipo()){
                        case 0:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            System.exit(1);
                            break;
                        case 1, 3:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            //this.view.getCampTitol().setText(rebuda.getMissatge());
                            this.view.getCampTitol().setSelectionStart(0);
                            this.view.getCampTitol().setSelectionEnd(this.view.getCampTitol().getText().length());
                            this.view.getCampTitol().requestFocus();

                            break;
                    }


            }
        }
    }

}
