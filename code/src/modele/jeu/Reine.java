package modele.jeu;

import modele.plateau.*;

import java.util.ArrayList;

public class Reine extends Piece
{
    public Reine(Plateau _plateau) {
        super(_plateau);
        casesAccessibles = new DecorateurCasesEnLigne(new DecorateurCasesEnDiagonale(null));

        // le décorateur récupère les cases en diagonale et en ligne
        // ArrayList<Case> lst = casesAccessibles.getCasesAccessibles();

    }


}