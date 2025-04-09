package modele.jeu;


import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

import javax.print.event.PrintJobEvent;

public class Jeu extends Thread{
    private Plateau plateau;
    private Joueur j1;
    private Joueur j2;
    protected Coup coupRecu;

    private Roi roi;

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();

        j1 = new Joueur(this);
        j2 = new Joueur(this);

        start();

    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void placerPieces() {

        plateau.placerPieces();
    }


    public void envoyerCoup(Coup coup) {
        Case dep = coup.dep;
        Case arr = coup.arr;
        Piece piece = dep.getPiece();

        // Vérifier si la pièce existe et si la case d'arrivée est dans ses cases accessibles
        if (piece != null) {
            ArrayList<Case> casesAccessibles = piece.getCasesAccessibles();

            if (casesAccessibles.contains(arr)) {
                // Capturer la pièce adverse si présente
                if (arr.getPiece() != null) {
                    arr.quitterLaCase();
                }

                // Déplacer la pièce
                dep.quitterLaCase();
                piece.setCase(arr);
                // Notifier les observateurs
                plateau.notifierObservateurs();
            }
        }
    }


    public void appliquerCoup(Coup coup) {
        plateau.deplacerPiece(coup.dep, coup.arr);
    }

    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            Coup c = j1.getCoup();
            appliquerCoup(c);
        }

    }




}
