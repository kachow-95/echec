package VueControleur;

import modele.jeu.Jeu;
import modele.jeu.PGNSauvegarde;
import modele.plateau.Plateau;

import javax.swing.*;
import java.io.File;

public class SauvegardeManager {
    public static void sauvegarder(JFrame parent, Jeu jeu) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            PGNSauvegarde.sauvegarderPartie(jeu.getHistorique(), jeu.getPlateau(), f.getAbsolutePath());
            JOptionPane.showMessageDialog(parent, "Partie sauvegardée !");
        }
    }

    public static void charger(JFrame parent, Jeu jeu, Runnable onLoaded) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            PGNSauvegarde.chargerPartie(f.getAbsolutePath(), jeu);
            JOptionPane.showMessageDialog(parent, "Partie chargée !");
            onLoaded.run(); // rafraîchi l'affichage
        }
    }
}
