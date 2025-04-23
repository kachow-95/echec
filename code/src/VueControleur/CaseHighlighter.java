package VueControleur;

import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CaseHighlighter {
    private final Plateau plateau;
    private final JLabel[][] tabJLabel;
    private final Color couleurAccessible;
    private final Color couleurImpaire;
    private final Color couleurPaire;
    private final java.util.List<Case> casesMarquees = new ArrayList<>();

    public CaseHighlighter(Plateau plateau, JLabel[][] tabJLabel,
                           Color couleurAccessible, Color couleurPaire, Color couleurImpaire) {
        this.plateau = plateau;
        this.tabJLabel = tabJLabel;
        this.couleurAccessible = couleurAccessible;
        this.couleurPaire = couleurPaire;
        this.couleurImpaire = couleurImpaire;
    }

}
