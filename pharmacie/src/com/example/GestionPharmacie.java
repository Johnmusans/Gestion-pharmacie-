package com.example;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

class GestionPharmacie {
    private final Map<String, Medicament> medicaments;
    private Connection connection;

    public GestionPharmacie() {
        medicaments = new HashMap<>();
        try {
            // Assurez-vous que le pilote JDBC est chargé
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pharmacie", "root", "");
            // Vérifiez si la table existe et créez-la si nécessaire
            String createTableQuery = "CREATE TABLE IF NOT EXISTS medicaments (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nom VARCHAR(100) NOT NULL, " +
                    "prix DOUBLE NOT NULL, " +
                    "type VARCHAR(50) NOT NULL, " +
                    "dosage VARCHAR(50))";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableQuery);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Pilote JDBC MySQL non trouvé.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterMedicament(Medicament medicament) {
        medicaments.put(medicament.getId(), medicament);
        String query = "INSERT INTO medicaments (id, nom, prix, type, dosage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, medicament.getId());
            stmt.setString(2, medicament.getNom());
            stmt.setDouble(3, medicament.getPrix());
            stmt.setString(4, medicament.getType());
            if (medicament instanceof MedicamentOrdonnance) {
                stmt.setString(5, ((MedicamentOrdonnance) medicament).getDosage());
            } else {
                stmt.setString(5, null);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerMedicament(String id) {
        medicaments.remove(id);
        String query = "DELETE FROM medicaments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Medicament rechercherMedicamentParId(String id) {
        Medicament medicament = medicaments.get(id);
        if (medicament == null) {
            String query = "SELECT * FROM medicaments WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String nom = rs.getString("nom");
                        double prix = rs.getDouble("prix");
                        String type = rs.getString("type");
                        if (type.equals("Ordonnance")) {
                            String dosage = rs.getString("dosage");
                            medicament = new MedicamentOrdonnance(id, nom, prix, dosage);
                        } else {
                            medicament = new MedicamentVenteLibre(id, nom, prix);
                        }
                        medicaments.put(id, medicament);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return medicament;
    }

    public void modifierMedicament(String id, String nom, double prix) {
        Medicament medicament = medicaments.get(id);
        if (medicament != null) {
            medicament.setNom(nom);
            medicament.setPrix(prix);
            String query = "UPDATE medicaments SET nom = ?, prix = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, nom);
                stmt.setDouble(2, prix);
                stmt.setString(3, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void afficherMedicamentsParType(String type) {
        String query = "SELECT * FROM medicaments WHERE type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("nom") + " - " + rs.getDouble("prix"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int nombreDeMedicamentsEnStock() {
        return medicaments.size();
    }

    public void listerMedicamentsParLettre(char lettre) {
        lettre = Character.toLowerCase(lettre);
        for (Medicament medicament : medicaments.values()) {
            if (Character.toLowerCase(medicament.getNom().charAt(0)) == lettre) {
                System.out.println(medicament.getNom() + " - " + medicament.getPrix());
            }
        }
    }
}
