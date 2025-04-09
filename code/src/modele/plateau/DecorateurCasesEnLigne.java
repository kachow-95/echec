package modele.plateau;

import java.awt.Point;
import java.util.ArrayList;

public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {
    public DecorateurCasesEnLigne(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> casesAccessibles = new ArrayList<>();

        if (piece == null || plateau == null || piece.getCase() == null) {
            return casesAccessibles;
        }

        Case caseActuelle = piece.getCase();
        Point position = plateau.getPositionCase(caseActuelle);

        if (position == null) {
            return casesAccessibles;
        }

        // Déplacement vers le haut (y décroissant)
        explorerDirection(position, 0, -1, casesAccessibles);

        // Déplacement vers le bas (y croissant)
        explorerDirection(position, 0, 1, casesAccessibles);

        // Déplacement vers la gauche (x décroissant)
        explorerDirection(position, -1, 0, casesAccessibles);

        // Déplacement vers la droite (x croissant)
        explorerDirection(position, 1, 0, casesAccessibles);

        return casesAccessibles;
    }

    private void explorerDirection(Point position, int dx, int dy, ArrayList<Case> casesAccessibles) {
        int limitePas = (piece instanceof modele.jeu.Roi) ? 1 : 7; // Le roi ne peut se déplacer que d'une case

        for (int pas = 1; pas <= limitePas; pas++) {
            int newX = position.x + (dx * pas);
            int newY = position.y + (dy * pas);

            // Vérifier si la position est dans la grille
            if (newX >= 0 && newX < Plateau.SIZE_X && newY >= 0 && newY < Plateau.SIZE_Y) {
                Case[][] cases = plateau.getCases();
                Case caseCandidate = cases[newX][newY];

                // Vérifier si la case est vide
                if (caseCandidate.getPiece() == null) {
                    casesAccessibles.add(caseCandidate);
                } else if (caseCandidate.getPiece().estBlanc() != piece.estBlanc()) {
                    // Si la case contient une pièce adverse, on peut y aller mais pas plus loin
                    casesAccessibles.add(caseCandidate);
                    break;
                } else {
                    // Case contenant une pièce de même couleur, on ne peut pas y aller
                    break;
                }
            } else {
                // Hors de la grille
                break;
            }
        }
    }
}