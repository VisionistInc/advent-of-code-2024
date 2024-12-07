# AOC in Gleam

So, you chose the human one, eh? Well, theere won't be fancy emojis in this file, but I'll at least try to talk a tiny bit about gleam and how to actually run the code here.

## Gleam?
[Gleam](https://gleam.run) is a new language that runs on `Erlang`'s beam engine. It's kinda like elixir, but better because it's statically-typed and uses c-style syntax (sorta).

Gleam says that:
> Gleam is friendly language for building type-safe systems that scale! [[1]](https://gleam.run)

But it also says:
> Gleam lacks exceptions, macros, type classes, early returns, and a variety of other features, instead going all-in with just first-class-functions and pattern matching [[2]](https://tour.gleam.run/everything/#advanced-features-use)

Maybe I'm just bad at FP, but I personally haven't found FP to be the one programming paradigm to rule them all, but it's worth a shot!

If you know gleam (or FP), you may want to leave now, unless you're preparead to roast some random engineer's ability to learn a language using arbitrary code challenges in the middle of the night as fast as possible.

## Running
Have gleam (and either erlang or a js runtime) installed. The best way is to use the devcontainer in this repo, or [asdf](https://asdf-vm.com) if you hate docker. 

If running out of docker, make sure you have `gleam`, `erlang`, and `nodejs` asdf plugins, and run `asdf install`.

Once that's done, to run a day, place a file called `input.txt` in one of the `dayXX` directories (this is because we [shouldn't have them in the repo](https://adventofcode.com/about#faq_copying)), and run `gleam run` from inside the dir of the day you want to run.

To run with js, do `gleam run -t javascript`