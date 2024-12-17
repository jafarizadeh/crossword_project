package GUI;

import Game.Score;
import Game.Temps;
import GrilleMots.Case;
import GrilleMots.Grille;
import GrilleMots.Mot;
import Utilitaire.Utilitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;


public class GrilleGUI extends JFrame {
    private Grille grille;
    private Temps temps;
    JLabel tempsLabel;
    JPanel mainPanel;
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 780;

    /**
     * Constructeur par défaut de la classe GrilleGUI.
     */
    public  GrilleGUI(){
        setTitle("Crossed Words");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }

    /**
     * Constructeur de la classe GrilleGUI avec une grille donnée.
     *
     * @param grille La grille de mots croisés.
     */
    public  GrilleGUI(Grille grille) {
        this();
        this.grille = grille;
        initialise();
    }

    /**
     * Constructeur de la classe GrilleGUI à partir d'un fichier.
     *
     * @param nom_fichier Le nom du fichier contenant la grille.
     */
    public GrilleGUI(String nom_fichier){
        this();
        try (BufferedReader br = new BufferedReader(new FileReader(nom_fichier))) {
            String[] dimensions = br.readLine().split("#");
            int nb_lignes = Integer.parseInt(dimensions[0]);
            int nb_colonnes = Integer.parseInt(dimensions[1]);
            int nb_horizontal = Integer.parseInt(dimensions[2]);
            int nb_vertical = Integer.parseInt(dimensions[3]);

            this.grille = new Grille(nb_lignes, nb_colonnes);
            this.grille.setVia_fichier(true);
            long tempsDeJeu = Long.parseLong(br.readLine());
            String[] ligne;
            List<Mot> liste_mots = new ArrayList<>();

            int positionX = 0;
            int positionY = 0;

            for (int i = 0; i < nb_vertical + nb_horizontal; i++) {
                ligne = br.readLine().split("#");
                positionX = Integer.parseInt(ligne[0]);
                positionY = Integer.parseInt(ligne[1]);
                String mot = ligne[2];
                String definition = ligne[3];
                Mot newMot = new Mot(positionX, positionY, mot, definition, i >= nb_horizontal);
                liste_mots.add(newMot);
                grille.ajouter_mot_grille(newMot);
            }
            grille.setListeMots(liste_mots);
            String line;
            while ((line = br.readLine()) != null){
                ligne = line.split("#");
                positionX = Integer.parseInt(ligne[0]);
                positionY = Integer.parseInt(ligne[1]);
                String lettre = ligne[2];

                if (Objects.equals(lettre, "%")){
                    grille.getGrille()[positionX][positionY].setStatut(Case.typeCase.CASE_NEUTRE);
                }
                else if (Objects.equals(lettre, "")){
                    grille.getGrille()[positionX][positionY].setStatut(Case.typeCase.CASE_VIDE);
                }
                else{
                    grille.getGrille()[positionX][positionY].setStatut(Case.typeCase.CASE_REMPLIE);
                    grille.getGrille()[positionX][positionY].setValeur(lettre.charAt(0));
                }
            }
            initialise();
            temps.setTemps_de_jeu(tempsDeJeu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise la fenêtre de l'interface graphique.
     */
    private void initialise(){
        initialise_gui();
        temps = new Temps(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTemps(temps.getTempsDeJeu());
            }
        });
    }

    /**
     * Initialise les composants graphiques de la fenêtre.
     */
    private void initialise_gui() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel = new JPanel(new GridLayout(grille.getNb_lignes() + 1, grille.getNb_colonnes() + 1));
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        initialise_navPanel(navPanel);
        initialise_mainPanel(mainPanel);
        initialise_infoPanel(infoPanel);

        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
    }

