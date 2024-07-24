package com.example;



abstract class Medicament {
    protected String id;
    protected String nom;
    protected double prix;

    public Medicament(String id, String nom, double prix) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public abstract String getType();

    public void setNom(String nom) {
    }

    public void setPrix(double prix) {
    }
}
