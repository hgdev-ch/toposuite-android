.. raw:: html

        <meta http-equiv='content-type' content='text/html; charset=utf-8' />


Hilfe bei der Benützung von TopoSuite
=====================================

.. contents::
  :local:

.. sectnum::

Allgemeine Hilfe
----------------

Programmeinheiten:

Alle Winkel müssen in "Grad" oder "Gon" eingegeben werden.

Alle Distanzen müssen in "Meter" eingegeben werden. Die vom Programm erwarteten
Distanzen sind alles Schrägdistanzen.  Wenn kein Zenitwinkel (Höhenwinkel)
angegeben ist, nimmt das Programm an, dass es sich um eine Horizontaldistanz
handelt. 

VORSICHT: bestätigen Sie Ihre letzten Werte in den Eingabemasken damit das
Programm diese Werte berücksichtigt. Um dies zu tun, setzen Sie den Cursor auf
das Feld der zu speichernden Punktnummer oder bestätigen Sie Ihren letzten Wert
mit "Weiter" oder "OK" oder "Enter".

Punktverwaltung
~~~~~~~~~~~~~~~

Um einen einzelnen Punkt zu ändern oder zu löschen, genügt ein verlängerter
Druck auf diesen Punkt in der Punkteliste. Dies bewirkt das Erscheinen eines
Kontextmenüs.

Der Knopf "Suchen" ermöglicht, einen gewünschten Punkt zu finden. Der Knopf
"Löschen" ermöglicht, alle Punkte gleichzeitig zu löschen. Um nicht frühere
Berechnungen zu beeinflussen, ist es nicht möglich, eine Punktnummer ändern. Da
die Punktnummer verwendet wird, um einen Punkt zu identifizieren, würde dies
aus dem chronologischen Ablauf wieder verwendete Berechnungen ändern.

Jobs
^^^^

Jede Aktion in TopoSuite wird in einer Datenbank aufgezeichnet. So werden alle
Berechnungen und Punkte bei einer späteren Öffnung von TopoSuite direkt
zurückgeholt.

Es ist möglich, über das Menü "Job" (verfügbar auf der linken Seite von
TopoSuite) den laufenden Job in eine \*.tpst-Datei exportieren, einen Job aus
einer Datei \*.tpst zu importieren oder die aktuelle Arbeit zu löschen.  Der
Backup-Auftrag eines Jobs sichert: die Einstellungen, den chronologischen
Ablauf der Berechnungen und die Punkte. Wenn ein Job importiert wird, ist es
klar, dass TopoSuite die früher gesicherten Daten wieder einführt.

Wie der Import von Punkten, ist es möglich, auch \*.tpst Dateien, welche sich
im TopoSuite/Verzeichnis befinden, zu importieren. Alle exportierten Dateien
befinden sich ebenfalls im gleichen Verzeichnis (zugänglich aus dem
Datei-Explorer).

Es ist möglich, direkt eine \*.tpst Datei aus dem Datei-Explorer zu öffnen (für
die zugelassenen Explorers, siehe Abschnitt Import).

Import
^^^^^^

Es ist möglich, entweder Punkte manuell anzufügen, oder alle Punkte mit Hilfe
einer Datei CSV, LTOP oder PTP direkt zu importieren.
 
Ab Gmail ist es nicht möglich, eine Punkt-Datei direkt zu öffnen. Man muss
diese erst auf ein Telefon speichern und danach ab den Dateibrowser öffnen.

Das Programm Cyanogenmod file manager ist zu vermeiden, denn ausser CSV ist die
Verwaltung anderer Dateien schlecht.  Folgende Programme sind gut akzeptiert:

- Jenes der Standardeinstellungen in den Samsung Tabletts
- ASTRO
- ES File Explorer
- Google Drive
- Dropbox
- Zarchiver

Beachten Sie bitte, dass diese Liste nicht erschöpfend ist, und es nicht
heissen will, weil ein Dateibrowser hier nicht erwähnt ist, dass dieser nicht
unterstützt wird.

Ab dem Gerät (ohne Verbindung): Sie müssen die Datei in den Ordner der
Anwendung platzieren (/TopoSuite), um aus dem Programm-Menü importiert zu
werden.  Die Alternative (mit oder ohne Verbindung): Durch "Drücken" auf die
Punkt-Datei bietet das Gerät an, die Datei mit mehreren Anwendungen,
einschließlich "TopoSuite", zu öffnen. Durch die Wahl TopoSuite in der Liste
öffnet sich die Anwendung, und der Import der Punkteliste erfordert eine
Bestätigung durch ein Popup.

