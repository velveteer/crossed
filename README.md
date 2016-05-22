#Crossed

[See it in action here!](http://crossed.lol)

Crossed is a multiplayer crossword game written entirely in Clojure. It is a project intended to get me familiar with the ecosystem, and an attempt to construct a useful project skeleton for future work.

## Technologies


This wouldn't have been possible without:

* [boot](https://github.com/boot-clj/boot) -- Run tasks written in Clojure -- as opposed to declarative Leiningen configurations
* [reagent](https://github.com/reagent-project/reagent) -- My favorite CLJS React bindings
* [re-frame](https://github.com/Day8/re-frame) -- A data flow library for Reagent
* [matchbox](https://github.com/crisptrutski/matchbox) -- Amazing Firebase wrappers
* [cljs-devtools](https://github.com/binaryage/cljs-devtools) -- A must have. This is awesome!


The crossword generation code is shamelessly pulled from Ben Denham's work here: [clj-crossword](https://github.com/ben-denham/clj-crosswords).


All of the crossword clues are generated using data from [https://github.com/donohoe/nyt-crossword/](https://github.com/donohoe/nyt-crossword/). They aren't always reliable, but there's enough of them to consistently generate unique puzzles.

## To Do

* Matchmaking
* Persistent sessions
* Game chat
* Versus mode
