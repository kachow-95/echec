package VueControleur;

import modele.jeu.*;
import modele.plateau.*;

import javax.swing.*;
import java.awt.*;

public class PromotionHelper {
    public static void gererPromotion(Plateau plateau, JFrame parent, Runnable callback) {
        Piece pion = plateau.getPionPromouvable();

        if (pion == null) return;

        String[] options = {"Dame", "Tour", "Cavalier", "Fou"};

        String choix = (String) JOptionPane.showInputDialog(
                parent,
                "Choisissez une pièce pour la promotion du pion :",
                "Promotion de Pion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix != null) {
            Piece nouvellePiece = switch (choix) {
                case "Dame" -> new Dame(plateau, pion.estBlanc());
                case "Tour" -> new Tour(plateau, pion.estBlanc());
                case "Cavalier" -> new Cavalier(plateau, pion.estBlanc());
                case "Fou" -> new Fou(plateau, pion.estBlanc());
                default -> null;
            };

            if (nouvellePiece != null) {
                Point position = plateau.getPositionCase(pion.getCase());
                if (position != null) {
                    Case casePion = plateau.getCases()[position.x][position.y];
                    nouvellePiece.allerSurCase(casePion);
                    plateau.setPionPromouvable(null);
                    callback.run(); // pour rafraîchir l'affichage
                }
            }
        }
    }
}
