package GUI;

import GrilleMots.Case;
import Utilitaire.Utilitaire;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CasePanel extends JPanel {
    private Case currentCase;
    private JTextField valeur;


    /**
     * Constructeur de la classe CasePanel.
     *
     * @param currentCase La case associée à ce panneau.
     */
    public CasePanel(Case currentCase) {
        this.currentCase = currentCase;
        setLayout(new BorderLayout());

        valeur = new JTextField();
        ((AbstractDocument) valeur.getDocument()).setDocumentFilter(new Utilitaire.LimiteTaille(1));
        valeur.setHorizontalAlignment(JTextField.CENTER);
        valeur.setEditable(currentCase.getStatut() != Case.typeCase.CASE_NEUTRE);

        switch (currentCase.getStatut()) {
            case CASE_NEUTRE:
                valeur.setBackground(Color.ORANGE);
                break;
            case CASE_VIDE:
                valeur.setBackground(Color.WHITE);
                break;
            case CASE_REMPLIE:
                valeur.setBackground(Color.WHITE);
                valeur.setText(String.valueOf(currentCase.getValeur()).toUpperCase());
                break;
        }

        valeur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nouvelleValeur = valeur.getText();
                if (!nouvelleValeur.isEmpty()) {
                    currentCase.setValeur(nouvelleValeur.charAt(0));
                } else {
                    currentCase.setValeur(' ');
                }
            }
        });
        add(valeur, BorderLayout.CENTER);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    valeur.setText("");
                    currentCase.setValeur(' ');
                }
            }
        });

        valeur.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                onCaseSelected();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                onCaseDeselected();
            }
        });

    }

    /**
     * Appelé lorsqu'une case est sélectionnée.
     */
    private void onCaseSelected(){
        if (currentCase.getStatut() != Case.typeCase.CASE_NEUTRE)
            this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }

    /**
     * Appelé lorsqu'une case est désélectionnée.
     */
    private void onCaseDeselected(){
        this.setBorder(null);
        String val = valeur.getText().toUpperCase();
        if (!val.isEmpty() && val.charAt(0)>= 'A' && val.charAt(0) <= 'Z'){
            valeur.setText(val);
            currentCase.setValeur(val.charAt(0));
            currentCase.setStatut(Case.typeCase.CASE_REMPLIE);
        }
        else{
            valeur.setText("");
        }
    }

    /**
     * Obtient le champ de texte associé à la case.
     *
     * @return Le champ de texte.
     */
    public JTextField getValeur(){
        return valeur;
    }

    /**
     * Obtient la case associée à ce panneau.
     *
     * @return La case associée.
     */
    public Case getCurrentCase(){
        return currentCase;
    }

    /**
     * Définit une bordure rouge autour du panneau.
     */
    public void setBordureRouge() {
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    /**
     * Définit une bordure verte autour du panneau.
     */
    public void setBordureVerte(){
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    }
}
