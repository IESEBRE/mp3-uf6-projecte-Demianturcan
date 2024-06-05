package org.example.view;

import org.example.model.entities.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaPeli;
    private DefaultTableModel modelTaulaFitxa;
    private ComboBoxModel<Pelicula.Fitxa.Caracteristica> comboBoxModel;

    //Getters


    public ComboBoxModel<Pelicula.Fitxa.Caracteristica> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaPeli() {
        return modelTaulaPeli;
    }

    public DefaultTableModel getModelTaulaFitxa() {
        return modelTaulaFitxa;
    }

    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels alumnes
        modelTaulaPeli =new DefaultTableModel(new Object[]{"Titol","Nota","Estat","Object"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };




        //Anem a definir l'estructura de la taula de les matrícules
        modelTaulaFitxa = new DefaultTableModel(new Object[]{"Característica", "Valor"}, 0) {
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                //Fem que cap cel·la es pugui editar
                return false;
            }

            //Definim el tipo de cada columna
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    case 1:
                        return String.class;
                    default:
                        return Object.class;
                }
            }
        };



        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(Pelicula.Fitxa.Caracteristica.values());



    }
}
