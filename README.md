
# Deca Compiler (Decac) - README

## Description

Welcome to **Decac**, the compiler for the **Deca** programming language. This project aims to provide a compiler capable of translating and executing programs written in Deca. This README will guide you through the main features of the compiler, its compilation options, and how to use it for yourself.

This project was developed as part of a team of five at **Ensimag**, showcasing our collaborative efforts in creating a functional and efficient compiler.
## Table of Contents

1. [Requirements](#requirements)
2. [Documentation](#documentation)
3. [Installation](#installation)
4. [Usage](#usage)
    - [Compilation Options](#compilation-options)
    - [Exemple usage](#example-usage)
5. [Test](#Test)


## Requirements

Before using the Decac compiler, ensure you have the following installed:

- **Java JDK** (version 11 or higher)
- **Git** (to clone the repository)
- **MVN** (to manage the build process)

## Documentation

For comprehensive information on design, TRIGO extensions, reports, user manuals, and more, please refer to the [documentation directory](./docs).

## Installation

1. Clone the project repository:

    ```bash
    git clone https://github.com/username/decac-compiler.git
    ```

2. Navigate to the project directory:

    ```bash
    cd Deca-Compiler-A-Custom-Language-Compiler
    ```

3. Build the project with `mvn`:

    ```bash
    mvn test-compile
    ```

   This will generate the `decac` executable in the `./src/main/bin` directory.

## Usage

You can run the Decac compiler using the following command:

```bash
./src/main/bin/decac <options> <file.deca>
```

### Compilation Options

| Option              | Description                                                                                      |
|---------------------|--------------------------------------------------------------------------------------------------|
| `-b`                | Displays a banner with the team's name.                                                           |
| `-p`                | Parses the Deca file and generates an abstract syntax tree.                                       |
| `-v`                | Performs lexical, syntactic, and contextual verification on the Deca file.                        |
| `-n`                | Skips runtime checks such as division by zero or overflow.                                        |
| `-r X`              | Limits available registers to `R0, ..., R[X-1]` where X is between 4 and 16.                      |
| `-d`                | Enables debug traces. Can be repeated for more detailed output.                                   |
| `-P`                | Enables parallel compilation for multiple Deca files.                                             |
| `-w`                | Displays warnings during compilation.                                                            |
| `--color`           | Adds color to the output for better readability.                                                  |

### Example Usage

To verify the syntax of a file **myProgram.deca** without full execution:

```bash
./src/main/bin/decac -v myProgram.deca
```

To display the abstract syntax tree with colors:

```bash
./src/main/bin/decac -p --color myProgram.deca
```


## Test

You can run the tests using one of the following methods:

1. **Using Maven**  
   Execute the following command in your terminal:
   ```bash
   mvn test
   ```
2. Directly with the Bash Script
   Alternatively, you can run the tests using the provided script:
   ```bash
   ./src/test/script/run_all_test.sh
   ```
---

Thank you for using the Decac compiler, and happy coding!
