package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        com.example.GestionPharmacie gestionPharmacie = new com.example.GestionPharmacie();
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    System.out.println("Ajouter un médicament:");
                    System.out.print("ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Nom: ");
                    String nom = scanner.nextLine();
                    System.out.print("Prix: ");
                    double prix = scanner.nextDouble();
                    scanner.nextLine(); // Consommer la nouvelle ligne
                    System.out.print("Type (1 pour Vente Libre, 2 pour Ordonnance): ");
                    int type = scanner.nextInt();
                    scanner.nextLine(); // Consommer la nouvelle ligne

                    if (type == 1) {
                        gestionPharmacie.ajouterMedicament(new MedicamentVenteLibre(id, nom, prix));
                    } else if (type == 2) {
                        System.out.print("Dosage: ");
                        String dosage = scanner.nextLine();
                        gestionPharmacie.ajouterMedicament(new MedicamentOrdonnance(id, nom, prix, dosage));
                    }
                    break;
                case 2:
                    System.out.print("ID du médicament à supprimer: ");
                    id = scanner.nextLine();
                    gestionPharmacie.supprimerMedicament(id);
                    break;
                case 3:
                    System.out.print("ID du médicament à rechercher: ");
                    id = scanner.nextLine();
                    Medicament medicament = gestionPharmacie.rechercherMedicamentParId(id);
                    if (medicament != null) {
                        System.out.println("Nom: " + medicament.getNom() + ", Prix: " + medicament.getPrix() + ", Type: " + medicament.getType());
                    } else {
                        System.out.println("Médicament non trouvé.");
                    }
                    break;
                case 4:
                    System.out.print("Type de médicament à afficher (Vente Libre/Ordonnance): ");
                    String typeMedicament = scanner.nextLine();
                    gestionPharmacie.afficherMedicamentsParType(typeMedicament);
                    break;
                case 5:
                    System.out.print("ID du médicament à modifier: ");
                    id = scanner.nextLine();
                    System.out.print("Nouveau nom: ");
                    nom = scanner.nextLine();
                    System.out.print("Nouveau prix: ");
                    prix = scanner.nextDouble();
                    scanner.nextLine(); // Consommer la nouvelle ligne
                    gestionPharmacie.modifierMedicament(id, nom, prix);
                    break;
                case 6:
                    System.out.print("Lettre alphabétique: ");
                    char lettre = scanner.nextLine().charAt(0);
                    gestionPharmacie.listerMedicamentsParLettre(lettre);
                    break;
                case 7:
                    System.out.println("Nombre de médicaments en stock: " + gestionPharmacie.nombreDeMedicamentsEnStock());
                    break;
                case 8:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
        scanner.close();
    }

    public static void afficherMenu() {
        System.out.println("1. Ajouter un médicament");
        System.out.println("2. Supprimer un médicament");
        System.out.println("3. Rechercher un médicament par ID");
        System.out.println("4. Afficher la liste des médicaments par type");
        System.out.println("5. Modifier un médicament par ID");
        System.out.println("6. Lister les médicaments par ordre alphabétique");
        System.out.println("7. Afficher le nombre de médicaments en stock");
        System.out.println("8. Quitter");
    }
}
