/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;


import modele.jeu.*;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;


public class Plateau extends Observable {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;


    private HashMap<Case, Point> map = new  HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }

        }

    }

    public void placerPieces() {
        Roi roi = new Roi(this);
        Case cK = grilleCases[4][7];
        roi.allerSurCase(cK);

        Reine reine = new Reine(this);
        Case cQ = grilleCases[3][7];
        reine.allerSurCase(cQ);

        Tour tour = new Tour(this);
        Case cR = grilleCases[0][7];
        tour.allerSurCase(cR);
        Case cR2 = grilleCases[7][7];
        tour.allerSurCase(cR2);

        Fou fou = new Fou(this);
        Case cB = grilleCases[2][7];
        fou.allerSurCase(cB);
        Case cB2 = grilleCases[5][7];
        fou.allerSurCase(cB2);

        Cheval cheval = new Cheval(this);
        Case cN = grilleCases[1][7];
        cheval.allerSurCase(cN);
        Case cN2 = grilleCases[6][7];
        cheval.allerSurCase(cN2);

        Pion pion = new Pion(this);
        Case cP = grilleCases[0][6];
        cheval.allerSurCase(cP);
        Case cP1 = grilleCases[1][6];
        cheval.allerSurCase(cP1);
        Case cP2 = grilleCases[2][6];
        cheval.allerSurCase(cP2);
        Case cP3 = grilleCases[3][6];
        cheval.allerSurCase(cP3);
        Case cP4 = grilleCases[4][6];
        cheval.allerSurCase(cP4);
        Case cP5 = grilleCases[5][6];
        cheval.allerSurCase(cP5);









        setChanged();
        notifyObservers();

    }

    public void arriverCase(Case c, Piece p) {

        c.p = p;

    }

    public void deplacerPiece(Case c1, Case c2) {
        if (c1.p != null) {
            c1.p.allerSurCase(c2);

        }
        setChanged();
        notifyObservers();

    }


    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Case caseALaPosition(Point p) {
        Case retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }


}
