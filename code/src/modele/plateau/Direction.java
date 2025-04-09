/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.awt.Point;


/** Type énuméré des directions : les directions correspondent à un ensemble borné de valeurs, connu à l'avance
 *
 *
 */
public enum Direction {
    haut, bas, gauche, droite,
    /// notre code
    haut_gauche, haut_droite, bas_droite, bas_gauche;
    public Point getDelta() {
        switch (this) {
            case haut:
                return new Point(0, -1);
            case bas:
                return new Point(0, 1);
            case gauche:
                return new Point(-1, 0);
            case droite:
                return new Point(1, 0);
            case bas_droite:
                return new Point(1, 1);
            case bas_gauche:
                return new Point(-1, 1);
            case haut_gauche:
                return new Point(-1, -1);
            case haut_droite:
                return new Point(1, -1);
            default:
                return null;
        }
    }
}
