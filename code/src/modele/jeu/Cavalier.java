package modele.jeu;

import modele.plateau.*;

public class Cavalier extends Piece {
    public Cavalier(Plateau _plateau, boolean _estBlanc) {
        super(_plateau, _estBlanc);
        casesAccessibles = new DecorateurCasesCavalier (null);
    }
}