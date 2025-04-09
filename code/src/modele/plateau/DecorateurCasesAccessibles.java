package modele.plateau;

import modele.jeu.Piece;

import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {

    protected Plateau plateau; // Changé en protected pour l'accès dans les sous-classes
    protected Piece piece; // Changé en protected pour l'accès dans les sous-classes

    private DecorateurCasesAccessibles base;

    public DecorateurCasesAccessibles(DecorateurCasesAccessibles _baseDecorateur) {
        base = _baseDecorateur;
    }

    // Ajouter cette méthode pour configurer la pièce et le plateau
    public void setPieceEtPlateau(Piece _piece, Plateau _plateau) {
        this.piece = _piece;
        this.plateau = _plateau;

        // Propager aux décorateurs de base
        if (base != null) {
            base.setPieceEtPlateau(_piece, _plateau);
        }
    }

    public ArrayList<Case> getCasesAccessibles() {
        ArrayList<Case> retour = getMesCasesAccessibles();

        if (base != null) {
            retour.addAll(base.getCasesAccessibles());
        }

        return retour;
    }

    public abstract ArrayList<Case> getMesCasesAccessibles();
}