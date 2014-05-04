# Lastenheft
### zum Projekt "Parenticket"

# 1. Zielbestimmung

  Die Anwendung "Parenticket" soll es dem Nutzer ermoeglichen, die
  Aufgabenverteilung in kleineren (Software-)Projekten zu verwalten.
  Dabei soll der Nutzer Projekte, und in diesen Tickets (Aufgaben)
  anlegen und bearbeiten (Loeschen, Editieren) koennen. Fuer jedes
  Projekt sollen die Tickets aufgelistet werden koennen.

  Tickets sollen einen Titel sowie eine detailliertere Beschreibung,
  sowie optional einen oder mehrere Tags (Beispiele: "Front-End",
  "Wichtig") besitzen. Tickets mit einem bestimmten Tag sollen
  aufgelistet werden koennen.

# 2. Produkteinsatz
 
  Die Software soll als Web-Anwendung realisiert werden. Dadurch kann
  sie sowohl auf dem eigenen Rechner (lokal) nur von einer Person
  genutzt, als auch auf einem (womoeglich) oeffentlichen Server
  mehreren Nutzern zur Verfuegung gestellt werden.

  Dadurch ergeben eignet sich die Software sowohl fuer die Verwaltung
  von persoenlichen Aufgaben, als auch fuer die Koordination von
  Aufgaben in kleineren Teams.

# 3. Produktübersicht

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

