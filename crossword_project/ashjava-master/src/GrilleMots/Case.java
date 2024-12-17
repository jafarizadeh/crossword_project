package GrilleMots;

/**
 * La classe Case représente une case individuelle dans une grille de mots croisés.
 */
public class Case {

    /**
     * Enumération représentant les différents types de cases.
     */
    public enum typeCase{
        CASE_NEUTRE,
        CASE_VIDE,
        CASE_REMPLIE
    }
    private int positionX;
    private int positionY;
    private char valeur;
    private typeCase statut;

    /**
     * Constructeur de la classe Case.
     *
     * @param positionX Position X de la case dans la grille.
     * @param positionY Position Y de la case dans la grille.
     * @param valeur    Valeur (lettre) initiale de la case.
     */
    public Case(int positionX, int positionY, char valeur)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        this.valeur = valeur;
    }

    /**
     * Définit la valeur (lettre) de la case.
     *
     * @param valeur Nouvelle valeur de la case.
     */
    public void setValeur(char valeur) {
        this.valeur = valeur;
    }

    /**
     * Obtient la valeur (lettre) actuelle de la case.
     *
     * @return Valeur de la case.
     */
    public char getValeur() {
        return valeur;
    }

    /**
     * Obtient le statut de la case (CASE_NEUTRE, CASE_VIDE, CASE_REMPLIE).
     *
     * @return Statut de la case.
     */
    public typeCase getStatut() {
        return statut;
    }

    /**
     * Définit le statut de la case (CASE_NEUTRE, CASE_VIDE, CASE_REMPLIE).
     *
     * @param statut Nouveau statut de la case.
     */
    public void setStatut(typeCase statut) {
        this.statut = statut;
    }
}
