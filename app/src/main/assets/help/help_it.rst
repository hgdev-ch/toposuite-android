.. raw:: html

        <meta http-equiv='content-type' content='text/html; charset=utf-8' />


Manuale d'uso di TopoSuite
==========================

.. contents::
  :local:

.. sectnum::

Aiuto generale
--------------

Unità del programma :

Gli angoli devono essere espressi in "GON".

Le distanze devono essere espresse in "Metri".

Le distanze che il programma si aspetta sono distanze inclinate.
Se l'angolo zenitale non è specificato, il programma presuppone che si tratta di una
distanza orizzontale.

Attenzione a convalidare gli ultimi valori nella maschera di acquisizione 
così che il programma possa tenerli in considerazione. 
Per fare ciò posizionare il cursore sulla casella del numero di punto da salvare 
o convalidare gli ultimi valori con "Suiv." o "OK" o "Enter".

Gestione dei punti
~~~~~~~~~~~~~~~~~~

Per modificare o eliminare un punto è sufficiente selezionarlo nella lista punti 
e tenere premuto qualche secondo per fare comparire il menu contestuale.

Il pulsante "Cerca" permette di ritrovare un punto. Il pulsante
"Cancella" permette di eliminare tutti i punti contemporaneamente.  Affinché non vengano
influenzati calcoli precedenti, **non è possibile modificare un numero di punto.** 
*Il numero del punto serve alla sua identificazione e questo modificherebbe i calcoli ripresi dallo storico.*

Importazione
^^^^^^^^^^^^

E' possibile aggiungere i punti manualmente o importare
direttamente tutti i punti utilizzando un file CSV o PTP LTOP.

Da Gmail (o casella mail) non è possibile aprire direttamente un file di punti. E' necessario
prima salvarlo sul telefono/tablet e solo dopo aprirlo dal
files explorer del programma.

Il programma Cyanogenmod file manager non è consigliato perché gestisce bene solo i files CSV.
I seguenti programmi sono ben supportati:

 - Programma di default sui tablet Samsung
 - Amaze File Manager
 - ASTRO
 - ES File Explorer
 - Google Drive
 - Dropbox
 - Zarchiver

Si noti che questo elenco non è esaustivo e che se un programma non è menzionato 
ciò non significa che non è supportato da TopoSuite.

Dal dispositivo (senza connessione): E' necessario posizionare il file nella cartella
dell'applicazione (/TopoSuite) per poter essere importata dal menu del programma.
Un altra soluzione (con o senza connessione): Premendo sul file di punti, 
il dispositivo vi chiederà  con quale applicazione volete aprirlo, 
tra le applicazioni proposte troverete anche "TopoSuite"
Scegliendo TopoSuite nella lista l'applicazione si aprirà e importerà i punti chiedendone conferma
attraverso un popup.

CSV
'''

Il file deve essere strutturato come segue::

        N° Punto ; Coordinata Est ; Coordinata Nord ; Quota

LTOP
''''

Il file LTOP non deve essere modificato. Deve iniziare con $$PK!

Le colonne da 1 a 14 sono dedicate al numero del punto;

Le colonne da 33 a 44 corrispondono alla coordinate Est;

Le colonne da 45 a 56 corrispondono alla coordinate Nord;

Le colonne da 61 a 70 contengono la quota (opzionale).

