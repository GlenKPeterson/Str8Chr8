# Str8Chr8 or "Straight Crate"

This project provides "Strings" and "characters" for Java internally represented as UTF-8.
This project is brand new.
As of 2018-08-03 I haven't even compiled it yet.
But the world needs something like this because MySQL 8 supports "high unicode chars" and they are an absolute mess in Java.
Parsing them into 16-bit fields was forward-thinking in 1990, but seems crazy today.

## To Do:
(Not necessarily in order)
* Str8B - like StringBuilder, but probably based on a Rope or RRB-Tree
* Interface directly with ByteArray Input/Output Streams.
* Read and write files
* Split/Combine combining diacritical marks (accents).
  Accents can sometimes be stored with the character or separately.
  Some fonts assume the former, some the latter.
  Probably newer fonts will keep accents separate because it's simpler for the artist.
  For equality testing characters, you probably want to pick one (combined, or separate).
  Characters can have multiple marks, and the order of the marks matter.
* Regular Expressions
* MySQL connector (and other database connectors)

## Out of Scope:
Java Chr8 and Str8 literals (they have to be built into the language).
Although someone could probably make a maven/gradle/compiler plug-in or annotation or something... 

## License: Apache 2.0