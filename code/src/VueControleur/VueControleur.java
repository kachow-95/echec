package VueControleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import modele.jeu.Cavalier;
import modele.jeu.Coup;
import modele.jeu.Dame;
import modele.jeu.Fou;
import modele.jeu.Jeu;
import modele.plateau.Case;
import modele.jeu.Piece;
import modele.jeu.Pion;
import modele.jeu.Roi;
import modele.jeu.Tour;
import modele.plateau.Plateau;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (clic position départ -> position arrivée pièce))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 50; // nombre de pixel par case
    // icones affichées dans la grille
    private ImageIcon icoRoiNoir;
    private ImageIcon icoRoiBlanc;
    private ImageIcon icoDameBlanc;
    private ImageIcon icoDameNoir;
    private ImageIcon icoFouBlanc;
    private ImageIcon icoFouNoir;
    private ImageIcon icoCavalierBlanc;
    private ImageIcon icoCavalierNoir;
    private ImageIcon icoTourBlanc;
    private ImageIcon icoTourNoir;
    private ImageIcon icoPionBlanc;
    private ImageIcon icoPionNoir;

    private Case caseClic1; // mémorisation des cases cliquées
    private Case caseClic2;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;



        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();

    }


    private void chargerLesIcones() {
        icoRoiBlanc = chargerIcone("Images/wK.png");
        icoDameBlanc = chargerIcone("Images/wQ.png");
        icoFouBlanc = chargerIcone("Images/wB.png");
        icoCavalierBlanc = chargerIcone("Images/wN.png");
        icoTourBlanc = chargerIcone("Images/wR.png");
        icoPionBlanc = chargerIcone("Images/wP.png");

        icoRoiNoir = chargerIcone("Images/bK.png");
        icoDameNoir = chargerIcone("Images/bQ.png");
        icoFouNoir = chargerIcone("Images/bB.png");
        icoCavalierNoir = chargerIcone("Images/bN.png");
        icoTourNoir = chargerIcone("Images/bR.png");
        icoPionNoir = chargerIcone("Images/bP.png");

    }
    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        ImageIcon icon = new ImageIcon(urlIcone);

        // Redimensionner l'icône
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);

        return resizedIcon;
    }



    // Ajoutez ces méthodes à VueControleur.java
    private ArrayList<Case> casesMarquees = new ArrayList<>();
    private Color couleurOriginaleCasePaire = new Color(150, 150, 210);
    private Color couleurOriginaleCaseImpaire = new Color(50, 50, 110);
    private Color couleurCaseAccessible = new Color(255, 255, 0, 100); // Jaune semi-transparent

    private void marquerCasesAccessibles(ArrayList<Case> cases) {
        // Sauvegarder les cases marquées pour pouvoir les effacer plus tard
        casesMarquees.clear();
        casesMarquees.addAll(cases);

        // Marquer chaque case accessible
        for (Case c : cases) {
            Point pos = plateau.getPositionCase(c);
            if (pos != null) {
                int x = pos.x;
                int y = pos.y;

                // Changer la couleur de fond de la case
                tabJLabel[x][y].setBackground(couleurCaseAccessible);
            }
        }
    }

    private void effacerMarquageCasesAccessibles() {
        for (Case c : casesMarquees) {
            Point pos = plateau.getPositionCase(c);
            if (pos != null) {
                int x = pos.x;
                int y = pos.y;

                // Restaurer la couleur d'origine
                if ((y%2 == 0 && x%2 == 0) || (y%2 != 0 && x%2 != 0)) {
                    tabJLabel[x][y].setBackground(couleurOriginaleCaseImpaire);
                } else {
                    tabJLabel[x][y].setBackground(couleurOriginaleCasePaire);
                }
            }
        }
        casesMarquees.clear();
    }


    private void placerLesComposantsGraphiques() {
        setTitle("Jeu d'Échecs");
        setResizable(false);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille


        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();

                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )

                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                // Modifiez la méthode mouseClicked dans VueControleur.java
                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (caseClic1 == null) {
                            caseClic1 = plateau.getCases()[xx][yy];

                            // Vérifie s'il y a une pièce sur la case et si c'est au tour du joueur
                            if (caseClic1.getPiece() != null) {
                                // On obtient les cases accessibles
                                ArrayList<Case> casesAccessibles = caseClic1.getPiece().getCasesAccessibles();

                                // Si aucune case accessible, annuler la sélection
                                if (casesAccessibles.isEmpty()) {
                                    caseClic1 = null;
                                } else {
                                    // Marquer ces cases pour l'affichage
                                    marquerCasesAccessibles(casesAccessibles);
                                }
                            } else {
                                // Pas de pièce sur la case, on réinitialise
                                caseClic1 = null;
                            }
                        } else {
                            caseClic2 = plateau.getCases()[xx][yy];

                            // Réinitialiser le marquage des cases accessibles
                            effacerMarquageCasesAccessibles();

                            // Vérifier si la case d'arrivée fait partie des cases accessibles
                            ArrayList<Case> casesAccessibles = caseClic1.getPiece().getCasesAccessibles();
                            if (casesAccessibles.contains(caseClic2)) {
                                // Envoyer le coup
                                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                            }

                            // Réinitialiser les clics
                            caseClic1 = null;
                            caseClic2 = null;
                        }
                    }
                });


                jlab.setOpaque(true);

                if ((y%2 == 0 && x%2 == 0) || (y%2 != 0 && x%2 != 0)) {
                    tabJLabel[x][y].setBackground(new Color(50, 50, 110));
                } else {
                    tabJLabel[x][y].setBackground(new Color(150, 150, 210));
                }

                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Case c = plateau.getCases()[x][y];

                if (c != null) {

                    Piece e = c.getPiece();

                    if (e != null) {
                        if (e instanceof Roi) {
                            tabJLabel[x][y].setIcon(e.estBlanc() ? icoRoiBlanc : icoRoiNoir);
                        } else if (e instanceof Dame) {
                            tabJLabel[x][y].setIcon(e.estBlanc() ? icoDameBlanc : icoDameNoir);
                        } else if (e instanceof Tour) {
                            tabJLabel[x][y].setIcon(e.estBlanc() ? icoTourBlanc : icoTourNoir);
                        } else if (e instanceof Fou) {
                            tabJLabel[x][y].setIcon(e.estBlanc() ? icoFouBlanc : icoFouNoir);
                        } else if (e instanceof Cavalier) {
                            tabJLabel[x][y].setIcon(e.estBlanc() ? icoCavalierBlanc : icoCavalierNoir);
                        } else if (e instanceof Pion) {
                            tabJLabel[x][y].setIcon(e.estBlanc() ? icoPionBlanc : icoPionNoir);
                        }
                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }


                }

            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*

        // récupérer le processus graphique pour rafraichir
        // (normalement, à l'inverse, a l'appel du modèle depuis le contrôleur, utiliser un autre processus, voir classe Executor)


        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
