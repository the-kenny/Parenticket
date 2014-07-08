# Lastenheft
### zum Projekt "Parenticket"

**Autor**: Sandra Classen, Moritz Ulrich

**Betreuer**: Andreas Becker, Farbian Noth

**Modul**: BA-INF 051

**Version**: 0.2 (Stand 04.05.2014)

# 1. Zielbestimmung

  Das Ticketsystem soll dazu benutzt werden können, um innerhalb eines
  Projektes einen Überblick über anstehenden Aufgaben zu erhalten. Es
  können Tickets, die eine offene Aufgabe repräsentieren, angelegt
  werden.. Ein Ticket besteht aus Titel und optionaler Beschreibung,
  Deadline, Priorität und Liste von Tags.

  Diese sind optisch in verschiedene Kategorien („Offene Aufgaben“,
  „In Arbeit“ und „Bearbeitet“, etc.). Die Tickets können vom User
  diesen Kategorien verschoben werden, um einen Arbeitsablauf
  detailliert zu planen.

  Innerhalb der Kategorien sind die einzelnen Tickets nach einer ihnen
  zugewiesenen Priorität sortiert, damit zusätzlich zu den Kategorien
  ein weiterer Überblick über die einzelnen Aufgaben gegeben ist.
  Zusätzlich kann jedem Ticket ein oder mehrere Tags zugeordnet werden, um
  diese weiter zu kategorisieren. Es koennen alle Tickets zu einem Tag
  aufgelistet werden.

# 2. Produkteinsatz

  Die Software soll als Web-Anwendung realisiert werden. Dadurch kann
  sie sowohl auf dem eigenen Rechner (lokal) nur von einer Person
  genutzt, als auch auf einem (womoeglich) oeffentlichen Server
  mehreren Nutzern zur Verfuegung gestellt werden.

  Dadurch ergeben eignet sich die Software sowohl fuer die Verwaltung
  von persoenlichen Aufgaben, als auch fuer die Koordination von
  Aufgaben in kleineren Teams.

# 3. Produktübersicht

![Produktübersicht](./target-group.png)

# 4. Produktfunktionen

## /LF 10/

Geschaeftsprozess: Ticket anlegen

Akteur: Benutzer

Beschreibung: Der Nutzer moechte ein Ticket in einem Projekt anlegen.

## /LF 15/

Geschaeftsprozess: Ticket anzeigen

Akteur: Benutzer

Beschreibung: Der Nutzer moechte ein einzelnes Ticket anzeigen

## /LF 20/

Geschaeftsprozess: Ticket bearbeiten

Akteur: Benutzer

Beschreibung: Der Nutzer moechte die Beschreibung oder den Titel eines
Tickets veraendern

## /LF 30/

Geschaeftsprozess: Ticket loeschen

Akteur: Benutzer

Beschreibung: Der Nutzer moechte ein Ticket loeschen

## /LF 35/

Geschaeftsprozess: Ticket verschieben

Akteur: Benutzer

Beschreibung: Der Nutzer moechte die Kategorie des Tickets aendern

## /LF 40/

Geschaeftsprozess: Ticket taggen

Akteur: Benutzer

Beschreibung: Der User moechte die Liste von Tags eines Tickets bearbeiten

## /LF 50/

Geschaeftsprozess: Ticketliste anzeigen

Akteur: Benutzer

Beschreibung: Der Nutzer moechte alle Tickets eines Projektes auflisten

## /LF 60/

Geschaeftsprozess: Projekt anlegen

Akteur: Benutzer

Beschreibung: Der Nutzer moechte ein neues Projekt anlegen

## /LF 70/

Geschaeftsprozess: Tickets mit Tag auflisten

Akteur: Benutzer

Beschreibung: Der Nutzer moechte alle Tickets mit einem bestimmten Tag auflisten

# 5. Produktdaten

## /LD 10/
  Projekte mit Titel und optionaler Beschreibung, mindestens 10

## /LD 20/
  Tickets mit Titel und optionaler Bescreibung und optionaler Liste
  von Tags, mindestens 100 pro Projekt

# 6. Produktleistungen

## /LL 10/

  Das User-Interface soll in unter 3 Sekunden geladen sein

## /LL 20/

  Das User-Interface soll unter schnell (<0.5s) auf Eingaben reagieren

## /LL 30/

  Wartet das User-Interface auf den Antwort Server soll ein Indikator
  angezeigt werden

## /LL 40/

  Das User-Interface soll gaengige Operationen eines Web-Browsers
  (Vor/Zurueck) und "Deep"-URLs um direkt auf eine
  Ticket- oder andere Ansicht zu springen unterstuetzen

# 7. Qualitaetsanforderungen

    | Eigenschaft         |   | Sehr Hoch | Hoch | Normal | Irrelevant |
    |---------------------+---+-----------+------+--------+------------|
    | Zuverlaessigkeit    |   |           | x    |        |            |
    | Intuitivite Nutzung |   | x         |      |        |            |
    | Sicherheit          |   |           |      |        | x          |
    | Benutzbarkeit       |   |           | x    |        |            |
    | Integrierbarkeit    |   |           |      | x      |            |

  Der Schwerpunkt der Anwendung soll auf einem einfach verstaendlichen
  User-Interface und damit intuitiver Nutzung liegen. Ausserdem ist
  eine hohe Zuverlaessigkeit ein Ziel.

  Sicherheit und Integrierbarkeit werde dabei nicht explizit
  angegangen. Ersteres laesst sich ueber erprobte Methoden (HTTP-Basic
  Authentication oder aehnliches) einfacher realisieren, wodurch auch
  nicht die Gefahr von Sicherheitslucken entsteht. Letzteres ergibt
  sich durch das Client/Server Modell und definierter API von alleine.

# 8. Versionsgeschichte

* Version 0.1 (03.05.2014)
  Initiale Version (Nicht im Repository)
  
* Version 0.2 (04.05.2014)
  Ergaenzungen, /LF 35/, /LF 15/
