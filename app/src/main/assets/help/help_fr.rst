.. raw:: html

        <meta http-equiv='content-type' content='text/html; charset=utf-8' />


Aide à l'utilisation de TopoSuite
=================================

.. contents::
  :local:

.. sectnum::

Aide générale
-------------

Unités du programme :

Tous les angles à introduire doivent être en "Grades" ou en "Gons".

Toutes les distances à introduire doivent être en "Mètres".

Les distances que le programme attend sont toutes des distances inclinées.
Si l'angle zénithal n'est pas indiqué, le programme considère que c'est une
distance horizontale (angle à 100 [g] par défaut).

ATTENTION à valider vos dernières valeurs dans les masques de saisies pour que
le programme prenne en compte cette valeur. Pour cela, il faut placer le curseur
sur la case du numéro de point à sauvegarder ou valider votre dernière valeur
avec "Suiv." ou "OK" ou "Enter".

Gestion des points
~~~~~~~~~~~~~~~~~~

Pour modifier un point, un clic ouvre la fenêtre d'édition.  Afin de ne pas
affecter de précédents calculs, **il n'est pas possible de modifier un numéro
de point.** Le numéro de point servant à identifier un point, cela modifierait
les calculs repris depuis l'historique.

Pour supprimer un point, il suffit d'un appui prolongé sur le point
souhaité dans la liste des points. Ceci a pour effet de faire apparaître un
menu contextuel.

Importation
^^^^^^^^^^^

Il est possible soit d'ajouter les points manuellement, soit d'importer
directement tous les points à l'aide d'un fichier CSV, LTOP ou PTP.

Depuis Gmail, il n'est pas possible d'ouvrir directement un fichier de point. Il
faut d'abord le sauvegarder sur le téléphone et ensuite l'ouvrir depuis
un explorateur de fichiers.

Le programme Cyanogenmod file manager est à proscrire car il ne gère pas bien
les fichiers autre que CSV. Les programmes suivant sont bien supportés:

 - Celui par défaut sur les tablettes Samsung
 - Amaze File Manager
 - ASTRO
 - ES File Explorer
 - Google Drive
 - Dropbox
 - Zarchiver

À noter que cette liste n'est pas exhaustive et que ce n'est pas parce qu'un
explorateur de fichier n'est pas mentionné ici qu'il n'est pas pris en charge.

Depuis l'appareil (sans connexion): Il faut placer le fichier dans le dossier de
l'application (/TopoSuite) pour être importé depuis le menu du programme.
L'autre solution (avec connexion ou non): En "appuyant" sur le fichier de
points, l'appareil va proposer d'ouvrir le fichier avec plusieurs applications,
dont "TopoSuite". En choisissant TopoSuite dans la liste, l'application va
s'ouvrir et importer la liste des points en demandant une confirmation au
travers d'une popup.

CSV
'''

Le fichier doit être structuré de la manière suivante::

        N° Point ; Coordonnée Est ; Coordonnée Nord ; Altitude

À noter que le séparateur peut indifféremment être "," ou ";".

LTOP
''''

Le fichier LTOP ne doit pas être modifié. Il doit bien commencer avec $$PK!

Les colonnes 1 à 14 sont consacrées au numéro de point;

Les colonnes 33 à 44 correspondent à la coordonnée Est,

Les colonnes 45 à 56 correspondent à la coordonnée Nord,

Les colonnes 61 à 70 contiennent l'altitude (optionnel).