    /**
     * Initialise le panneau de navigation de la fenêtre.
     *
     * @param navPanel Le panneau de navigation.
     */
    private void initialise_navPanel(JPanel navPanel) {
        tempsLabel = new JLabel("Temps écoulé: 00:00:00");
        JButton pauseButton = new JButton("Pause");
        JButton sauvegarderButton = new JButton("Sauvegarder");
        JButton verifierButton = new JButton("Vérifier");
        JButton quitterPartie = new JButton("Quitter la partie");

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temps.togglePause();
                togglePause();
                if (temps.estEnPause()){
                    pauseButton.setText("Continuer");
                }
                else{
                    pauseButton.setText("Pause");
                }
            }
        });

        verifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verification_grille();
            }
        });

        sauvegarderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temps.togglePause();
                togglePause();
                sauvegarde_partie();
                temps.togglePause();
                togglePause();
            }
        });

        quitterPartie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Menu menu = new Menu();
            }
        });

        navPanel.add(tempsLabel);
        navPanel.add(pauseButton);
        navPanel.add(sauvegarderButton);
        navPanel.add(verifierButton);
        navPanel.add(quitterPartie);
    }

    /**
     * Initialise le panneau principal de la fenêtre.
     *
     * @param mainPanel Le panneau principal.
     */
    private void initialise_mainPanel(JPanel mainPanel){
        mainPanel.add(new JLabel("", SwingConstants.CENTER));
        for (int i = 0; i < grille.getNb_colonnes(); i++) {
            char id = (char) ('A' + i);
            mainPanel.add(new JLabel(String.valueOf(id), SwingConstants.CENTER));
        }

        for (int i = 0; i < grille.getNb_lignes(); i++) {
            mainPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            for (int j = 0; j < grille.getNb_colonnes(); j++) {
                Case currentCase = grille.getCase(i, j);
                CasePanel currentCasePanel = new CasePanel(currentCase);
                mainPanel.add(currentCasePanel);
            }
        }
    }

    /**
     * Initialise le panneau d'information de la fenêtre.
     *
     * @param infoPanel Le panneau d'information.
     */
    private void initialise_infoPanel(JPanel infoPanel){
        List<Mot> mots = grille.getListeMots();
        infoPanel.add(new JLabel("Horizontalement"));
        for (int i = 0; i < grille.getNb_horizontal(); i++) {
            Mot mot = mots.get(i);
            JLabel label = new JLabel(String.valueOf(mot.getPositionX() + 1) + ". " + mot.getDefinition());
            infoPanel.add(label);
        }
        infoPanel.add(new JLabel("         "));
        infoPanel.add(new JLabel("Verticalement"));
        for (int i = 0; i < grille.getNb_vertical(); i++) {
            Mot mot = mots.get(i + grille.getNb_horizontal());
            char id = (char)('A' + mot.getPositionY());
            JLabel label = new JLabel(String.valueOf(id) + ". " + mot.getDefinition());
            infoPanel.add(label);
        }
    }

    /**
     * Met à jour l'affichage du temps écoulé.
     *
     * @param tempsDeJeu Le temps de jeu écoulé.
     */
    private void updateTemps(long tempsDeJeu){
        SwingUtilities.invokeLater(() -> tempsLabel.setText("Temps écoulé: " + Utilitaire.formatTemps(tempsDeJeu)));
    }

    /**
     * Gère la pause du jeu.
     */
    private void togglePause(){
        for (Component component : mainPanel.getComponents()){
            if (component instanceof CasePanel){
                if (((CasePanel) component).getCurrentCase().getStatut() != Case.typeCase.CASE_NEUTRE){
                    ((JTextField)((CasePanel) component).getValeur()).setEditable(!temps.estEnPause());
                }

            }
        }
    }

    /**
     * Vérifie la grille de mots croisés.
     */
    private void verification_grille(){
        Case[][] grille_jeu = grille.getGrille();
        Case[][] grille_reference = grille.getGrille_reference();
        boolean terminee = true;
        for (int i = 0; i < grille.getNb_lignes(); i++) {
            for (int j = 0; j < grille.getNb_colonnes(); j++) {
                CasePanel casePanel = (CasePanel) mainPanel.getComponent((i + 1) * (grille.getNb_colonnes() + 1) + j + 1);

                if (grille_jeu[i][j].getStatut() != Case.typeCase.CASE_NEUTRE) {
                    if (grille_jeu[i][j].getValeur() != grille_reference[i][j].getValeur()) {
                        if (grille_jeu[i][j].getStatut() != Case.typeCase.CASE_VIDE) casePanel.setBordureRouge();
                        terminee = false;
                    } else if (grille_jeu[i][j].getStatut() != Case.typeCase.CASE_VIDE) casePanel.setBordureVerte();
                }

            }
        }
        if (terminee) fin_partie();
    }

    /**
     * Traite la fin de la partie.
     */
    private void fin_partie(){
        TableauScore tableau_scores = new TableauScore();
        List<Score> liste_scores = tableau_scores.getListe_scores();
        String current = grille.getNb_lignes() + "x" + grille.getNb_colonnes();
        JOptionPane.showMessageDialog(this, "Félicitations ! Partie terminée.", "Partie terminée", JOptionPane.INFORMATION_MESSAGE);

        for (Score score : liste_scores){
            if (Objects.equals(score.getDimensions(), current)){
                if (temps.getTempsDeJeu() > score.getMeilleur_score()){
                    this.dispose();
                    tableau_scores.afficher_tableau();
                    return;
                }
                else{
                    JOptionPane.showMessageDialog(this, "Nouveau meilleur score !", "Partie terminée", JOptionPane.INFORMATION_MESSAGE);
                    liste_scores.remove(score);
                }
            }
        }

        String pseudo = JOptionPane.showInputDialog(null, "Entrez votre pseudo :", "Saisie du pseudo", JOptionPane.PLAIN_MESSAGE);
        if (pseudo == null || pseudo.isEmpty()){
            pseudo = "Anonymous";
        }
        tableau_scores.ajouterNouveauScore(new Score(grille.getNb_lignes(), grille.getNb_colonnes(), temps.getTempsDeJeu(), pseudo));
        this.dispose();
        tableau_scores.afficher_tableau();
    }

    /**
     * Sauvegarde la partie en cours.
     */
    private void sauvegarde_partie(){
        String nom_fichier = Utilitaire.choix_fichier("cwds", "data/saved/", false);
        if (nom_fichier.isEmpty()){
            return;
        }
        if (!nom_fichier.contains(".cwds"))
            nom_fichier += ".cwds";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nom_fichier))) {
            writer.write(grille.getNb_lignes() + "#" + grille.getNb_colonnes() + "#" + grille.getNb_horizontal() + "#" + grille.getNb_vertical());
            writer.newLine();

            writer.write("" + temps.getTempsDeJeu());
            writer.newLine();

            for (Mot mot : grille.getListeMots()) {
                writer.write(mot.getPositionX() + "#" + mot.getPositionY() + "#" + mot.getMot() + "#" + mot.getDefinition());
                writer.newLine();
            }

            for (int i = 0; i < grille.getNb_lignes(); i++) {
                for (int j = 0; j < grille.getNb_colonnes(); j++) {
                    Case caseCourante = grille.getGrille()[i][j];
                    writer.write(i + "#" + j + "#" + ((caseCourante.getStatut() == Case.typeCase.CASE_NEUTRE) ? "%" : caseCourante.getValeur()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient la grille de mots croisés associée.
     *
     * @return La grille de mots croisés.
     */
    public Grille getGrille(){
        return grille;
    }
}