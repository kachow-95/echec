package VueControleur;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class IconManager {
    private static final int pxCase = 50;
    private final Map<String, ImageIcon> icons = new HashMap<>();

    public IconManager() {
        chargerToutesLesIcones();
    }

    private void chargerToutesLesIcones() {
        ajouter("wK", "Images/wK.png");
        ajouter("wQ", "Images/wQ.png");
        ajouter("wB", "Images/wB.png");
        ajouter("wN", "Images/wN.png");
        ajouter("wR", "Images/wR.png");
        ajouter("wP", "Images/wP.png");

        ajouter("bK", "Images/bK.png");
        ajouter("bQ", "Images/bQ.png");
        ajouter("bB", "Images/bB.png");
        ajouter("bN", "Images/bN.png");
        ajouter("bR", "Images/bR.png");
        ajouter("bP", "Images/bP.png");
    }

    private void ajouter(String key, String chemin) {
        ImageIcon icon = new ImageIcon(chemin);
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        icons.put(key, new ImageIcon(img));
    }

    public ImageIcon get(String key) {
        return icons.get(key);
    }
}
