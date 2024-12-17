package GrilleMots;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * La classe Grille représente la grille de mots croisés avec ses cases et les mots associés.
 */
public class Grille {
    private int nb_lignes;
    private int nb_colonnes;
    private int nb_vertical;
    private int nb_horizontal;
    private boolean via_fichier;
    private Case[][] grille;
    private  Case[][] grille_reference;
    private List<Mot> liste_mots;

    /**
     * Constructeur de la classe Grille sans fichier.
     *
     * @param nb_lignes    Nombre de lignes de la grille.
     * @param nb_colonnes  Nombre de colonnes de la grille.
     */
    public  Grille(int nb_lignes, int nb_colonnes){
        this.grille = new Case[nb_lignes][nb_colonnes];
        this.grille_reference = new Case[nb_lignes][nb_colonnes];
        this.nb_lignes = nb_lignes;
        this.nb_colonnes = nb_colonnes;
        this.liste_mots = new ArrayList<>();
        this.via_fichier = false;

        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                this.grille[i][j] = new Case(i, j, ' ');
                this.grille_reference[i][j] = new Case(i, j, ' ');
                this.grille[i][j].setStatut(Case.typeCase.CASE_VIDE);
                this.grille_reference[i][j].setStatut(Case.typeCase.CASE_VIDE);
            }
        }
    }

    /**
     * Constructeur de la classe Grille à partir d'un fichier.
     *
     * @param nom_fichier Chemin vers le fichier contenant la grille.
     */
    public Grille(String nom_fichier) {
        this.via_fichier = true;
        try (BufferedReader br = new BufferedReader(new FileReader(nom_fichier))) {
            String[] dimensions = br.readLine().split("#");
            this.nb_lignes = Integer.parseInt(dimensions[0]);
            this.nb_colonnes = Integer.parseInt(dimensions[1]);
            this.nb_horizontal = Integer.parseInt(dimensions[2]);
            this.nb_vertical = Integer.parseInt(dimensions[3]);

            this.grille = new Case[nb_lignes][nb_colonnes];
            this.grille_reference = new Case[nb_lignes][nb_colonnes];
            this.liste_mots = new ArrayList<>();

            for (int i = 0; i < nb_lignes; i++) {
                for (int j = 0; j < nb_colonnes; j++) {
                    this.grille[i][j] = new Case(i, j, ' ');
                    this.grille_reference[i][j] = new Case(i, j, ' ');
                    this.grille[i][j].setStatut(Case.typeCase.CASE_VIDE);
                    this.grille_reference[i][j].setStatut(Case.typeCase.CASE_VIDE);
                }
            }

            for (int i = 0; i < nb_horizontal + nb_vertical; i++)
            {
                String[] ligne = br.readLine().split("#");
                int positionX = Integer.parseInt(ligne[0]);
                int positionY = Integer.parseInt(ligne[1]);
                String mot = ligne[2];
                String definition = ligne[3];
                Mot newMot = new Mot(positionX, positionY, mot, definition, i >= nb_horizontal);
                liste_mots.add(newMot);
                if (!taille_mot_acceptable(newMot))
                {
                    System.out.print("Error : ");
                    System.out.println(mot);
                    return;
                }
                else ajouter_mot_grille(newMot);
            }

            for (int i = 0; i < nb_lignes; i++){
                for (int j = 0; j < nb_colonnes; j++){
                    if (grille_reference[i][j].getStatut() == Case.typeCase.CASE_VIDE){
                        grille_reference[i][j].setStatut(Case.typeCase.CASE_NEUTRE);
                        grille[i][j].setStatut(Case.typeCase.CASE_NEUTRE);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient la grille principale (avec les lettres).
     *
     * @return La grille principale.
     */
    public Case[][] getGrille() {
        return grille;
    }

    /**
     * Obtient la grille de référence (avec les cases vides et neutres).
     *
     * @return La grille de référence.
     */
    public Case[][] getGrille_reference() {
        return grille_reference;
    }


    /**
     * Obtient le nombre de lignes de la grille.
     *
     * @return Le nombre de lignes de la grille.
     */
    public int getNb_lignes(){return nb_lignes;}

    /**
     * Obtient le nombre de colonnes de la grille.
     *
     * @return Le nombre de colonnes de la grille.
     */
    public int getNb_colonnes(){return nb_colonnes;}


    /**
     * Vérifie si la taille d'un mot est acceptable pour la grille.
     *
     * @param mot Le mot à vérifier.
     * @return Vrai si la taille du mot est acceptable, sinon faux.
     */
    public boolean taille_mot_acceptable(Mot mot)
    {
        int taille_mot = mot.getMot().length();
        if (mot.isVertical() && (this.nb_lignes < (mot.getPositionX() + taille_mot))) return false;
        return (mot.isVertical()) || (this.nb_colonnes >= (mot.getPositionY() + taille_mot));
    }

    /**
     * Ajoute un mot à la grille.
     *
     * @param mot Le mot à ajouter.
     */
    public void ajouter_mot_grille(Mot mot)
    {
        int positionX = mot.getPositionX();
        int positionY = mot.getPositionY();
        String le_mot = mot.getMot();

        if (mot.isVertical())
        {
            for (int xx = 0; xx < le_mot.length(); xx++)
            {
                grille_reference[xx + positionX][positionY].setStatut(Case.typeCase.CASE_REMPLIE);
                grille_reference[xx + positionX][positionY].setValeur(le_mot.charAt(xx));
            }
            if (!via_fichier){
                if ((positionX - 1) >= 0) {
                    grille_reference[positionX - 1][positionY].setStatut(Case.typeCase.CASE_NEUTRE);
                    grille[positionX - 1][positionY].setStatut(Case.typeCase.CASE_NEUTRE);
                }
                if ((positionX + le_mot.length()) < nb_lignes) {
                    grille_reference[positionX + le_mot.length()][positionY].setStatut(Case.typeCase.CASE_NEUTRE);
                    grille[positionX + le_mot.length()][positionY].setStatut(Case.typeCase.CASE_NEUTRE);
                }
            }
        }
        else
        {
            for (int xx = 0; xx < le_mot.length(); xx++)
            {
                grille_reference[positionX][xx + positionY].setStatut(Case.typeCase.CASE_REMPLIE);
                grille_reference[positionX][xx + positionY].setValeur(le_mot.charAt(xx));
            }
            if (!via_fichier){
                if ((positionY - 1) >= 0){
                    grille_reference[positionX][positionY - 1].setStatut(Case.typeCase.CASE_NEUTRE);
                    grille[positionX][positionY - 1].setStatut(Case.typeCase.CASE_NEUTRE);
                }
                if ((positionY + le_mot.length()) < nb_lignes) {
                    grille_reference[positionX][positionY + le_mot.length()].setStatut(Case.typeCase.CASE_NEUTRE);
                    grille[positionX][positionY + le_mot.length()].setStatut(Case.typeCase.CASE_NEUTRE);
                }
            }
        }
    }


    /**
     * Affiche la grille avec les symboles correspondants aux types de cases.
     *
     * @param grille La grille à afficher.
     */
    public void afficher_Grille(Case[][] grille) {
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                char symbole = ' ';
                switch (grille[i][j].getStatut()) {
                    case CASE_NEUTRE:
                        symbole = '#';
                        break;
                    case CASE_VIDE:
                        symbole = ' ';
                        break;
                    case CASE_REMPLIE:
                        symbole = grille[i][j].getValeur();
                        break;
                }
                System.out.print(symbole + " ");
            }
            System.out.println();
        }

        for (Mot mot : liste_mots) {
            System.out.println(mot);
        }
    }

    /**
     * Obtient une case spécifique de la grille.
     *
     * @param i L'indice de ligne de la case.
     * @param j L'indice de colonne de la case.
     * @return La case à l'indice spécifié.
     */
    public Case getCase(int i, int j) {
        return grille[i][j];
    }


    /**
     * Obtient la liste des mots présents dans la grille.
     *
     * @return La liste des mots.
     */
    public List<Mot> getListeMots() {
        return liste_mots;
    }

    /**
     * Obtient le nombre de mots horizontaux dans la grille.
     *
     * @return Le nombre de mots horizontaux.
     */
    public int getNb_horizontal() {
        return nb_horizontal;
    }


    /**
     * Obtient le nombre de mots verticaux dans la grille.
     *
     * @return Le nombre de mots verticaux.
     */
    public int getNb_vertical() {
        return nb_vertical;
    }

    /**
     * Définit la liste des mots et met à jour les compteurs de mots horizontaux et verticaux.
     *
     * @param liste_mots La nouvelle liste de mots.
     */
    public void setListeMots(List<Mot> liste_mots) {
        for (Mot mot : liste_mots) {
            this.liste_mots.add(mot);
            if (!mot.isVertical()) {
                nb_horizontal++;
            } else {
                nb_vertical++;
            }
        }
        this.liste_mots.sort(Comparator
                .comparing(Mot::isVertical)
                .thenComparing(mot -> mot.isVertical() ? mot.getPositionY() : mot.getPositionX())
                .thenComparing(mot -> mot.isVertical() ? mot.getPositionX() : mot.getPositionY()));
    }

    /**
     * Définit le mode de génération de la grille (via fichier ou non).
     *
     * @param via_fichier Vrai si la grille est générée via un fichier, sinon faux.
     */
    public void setVia_fichier(boolean via_fichier) {
        this.via_fichier = via_fichier;
    }
}
