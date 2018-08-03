# Str8Chr8 or "Straight Crate"

"Strings" and "characters" for Java internally represented as UTF-8.

The world needs something like this because MySQL 8 supports "high unicode chars" and they are an absolute mess in Java.
Parsing them into 16-bit fields was forward-thinking in 1990, but seems crazy today.

## Implementation
UTF-8 data, files, and streams are just byte arrays.
Each Chr8 references the underlying byte array and the starting offset of that character so that unmodified Str8's can be read and written with a minimum of memory allocation.
This raises the question of how to read ByteArrayInputStreams in a way that is efficient yet respects character boundaries.
If we read whole files, we don't have to worry about that.

We may cache:
* The first 128 (ASCII) Chr8's
* Chr8's we need in memory anyway for parsing/comparisons
* Other popular Chr8's (smart quotes, whitespace characters, em-dash, bullet, currency symbols, smileys, combining diacritical marks)?  

## Status
This project is brand new.
As of 2018-08-03 I have compiled it, but haven't tested anything yet.

## To Do:
(Not necessarily in order)
* Str8B - like StringBuilder, but probably based on a Rope or maybe RRB-Tree
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