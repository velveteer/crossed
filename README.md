#Crossed

Crossed is a multiplayer crossword game made with [Clojure](http://clojure.org/) and [Firebase](https://firebase.google.com/).

Works best on recent versions of Chrome for desktop and mobile.

Currently in the process of migrating to another host -- so no live demo at the moment.

## Technologies

This wouldn't have been possible without:

* [boot](https://github.com/boot-clj/boot) -- Run tasks written in Clojure
* [reagent](https://github.com/reagent-project/reagent) -- My favorite CLJS React bindings
* [re-frame](https://github.com/Day8/re-frame) -- A data flow library for Reagent
* [tachyons](http://tachyons.io/) -- Functional CSS for humans
* [firebase-cljs](https://github.com/degree9/firebase-cljs/) -- CLJS Firebase wrappers
* [cljs-devtools](https://github.com/binaryage/cljs-devtools) -- A must have. This is awesome!

The crossword generation code is shamelessly pulled from Ben Denham's work here: [clj-crosswords](https://github.com/ben-denham/clj-crosswords).
It has been slightly tweaked but all the credit is due to him.

All of the crossword clues are generated using modified data from [https://github.com/donohoe/nyt-crossword/](https://github.com/donohoe/nyt-crossword/).

## Roadmap

* Game chat
* Time trial
* Versus
