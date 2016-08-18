#Crossed

[See it in action here!](http://crossed.lol)

Crossed is a multiplayer crossword game written entirely in Clojure and Clojurescript.

## Technologies

This wouldn't have been possible without:

* [boot](https://github.com/boot-clj/boot) -- Run tasks written in Clojure
* [reagent](https://github.com/reagent-project/reagent) -- My favorite CLJS React bindings
* [re-frame](https://github.com/Day8/re-frame) -- A data flow library for Reagent
* [firebase-cljs](https://github.com/degree9/firebase-cljs/) -- CLJS Firebase wrappers
* [cljs-devtools](https://github.com/binaryage/cljs-devtools) -- A must have. This is awesome!


The crossword generation code is shamelessly pulled from Ben Denham's work here: [clj-crossword](https://github.com/ben-denham/clj-crosswords). It has been slightly tweaked but all the credit is due to him.

All of the crossword clues are generated using data from [https://github.com/donohoe/nyt-crossword/](https://github.com/donohoe/nyt-crossword/). They aren't always reliable, but there's enough of them to consistently generate unique puzzles.

## Roadmap

* Game chat
* Time trial
* Versus
