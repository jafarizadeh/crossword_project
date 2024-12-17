package GrilleMots;

/**
 * La classe Mot représente un mot dans la grille de mots croisés.
 */
public class Mot {
    private int positionX;
    private int positionY;
    private boolean estVertical;
    private String mot;
    private String definition;

    /**
     * Constructeur de la classe Mot avec le mot et la définition.
     *
     * @param mot        Le mot.
     * @param definition La définition du mot.
     */
    public Mot(String mot, String definition)
    {
        this.mot = mot;
        this.definition = definition;
    }


    /**
     * Constructeur de la classe Mot avec la position, le mot, la définition et l'orientation.
     *
     * @param positionX  La position X du mot.
     * @param positionY  La position Y du mot.
     * @param mot        Le mot.
     * @param definition La définition du mot.
     * @param estVertical True si le mot est vertical, sinon false.
     */
    public Mot(int positionX, int positionY, String mot, String definition, boolean estVertical)
    {
        this(mot, definition);
        this.positionX = positionX;
        this.positionY = positionY;
        this.estVertical = estVertical;
    }

    /**
     * Obtient la position X du mot.
     *
     * @return La position X.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Obtient la position Y du mot.
     *
     * @return La position Y.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Indique si le mot est vertical.
     *
     * @return True si le mot est vertical, sinon false.
     */
    public boolean isVertical() {return estVertical;}

    /**
     * Obtient la définition du mot.
     *
     * @return La définition du mot.
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Obtient le mot.
     *
     * @return Le mot.
     */
    public String getMot() {
        return mot;
    }


    /**
     * Obtient une représentation textuelle du mot.
     *
     * @return Une chaîne de caractères représentant le mot.
     */
    @Override
    public String toString() {
        return "Mot \"" + mot + "\" - Position : (" + positionX + "x" + positionY + ") - orientation : " + (estVertical ? "Verticale" : "Horizontale");
    }

    /**
     * Modifie la position X du mot.
     *
     * @param positionX La nouvelle position X.
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * Modifie la position Y du mot.
     *
     * @param positionY La nouvelle position Y.
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Modifie l'orientation du mot.
     *
     * @param estVertical True si le mot est vertical, sinon false.
     */
    public void setEstVertical(boolean estVertical) {
        this.estVertical = estVertical;
    }
}