CSV
'''

Diese Datei muss folgendermassen strukturiert sein:

Punktnummer; Koordinate Ost; Koordinate Nord; Höhe.

LTOP
''''

Die Datei LTOP darf nicht verändert werden. Sie muss wirklich mit $$PK
beginnen!

Die Kolonnen 1 bis 14 sind für die Punktnummer bestimmt. 

Die Kolonnen 33 bis 44 entsprechen der Koordinate Ost.

Die Kolonnen 45 bis 56 entsprechen der Koordinate Nord.

Die Kolonnen 61 bis 70 enthalten die Höhe (optional).

PTP
'''

Die Datei PTP darf keine anderen Linien enthalten ausser jenen, welche die zu
importierenden Punkte betreffen.

Die Kolonnen 11 bis 22 sind für die Punktnummer bestimmt.

Die Kolonnen 33 bis 43 entsprechen der Koordinate Ost.

Die Kolonnen 45 bis 55 entsprechen der Koordinate Nord.

Die Kolonnen 57 bis 64 enthalten die Höhe (optional).

Für die Punktnummer nimmt das Programm nur Rücksicht auf die Kolonne dieser
Nummer, ohne Plan- oder Gemeindenummern, usw.  zu verwalten. 

Wenn die gleiche Nummer zweimal auftritt (z.B. auf zwei verschiedenen Plänen),
bearbeitet hält das Programm nur den ersten gelesenen Punkt der Datei.  Achten
Sie auf eventuelle Titellinien zu Beginn der Datei.

Export
^^^^^^

Es ist auch möglich, die Punkt-Datei mit dem Knopf "Teilen" oder dem Knopf
"Exportieren" auszuführen. Dieser Letzte ermöglicht Ihnen, die Datei in die
interne Datei von TopoSuite zu exportieren und kann dann im Programm mit der
Funktion "Import" zurückgeholt werden.

Chronologischer Ablauf
~~~~~~~~~~~~~~~~~~~~~~

Dieser Ablauf erlaubt, jede bereits ausgeführte Berechnung zu wiederholen. 

Eine Taste erlaubt ebenfalls, alle gespeicherten Daten zu löschen. 

Einstellungen
~~~~~~~~~~~~~

Die Einstellungen erlauben verschiedene Optionen:

- Erlaubnis oder nicht der Eingabe von negativen Koordinaten
- Die Genauigkeit, mit welcher das Programm die Koordinaten für die
  Berechnungen benützt (Anzahl Dezimalstellen nach dem Meter)

Die Anzeige-Parameter erlauben die Wahl der anzuzeigenden Dezimalstellen für: 

- Die Koordinaten (Anzahl Dezimalstellen nach dem Meter);
- Die Winkel (Anzahl Dezimalstellen nach dem Grad/gon);
- Die Distanzen – einschliesslich der Massstabsfaktoren (Anzahl Dezimalstellen
  nach dem Meter);
- Die Mittelwerte (Anzahl Dezimalstellen nach dem Zentimeter);
- Die Abweichungen (Anzahl Dezimalstellen nach dem Zentimeter);
- Die Flächen (Anzahl Dezimalstellen nach dem Quadratmeter). 

Die Option negative Koordinaten hat keinen Einfluss auf den Import von Punkten,
sowie weder auf Punkte, welche bereits in die Anwendung eingegeben wurden, noch
bereits berechnete Punkte.

Berechnungen
------------

Polarberechnungen
~~~~~~~~~~~~~~~~~

Satzorientierung
^^^^^^^^^^^^^^^^

Die Berechnung der Satzorientierung erlaubt die Orientierungsunbekannte zu
erhalten. Die Änderung eines Wertes erhält man durch längeres Klicken auf
dieses Mass. 

Freie Station 
^^^^^^^^^^^^^

Die Berechnung der freien Station ergibt die Koordinaten (3D) der Station sowie
die Orientierungsunbekannte. Um die freie Station mit den klassischen Mitteln
zu berechnen, müssen einige Voraussetzungen respektiert werden:

- Mindestens 3 Anschlusspunkte messen; 
- Winkel und Distanz auf jeden Anschlusspunkt messen; 
- Die Anschlusspunkte gut verteilen, sodass das Arbeitsgebiet umschlossen ist; 
- Die freie Station sollte so gut wie möglich innerhalb des Polygons liegen,
  welchen die Anschlusspunkte beschreiben; 
- Repräsentative Anschlusspunkte der Qualität des Arbeitsgebietes wählen (Regel
  der Nachbarschaft). Es ist zum Beispiel unnötig, LFP2 zu nehmen, um sich in
  die Zone einer digitalisierten graphischen Vermessung zu integrieren. In
  einer Zone der numerischen Vermessung guter Qualität, ist die Messung von
  LFP3 vorzuziehen; 
- Nicht zwei zu nahe beieinander liegende Anschlusspunkte messen. Dies kann
  ungünstige Werte für den Massstabsfaktor und den Rotationswinkel ergeben; 
- Das Messen von Fixpunkten bevorzugen, deren Versicherung unzweifelhaft ist
  (wenn möglich Bolzen statt schief stehende Marksteine). 

Die Methode von Helmert wird zur Berechnung der freien Station benützt. 

sE = sN = mittlerer Fehler der Koordinate Ost und Nord der freien Station

sH = mittlerer Fehler der Höhe der Koordinate der freien Station 

vE = Restfehler Ost auf den betreffenden Punkt  

vN = Restfehler Nord auf den betreffenden Punkt

vH = Restfehler der Höhe auf den betreffenden Punkt

.. vα = Restfehler des Winkels auf den betreffenden Punkt

.. sZo = mittlerer Fehler einer Orientierungsunbekannten

.. vZo = mittlerer Fehler einer kompensierten Richtung

Maßstab = Massstabsfaktor der Berechnung der freien Station (Bruchteil und ppm).
Der Massstabsfaktor bleibt aber bei 1 für alle weiteren Polarberechnungen.

Polaraufnahme
^^^^^^^^^^^^^

Der Wert der Orientierungsunbekannten kann mit einem Haken links oben geborgen
werden.

Die folgende geometrische Bedingung sollte erfüllt sein: 


- Die Distanz von der Station zum Neupunkt sollte nicht 1.25 Mal die Länge des
  längsten Orientierungsvektors übersteigen (Distanz zwischen Station und dem
  zur Orientierung dienenden Punkt). 

Für die Verschiebungen müssen folgende Vorzeichen respektiert werden:  

|leve polaire|
 
VORSICHT: Die Höhe eines Punktes mit DM1 oder DM2 ist nicht am Ort der
definitiven Koordinaten.  Diese Höhe muss in der Punkteliste beseitigt werden,
falls sie nicht bedeutsam ist.

Polar-Absteckung
^^^^^^^^^^^^^^^^ 

Die Berechnung Polarabsteckung (ab Koordinaten) ergibt folgende Werte: 

- Horizontalwinkel (Hz)
- Horizontaldistanz, 
- Schrägdistanz 
- Zenitwinkel
- Höhe des Prismas  
- Azimut (φ) 

Die folgende geometrische Bedingung sollte erfüllt sein: 

- Die Distanz ab der Station zum abzusteckenden Punkt darf die Länge des
  längsten Orientierungsvektors (Distanz zwischen Station und
  Orientierungspunkt) nicht um mehr als 1.25 mal übersteigen. 

 |Implantation polaire|
 
Absteckung einer Achse
^^^^^^^^^^^^^^^^^^^^^^

Diese Berechnung ergibt die Quer- und Längsverschiebung zu einer Achse AB
(berechnet ab dem Basispunkt "A").

Orthogonalberechnungen
~~~~~~~~~~~~~~~~~~~~~~

Orthogonalaufnahme
^^^^^^^^^^^^^^^^^^

Der gemessene Wert der Basis der Orthogonalaufnahme (A-B) muss eingegeben
werden, damit der Massstabsfaktor berechnet werden kann. 

Die Werte von Abszissen (x) und Ordinaten (y) müssen gemäss untenstehendem Bild
eingegeben werden:

 |leve orthogonal|

Die folgenden geometrischen Bedingungen sollten erfüllt sein:

- Die Abszisse ausser Basis sollte allgemein den Viertel der Basislänge nicht übersteigen. 
- Die Ordinatenlänge darf (für einen Grenzpunkt) 35 Meter nicht übersteigen. 
- Die Ordinate sollte allgemein die Länge der Basis nicht übersteigen.
 
Orthogonalzug
^^^^^^^^^^^^^

Die zu respektierenden Vorzeichen für den Orthogonalzug sind folgende: 

 |cheminement orthogonal|
 
Die folgenden geometrischen Bedingungen sollten erfüllt sein:

- Die Differenz zwischen AB berechnet und AB gemessen muss in der Toleranz
  sein. 
- Die totale Länge des Orthogonalzuges soll die dreifache Distanz AB nicht
  übersteigen. 
- Punkte können "ausser Basis" bestimmt werden (freier Zug ab den
  Anschluss-Punkten), aber die kumulierte Distanz ausser Basis soll ½ AB nicht
  übersteigen.

Orthogonale Absteckung
^^^^^^^^^^^^^^^^^^^^^^

Die Berechnung der orthogonalen Absteckung (ab Koordinaten) ergibt die Werte
der Abszissen und Ordinaten, welche die Absteckung im Gelände erlaubt. Die
Abszissen- (x) und Ordinatenwerte (y) müssen nach folgendem Schema gegeben
werden:

 |Implantation orthogonale|

Die geometrischen Bedingungen der Orthogonalaufnahme sollten erfüllt sein.

Schnittpunkte
~~~~~~~~~~~~~

Gerade (und/oder Richtungen)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Die Definition der Geraden kann eingegeben werden: 

- Entweder durch zwei Punkte; 
- Oder durch einen Punkt und ein Azimut (φ). 

Die Gerade kann entweder verschoben werden, oder rechtwinklig zur vorgegebenen
Geraden sein.

Für die Verschiebung (Wert X), ein Plus Zeichen verschiebt nach rechts, ein
Minus Zeichen verschiebt nach links.

Wenn die Gerade rechtwinklig zur vorgegebenen Geraden steht, kann eine Distanz
vom Punkt C (Wert Y) eingegeben werden.
 
Siehe Bild hier unten: 

 |Intersection de lignes|

Schnittpunkte von Kreisen (Distanzen)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Zwei Möglichkeiten zur Berechnung von Kreis-Schnittpunkten: 

- Manuelle Eingabe der Dimension der Radien;
- Wahl eines Kreispunktes, die Grösse des Radius wird automatisch berechnet. 

Achtung: es gibt fast jedes Mal 2 Lösungen. 

Eine einzige Lösung ergeben nur 2 sich tangierende Kreise. 

Keine Lösung ergibt sich, wenn sich die Kreise nicht berühren.  

Definition des ersten Kreises    : Zentrum: A und Radius: r1

Definition des zweiten Kreises : Zentrum: B und Radius: r2

 |Intersection de cercles|

Die berechneten Schnittpunkte sind I1 und I2.

Gerade/Kreis
^^^^^^^^^^^^

Die Definition der Geraden kann gegeben werden:
 
- Entweder durch zwei Punkte 
- Oder durch einen Punkt und ein Azimut (G) 

Die Definition des Kreises kann gegeben werden:
 
- Entweder durch manuelle Eingabe der Radien  
- Oder durch einen Kreispunkt, die Grösse des Radius wird automatisch berechnet.

Die Gerade kann verschoben werden (Plus Zeichen = rechts; Minus Zeichen = links). 

 |Intersection d'un cercle et d'une droite|

Die beiden berechneten Punkte sind I1 und I2.

Flächen
~~~~~~~~

Flächenberechnung (inkl. mit Kreisbogen)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Für die Flächenberechnung müssen folgende Elemente eingegeben werden: 

- Die Punkte im Uhrzeigersinn. 

Siehe Bild hier unten:  

|Surface|
 
Punkt 1

Punkt 2, Radius + 

Punkt 3, 

Punkt 4, 

Punkt 5, Radius - 

Punkt 6, 

Punkt 7 

Parallel-Verschiebung einer Grenze mit vorgeschriebener Fläche
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Das Ziel der Berechnung ist, den Wert der Verschiebung der Grenze AD zu
erhalten, um damit die gewünschte Fläche (S) zu erreichen.

XY muss parallel zu AD sein. 

Die erhaltenen Werte sind folgende: 

d = Länge der Verschiebung der Grenze AD 

S = vorgeschriebene Fläche

 |Deplacement parallele de limite avec surface imposable|
 

Verschiedenes
~~~~~~~~~~~~~

Azimut (Richtungswinkel) / Distanz
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Zur Berechnung von Azimut/Distanz genügt es, in der Punktliste zu wählen:

- Den Standpunkt,
- Den Orientierungspunkt.

Projektion eines Punktes auf eine Gerade
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Um die Gerade zu definieren, gibt es zwei Möglichkeiten: 

- Durch zwei bekannte Punkte;
- Durch einen Punkt und ein Azimut.

Die Gerade kann durch ein Plus-Zeichen nach rechts verschoben werden, durch ein
Minus-Zeichen nach links. 

Jetzt wählt man den zu projizierenden Punkt in der Liste der bekannten Punkte. 

Die verlangte Nummer ist jene des auf die Gerade projizierten Punktes. 

 |Projection d'un point sur une droite|
 
x = Distanz des zu projizierenden Punktes (P) auf die Gerade 

y = Distanz des projizierten Punktes zum Punkt A 

z = Distanz des projizierten Punktes zum Punkt B 

Berechnung eines Kreises durch 3 Punkte
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Ab drei Punkten der Koordinatenliste ist es möglich, die Koordinaten des
Zentrums und den Radius eines Kreises zu erhalten. 

Die Koordinaten des Zentrums können nach Eingabe einer Punknummer registriert
werden.  

Teilung eines Bogens
^^^^^^^^^^^^^^^^^^^^

Die Berechnung erlaubt, gleichmässig verteilte Koordinaten entlang eines Bogens
zu erhalten.

Die Teilung erhält man durch die gewünschte Anzahl der Segmente (Bild A) oder
anhand einer manuell eingegebenen Distanz (a) - (Bild B).

Bei der zweiten Lösung ist es klar, dass die Restdistanz zwischen dem letzten
gerechneten Punkt und dem Ende des Bogens nicht mit den anderen Bogenlängen
übereinstimmt (?). 
 
 |Segmentation d'un arc de cercle|

Mathematik
~~~~~~~~~~

Auflösung von Dreiecken
^^^^^^^^^^^^^^^^^^^^^^^

Die zur Auflösung von Dreiecken einzugebenden Elemente sind folgende:
 
- 3 Seiten
- 2 Seiten, 1 eingeschriebener Winkel 
- 1 Seite, 2 Winkel

 |Triangle|
 
r = Radius des Inkreises (eingeschriebenen Kreises)

R = Radius des Umkreises 

h = berechnete Höhe 

Berechnung von Kreiselementen 
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Die zur Berechnung einzugebenden Elemente sind folgende:

- Radius, Sehne
- Radius, Zentriwinkel
- Radius, Tangente
- Radius, Pfeilhöhe
- Sehne, Zentriwinkel
- Sehne, Tangente
- Sehne, Pfeilhöhe
- Tangente, Zentriwinkel

 |Elements circulaires|
 
S = Tangentenschnittpunkt

C = Zentrum

CO = CM = CF = Radius 

OF = Sehne (OF)

OS = FS = Tangente

MS = Winkelhalbierende

β = Tangentenwinkel (Innenwinkel)

2α = Zentriwinkel

MB = Bogenmitte 

 |Elements circulaires 2|

Das obere Bild stellt die Fläche des Sektors (Kreisausschnitts) dar. 

Das untere Bild stellt die Fläche des Segmentes (Kreisabschnitts) dar. 

.. |leve polaire| image:: file:///android_asset/help/img/polar_survey.png
.. |Implantation polaire| image:: file:///android_asset/help/img/polar_implantation.png
.. |leve orthogonal| image:: file:///android_asset/help/img/orthogonal_implantation.png
.. |cheminement orthogonal| image:: file:///android_asset/help/img/chem_ortho.png
.. |Implantation orthogonale| image:: file:///android_asset/help/img/orthogonal_implantation.png
.. |Intersection de lignes| image:: file:///android_asset/help/img/lines_intersection.png
.. |Intersection de cercles| image:: file:///android_asset/help/img/circles_intersection.png
.. |Intersection d'un cercle et d'une droite| image:: file:///android_asset/help/img/line_circle_intersection.png
.. |Surface| image:: file:///android_asset/help/img/surface.png
.. |Deplacement parallele de limite avec surface imposable| image:: file:///android_asset/help/img/parallel_displacement.png
.. |Projection d'un point sur une droite| image:: file:///android_asset/help/img/point_project_line.png
.. |Segmentation d'un arc de cercle| image:: file:///android_asset/help/img/circular_segmentation.png
.. |Triangle| image:: file:///android_asset/help/img/triangle.png
.. |Elements circulaires| image:: file:///android_asset/help/img/circular_curve.png
.. |Elements circulaires 2| image:: file:///android_asset/help/img/circular_curve_2.png
