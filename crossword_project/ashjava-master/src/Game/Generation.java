package Game;

import GrilleMots.Case;
import GrilleMots.Grille;
import GrilleMots.Mot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * La classe Generation est responsable de la génération de la grille de mots croisés.
 */
public class Generation {
    private int nb_lignes;
    private int nb_colonnes;
    private int nb_vertical;
    private int nb_horizontal;
    private List<Mot> liste_mots;
    private List<Mot> liste_mots_places;
    private Grille grille_generee;
    Random random;

    /**
     * Constructeur de la classe Generation.
     *
     * @param nom_fichier Chemin du fichier contenant la liste des mots et leurs définitions.
     * @param nb_lignes   Nombre de lignes de la grille de mots croisés.
     * @param nb_colonnes Nombre de colonnes de la grille de mots croisés.
     */
    public Generation(String nom_fichier, int nb_lignes, int nb_colonnes)
    {
        this.nb_lignes = nb_lignes;
        this.nb_colonnes = nb_colonnes;
        this.nb_vertical = 0;
        this.nb_horizontal = 0;
        this.grille_generee = new Grille(nb_lignes, nb_colonnes);
        this.random = new Random(System.currentTimeMillis());
        this.liste_mots_places = new ArrayList<>();
        extraire_mots(nom_fichier);
        if (!liste_mots.isEmpty()){
            genere_grille();
        }
    }