PTP
'''

Il file PTP deve contenere solo linee con i punti da importare.

Le colonne da 11 a 22 sono dedicate al numero del punto;

Le colonne da 33 a 43 corrispondono alla coordinate Est;

Le colonne da 45 a 55 corrispondono alla coordinate Nord;

Le colonne da 57 a 64 contengono la quota (opzionale).

Per il numero del punto, il programma tiene conto solo della colonna numero
senza gestire il piano, il comune, ecc. Se ci fosse due volte lo stesso numero (su
due piani differenti ad esempio), il programma terrà in memoria solo il primo punto letto. 
Attenzione ad eventuali linee di titolo all'inizio del file.

Esportazione
^^^^^^^^^^^^

E' possibile esportare il file dei punti con il tasto
"Condividi" o il tasto "Esporta". Quest'ultimo permette di esportare il file 
nella cartella interna di TopoSuite così da poterlo recuperare direttamente 
nel software con la funzione "Importa".

Storico
~~~~~~~

Lo storico permette di riprendere tutti i calcoli già effettuati.

Un tasto permette di cancellare tutti dati registrati.

Lavori
~~~~~~

Qualsiasi azione in TopoSuite viene registrata in un database. In questo modo
tutti i calcoli ed i punti sono direttamente recuperati in un'apertura
successiva del programma.

E' possibile tramite il menu "Lavoro" (disponibile dal pannello di sinistra di
TopoSuite) esportare il lavoro corrente in un file \*.tpst, importare un lavoro
da un file \*.tpst o eliminare il lavoro corrente. Con l' esportazione di un lavoro
si salveranno: le preferenze, lo storico dei calcoli e i punti. Quando si
importa un lavoro, Toposuite ovviamente riprende gli stessi dati di quelli esportati.

Come per importazione di punti, è possibile importare files
\*.tpst che si trovano nella directory Toposuite/. Tutti i files
esportati si trovano nella stessa directory (che è accessibile
dal files explorer del programma).

È possibile aprire direttamente un file \*.tpst dal file explorer del programma
(vedi sezione Importazione per gli explorer supportati).

Preferenze
~~~~~~~~~~

Nelle preferenze sono disponibili diverse opzioni:

 - Consentire o impedire l'ingresso di coordinate negative
 - La precisione con cui il programma utilizza le coordinate nei calcoli (numero di decimali dopo il metro)

Le impostazioni di visualizzazione permettono di scegliere il numero di decimali 
da mostrare per:

 - Le coordinate (numero di decimali dopo il metro);
 - Gli angoli (numero di decimali dopo il Gon);
 - Le distanze (comprende anche i fattori di scala) (numero di decimali dopo il metro);
 - Le medie (numero di decimali dopo il centimetro);
 - Gli scarti (numero di decimali dopo il centimetro);
 - Le superfici (numero di decimali dopo il metro quadro).

L'opzione delle coordinate negative non pregiudica l'importazione dei punti o
di punti già esistenti nell'applicazione o di punti calcolati.

Calcoli
-------

Calcoli polari
~~~~~~~~~~~~~~

Orientamento alla stazione
^^^^^^^^^^^^^^^^^^^^^^^^^^

Il calcolo dell'orientamento alla stazione permette di calcolare le incognite
di orientamento. La modifica di un valore può essere fatta premendo qualche secondo 
su una misura

Stazione libera
^^^^^^^^^^^^^^^

Il calcolo della stazione libera fornisce le coordinate (3D) della stazione
e risolve l'incognita di orientamento. Per calcolare la stazione libera con
metodi classici, bisogna rispettare alcune condizioni :

 - Misurare almeno 3 punti di orientamento;
 - Misurare angoli e distanze per ogni punto di orientamento;
 - Ripartire i punti di orientamento in maniera da inglobare la zona di lavoro;
 - La stazione libera deve possibilmente essere posizionata all'interno del poligono
   che è descritto dai punti di orientamento;
 - Scegliere i punti di orientamento coerenti con la precisione della zona
   di lavoro. Ad esempio, è inutile misurare dei PFP2 in una zona digitalizzata.
   In una zona numerico completa di buona qualità bisogna invece privilegiare
   i PFP3 come punti di orientamento;
 - Non scegliere punti di orientamento troppo vicini l'uno dall'altro. Si potrebbe 
   ottenere dei valori sfavorevoli per il fattore  di scala e l'angolo di rotazione;
 - Privilegiare punti stabili con una materializzazione sicura 
   (meglio scegliere un bullone che una termine leggermente inclinato).

Il metodo utilizzato per il calcolo della stazione libera è la trasformazione Helmert.

sE = sN = Errore medio della coordinata Est et Nord della stazione

sH = Errore medio in quota sulle coordinate della stazione libera

vE = Errore residuo Est sul punto in esame

vN = Errore residuo Nord sul punto in esame

vH = Errore residuo altimetrico sul punto in esame

.. vα = Errore residuo angolare sul punto in esame

.. sZo = Errore medio dell'incognita di orientamento

.. vZo = Errore medio di un orientamento compensato

Rilievo polare
^^^^^^^^^^^^^^

Il valore dell'incognita di orientamento può essere recuperato con la spunta in alto a sinistra.

La seguente condizione geometrica dovrebbe essere soddisfatta:

 - La distanza dalla stazione al nuovo punto non deve superare 1,25 volte
   la lunghezza del vettore di orientamento più lungo (distanza dalla stazione
   al punto d'orientamento più distante).

Per gli spostamenti, i seguenti simboli sono da rispettare:

|levé polaire|

ATTENZIONE: La quota di un punto con DM1 o DM2 non è alla posizione
delle coordinate definitive.

Se l'altimetria non è significativa bisogna rimuoverla dall' editor dei punti.

Tracciamento polare
^^^^^^^^^^^^^^^^^^^

Il calcolo del tracciamento polare permette (partendo da coordinate) d'ottenere
i seguenti valori:

 - l'angolo orizzontale,(Hz)
 - la distanza orizzontale,
 - la distanza inclinata,
 - l'angolo zenitale,
 - l'altezza del prisma,
 - l'azimut (φ)

La seguente condizione geometrica dovrebbe essere soddisfatta:

 - La distanza dalla stazione al nuovo punto non deve superare 1,25 volte
   la lunghezza della vettore di orientamento più lungo (distanza dalla stazione
   al punto d'orientamento più distante).

 |Implantation polaire|

Tracciamento assi
^^^^^^^^^^^^^^^^^

Il calcolo permette d'ottenere gli spostamenti trasversali e longitudinali rispetto ad un asse AB (partendo dal punto base "A")

Calcoli Ortogonali
~~~~~~~~~~~~~~~~~~

Rilievo ortogonale
^^^^^^^^^^^^^^^^^^

Il valore della misura della base (A-B) del rilievo ortogonale  deve essere inserito
per il calcolo del fattore di scala.

I valori delle ascisse (x) e ordinate (y) devono essere inseriti come 
nella seguente immagine:

 |levé orthogonal|

La seguente condizione geometrica dovrebbe essere soddisfatta:

 - L'ascissa fuori della base non dovrebbe superare un quarto della
   lunghezza della base.
 - L'ordinata non deve superare i 35 m (per un punto di confine).
 - L'ordinata non dovrebbe superare la lunghezza della base.

Camminamento ortogonale
^^^^^^^^^^^^^^^^^^^^^^^

I segni da rispettare nel camminamento ortogonale sono:

 |cheminement orthogonal|

La seguente condizione geometrica dovrebbe essere soddisfatta:

 - La differenza tra AB calcolato e AB misurato non deve superare le tolleranze.
 - La lunghezza totale del camminamento non deve superare 3 volte la distanza AB.
 - I punti possono essere determinati "fuori base" (camminamento lanciato da 
   ogni punto di raccordo), ma la distanza cumulata fuori base non
   deve superare ½ AB.

Tracciamento ortogonale
^^^^^^^^^^^^^^^^^^^^^^^

Il calcolo del tracciamento ortogonale permette (partendo dalle coordinate) d'ottenere
i valori delle ascisse ed ordinate per effettuare il tracciamento sul terreno. 
I valori delle ascisse (x) e ordinate (y) devono essere inseriti come 
nella seguente immagine:

 |Implantation orthogonale|

Le condizioni geometriche dovrebbero essere soddisfatte.

Intersezioni
~~~~~~~~~~~~~

Rette (e/o direzioni)
^^^^^^^^^^^^^^^^^^^^^

La definizione delle rette può essere data:

 - Con due punti;
 - Con un punto e un angolo (φ).

Può essere dato un offset alla retta d'intersezione o intersecarla perpendicolarmente alla retta definita.

Per l'offset (valore X), con il segno positivo lo spostamento sarà a destra 
con il segno negativo lo spostamento sarà sulla sinistra.
Se le rette sono perpendicolari una distanza A (valore Y)
può essere inserita.

Guardare l'immagine seguente:

 |Intersection de lignes|

Intersezione di cerchi (distanze)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Per l'intersezione di cerchi ci sono due possibilità:

 - Inserire manualmente le dimensioni dei raggi;
 - Scegliere un punto sul cerchio, la dimensione del raggio sara calcolata
   automaticamente.

Attenzione: Ci saranno quasi sempre due soluzioni.

Ci sarà una unica soluzione solo se i due cerchi sono tangenti.

Non ci saranno soluzione se i due cerchi non si intersecano.

Definizione del primo cerchio ➜ Centro : A et Raggio : r1

Definizione del secondo cerchio ➜ Centro : B et Raggio : r2

 |Intersection de cercles|

I punti di intersezioni calcolati sono: I1 et I2.

Retta/Cerchio
^^^^^^^^^^^^^

La definizione delle rette può essere data:

 - Con due punti;
 - Con un punto e un angolo (G).

La definizione del cerchio può essere data:

 - Inserendo manualmente le dimensioni dei raggi;
 - Scegliendo un punto sul cerchio, la dimensione del raggio sarà calcolata
   automaticamente.

Può essere dato un offset alla retta (segno positivo = a destra ; segno negativo = a
sinistra).

 |Intersection d'un cercle et d'une droite|

I due punti calcolati sono I1 et I2.

Superfici
~~~~~~~~~

Calcolo di superficie (anche con archi)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Per il calcolo di superficie gli elementi da inserire sono:

 - I punti in senso orario.

Così come l'immagine seguente:

 |Surface|

 Punto 1

 Punto 2, Raggio +

 Punto 3,

 Punto 4,

 Punto 5, Raggio -

 Punto 6,

 Punto 7

Spostamento parallelo di un confine con superficie imposta
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Lo scopo di questo calcolo è di trovare il valore dello spostamento del confine AD 
per ottenere la superficie desiderata (S).

XY deve essere parallelo a AD

Si ottengono i seguenti valori:

d = lunghezza dello spostamento del confine AD

S = Superficie imposta

 |Déplacement parallèle de limite avec surface imposée|

Vari
~~~~

Azimut/Distanza
^^^^^^^^^^^^^^^

Per il calcolo dell' azimut/distanza è sufficiente scegliere nella lista dei punti:

 - il punto d'origine,
 - il punto d'orientamento.

Proiezione del punto sulla retta
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Per definire la retta esistono due possibilità:

 - Con due punti;
 - Con un punto e l'azimut.

Dando un offset alla retta con segno positivo si effettuerà uno spostamento a destra 
e con un segno negativo a sinistra.

Successivamente si sceglie il punto conosciuto da proiettare. 

Il N° del punto richiesto è quello proiettato sulla linea. 

 |Projection d'un point sur une droite|

x = Distanza dal punto da proiettare (P) alla retta 

y = Distanza dal punto da proiettare al punto A

z = Distanza dal punto da proiettare al punto B

Calcolo di un cerchio con tre punti
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Con tre punti conosciuti si possono calcolare le coordinate del centro del cerchio 
ed il suo raggio

Si possono salvare le coordinate del centro del cerchio dando un N°al punto calcolato.

Segmentazione di un arco
^^^^^^^^^^^^^^^^^^^^^^^^

Questo calcolo fornisce coordinate equidistanti distribuite lungo l'arco.

La segmentazione può essere eseguita inserendo il numero di segmenti desiderati
(Immagine A) o una lunghezza (a) inserita manualmente (Immagine B).

Ovviamente, nel secondo caso, la lunghezza dell'ultimo segmento sarà diversa da
quella impostata per il calcolo (?) .

 |Segmentation d'un arc de cercle|

Matematica
~~~~~~~~~~

Risoluzione dei triangoli
^^^^^^^^^^^^^^^^^^^^^^^^^

Gli elementi necessari per la risoluzione di un triangolo sono i seguenti:

 - 3 Lati
 - 2 Lati e l' Angolo compreso
 - 1 Lato, 2 Angoli

 |Triangle|

r = Raggio del cerchio inscritto

R = Raggio del cerchio circoscritto

h = Altezza calcolata

Risoluzione di elementi circolari
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Gli elementi da inserire per il calcolo sono:

 - Raggio, Corda
 - Raggio, Angolo al centro
 - Raggio, Tangente
 - Raggio, Freccia
 - Corda, Angolo al centro
 - Corda, Tangente
 - Corda, Freccia
 - Tangente, Angolo al centro

 |Eléments circulaires|

S = Vertici

C = Centro

CO = CM = CF = Raggio

OF = Corda (OF)

OS = FS = Tangente

MS = Bisettrice

β = Angolo al vertice

2α = Angolo al centro

M = Mezzaria dell' arco

MB = Freccia

 |Eléments circulaires 2|

L'immagine in alto rappresenta la superficie del settore.

L'immagine in basso rappresenta la superficie del segmento.

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
