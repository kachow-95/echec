package modele.jeu;

import modele.plateau.Case;
import modele.plateau.DecorateurCasesAccessibles;
import modele.plateau.Direction;
import modele.plateau.Plateau;

/**
 * Entités amenées à bouger
 */
public abstract class Piece<coul> {

    protected Case c;
    protected Plateau plateau;
    protected DecorateurCasesAccessibles casesAccessibles;
    protected boolean couleur;

    public Piece(Plateau _plateau) {
        plateau = _plateau;

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

    public void setColor(boolean coul){
        this.couleur=coul;
    }

    public boolean getColor(){
        return this.couleur;
    }






}
