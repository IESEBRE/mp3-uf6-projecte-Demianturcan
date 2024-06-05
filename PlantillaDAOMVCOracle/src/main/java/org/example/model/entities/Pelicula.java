package org.example.model.entities;

import java.util.Collection;
import java.util.TreeSet;

public class Pelicula {

    private Long id;
    private String titol;

    private double nota;
    private Object estat;

    private Collection<Fitxa> fitxes;



    public Pelicula(){}

    public Pelicula(String titol, double nota, Object estat, Collection<Fitxa> fitxes) {

        this.titol = titol;
        this.nota = nota;
        this.estat=estat;
        this.fitxes=fitxes;
    }

    public Pelicula(Long id, String titol) {
        this.id = id;
        this.titol = titol;
    }

    public Pelicula(long id, String titol, double nota) {
        this.id = id;
        this.titol = titol;
        this.nota = nota;
    }

    public Pelicula(long id, String titol, double nota, Object estat, TreeSet<Fitxa> fitxes) {
        this.id = id;
        this.titol = titol;
        this.nota = nota;
        this.estat=estat;
        this.fitxes = fitxes;
    }

    public Collection<Pelicula.Fitxa> getFitxes() {
        return fitxes;
    }

    private void setFitxes(Collection<Fitxa> fitxes) {
        this.fitxes = fitxes;
    }

    public Long getId() {
        return id;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String nom) {
        this.titol = titol;
    }

    public double getNota() {
        return nota;
    }

    public void setNota (double pes) {
        this.nota = nota;
    }

    public Object getEstat() {
        return estat;
    }

    public void setEstat(Object estat) {
        this.estat = estat;
    }

    public static class Fitxa implements Comparable<Fitxa>{

        @Override
        public int compareTo(Fitxa o) {
            return this.caract.compareTo(o.getCaract());
        }

        public static enum Caracteristica {
            A("Gèneres"),  B("Sinopsi"), C("Any"),
            D("Direcció"), E("País"), F("Premis"),
            G("Idioma"), I("Anàlisi");

            private String nom;

            private Caracteristica(String nom) {
                this.nom = nom;
            }

            public String getNom() {
                return nom;
            }

            @Override
            public String toString() {
                return nom;
            }
        }

        private Caracteristica caract;
        private String valor;

        public Fitxa(Fitxa.Caracteristica caract, String valor) {
            this.caract = caract;
            this.valor = valor;
        }

        public Caracteristica getCaract() {
            return caract;
        }

        public void setCaract(Caracteristica modul) {
            this.caract = modul;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }



}

