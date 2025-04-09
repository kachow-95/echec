package modele.jeu;

import modele.plateau.*;

public class Dame extends Piece {
    public Dame(Plateau _plateau, boolean _estBlanc) {
        super(_plateau, _estBlanc);
        // Combiner les deux décorateurs pour la dame
        DecorateurCasesAccessibles enLigne = new DecorateurCasesEnLigne(null);
        DecorateurCasesAccessibles enDiagonale = new DecorateurCasesEnDiagonale(enLigne);
        casesAccessibles = enDiagonale;
    }

}
