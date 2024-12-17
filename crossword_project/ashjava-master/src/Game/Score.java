package Game;


/**
 * La classe Score représente les informations associées au score d'un joueur pour une grille de mots croisés.
 */
public class Score {
    private int lignes;
    private int colonnes;
    private long meilleur_score;
    private String nom_joueur;

    /**
     * Constructeur de la classe Score.
     *
     * @param lignes         Nombre de lignes de la grille de mots croisés.
     * @param colonnes       Nombre de colonnes de la grille de mots croisés.
     * @param meilleur_score Meilleur score obtenu par le joueur.
     * @param nom_joueur     Nom du joueur associé au score.
     */
    public Score(int lignes, int colonnes, long meilleur_score, String nom_joueur) {
        this.lignes = lignes;
        this.colonnes = colonnes;
        this.meilleur_score = meilleur_score;
        this.nom_joueur = nom_joueur;
    }

    /**
     * Obtient les dimensions de la grille de mots croisés sous forme de chaîne de caractères.
     *
     * @return Chaîne de caractères représentant les dimensions (lignes x colonnes) de la grille.
     */
    public String getDimensions() {
        return lignes + "x" + colonnes;
    }


    /**
     * Obtient le nom du joueur associé au score.
     *
     * @return Nom du joueur.
     */
    public String getNom_joueur() {
        return nom_joueur;
    }

    /**
     * Obtient le meilleur score obtenu par le joueur.
     *
     * @return Meilleur score du joueur.
     */
    public long getMeilleur_score() {
        return meilleur_score;
    }

    /**
     * Obtient le nombre de lignes de la grille de mots croisés.
     *
     * @return Nombre de lignes de la grille.
     */
    public int getLignes() {
        return lignes;
    }

    /**
     * Obtient le nombre de colonnes de la grille de mots croisés.
     *
     * @return Nombre de colonnes de la grille.
     */
    public int getColonnes(){
        return colonnes;
    }
}
