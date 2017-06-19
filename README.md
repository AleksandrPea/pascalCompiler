# pascalCompiler
## Language description
Created programming language(that is very similar to Pascal) has 4 data types: **integer**(32 bit), **float**(64 bit), **boolean** and **string**. This language is strongly-typed. For example statement ``` float a := 3 ``` has error, because 3 is integer constant.

Also this language is case-sensitive.

Grammar of input language has next **lexemes**: "program", "var", "integer", float", "boolean", "string", "if", "then", "else","begin", "end", "while", "do", "for", "to", "repeat", "until", "true", "false", "or", "and", "div", "mod", "not" та ‘;’ ‘:’ ‘,’ ‘.’ ‘(’ ‘)’ ‘[‘ ‘]’ ‘=’ ‘< >’ ‘<’ ‘>’ ‘<=’ ‘>=’ ‘*’ ‘/’ ‘+’ ‘-‘ ‘:=’ ‘ ’.

There are 3 types of **cycles** realized: for-do, repeat-untile, while-do. Here is example:
```
for i := 1 to 4 do begin … end;
```
Starting and ending indices can't be variables.

There are arithmetic operators for integer and float types: *sum(+), subtract(-), dividing(div or /), multiplying(*), getting remainder(mod - only for integer), unary operators + and -; all possible comparison operations*. For boolean type next logical operators are realized: *OR, AND, NOT*. Additionally there are statements: *float:sqrt(float), printFloat(float), printInteger(integer), printBoolean(boolean), print(string), println(string).*

## Lexical analyzer
Lexical analyzer is built by setting [left regular grammar](https://en.wikipedia.org/wiki/Regular_grammar) that is programmatically converted to appropriate [finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine). Thus *lexical analyzer generator* is created(class **LexicalAnalyzer**) which at the entrance accepts: left regular grammar represented by object of class **RegularGrammar**, arrays of delimiters and keywords. The main constraint imposed on grammar is exclusioning rules with same right side.

Setting grammar for Pascal lexemes(not all) you can find in class **PascalAnalyzer**.

## Syntactic analyzer(Parser)
Syntactic analyzer is built by setting [LL(1) grammar](https://en.wikipedia.org/wiki/LL_grammar)(class **Grammar**). LL(1) grammar can have rules of the next form: A -> α | ε, where α – any sequence of terminals and nonterminals, ε - empty symbol. Thus *descending syntactic analyzer generator* is created(class **Parser**). For input grammar these are not allowed:

- rules with left recursion;
- rules having at least one same first-symbol. For example A -> a | aB.

For each LL(1) grammar you can build [pushdown automaton](https://en.wikipedia.org/wiki/Pushdown_automaton) that has memory(stack) and recognises sentential forms of described language.

By storing rules, that are used in parsing, parse tree is built(using class **Node**).

Setting grammar for Pascal syntactic constructions(not all) you can find in class **PascalParser**.

## Semantic analyzer
For this purpose simple classes are created. They represent some syntactic constructions(for example BinaryOp). Also converting parse tree to abstract syntactic tree(AST) is programmed.

Concrete implementation of interface **AstVisitor** - **TypeChecker** is the semantic analyzer. Input arguments are: root of AST and table of operator types in form of object of class **OperatorTable**. In the method visit( ): types of argumets and variables(was it declared) are checked; loop and if operators, which has boolean constant as condition, are found.

As a result of semantic analyzer work is *table of variables*, which includes each declared variable and its type.

## Intermediate code generator
Generator **IrGenerator** is concrete implementation of **AstVisitor** and its main target is converting parse tree to more specific *list of intermediate commands*. Beside this, it supplements early created *table of variables* with new temporary variables.

## Target code(asm) generator
Target code generator is represented by class **IA32CodeGenerator**, which uses concrete implementation of interface **IrVisitor** - **IA32ProgramVisitor**. Its main target is constructing program in form of list of assembler strings. Input arguments are *table of variables* and *list of intermediate commands*.

Also **IA32Operators** class is used in this generator. It describes transformations of all standard operators from class **StandardTypes**. Additionally this class allows for each named operator type(object of class **NamedType**) mapping specified object of class **IA32Operator**, which has method for code generation, or inform that there is no mapping.

Target program could be compiled to .exe file on the computers with IA32 architecture.
