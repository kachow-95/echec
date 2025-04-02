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
        placerPiece(new Roi(this), 4, 7, true);
        placerPiece(new Reine(this), 3, 7, true);
        placerPiece(new Tour(this), 0, 7, true);
        placerPiece(new Tour(this), 7, 7, true);
        placerPiece(new Fou(this), 2, 7, true);
        placerPiece(new Fou(this), 5, 7, true);
        placerPiece(new Cheval(this), 1, 7, true);
        placerPiece(new Cheval(this), 6, 7, true);
        for (int i = 0; i < SIZE_X; i++) {
            placerPiece(new Pion(this), i, 6, true);
        }

        placerPiece(new Roi(this), 4, 0, false);
        placerPiece(new Reine(this), 3, 0, false);
        placerPiece(new Tour(this), 0, 0, false);
        placerPiece(new Tour(this), 7, 0, false);
        placerPiece(new Fou(this), 2, 0, false);
        placerPiece(new Fou(this), 5, 0, false);
        placerPiece(new Cheval(this), 1, 0, false);
        placerPiece(new Cheval(this), 6, 0, false);
        for (int i = 0; i < SIZE_X; i++) {
            placerPiece(new Pion(this), i, 1, false);
        }


        setChanged();
        notifyObservers();

    }

    private void placerPiece(Piece piece, int x, int y, boolean color) {
        Case c = grilleCases[x][y];
        piece.allerSurCase(c);
        piece.setColor(color);
        map.put(c, new Point(x, y));
    }

    public void arriverCase(Case c, Piece p) {

        //Case dep = p.getCase();
        //dep.p = null;


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
