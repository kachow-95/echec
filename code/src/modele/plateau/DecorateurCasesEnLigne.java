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

        // vecteur vers le haut
        explorerDirection(position, 0, -1, casesAccessibles);

        // vecteur vers le bas
        explorerDirection(position, 0, 1, casesAccessibles);

        // vecteur vers la gauche
        explorerDirection(position, -1, 0, casesAccessibles);

        // vecteur vers la droite
        explorerDirection(position, 1, 0, casesAccessibles);

        return casesAccessibles;
    }

    private void explorerDirection(Point position, int dx, int dy, ArrayList<Case> casesAccessibles) {
        int limitePas = (piece instanceof modele.jeu.Roi) ? 1 : 7; // une case pour le roi et 7 pour les autres

        for (int pas = 1; pas <= limitePas; pas++) {
            int newX = position.x + (dx * pas);
            int newY = position.y + (dy * pas);

            // Vérifie si la position est dans le plateau
            if (newX >= 0 && newX < Plateau.SIZE_X && newY >= 0 && newY < Plateau.SIZE_Y) {
                Case[][] cases = plateau.getCases();
                Case caseCandidate = cases[newX][newY];

                // case vide?
                if (caseCandidate.getPiece() == null) {
                    casesAccessibles.add(caseCandidate);
                } else if (caseCandidate.getPiece().estBlanc() != piece.estBlanc()) {
                    // Si case contient une pièce ennemie, on peut y aller
                    casesAccessibles.add(caseCandidate);
                    break;
                } else {
                    // Case contient une pièce alliée, on ne peut pas y aller
                    break;
                }
            } else {
                // Hors du plateau
                break;
            }
        }
    }
}