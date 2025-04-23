package modele.jeu;

import modele.plateau.*;

public class Tour extends Piece {
    public Tour(Plateau _plateau, boolean _estBlanc) {
        super(_plateau, _estBlanc);
        casesAccessibles = new DecorateurCasesEnLigne(null);
    }
}