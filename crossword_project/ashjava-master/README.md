## Utilisation du programme

### Sur le menu principal :

- **Nouvelle Partie :**
    - Permet de sélectionner un fichier `.g2cwds` qui permet de générer une grille de mots croisés.
    - Sélectionne le nombre de lignes.
    - Sélectionne le nombre de colonnes.

- **Charger une grille depuis un fichier :**
    - Sélectionne un fichier `.gcwds` et affiche la grille.

- **Charger une partie sauvegardée :**
    - Sélectionne un fichier `.cwds` qui est l'état d'une partie antérieure et charge cette partie.

- **Tableau des scores :**
    - Affiche le tableau des scores organisé par type de grille.

- **Quitter :**
    - Quitter le jeu.

### En jeu :

- **Barre de navigation :**
    - **Temps écoulé :**
        - Indique le temps de jeu.
    - **Sauvegarder :**
        - Permet de sauvegarder l'état courant du jeu dans un fichier `.cwds`.
    - **Vérifier :**
        - Permet de vérifier l'état du jeu.
            - Cadre rouge si une lettre est au mauvais endroit.
            - Cadre vert si une lettre est au bon endroit.
            - Si toutes les cases sont correctement remplies, la partie est considérée comme terminée.
                - Si on a un nouveau meilleur temps, le pseudo du joueur est demandé, sinon, il est enregistré au nom "Anonymous".
                - On affiche le tableau des scores.
    - **Quitter la partie :**
        - Revient au menu principal.
