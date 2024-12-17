package GUI;

import Game.Generation;
import GrilleMots.Grille;
import Utilitaire.Utilitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * La classe Menu représente le menu principal du jeu Mots Croisés.
 */
public class Menu extends JFrame {

    private int nb_lignes;
    private int nb_colonnes;

    /**
     * Constructeur de la classe Menu.
     */
    public Menu() {
        this.nb_lignes = -1;
        this.nb_colonnes = -1;
        setTitle("Crossed Words - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Crossed Words", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        contentPane.add(titlePanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.add(menuPanel, BorderLayout.CENTER);

        JButton nouveauJeuButton = new JButton("Nouvelle partie");
        JButton chargerGrilleButton = new JButton("Charger une Grille depuis un Fichier");
        JButton chargerPartieButton = new JButton("Charger une Partie Sauvegardée");
        JButton scoresButton = new JButton("Tableau des Scores");
        JButton quitterButton = new JButton("Quitter");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        menuPanel.add(nouveauJeuButton, gbc);
        gbc.gridy++;
        menuPanel.add(chargerGrilleButton, gbc);
        gbc.gridy++;
        menuPanel.add(chargerPartieButton, gbc);
        gbc.gridy++;
        menuPanel.add(scoresButton, gbc);
        gbc.gridy++;
        menuPanel.add(quitterButton, gbc);

        nouveauJeuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nouveau_jeu();
            }
        });

        chargerGrilleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                charger_grille();
            }
        });

        chargerPartieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                charger_partie();
            }
        });

        scoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableau_scores();
            }
        });

        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Méthode qui gère la sélection des dimensions pour une nouvelle partie.
     */
    private void dimensions(){
        int minValeur = 3;
        int maxValeur = 26;

        try {
            String lignesStr = JOptionPane.showInputDialog(null, "Entrez le nombre de lignes (entre " + minValeur + " et " + maxValeur + "):");
            if (lignesStr == null)
                return;
            nb_lignes = Integer.parseInt(lignesStr);
            if (nb_lignes < minValeur || nb_lignes > maxValeur)
                throw new NumberFormatException();


            String colonnesStr = JOptionPane.showInputDialog(null, "Entrez le nombre de nb_colonnes (entre " + minValeur + " et " + maxValeur + "):");
            if (colonnesStr == null)
                return;
            nb_colonnes = Integer.parseInt(colonnesStr);
            if (nb_colonnes < minValeur || nb_colonnes > maxValeur)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer des valeurs valides entre " + minValeur + " et " + maxValeur, "Erreur", JOptionPane.ERROR_MESSAGE);
            dimensions();
        }
    }

    /**
     * Méthode qui lance une nouvelle partie en générant une grille.
     */
    private void nouveau_jeu(){
        String nom_fichier = Utilitaire.choix_fichier("g2cwds", "data/init", true);
        if (nom_fichier.isEmpty()) return;
        if (!(new File(nom_fichier)).exists()) return;
        dimensions();
        if (nb_lignes == -1 || nb_colonnes == -1) return;
        Generation grille_genere = new Generation(nom_fichier, nb_lignes, nb_colonnes);
        if (grille_genere.getGrille_generee().getListeMots().isEmpty()){
            JOptionPane.showMessageDialog(null, "Dimensions de la grille trop petite", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        else{
            Grille motsCroises = grille_genere.getGrille_generee();
            GrilleGUI grilleGUI = new GrilleGUI(motsCroises);
            this.dispose();
            grilleGUI.setVisible(true);
        }
    }

    /**
     * Méthode qui charge une grille depuis un fichier.
     */
    private void charger_grille(){
        String nom_fichier = Utilitaire.choix_fichier("gcwds", "data/init", true);
        if (nom_fichier.isEmpty()) return;
        Grille motsCroises = new Grille(nom_fichier);
        GrilleGUI grilleGUI = new GrilleGUI(motsCroises);
        this.dispose();
        grilleGUI.setVisible(true);
    }

    /**
     * Méthode qui charge une partie sauvegardée.
     */
    private void charger_partie(){
        String nom_fichier = Utilitaire.choix_fichier("cwds", "data/saved", true);
        if (nom_fichier.isEmpty()) return;
        GrilleGUI grilleGUI = new GrilleGUI(nom_fichier);
        this.dispose();
        grilleGUI.setVisible(true);
    }

    /**
     * Méthode qui affiche le tableau des scores.
     */
    private void tableau_scores(){
        this.dispose();
        TableauScore tableauScore = new TableauScore();
        tableauScore.afficher_tableau();
    }
}
