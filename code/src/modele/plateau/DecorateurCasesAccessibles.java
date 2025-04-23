package modele.plateau;

import modele.jeu.Piece;

import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {

    protected Plateau plateau; // protected pour que les sous-classes puisse y acceder
    protected Piece piece; // protected pour que les sous-classes puisse y acceder

    private DecorateurCasesAccessibles base;

    public DecorateurCasesAccessibles(DecorateurCasesAccessibles _baseDecorateur) {
        base = _baseDecorateur;
    }

    // configuration de la pièce et le plateau
    public void setPieceEtPlateau(Piece _piece, Plateau _plateau) {
        this.piece = _piece;
        this.plateau = _plateau;
        
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