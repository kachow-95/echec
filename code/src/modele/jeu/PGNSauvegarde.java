package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.io.*;
import java.awt.Point;
import java.util.ArrayList;

public class PGNSauvegarde {

    // Convertit un Point en notation d'echecs
    private static String convertirCoordonnees(Point p) {
        char colonne = (char) ('a' + p.x);
        int ligne = 8 - p.y;
        return "" + colonne + ligne;
    }

    // Convertit une chaîne PGN en Points
    private static Point convertirDepuisPGN(String pgn) {
        char colonne = pgn.charAt(0);
        int ligne = Character.getNumericValue(pgn.charAt(1));

        int x = colonne - 'a';
        int y = 8 - ligne;
        return new Point(x, y);
    }

    // Sauvegarder la partie dans un fichier PGN
    public static void sauvegarderPartie(ArrayList<Coup> coups, Plateau plateau, String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            int numeroCoup = 1;
            for (int i = 0; i < coups.size(); i++) {
                Coup coup = coups.get(i);
                Point posDep = plateau.getPositionCase(coup.getDep());
                Point posArr = plateau.getPositionCase(coup.getArr());

                String coupPGN = convertirCoordonnees(posDep) + convertirCoordonnees(posArr);

                if (i % 2 == 0) {
                    writer.write(numeroCoup + ". ");
                    numeroCoup++;
                }

                writer.write(coupPGN + " ");
            }
            writer.newLine();
            System.out.println("Partie sauvegardée!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Charger une partie à partir d’un fichier PGN
    public static void chargerPartie(String cheminFichier, Jeu jeu) {
        Plateau plateau = jeu.getPlateau();

        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] tokens = ligne.split(" ");
                for (String token : tokens) {
                    // prend en compte que les positions
                    if (token.contains(".")) continue;

                    if (token.length() == 4) {
                        String fromStr = token.substring(0, 2);
                        String toStr = token.substring(2, 4);

                        Point from = convertirDepuisPGN(fromStr);
                        Point to = convertirDepuisPGN(toStr);

                        Case caseDepart = plateau.getCases()[from.x][from.y];
                        Case caseArrivee = plateau.getCases()[to.x][to.y];

                        if (caseDepart != null && caseArrivee != null && caseDepart.getPiece() != null) {
                            Coup coup = new Coup(caseDepart, caseArrivee);
                            jeu.appliquerCoup(coup);
                            jeu.getHistorique().add(coup); // si getHistorique() existe
                            jeu.changerTour();
                        }
                    }
                }
            }
            System.out.println("Partie chargée!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
