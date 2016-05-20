#Crossed

[See it in action here!](http://crossed.lol)

## What is this?

Crossed is a multiplayer crossword game written entirely in Clojure. It is a project intended to get me familiar with the ecosystem, and an attempt to construct a useful project skeleton for future work.

## Technologies

There's a huge mix of work in here. The Clojure community is filled with helpful code if one knows where to find it. As such this codebase is the result of a massive research effort that encompasses source code, gists, blog posts,
documentation, and good old fashioned trial and error.

The crossword generation code is shamelessly pulled from Ben Denham's work here: [clj-crossword](https://github.com/ben-denham/clj-crosswords).
All of the crossword clues are generated using data from [https://github.com/donohoe/nyt-crossword/](https://github.com/donohoe/nyt-crossword/).

Most of my job consisted in gluing everything together, and I can't express how grateful I am for the following libraries that made this game possible:

* [boot](https://github.com/boot-clj/boot) -- Run tasks written in Clojure -- as opposed to declarative Leiningen configurations
* [reagent](https://github.com/reagent-project/reagent) -- My favorite CLJS React bindings
* [re-frame](https://github.com/Day8/re-frame) -- A data flow library for Reagent
* [matchbox](https://github.com/crisptrutski/matchbox) -- Amazing Firebase wrappers
* [cljs-devtools](https://github.com/binaryage/cljs-devtools) -- A must have. This is awesome!


## Why is this remarkable?

Development and production environments are served using the same backend. Deployment is as simple as building a jar and pushing it. Check out [`build.boot`](https://github.com/velveteer/crossed/blob/master/build.boot) for how this is accomplished. I am very passionate about tooling, and this setup has been a refreshing experience. Normally I would have wrangled with a more complicated build pipeline in JS.

Does this serve as a suitable starting point for single-page applications? I believe so, but it might be too heavy for anyone looking to simply generate static files. When coupled with the Clojure backend it provides a powerful all-in-one environment for doing "full stack" web development. Having a single jar file for deployment is comparable to the increasingly popular container technologies for shipping applications. Why run a Docker daemon when you have the JVM?

## Would I do it again?

You bet. Clojure/CLJS is a delight to work in -- it's not strongly typed like Elm or Purescript, but it still provides a functional paradigm for writing UI and server logic. LISP is an acquired taste, but well documented Clojure, while deceptively terse, is more manageable and pleasing to work with. Things just work and that's a good feeling. The ecosystem is still evolving but there's enough out there to build anything.

## Challenges

This would have been easier to do in JS, my comfort zone, but that's not the point. Honestly the hardest part about this, as many CLJS newcomers might understand, was moving between Javascript and Clojure data structures. It's not a big deal when you're isolated in UI state, but once you start talking to the world at large you have to deal with JSON -- and it can get messy. Keywordize all day long. I have seen some attempts at solving this headache, most notably [transit](https://github.com/cognitect/transit-clj).
