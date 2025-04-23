package modele.jeu;

import java.util.ArrayList;

import modele.plateau.Case;
import modele.plateau.DecorateurCasesAccessibles;
import modele.plateau.Direction;
import modele.plateau.Plateau;

/**
 * Entités amenées à bouger
 */
public abstract class Piece {

    protected Case c;
    protected Plateau plateau;
    protected DecorateurCasesAccessibles casesAccessibles;
    protected boolean estBlanc;

    public Piece(Plateau _plateau, boolean _estBlanc) {
        plateau = _plateau;
        estBlanc = _estBlanc;
    }

    public void quitterCase() {
        c.quitterLaCase();
    }
    public void allerSurCase(Case _c) {
        if (c != null) {
            quitterCase();
        }
        c = _c;
        plateau.arriverCase(c, this);
    }

    public Case getCase() {
        return c;
    }

    public boolean estBlanc() {
        return estBlanc;
    }

    public void setCase(Case _case) {
        c = _case;
        // Lier la pièce à la case
        if (_case != null) {
            _case.p = this;
        }
    }

    public ArrayList<Case> getCasesAccessibles() {
        if (casesAccessibles != null) {
            casesAccessibles.setPieceEtPlateau(this, plateau);
            return casesAccessibles.getCasesAccessibles();
        }
        return new ArrayList<>();
    }
    public String getType() {
        // redefini fonction dans pion et roi (pour promouvoir le pion et pour verifier si le roi est en echec)
        return "Pièce inconnue";
    }

    /**
     * Vérifie si les cases accessibles de cette pièce contiennent un Roi.
     * Retourne la case contenant le Roi si trouvé, sinon retourne null.
     * @return La case contenant le Roi, ou null si aucun Roi n'est trouvé.
     */
    public Case contientRoi() {
        ArrayList<Case> casesAccessibles = getCasesAccessibles();

        for (Case caseAccessible : casesAccessibles) {
            if (caseAccessible.getPiece() != null) {
                Piece pieceAccessible = caseAccessible.getPiece();
                if (pieceAccessible.getType().equals("Roi")) {
                    return caseAccessible;  // Retourne la case contenant le Roi
                }
            }
        }
        return null;  // Aucune case accessible ne contient un Roi
    }
}
