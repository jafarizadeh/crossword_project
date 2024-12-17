package Game;

import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * La classe Temps gère la mesure du temps de jeu pour une partie de mots croisés.
 */
public class Temps {
    private Timer timer;
    private long tempsDeJeu;
    private boolean enPause;
    private ActionListener listener;

    /**
     * Constructeur de la classe Temps.
     *
     * @param listener ActionListener à appeler à chaque seconde écoulée.
     */
    public Temps(ActionListener listener) {
        this.listener = listener;
        this.tempsDeJeu = 0;
        this.enPause = false;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!enPause) {
                    tempsDeJeu += 1;
                    listener.actionPerformed(null);
                }
            }
        }, 0, 1000);
    }

    /**
     * Bascule entre l'état de pause et de reprise du temps de jeu.
     */
    public void togglePause(){
        enPause = !enPause;
    }

    /**
     * Obtient le temps de jeu actuel.
     *
     * @return Temps de jeu en secondes.
     */
    public long getTempsDeJeu(){
        return tempsDeJeu;
    }

    /**
     * Vérifie si le temps de jeu est actuellement en pause.
     *
     * @return Vrai si le temps de jeu est en pause, sinon faux.
     */
    public boolean estEnPause(){
        return enPause;
    }

    /**
     * Définit le temps de jeu à une valeur spécifiée.
     *
     * @param tempsDeJeu Nouvelle valeur du temps de jeu en secondes.
     */
    public void setTemps_de_jeu(long tempsDeJeu) {
        this.tempsDeJeu = tempsDeJeu;
    }
}
