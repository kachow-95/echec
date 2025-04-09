package modele.plateau;

import modele.jeu.Piece;
import java.awt.Point;

import java.util.ArrayList;

public class DecorateurCasesPion extends DecorateurCasesAccessibles {
    public DecorateurCasesPion(DecorateurCasesAccessibles _baseDecorateur) {
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

        int direction = piece.estBlanc() ? -1 : 1; // Direction de déplacement (vers le haut pour les blancs, vers le bas pour les noirs)
        boolean premierCoup = (piece.estBlanc() && position.y == 6) || (!piece.estBlanc() && position.y == 1);

        // Avancée simple
        int newY = position.y + direction;
        if (newY >= 0 && newY < Plateau.SIZE_Y) {
            Case[][] cases = plateau.getCases();
            Case caseDavant = cases[position.x][newY];

            // Vérifier si la case devant est vide
            if (caseDavant.getPiece() == null) {
                casesAccessibles.add(caseDavant);

                // Double avancée (premier coup)
                if (premierCoup) {
                    int doublePas = position.y + (2 * direction);
                    if (doublePas >= 0 && doublePas < Plateau.SIZE_Y) {
                        Case caseDoublePas = cases[position.x][doublePas];
                        if (caseDoublePas.getPiece() == null) {
                            casesAccessibles.add(caseDoublePas);
                        }
                    }
                }
            }

            // Capture en diagonale
            for (int dx : new int[]{-1, 1}) {
                int newX = position.x + dx;
                if (newX >= 0 && newX < Plateau.SIZE_X) {
                    Case caseDiag = cases[newX][newY];
                    if (caseDiag.getPiece() != null && caseDiag.getPiece().estBlanc() != piece.estBlanc()) {
                        casesAccessibles.add(caseDiag);
                    }
                }
            }
        }

        return casesAccessibles;
    }
}