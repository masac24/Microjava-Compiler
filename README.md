# MikroJava Compiler

A compiler for **MikroJava**, a simplified educational subset of Java, built as part of the *Compiler Construction* course at the University of Belgrade, School of Electrical Engineering (ETF).

The compiler translates MikroJava source code into bytecode that runs on a custom virtual machine, and is built on top of the [Cup2](http://www2.cs.tum.edu/projects/cup/) / JFlex parser-generator toolchain commonly used in academic compiler courses.

## Features

- **Lexical analysis** — tokenizes MikroJava source code and reports lexical errors.
- **Syntax analysis** — builds an abstract syntax tree (AST) from the token stream, with syntax-error recovery so parsing can continue after an error is found.
- **Semantic analysis** — traverses the AST to check semantic correctness (type checking, scope resolution, symbol table management) and reports semantic errors.
- **Code generation** — traverses the AST to emit executable MikroJava bytecode for the MikroJava Virtual Machine (MJVM).

## Project Structure

```
config/    # Compiler/parser configuration
lib/       # External libraries used by the build
spec/      # Language specification / grammar reference
src/       # Compiler source code (lexer, parser, semantic analyzer, code generator)
test/      # Sample MikroJava programs used for testing
build.xml  # Ant build script
```

## Tech Stack

Java, Cup2/JFlex (lexer & parser generation), Apache Ant (build)

