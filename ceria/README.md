<p align="center">
    <img src="./kotlin-1.svg" width="250" height="250">
</p>

# Advent of Code 2024 - in [Kotlin](https://kotlinlang.org/)

I'm going to attempt (likely with varying degrees of motivation) to implement the advent of code problems in Kotlin. I don't get to use Kotlin for work (Java, Golang, Javascript and TypeScript are my dailies), so there will certainly be solutions that more idiomatic to Kotlin than my solutions, but shrug. 

Since [VSCode](https://code.visualstudio.com/) relies on [Gradle](https://gradle.org/) or Maven at the moment to supply code completion for Kotlin, each day of the advent is a simple Gradle project. (I know JetBrains' IDE is built for Kotlin, but unless doing something complex in a large code base, I prefer VSCode's simplicity.) You do not need to have Gradle or Kotlin installed. Using either option of execution descbied below, you only need to have Java 21 installed.

The code in the "utils" package is because I've done AOC before, and have come to expect certain types of problems, so I've made generic implementations of common algorithms to handle those problems quicker. I don't claim them to be the most efficient, or best implementations of those algorithms.

## Running the code

I've included the input file supplied by advent of code for each problem. Both options assume you are executiing from the top level, i.e. `<day>/`.


Using gradle,
```
    ./gradlew run --args=input
```
You can substitute `input` with the path to your own input if you desire.


Alternatively, you can manually run the code,
```
   java -jar Solution.jar input
```
You may also download just this jar file and execute it from any directory `java -jar Solution.jar <path-to-input>`