PTP
'''

Le fichier PTP ne doit pas comporter de lignes autres que celles contenant les
points à importer.

Les colonnes 11 à 22 sont pour le numéro de point

Les colonnes 33 à 43 correspondent à la coordonnée Est,

Les colonnes 45 à 55 correspondent à la coordonnée Nord,

Les colonnes 57 à 64 contiennent l'altitude (optionnel).

Pour le numéro de point, le programme ne prend en compte que la colonne numéro
sans gérer le plan, la commune, etc.  S'il y a deux fois le même numéro (sur
deux plans différents par ex.), le programme ne prend que le premier point lu du
fichier.  Attention aux éventuelles lignes de titre en début de fichier.

Exportation
^^^^^^^^^^^

Il est également possible d'exporter le fichier de point avec le bouton
"Partager", ou le bouton "Exporter". Ce dernier permet d'exporter le ficher dans
le dossier interne de TopoSuite et peut être récupéré dans le logiciel avec la
fonction "Importer".

Historique
~~~~~~~~~~

L'historique permet de reprendre n'importe quel calcul déjà effectué.

Une touche permet également d'effacer toutes les données stockées.

Jobs
~~~~

Toute action dans TopoSuite est enregistrée dans une base de données.  Ainsi
tous les calculs et points sont directement récupérés lors d'une ouverture
ultérieure de TopoSuite.

Il est possible via le menu "Job" (disponible depuis le volet gauche de
TopoSuite) d'exporter le job courant vers un fichier \*.tpst, d'importer un job
depuis un fichier \*.tpst ou même d'effacer le job courant.  L'exportation d'un
job sauvegarde: les préférences, l'historique des calculs et les points. Lors de
l'import de job, il est évident que Toposuite reprend les mêmes données que
celles exportées.

A l'instar de l'import de points, il est possible d'importer les fichiers
\*.tpst qui se trouvent dans le répertoire Toposuite/. Tous les fichiers
exportés se trouvent également dans ce même répertoire (qui est accessible
depuis l'explorateur de fichier).

Il est possible d'ouvrir directement un fichier \*.tpst depuis l'explorateur de
fichier (Voir section Importation pour les explorateurs supportés).

Préférences
~~~~~~~~~~~

Dans les préférences, plusieurs options sont possibles.

Via les paramètres généraux, il est possible de:

 - Spécifier le séparateur pour les fichiers CSV;
 - Autoriser ou non la saisie des coordonnées négatives;
 - Spécifier la précision avec laquelle le programme utilise les coordonnées
   pour les calculs (nombre de décimales après le mètre);

Les paramètres d'affichage permettent de choisir le nombre de décimales à
afficher pour:

 - Les coordonnées (nombre de décimales après le mètre);
 - Les angles (nombre de décimales après le grade);
 - Les distances (comprenant également les facteurs d'échelles) (nombre de
   décimales après le mètre);
 - Les moyennes(nombre de décimales après le centimètre);
 - Les écarts et (nombre de décimales après le centimètre)
 - Les surfaces (nombre de décimales après le mètre carré).

L'option des coordonnées négatives, n'affecte pas l'import de points ou les
points qui ont déjà été entrés dans l'application ni même les points calculés.

Calculs
-------

Calculs polaires
~~~~~~~~~~~~~~~~

Orientation à la station
^^^^^^^^^^^^^^^^^^^^^^^^

Le calcul d'orientation à la station permet de calculer l'inconnue
d'orientation. La modification d'une valeur peut se faire en cliquant longuement
sur une mesure.

Station libre
^^^^^^^^^^^^^

Le calcul de station libre permet d'obtenir les coordonnées (3D) de la station
ainsi que l'inconnue d'orientation Afin de calculer la station libre avec des
moyens classiques, il y a quelques conditions à respecter :

 - Prendre au minimum 3 points de rattachement;
 - Mesurer les angles et distances sur chaque point de rattachement;
 - Bien répartir les points de rattachement de manière à envelopper la zone de
   travail;
 - La station libre doit si possible se situer à l'intérieur du polygone décrit
   par les points de rattachement;
 - Choisir les points de rattachement représentatifs de la qualité de la zone de
   travail (règles de voisinage). Par exemple, il est inutile de prendre des
   PFP2 pour s’intégrer dans une zone de mensuration graphique numérisée. Dans
   une zone de mensuration numérique de bonne qualité, il faut privilégier la
   prise de PFP3;
 - Ne pas prendre deux points de rattachement trop proches l'un de l'autre. Cela
   peut donner des valeurs peu favorables pour le facteur d’échelle et pour
   l’angle de rotation;
 - Privilégier la prise de points stables dont la matérialisation n’est pas
   douteuse (si possible des chevilles à la place de bornes penchées).

La méthode utilisée pour la station libre est un calcul d'Helmert.

sE = sN = Erreur moyenne de la coordonnée Est et Nord de la station

sH = Erreur moyenne de l'altimétrie sur la coordonnée de la station libre

vE = Erreur résiduelle Est sur le point concerné

vN = Erreur résiduelle Nord sur le point concerné

vH = Erreur résiduelle en altimétrie sur le point concerné

.. vα = Erreur résiduelle angulaire sur le point concerné

.. sZo = Erreur moyenne sur l'inconnue d'orientation

.. vZo = Erreur moyenne d'une direction compensée

Echelle = Facteur d'échelle du calcul de la station libre (fraction et ppm).
Ce facteur d'échelle reste à 1 pour tous les autres calculs polaires.

Levé polaire
^^^^^^^^^^^^

La valeur de l'inconnue d'orientation peut être récupérée avec la
coche en haut à gauche.

La condition géométrique suivante devrait être remplie:

 - La distance de la station au point nouveau ne doit pas dépasser 1.25 fois la
   longueur du vecteur d'orientation le plus long (distance de la station au
   point servant d'orientation).

Pour les déplacements, les signes suivants sont à respecter:

|levé polaire|

ATTENTION: L'altitude d'un point avec un DM1 ou un DM2 n'est pas à l'emplacement
des coordonnées définitives.

Il faut supprimer l'altimétrie dans le gestionnaire des points si celle-ci n'est
pas significative.

Implantation polaire
^^^^^^^^^^^^^^^^^^^^

Le calcul d'implantation polaire permet (à partir de coordonnées) d'obtenir les
valeurs suivantes:

 - l'angle horizontal,(Hz)
 - la distance horizontale,
 - la distance inclinée,
 - l'angle zénithal,
 - la hauteur de prisme,
 - le gisement (φ)

La condition géométrique suivante devrait être remplie:

 - La distance de la station au point implanté ne doit pas dépasser 1.25 fois la
   longueur du vecteur d'orientation le plus long (distance de la station au
   point servant d'orientation).

 |Implantation polaire|

Implantation d'un axe
^^^^^^^^^^^^^^^^^^^^^

Ce calcul permet d'obtenir le décalage transversal et longitudinal par rapport à
un axe AB (à partir du point de base "A")

Calculs orthogonaux
~~~~~~~~~~~~~~~~~~~

Levé orthogonal
^^^^^^^^^^^^^^^

La valeur mesurée de la base du levé orthogonal (A-B) doit être rentrée afin de
calculer le facteur d'échelle.

Les valeurs d'abscisses (x) et d'ordonnées (y) doivent être introduites selon
l'image ci-dessous:

 |levé orthogonal|

Les conditions géométriques suivantes devraient être remplies:

 - L'abscisse hors base ne doit généralement pas dépasser le quart de la
   longueur de la base.
 - L'ordonnée ne doit pas dépasser 35 m (pour un point limite).
 - L'ordonnée ne doit généralement pas dépasser la longueur de la base.

Cheminement orthogonal
^^^^^^^^^^^^^^^^^^^^^^

Les signes à respecter pour le cheminement orthogonal sont les suivants:

 |cheminement orthogonal|

Les conditions géométriques suivantes devraient être remplies:

 - La différence entre AB calculé et AB mesuré doit être dans la tolérance.
 - La longueur totale du cheminement ne doit pas dépasser 3 fois la distance AB.
 - Des points peuvent être déterminés "hors base" (cheminement lancé depuis
   chacun des points de rattachement), mais la distance cumulée hors base ne
   doit pas dépasser ½ AB.

Implantation orthogonale
^^^^^^^^^^^^^^^^^^^^^^^^

Le calcul d'implantation orthogonale permet (à partir de coordonnées) d'obtenir
les valeurs d'abscisses et d'ordonnées pour faire une implantation sur le
terrain. Les valeurs d'abscisses (x) et d'ordonnées (y) doivent être données
selon l'image ci-dessous:

 |Implantation orthogonale|

Les conditions géométriques du levé orthogonal devraient être remplies.

Intersections
~~~~~~~~~~~~~

Droites (et/ou directions)
^^^^^^^^^^^^^^^^^^^^^^^^^^

La définition des droites peut être donnée:

 - Soit par deux points;
 - Soit par un point et un angle (φ).

La droite peut être soit décalée, soit perpendiculaire à la droite
définie.

Pour le décalage (valeur X), un signe positif décale la droite sur la droite et
un signe négatif la décale sur la gauche.

Si la droite est à la perpendiculaire de celle donnée, une distance du point A
(valeur Y) peut-être rentrée.

Voir image ci-dessous:

 |Intersection de lignes|

Intersections de cercles (distances)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Pour l'intersection de cercles, deux possibilités:

 - Rentrer manuellement la grandeur des rayons;
 - Choisir un point sur le cercle, la grandeur du rayon est calculée
   automatiquement.

Attention: Il y a presque toujours 2 solutions.

Il n'y en a qu'une seulement si les deux cercles sont tangents.

Il n'y en a aucune si les deux cercles ne se croisent pas.

Définition du premier cercle ➜ Centre : A et Rayon : r1

Définition du deuxième cercle ➜ Centre : B et Rayon : r2

 |Intersection de cercles|

Les intersections calculées sont: I1 et I2.

Droite/Cercle
^^^^^^^^^^^^^

La définition des droites peut être donnée:

 - Soit par deux points
 - Soit par un point et un angle (G)

La définition du cercle peut être donnée:

 - Soit en rentrant manuellement la grandeur des rayons
 - Soit en choisissant un point sur le cercle, la grandeur du rayon est
   calculée automatiquement.

La droite peut être décalée (signe positif = à droite ; signe négatif = à
gauche).

 |Intersection d'un cercle et d'une droite|

Les deux points calculés sont I1 et I2.

Surfaces
~~~~~~~~

Calcul de surface (y c. avec arcs de cercles)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Pour le calcul de surface, les éléments à saisir sont:

 - Les points dans le sens des aiguilles d'une montre.

Selon l'image ci-dessous:

 |Surface|

 Point 1

 Point 2, Rayon +

 Point 3,

 Point 4,

 Point 5, Rayon -

 Point 6,

 Point 7

Déplacement parallèle de limite avec surface imposée
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Le but de ce calcul est de retrouver la valeur du décalage de la limite AD afin
d'obtenir la surface désirée (S).

XY doit être parallèle à AD

Les valeurs obtenues sont les suivantes:

d = longueur du déplacement de la limite AD

S = Surface imposée

 |Déplacement parallèle de limite avec surface imposée|

Divers
~~~~~~

Gisement/Distance
^^^^^^^^^^^^^^^^^

Pour le calcul de gisement/distance, il suffit de choisir dans la liste des
points connus:

 - le point d'origine,
 - le point d'orientation.

Projection d'un point sur une droite
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Pour définir la droite, il existe deux possibilités:

 - Par deux points connus;
 - Par un point et un gisement.

La droite peut être déplacée sur la droite avec un signe positif, et sur la
gauche avec un signe négatif.

Il faut ensuite choisir le point à projeter dans la liste des points connus.

Le N° de point demandé est celui de la projection du point sur la ligne.

 |Projection d'un point sur une droite|

x = Distance du point à projeter (P) à la droite

y = Distance du point projeté au point A

z = Distance du point projeté au point B

Calcul d'un cercle par trois points
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A partir de trois points de la liste de coordonnées, il est possible d'obtenir
les coordonnées du centre et le rayon du cercle.

Il est possible d'enregistrer les coordonnées du centre en rentrant un N° de
point.

Segmentation d'un arc de cercle
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Ce calcul permet d'obtenir des coordonnées réparties le long de l'arc de cercle.

La segmentation peut se faire à partir du nombre de segments que l'on veut
(Image A) ou d'une distance (a) rentrée manuellement (Image B).

Il est clair, dans la deuxième solution, que la distance restante entre le
dernier point calculé et l'extrémité de l'arc de cercle n'est pas égale aux
autres distances (?) .

 |Segmentation d'un arc de cercle|

Mathématiques
~~~~~~~~~~~~~

Résolutions de triangles
^^^^^^^^^^^^^^^^^^^^^^^^

Les éléments à saisir pour résoudre un triangle sont les suivants:

 - 3 Côtés
 - 2 Côtés, 1 Angle compris
 - 1 Côté, 2 Angles

 |Triangle|

r = Rayon du cercle inscrit

R = Rayon du cercle circonscrit

h = Hauteur calculée

Résolution d'éléments circulaires
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Les éléments à saisir pour calculer sont les suivants:

 - Rayon, Corde
 - Rayon, Angle au centre
 - Rayon, Tangente
 - Rayon, Flèche
 - Corde, Angle au centre
 - Corde, Tangente
 - Corde, Flèche
 - Tangente, Angle au centre

 |Eléments circulaires|

S = Sommet

C = Centre

CO = CM = CF = Rayon

OF = Corde (OF)

OS = FS = Tangente

MS = Bissectrice

β = Angle au sommet

2α = Angle au centre

M = Milieu de courbe

MB = Flèche

 |Eléments circulaires 2|

L'image du haut représente la surface du secteur

L'image du bas représente la surface du segment.

.. |levé polaire| image:: file:///android_asset/help/img/polar_survey.png
.. |Implantation polaire| image:: file:///android_asset/help/img/polar_implantation.png
.. |levé orthogonal| image:: file:///android_asset/help/img/orthogonal_implantation.png
.. |cheminement orthogonal| image:: file:///android_asset/help/img/chem_ortho.png
.. |Implantation orthogonale| image:: file:///android_asset/help/img/orthogonal_implantation.png
.. |Intersection de lignes| image:: file:///android_asset/help/img/lines_intersection.png
.. |Intersection de cercles| image:: file:///android_asset/help/img/circles_intersection.png
.. |Intersection d'un cercle et d'une droite| image:: file:///android_asset/help/img/line_circle_intersection.png
.. |Surface| image:: file:///android_asset/help/img/surface.png
.. |Déplacement parallèle de limite avec surface imposée| image:: file:///android_asset/help/img/parallel_displacement.png
.. |Projection d'un point sur une droite| image:: file:///android_asset/help/img/point_project_line.png
.. |Segmentation d'un arc de cercle| image:: file:///android_asset/help/img/circular_segmentation.png
.. |Triangle| image:: file:///android_asset/help/img/triangle.png
.. |Eléments circulaires| image:: file:///android_asset/help/img/circular_curve.png
.. |Eléments circulaires 2| image:: file:///android_asset/help/img/circular_curve_2.png
