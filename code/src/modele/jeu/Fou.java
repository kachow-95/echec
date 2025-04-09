package modele.jeu;

import modele.plateau.*;

public class Fou extends Piece {
    public Fou(Plateau _plateau, boolean _estBlanc) {
        super(_plateau, _estBlanc);
        casesAccessibles = new DecorateurCasesEnDiagonale(null);
    }
}

