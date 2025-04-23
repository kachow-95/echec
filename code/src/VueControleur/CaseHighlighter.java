package VueControleur;

import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CaseHighlighter {
    private final Plateau plateau;
    private final JLabel[][] tabJLabel;
    private final Color couleurAccess;
    private final Color couleurCaseImpaire;
    private final Color couleurCasePaire;
    private final java.util.List<Case> casesMarquees = new ArrayList<>();

    public CaseHighlighter(Plateau plateau, JLabel[][] tabJLabel,
                           Color couleurAccessible, Color couleurPaire, Color couleurImpaire) {
        this.plateau = plateau;
        this.tabJLabel = tabJLabel;
        this.couleurAccess = couleurAccessible;
        this.couleurCasePaire = couleurPaire;
        this.couleurCaseImpaire = couleurImpaire;
    }

}
