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

import modele.jeu.PGNSauvegarde;
import java.io.File;


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

    private Case caseClic1; // mémorisation des cases cliquées
    private Case caseClic2;
    private IconManager iconManager= new IconManager();


    private Color couleurCaseRoiEnDanger = new Color(255, 0, 0, 100); // Rouge semi-transparent pour le roi en danger

    private JLabel[][] tabJLabel; //
    private CaseHighlighter caseHighlighter;


    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;

        // Initialisation de CaseHighlighter
        caseHighlighter = new CaseHighlighter(plateau, tabJLabel,
                new Color(255, 255, 0, 100), // couleurAccessible
                new Color(150, 150, 210), // couleurPaire
                new Color(50, 50, 110)); // couleurImpaire

        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();
    }

    private void marquerRoiEnDanger(Case roiCase) {
        if (roiCase != null) {
            Point pos = plateau.getPositionCase(roiCase);
            if (pos != null) {
                int x = pos.x;
                int y = pos.y;
                tabJLabel[x][y].setBackground(couleurCaseRoiEnDanger);  // Appliquer la couleur rouge
            }
        }
    }



    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        ImageIcon icon = new ImageIcon(urlIcone);

        // Redimensionner l'icône
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);

        return resizedIcon;
    }



    private ArrayList<Case> casesMarquees = new ArrayList<>();
    private Color couleurOriginaleCasePaire = new Color(150, 150, 210);
    private Color couleurOriginaleCaseImpaire = new Color(50, 50, 110);
    private Color couleurCaseAccessible = new Color(255, 255, 0, 100); // Jaune semi-transparent

    private void marquerCasesAccessibles(ArrayList<Case> cases) {
        casesMarquees.clear();
        casesMarquees.addAll(cases);

        // Marque chaque case accessible
        for (Case c : cases) {
            Point pos = plateau.getPositionCase(c);
            if (pos != null) {
                int x = pos.x;
                int y = pos.y;

                // Change la couleur de la case
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

                // Restaure la couleur d'origine de la case
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Ajoute une barre avec un bouton "Sauvegarder la partie"
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton boutonSauvegarder = new JButton("Sauvegarder la partie");
        // Bouton pour charger une partie
        JButton boutonCharger = new JButton("Charger la partie");
        boutonSauvegarder.addActionListener(e -> SauvegardeManager.sauvegarder(this, jeu));
        boutonCharger.addActionListener(e -> SauvegardeManager.charger(this, jeu, this::mettreAJourAffichage));


        // Ajout des boutons dans la barre
        topPanel.add(boutonCharger); // Ajout du bouton "Charger" à la barre

        topPanel.add(boutonSauvegarder);
        add(topPanel, BorderLayout.NORTH); // Ajoute le bouton en haut de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // affichage de l'echiquier


        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();

                tabJLabel[x][y] = jlab;

                final int xx = x;
                final int yy = y;
                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (caseClic1 == null) {
                            caseClic1 = plateau.getCases()[xx][yy];

                            // Check si la case selectionnée a une piece
                            if (caseClic1.getPiece() != null) {
                                // bon tour joueur?
                                if (caseClic1.getPiece().estBlanc() != jeu.estTourDesBlancs()) {
                                    caseClic1 = null; // pas le bon joueur => refait
                                    return;
                                }

                                ArrayList<Case> casesAccessibles;

                                // si le roi est en echec alors il limite les mouvments pour le defendre
                                if (jeu.isEnEchec()) {
                                    casesAccessibles = jeu.getCoupsFiltres(caseClic1.getPiece());
                                } else {
                                    casesAccessibles = caseClic1.getPiece().getCasesAccessibles();
                                }

                                if (casesAccessibles.isEmpty()) {
                                    caseClic1 = null; // pas de mouvements valide => reessaye
                                } else {
                                    marquerCasesAccessibles(casesAccessibles); // marque les cases accessibles
                                }
                            } else {
                                // si pas de piece alors reset casClic1
                                caseClic1 = null;
                            }
                        } else {
                            caseClic2 = plateau.getCases()[xx][yy];
                            effacerMarquageCasesAccessibles();

                            // renvoie les cases accessibles pour la piece
                            ArrayList<Case> casesAccessibles = caseClic1.getPiece().getCasesAccessibles();

                            // si destination accessible alors bouger la piece
                            if (casesAccessibles.contains(caseClic2)) {
                                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                                jeu.appliquerCoup(new Coup(caseClic1, caseClic2));
                                if (jeu.verifierPromotionPion(caseClic2)) {
                                    PromotionHelper.gererPromotion(plateau, VueControleur.this, VueControleur.this::mettreAJourAffichage);
                                }


                                jeu.changerTour(); // Change tour

                            }

                            // Reset les cases selectionnées après bouger
                            caseClic1 = null;
                            caseClic2 = null;

                            // verifie si roi est en echec et mat
                            if (jeu.isEchecEtMat()) {
                                JOptionPane.showMessageDialog(null, "Échec et mat ! Nouvelle partie.");
                                reinitialiserJeu(); // Reset le jeu si roi est en checkmate
                            }
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

    private void reinitialiserJeu() {
        jeu = new Jeu(); // recrée un jeu
        plateau = jeu.getPlateau(); // on met à jour le plateau
        plateau.addObserver(this);
        mettreAJourAffichage();
    }


    private void mettreAJourAffichage() {
        // Réinitialise la couleur de toutes les cases
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if ((y % 2 == 0 && x % 2 == 0) || (y % 2 != 0 && x % 2 != 0)) {
                    tabJLabel[x][y].setBackground(couleurOriginaleCaseImpaire);
                } else {
                    tabJLabel[x][y].setBackground(couleurOriginaleCasePaire);
                }
            }
        }

        // Affichage des pièces
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Case c = plateau.getCases()[x][y];

                if (c != null) {
                    Piece e = c.getPiece();

                    if (e != null) {
                        String couleur = e.estBlanc() ? "w" : "b";
                        String code = "";

                        if (e instanceof Roi) code = "K";
                        else if (e instanceof Dame) code = "Q";
                        else if (e instanceof Tour) code = "R";
                        else if (e instanceof Fou) code = "B";
                        else if (e instanceof Cavalier) code = "N";
                        else if (e instanceof Pion) code = "P";

                        tabJLabel[x][y].setIcon(iconManager.get(couleur + code));

                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }
                }
            }
        }

        // Afficher en rouge la case du roi en danger, si elle existe
        Case caseRoiEnDanger = jeu.verifierRoiEnDanger();
        if (caseRoiEnDanger != null) {
            Point pos = plateau.getPositionCase(caseRoiEnDanger);
            if (pos != null) {
                int x = pos.x;
                int y = pos.y;
                tabJLabel[x][y].setBackground(Color.RED);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();

    }
}
