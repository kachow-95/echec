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
        Tour tour1 = new Tour(this);
        Case cR2 = grilleCases[7][7];
        tour1.allerSurCase(cR2);

        Fou fou = new Fou(this);
        Case cB = grilleCases[2][7];
        fou.allerSurCase(cB);
        Fou fou1 = new Fou(this);
        Case cB2 = grilleCases[5][7];
        fou1.allerSurCase(cB2);

        Cheval cheval = new Cheval(this);
        Case cN = grilleCases[1][7];
        cheval.allerSurCase(cN);
        Cheval cheval1 = new Cheval(this);
        Case cN2 = grilleCases[6][7];
        cheval1.allerSurCase(cN2);

        Pion pion = new Pion(this);
        Case cP = grilleCases[0][6];
        pion.allerSurCase(cP);
        Pion pion1 = new Pion(this);
        Case cP1 = grilleCases[1][6];
        pion1.allerSurCase(cP1);
        Pion pion2 = new Pion(this);
        Case cP2 = grilleCases[2][6];
        pion2.allerSurCase(cP2);
        Pion pion3 = new Pion(this);
        Case cP3 = grilleCases[3][6];
        pion3.allerSurCase(cP3);
        Pion pion4 = new Pion(this);
        Case cP4 = grilleCases[4][6];
        pion4.allerSurCase(cP4);
        Pion pion5 = new Pion(this);
        Case cP5 = grilleCases[5][6];
        pion5.allerSurCase(cP5);
        Pion pion6 = new Pion(this);
        Case cP6 = grilleCases[6][6];
        pion6.allerSurCase(cP6);
        Pion pion7 = new Pion(this);
        Case cP7 = grilleCases[7][6];
        pion7.allerSurCase(cP7);

        Roi roiN = new Roi(this);
        Case cKN = grilleCases[4][0];
        roiN.allerSurCase(cKN);

        Reine reineN = new Reine(this);
        Case cQN = grilleCases[3][0];
        reineN.allerSurCase(cQN);

        Tour tourN = new Tour(this);
        Case cRN = grilleCases[0][0];
        tourN.allerSurCase(cRN);
        Tour tourN1 = new Tour(this);
        Case cR2N = grilleCases[7][0];
        tourN1.allerSurCase(cR2N);

        Fou fouN = new Fou(this);
        Case cBN = grilleCases[2][0];
        fouN.allerSurCase(cBN);
        Fou fouN1 = new Fou(this);
        Case cB2N = grilleCases[5][0];
        fouN1.allerSurCase(cB2N);

        Cheval chevalN = new Cheval(this);
        Case cNN = grilleCases[1][0];
        chevalN.allerSurCase(cNN);
        Cheval chevalN1 = new Cheval(this);
        Case cN2N = grilleCases[6][0];
        chevalN1.allerSurCase(cN2N);

        Pion pionN = new Pion(this);
        Case cPN = grilleCases[0][1];
        pionN.allerSurCase(cPN);
        Pion pionN1 = new Pion(this);
        Case cP1N = grilleCases[1][1];
        pionN1.allerSurCase(cP1N);
        Pion pionN2 = new Pion(this);
        Case cPN2 = grilleCases[2][1];
        pionN2.allerSurCase(cPN2);
        Pion pionN3 = new Pion(this);
        Case cPN3 = grilleCases[3][1];
        pionN3.allerSurCase(cPN3);
        Pion pionN4 = new Pion(this);
        Case cPN4 = grilleCases[4][1];
        pionN4.allerSurCase(cPN4);
        Pion pionN5 = new Pion(this);
        Case cPN5 = grilleCases[5][1];
        pionN5.allerSurCase(cPN5);
        Pion pionN6 = new Pion(this);
        Case cPN6 = grilleCases[6][1];
        pionN6.allerSurCase(cPN6);
        Pion pionN7 = new Pion(this);
        Case cPN7 = grilleCases[7][1];
        pionN7.allerSurCase(cPN7);










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
