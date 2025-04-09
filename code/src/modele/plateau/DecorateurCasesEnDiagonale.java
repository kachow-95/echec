package modele.plateau;

import java.awt.Point;
import java.util.ArrayList;

public class DecorateurCasesEnDiagonale extends DecorateurCasesAccessibles {

    public DecorateurCasesEnDiagonale(DecorateurCasesAccessibles _baseDecorateur) {
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

        // Diagonale haut-gauche
        explorerDirection(position, -1, -1, casesAccessibles);

        // Diagonale haut-droite
        explorerDirection(position, 1, -1, casesAccessibles);

        // Diagonale bas-gauche
        explorerDirection(position, -1, 1, casesAccessibles);

        // Diagonale bas-droite
        explorerDirection(position, 1, 1, casesAccessibles);

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