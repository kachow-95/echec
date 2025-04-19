package modele.jeu;

import modele.plateau.*;

public class Pion extends Piece {
    private boolean peutEtrePromu = false;

    public Pion(Plateau _plateau, boolean _estBlanc) {
        super(_plateau, _estBlanc);
        casesAccessibles = new DecorateurCasesPion(null);

    }

    public boolean peutEtrePromu() {
        return peutEtrePromu;
    }

    public void setPeutEtrePromu(boolean peutEtrePromu) {
        this.peutEtrePromu = peutEtrePromu;
    }
    @Override
    public String getType() {
        return "Pion";
    }

}
