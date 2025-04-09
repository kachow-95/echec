package modele.plateau;

import modele.jeu.Piece;
import java.awt.Point;

import java.util.ArrayList;

public class DecorateurCasesCavalier extends DecorateurCasesAccessibles {
    public DecorateurCasesCavalier(DecorateurCasesAccessibles _baseDecorateur) {
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

        // Définir les mouvements en L du cavalier
        int[][] mouvements = {
                {1, 2}, {2, 1}, {2, -1}, {1, -2},
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
        };

        for (int[] mouvement : mouvements) {
            int newX = position.x + mouvement[0];
            int newY = position.y + mouvement[1];

            // Vérifier si la position est dans la grille
            if (newX >= 0 && newX < Plateau.SIZE_X && newY >= 0 && newY < Plateau.SIZE_Y) {
                Case[][] cases = plateau.getCases();
                Case caseCandidate = cases[newX][newY];

                // Vérifier si la case est vide ou contient une pièce adverse
                if (caseCandidate.getPiece() == null ||
                        caseCandidate.getPiece().estBlanc() != piece.estBlanc()) {
                    casesAccessibles.add(caseCandidate);
                }
            }
        }

        return casesAccessibles;
    }
}