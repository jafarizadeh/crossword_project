package GUI;

import Game.Score;
import Utilitaire.Utilitaire;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe TableauScore représente le tableau des scores du jeu "Mots Croisés".
 */
public class TableauScore extends JFrame {
    private List<Score> liste_scores;
    private String path = "data/scores.scwds";

    /**
     * Constructeur de la classe TableauScore.
     */
    public TableauScore(){
        this.liste_scores = new ArrayList<>();
        String path = System.getProperty("user.dir");
        if (path.contains("bin")){
            path = Paths.get(path).getParent().toString();
        }
        this.path = Paths.get(path, this.path).toString();
        readScore();
    }

    /**
     * Méthode pour lire les scores depuis le fichier.
     */
    private void readScore(){
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] valeurs = ligne.split("#");
                int lignes = Integer.parseInt(valeurs[0]);
                int colonnes = Integer.parseInt(valeurs[1]);
                int meilleur_score = Integer.parseInt(valeurs[2]);
                String nom_joeur = valeurs[3];
                liste_scores.add(new Score(lignes, colonnes, meilleur_score, nom_joeur));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour afficher le tableau des scores.
     */
    public void afficher_tableau(){
        setTitle("Crossed Words - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Nom du Joueur", "Dimensions", "Meilleur Score"},
                0
        );

        for (Score score : liste_scores) {
            tableModel.addRow(new Object[]{score.getNom_joueur(), score.getDimensions(), Utilitaire.formatTemps(score.getMeilleur_score())});
        }

        JTable scoreTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        getContentPane().add(scrollPane);

        JButton retourMenuButton = new JButton("Retour au Menu");
        retourMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Menu menu = new Menu();
            }
        });
        getContentPane().add(retourMenuButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Méthode pour récupérer la liste des scores.
     */
    public List<Score> getListe_scores() {
        return liste_scores;
    }

    /**
     * Méthode pour ajouter un nouveau score et le sauvegarder dans le fichier.
     */
    public void ajouterNouveauScore(Score new_score) {
        liste_scores.add(new_score);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Score score : liste_scores){
                writer.write(score.getLignes() + "#" + score.getColonnes() + "#" + score.getMeilleur_score() + "#" + score.getNom_joueur());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
