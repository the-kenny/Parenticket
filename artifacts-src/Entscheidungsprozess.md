# Entscheidungsprozess

Die Applikation soll eine Webapplikation sein um keine speziellen
Anforderungen an das System des Users zu stellen. Ein handelsübliches
Gerät mit einem modernen Browser soll ausreichen. Dazu gehören
beispielsweise Laptops und Rechner der letzten 10 Jahre, aktuelle
Tablets und Smartphones oder auch der Raspberry PI, der mit
Ein-/Ausgabegeräten versehen ist.

Da der Wunsch der Entwickler bestand, sowohl in Ruby/Rails als auch in
Clojurescript zu entwickeln, wurde diesem Rechnung getragen in dem die
Webapplikation in 2 Teile geteilt wurde. Der eine Teil ist der Server,
der Daten persistiert und eine REST-Schnittstelle über HTTP/JSON
bereitstellt. Der andere Teil ist der Client, der in Clojurescript
geschrieben wird, aber nach Javascript kompiliert und als solches im
Browser ausgeführt wird. Die Darstellung erfolgt durch den Browser.

Sowohl im Client als auch im Server wird das MVC-Pattern
verwendet. Auf der Serverseite wird es vor allem deshalb verwendet, da
eine Entwicklung mit Rails automatisch zu einer Software aufgeteilt in
Models, Views und Controller führt. AUf der Clientseite wird es
deshalb verwendet, weil es auch angenehm ist eine Software nach diesem
Pattern zu entwickeln und die Views, die der Server bereitstellt als
Models im Client wiederverwendet werden können.