    /**
     * Extrait les mots et leurs définitions à partir d'un fichier.
     *
     * @param nom_fichier Chemin du fichier contenant la liste des mots et leurs définitions.
     */
    private void extraire_mots(String nom_fichier)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(nom_fichier))) {
            this.liste_mots = new ArrayList<>();
            String ligne;
            while ((ligne = br.readLine()) != null){
                String[] elements = ligne.split("#");
                String mot = elements[0];
                String definition = elements[1];
                if (mot.length() < nb_lignes && mot.length() < nb_colonnes){
                    Mot newMot = new Mot(mot, definition);
                    this.liste_mots.add(newMot);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Génère la grille de mots croisés.
     */
    private void genere_grille()
    {
        liste_mots.sort(new Comparator<Mot>() {
            @Override
            public int compare(Mot mot1, Mot mot2) {
                return Integer.compare(mot2.getMot().length(), mot1.getMot().length());
            }
        });

        boolean estVertical = random.nextBoolean();
        int index = 0;
        place_mot(liste_mots.get(index), 0, 0, estVertical);
        System.out.println(liste_mots.get(index));

        for (index = 1; index < liste_mots.size(); index++){
            int nb_essais = 100;
            Mot mot = liste_mots.get(index);
            estVertical = nb_vertical < nb_horizontal;
            for (int essai = 0; essai <= nb_essais; essai++){
                if (essai == 100){
                    System.out.println("Impossible de placer \"" + mot.getMot() + "\" après " + nb_essais + " essais.");
                }
                else{
                    int positionX, positionY, positionX_inter = 0, positionY_inter = 0;
                    int[] indices = {-1, -1, -1, -1, -1};
                    if (essai == 0){
                        indices = peut_intersecter(mot);
                    }
                    if (indices[0] != -1){
                        positionX = indices[0];
                        positionY = indices[1];
                        positionX_inter = indices[2];
                        positionY_inter = indices[3];
                        grille_generee.getGrille_reference()[positionX_inter][positionY_inter].setStatut(Case.typeCase.CASE_VIDE);
                        estVertical = !liste_mots_places.get(indices[4]).isVertical();
                    }
                    else{
                        positionX = random.nextInt(nb_lignes);
                        positionY = random.nextInt(nb_colonnes);
                    }
                    if (peut_placer_mot(mot, positionX, positionY, estVertical)) {
                        place_mot(mot, positionX, positionY, estVertical);
                        // if (indices[0] != - 1){
                            // System.out.println("Réussi intersection avec : " + mot.getMot() + " et " + liste_mots_places.get(indices[4]).getMot());
                        // }
                        // System.out.println(mot);
                        break;
                    }
                    if (indices[0] != -1){
                        grille_generee.getGrille_reference()[positionX_inter][positionY_inter].setStatut(Case.typeCase.CASE_REMPLIE);
                    }
                }
            }
        }
        for (int x = 0; x < nb_lignes; x++){
            for (int y = 0; y < nb_colonnes; y++){
                if (this.grille_generee.getGrille_reference()[x][y].getStatut() == Case.typeCase.CASE_VIDE){
                    this.grille_generee.getGrille_reference()[x][y].setStatut(Case.typeCase.CASE_NEUTRE);
                    this.grille_generee.getGrille()[x][y].setStatut(Case.typeCase.CASE_NEUTRE);
                }
            }
        }
        this.grille_generee.setListeMots(liste_mots_places);

    }

    /**
     * Place un mot dans la grille à la position spécifiée.
     *
     * @param mot        Mot à placer.
     * @param positionX  Position X de départ.
     * @param positionY  Position Y de départ.
     * @param estVertical Indique si le mot doit être placé verticalement.
     */
    private void place_mot(Mot mot, int positionX, int positionY, boolean estVertical)
    {
        mot.setPositionX(positionX);
        mot.setPositionY(positionY);
        mot.setEstVertical(estVertical);
        this.grille_generee.ajouter_mot_grille(mot);
        liste_mots_places.add(mot);
        if (estVertical) nb_vertical++;
        else nb_horizontal++;
    }

    /**
     * Vérifie si un mot peut être placé à une position spécifiée dans la grille.
     *
     * @param mot        Mot à vérifier.
     * @param positionX  Position X de départ.
     * @param positionY  Position Y de départ.
     * @param estVertical Indique si le mot doit être placé verticalement.
     * @return Vrai si le mot peut être placé, sinon faux.
     */
    private boolean peut_placer_mot(Mot mot, int positionX, int positionY, boolean estVertical)
    {
        int taille_mot = mot.getMot().length();
        Case[][] reference = grille_generee.getGrille_reference();
        if (estVertical && (nb_lignes < (positionX + taille_mot))) return false;
        if (!estVertical && (nb_colonnes < (positionY + taille_mot))) return false;
        for (int i = 0; i < taille_mot; i++){
            if (estVertical && reference[i + positionX][positionY].getStatut() != Case.typeCase.CASE_VIDE)
                return false;
            else if (!estVertical && reference[positionX][i + positionY].getStatut() != Case.typeCase.CASE_VIDE)
                return false;
        }
        if (estVertical && ((positionX - 1) >= 0) && reference[positionX - 1][positionY].getStatut() != Case.typeCase.CASE_VIDE)
            return false;
        else if (!estVertical && ((positionY - 1) >= 0) && reference[positionX][positionY - 1].getStatut() != Case.typeCase.CASE_VIDE)
            return false;
        if (estVertical && ((positionX + taille_mot) < nb_lignes) && reference[positionX + taille_mot][positionY].getStatut() != Case.typeCase.CASE_VIDE)
            return false;
        else if (!estVertical && ((positionY + taille_mot) < nb_colonnes) && reference[positionX][positionY + taille_mot].getStatut() != Case.typeCase.CASE_VIDE)
            return false;
        return true;
    }

    /**
     * Vérifie si un mot peut intersecter avec un mot déjà placé dans la grille.
     *
     * @param mot Mot à vérifier.
     * @return Tableau d'entiers contenant les informations sur l'intersection, sinon tableau de -1.
     */
    private int[] peut_intersecter(Mot mot) {
        String string_mot = mot.getMot();
        int taille_mot = string_mot.length();
        boolean est_vertical_mot;

        for (Mot motPlace : liste_mots_places) {
            est_vertical_mot = !motPlace.isVertical();
            String string_mot_place = motPlace.getMot();
            int xDeb_mot_place = motPlace.getPositionX();
            int yDeb_mot_place = motPlace.getPositionY();

            for (int i = 0; i < taille_mot; i++) {
                if (xDeb_mot_place == 0 && est_vertical_mot && i != 0) break;
                if (yDeb_mot_place == 0 && !est_vertical_mot && i!= 0) break;

                char lettre = string_mot.charAt(i);
                int ii = string_mot_place.indexOf(lettre);
                List<Integer> indices = new ArrayList<>();
                while (ii != -1){
                    indices.add(ii);
                    ii = string_mot_place.indexOf(lettre, ii+1);
                }

                for (int index : indices){
                    int xpos_inter = est_vertical_mot ? xDeb_mot_place : xDeb_mot_place + index;
                    int ypos_inter = est_vertical_mot ? yDeb_mot_place + index : yDeb_mot_place;

                    if (est_vertical_mot && checkVerticalIntersection(xpos_inter, ypos_inter, i, taille_mot)) {
                        return new int[]{xpos_inter - i, ypos_inter, xpos_inter, ypos_inter, liste_mots_places.indexOf(motPlace)};
                    } else if (!est_vertical_mot && checkHorizontalIntersection(xpos_inter, ypos_inter, i, taille_mot)) {
                        return new int[]{xpos_inter, ypos_inter - i, xpos_inter, ypos_inter, liste_mots_places.indexOf(motPlace)};
                    }
                }
            }
        }
        return new int[]{-1, -1, -1, -1, -1};
    }

    /**
     * Vérifie l'intersection verticale entre deux mots dans la grille.
     *
     * @param xpos_inter Position X de l'intersection.
     * @param ypos_inter Position Y de l'intersection.
     * @param i          Index du caractère du mot en cours d'intersection.
     * @param taille_mot Taille du mot en cours d'intersection.
     * @return Vrai si l'intersection est possible, sinon faux.
     */
    private boolean checkVerticalIntersection(int xpos_inter, int ypos_inter, int i, int taille_mot) {
        for (int j = 0; j < taille_mot; j++) {
            int x = xpos_inter - i + j;
            if (x < 0 || x >= nb_lignes || (x != xpos_inter && grille_generee.getGrille_reference()[x][ypos_inter].getStatut() != Case.typeCase.CASE_VIDE)) {
                return false;
            }
        }
        return xpos_inter - i >= 0;
    }

    /**
     * Vérifie l'intersection horizontale entre deux mots dans la grille.
     *
     * @param xpos_inter Position X de l'intersection.
     * @param ypos_inter Position Y de l'intersection.
     * @param i          Index du caractère du mot en cours d'intersection.
     * @param taille_mot Taille du mot en cours d'intersection.
     * @return Vrai si l'intersection est possible, sinon faux.
     */
    private boolean checkHorizontalIntersection(int xpos_inter, int ypos_inter, int i, int taille_mot) {
        for (int j = 0; j < taille_mot; j++) {
            int y = ypos_inter - i + j;
            if (y < 0 || y >= nb_colonnes || (y != ypos_inter && grille_generee.getGrille_reference()[xpos_inter][y].getStatut() != Case.typeCase.CASE_VIDE)) {
                return false;
            }
        }
        return ypos_inter - i >= 0;
    }

    /**
     * Obtient la grille générée.
     *
     * @return Objet Grille représentant la grille de mots croisés générée.
     */
    public Grille getGrille_generee() {
        return grille_generee;
    }
}
