DOCUMENTO DELLE SPECIFICHE DI "iFermi"

Scopo

L'applicazione è mirata agli utenti android della nostra scuola e l'obiettivo sarebbe di rendere più semplice per loro l'uso del nostro sito della scuola.
Nell'applicazione saranno fornite le funzionalità più usate e più utili del sito, in modo da che l'utente non senta più il bisogno di andare direttamente sul sito web.

Parti del progetto

-agenda: è l'equivalente del calendario giornaliero della scuola
-calendario: contiene un calendario interattivo con tutti gli eventi scritti sul calendario scolastico
-news: pagina principale dell'app, contiene le news
-orario: equivalente alla sezione orario definitivo
-settings: impostazioni dell'applicazione
-tutorial: è un piccolo tutorial che parte al primo avvio in modo da spiegare all'utente come funziona questa applicazione

Comunicazione tra parti

Tramite un file model.java, le varie parti dell'app comunicano tra di loro(ad esempio le varie view potranno leggere le impostazioni dal model)

Divisione delle parti

-Castagna: news, orario + model
-Sahni: agenda, calendario + collegamento a registro e moodle
-Cattani: settings, tutorial
