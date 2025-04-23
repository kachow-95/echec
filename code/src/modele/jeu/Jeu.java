package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;
import java.awt.Point;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur j1;
    private Joueur j2;
    public Coup coupRecu;
    private boolean tourJoueur1;
    private boolean echecEtMat;
    private boolean enEchec = false;
    private ArrayList<Coup> historiqueCoups = new ArrayList<>();

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();

        j1 = new Joueur(this);
        j2 = new Joueur(this);
        tourJoueur1 = true;  // Commence avec le joueur 1 (blanc)
        echecEtMat = false;  // ➕ initialisation
        start();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void envoyerCoup(Coup coup) {
        Case dep = coup.dep;
        Case arr = coup.arr;
        Piece piece = dep.getPiece();

        if (piece != null) {
            ArrayList<Case> casesAccessibles = piece.getCasesAccessibles();

            if (casesAccessibles.contains(arr)) {
                if (arr.getPiece() != null) {
                    arr.quitterLaCase();
                }

                dep.quitterLaCase();
                piece.setCase(arr);
                plateau.notifierObservateurs();

            }
        }
    }

    public void appliquerCoup(Coup coup) {
        plateau.deplacerPiece(coup.dep, coup.arr);
        historiqueCoups.add(coup);

    }

    public boolean estTourDesBlancs() {
        return tourJoueur1;
    }
    public ArrayList<Coup> getHistorique() {
        return historiqueCoups;
    }

    public void changerTour() {
        tourJoueur1 = !tourJoueur1;
        enEchec = verifierRoiEnDanger() != null;
        // Vérifie l’échec et mat
        if (estEchecEtMat()) {
            echecEtMat = true;
            System.out.println("Échec et mat !");
        }
    }

    public boolean verifierPromotionPion(Case caseArrivee) {
        Piece piece = caseArrivee.getPiece();
        if (piece != null && piece.getType().equals("Pion")) {
            boolean estBlanc = piece.estBlanc();
            int lignePromotion = estBlanc ? 0 : plateau.SIZE_Y - 1;

            Point position = plateau.getPositionCase(caseArrivee);
            if (position != null && position.y == lignePromotion) {
                plateau.setPionPromouvable(piece);
                return true;
            }
        }
        return false;
    }


    public boolean isEnEchec() {
        return enEchec;
    }

    public boolean isEchecEtMat() {
        return echecEtMat;
    }

    @Override
    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {
        while (true) {
            if (tourJoueur1) {
                Coup c = j1.getCoup();  // Le joueur 1 (blanc) joue
                appliquerCoup(c);
            } else {
                Coup c = j2.getCoup();  // Le joueur 2 (noir) joue
                appliquerCoup(c);
            }
        }
    }

    public Case verifierRoiEnDanger() {
        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case casePiece = plateau.getCases()[x][y];
                Piece piece = casePiece.getPiece();

                if (piece != null) {
                    Case caseRoi = piece.contientRoi();
                    if (caseRoi != null) {
                        return caseRoi;
                    }
                }
            }
        }
        return null;
    }

    public boolean estEchecEtMat() {
        Case caseRoi = verifierRoiEnDanger();
        if (caseRoi == null) return false;

        Piece roi = caseRoi.getPiece();
        if (roi == null || !(roi instanceof Roi)) return false;

        boolean roiBlanc = roi.estBlanc();

        for (int x = 0; x < plateau.SIZE_X; x++) {
            for (int y = 0; y < plateau.SIZE_Y; y++) {
                Case c = plateau.getCases()[x][y];
                Piece p = c.getPiece();

                if (p != null && p.estBlanc() == roiBlanc) {
                    ArrayList<Case> acces = p.getCasesAccessibles();

                    for (Case cible : acces) {
                        Piece ancienne = cible.getPiece();
                        plateau.deplacerPiece(c, cible);
                        Case dangerApres = verifierRoiEnDanger();
                        plateau.deplacerPiece(cible, c);
                        cible.setPiece(ancienne);

                        if (dangerApres == null) return false;
                    }
                }
            }
        }

        return true;
    }
    public ArrayList<Case> getCoupsFiltres(Piece piece) {
        ArrayList<Case> coupsValidés = new ArrayList<>();
        Case caseDepart = piece.getCase();

        for (Case caseCible : piece.getCasesAccessibles()) {
            Piece pieceCapturee = caseCible.getPiece();

            // Simule le déplacement
            plateau.deplacerPiece(caseDepart, caseCible);

            // Vérifie si le roi est encore en danger
            boolean roiEnDangerApres = verifierRoiEnDanger() != null;

            // Revenir à l'état initial
            plateau.deplacerPiece(caseCible, caseDepart);
            caseCible.setPiece(pieceCapturee);

            // Si le roi n'est pas en danger, le coup est valide
            if (!roiEnDangerApres) {
                coupsValidés.add(caseCible);
            }
        }

        return coupsValidés;
    }

}
