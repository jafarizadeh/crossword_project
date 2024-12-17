package Utilitaire;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Utilitaire {
    public static String choix_fichier(String extension, String racine, boolean ouverture){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers " + extension.toUpperCase() + " (*." + extension + ")", extension);
        fileChooser.setFileFilter(filtre);
        String path = System.getProperty("user.dir");
        if (path.contains("bin")){
            path = Paths.get(path).getParent().toString();
        }
        path = Paths.get(path, racine).toString();
        fileChooser.setCurrentDirectory(new java.io.File(path));
        int selection = ouverture ? fileChooser.showOpenDialog(null) : fileChooser.showSaveDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getAbsolutePath();
        else{
            return "";
        }
    }
    public static String formatTemps(long tempsEnSecondes) {
        long heures = tempsEnSecondes / 3600;
        long minutes = (tempsEnSecondes % 3600) / 60;
        long secondes = tempsEnSecondes % 60;
        return String.format("%02d:%02d:%02d", heures, minutes, secondes);
    }
    public static class LimiteTaille extends DocumentFilter {
        private int tailleMax;

        public LimiteTaille(int tailleMax) {
            this.tailleMax = tailleMax;
        }

        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
            if ((fb.getDocument().getLength() + str.length()) <= this.tailleMax) {
                super.insertString(fb, offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
            if ((fb.getDocument().getLength() + str.length() - length) <= tailleMax) {
                super.replace(fb, offs, length, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
