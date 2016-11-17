package com.apea.compiler.syntactic;

import com.apea.compiler.ast.*;
import com.apea.compiler.lexical.Lexeme;
import com.apea.compiler.tools.grammars.Grammar;
import com.apea.compiler.tools.grammars.IsNotInAlphabetException;
import com.apea.compiler.tools.grammars.Symbol;
import com.apea.compiler.types.*;

import java.util.ArrayList;

public class PascalParser extends Parser {

    static {
        String[] terms = {"ID", "NUMERAL","REAL", "LINE","FUNCTION",
                "SEMICOLON", "COLON","COMMA","DOT", "LPAREN", "RPAREN",
                "LBRAC", "RBRAC",
                "EQUAL", "NOTEQUAL", "LT", "GT", "LE","GE",
                "STAR", "SLASH", "PLUS", "MINUS",
                "ASSIGMENT", "QUOTE",
                "PROGRAM","VAR", "INTEGER", "FLOAT", "BOOLEAN","STRING", "IF", "THEN", "ELSE",
                "BEGIN", "END", "WHILE", "DO","FOR", "TO", "REPEAT", "UNTIL",
                "TRUE", "FALSE", "OR", "AND", "DIV", "MOD", "NOT",
        };
        String[] nterms = {"program", "varDefPart", "varDef", "namedType", "actualParams","functionCall",
                "ifStatement", "statement", "compoundStatement", "assignmentStatement",
                "whileStatement","forStatement", "repeatStatement", "expression", "simpleExpression", "var",
                "signOperator", "addingOperator","relationalOperator","multiplyingOperator",
                "term", "factor", "numeral", "real","bool", "line",
                "nt1", "nt2", "nt3","nt4", "nt5","nt6", "simpleExpression*", "term*"};
        Grammar cfGR = new Grammar(terms, nterms);
        try {
            cfGR.createRule("program",
                    new Symbol("PROGRAM", true),
                    new Symbol("ID", true),
                    new Symbol("SEMICOLON", true),
                    new Symbol("varDefPart", false),
                    new Symbol("compoundStatement", false),
                    new Symbol("DOT", true));

            cfGR.createRule("varDefPart",
                    new Symbol("VAR", true),
                    new Symbol("varDef", false),
                    new Symbol("nt5", false));

            cfGR.createRule("nt5",
                    new Symbol("varDef", false),
                    new Symbol("nt5", false));
            cfGR.createEmptyRule("nt5");

            cfGR.createRule("varDef",
                    new Symbol("ID", true),
                    new Symbol("COLON", true),
                    new Symbol("namedType", false),
                    new Symbol("SEMICOLON", true));

            cfGR.createRule("namedType",
                    new Symbol("INTEGER", true));
            cfGR.createRule("namedType",
                    new Symbol("FLOAT", true));
            cfGR.createRule("namedType",
                    new Symbol("BOOLEAN", true));
            cfGR.createRule("namedType",
                    new Symbol("STRING", true));

            cfGR.createRule("statement",
                    new Symbol("functionCall", false));
            cfGR.createRule("statement",
                    new Symbol("assignmentStatement", false));
            cfGR.createRule("statement",
                    new Symbol("ifStatement", false));
            cfGR.createRule("statement",
                    new Symbol("compoundStatement", false));
            cfGR.createRule("statement",
                    new Symbol("whileStatement", false));
            cfGR.createRule("statement",
                    new Symbol("forStatement", false));
            cfGR.createRule("statement",
                    new Symbol("repeatStatement", false));
            cfGR.createRule("statement",
                    new Symbol("SEMICOLON", true));
            cfGR.createEmptyRule("statement");

            cfGR.createRule("functionCall",
                    new Symbol("FUNCTION", true),
                    new Symbol("actualParams", false),
                    new Symbol("RPAREN", true));

            cfGR.createRule("actualParams",
                    new Symbol("expression", false),
                    new Symbol("nt6", false));
            cfGR.createRule("nt6",
                    new Symbol("COMMA", true),
                    new Symbol("expression", false),
                    new Symbol("nt6", false));
            cfGR.createEmptyRule("nt6");
            cfGR.createEmptyRule("actualParams");

            cfGR.createRule("assignmentStatement",
                    new Symbol("var", false),
                    new Symbol("ASSIGMENT", true),
                    new Symbol("expression", false));

            cfGR.createRule("var",
                    new Symbol("ID", true),
                    new Symbol("nt4",false));
            cfGR.createRule("nt4",
                    new Symbol("LBRAC", true),
                    new Symbol("expression", false),
                    new Symbol("RBRAC", true));
            cfGR.createEmptyRule("nt4");

            cfGR.createRule("compoundStatement",
                    new Symbol("BEGIN", true),
                    new Symbol("statement", false),
                    new Symbol("nt3", false),
                    new Symbol("END", true));

            cfGR.createRule("nt3",            //допоміжний нетермінал
                    new Symbol("SEMICOLON", true),
                    new Symbol("statement", false),
                    new Symbol("nt3", false));
            cfGR.createEmptyRule("nt3");

            cfGR.createRule("ifStatement",
                    new Symbol("IF", true),
                    new Symbol("expression", false),
                    new Symbol("THEN", true),
                    new Symbol("statement", false),
                    new Symbol("nt1", false));

            cfGR.createRule("nt1",
                    new Symbol("ELSE", true),
                    new Symbol("statement", false));
            cfGR.createEmptyRule("nt1");

            cfGR.createRule("whileStatement",
                    new Symbol("WHILE", true),
                    new Symbol("expression", false),
                    new Symbol("DO", true),
                    new Symbol("statement", false));

            cfGR.createRule("forStatement",
                    new Symbol("FOR", true),
                    new Symbol("var", false),
                    new Symbol("ASSIGMENT", true),
                    new Symbol("NUMERAL", true),
                    new Symbol("TO", true),
                    new Symbol("NUMERAL", true),
                    new Symbol("DO", true),
                    new Symbol("statement", false));

            cfGR.createRule("repeatStatement",
                    new Symbol("REPEAT", true),
                    new Symbol("statement", false),
                    new Symbol("UNTIL", true),
                    new Symbol("expression", false));

            cfGR.createRule("expression",
                    new Symbol("simpleExpression", false),
                    new Symbol("nt2", false));

            cfGR.createRule("nt2",
                    new Symbol("relationalOperator", false),
                    new Symbol("simpleExpression", false));
            cfGR.createEmptyRule("nt2");

            cfGR.createRule("simpleExpression",
                    new Symbol("signOperator", false),
                    new Symbol("term", false),
                    new Symbol("simpleExpression*", false));
            cfGR.createRule("simpleExpression",
                    new Symbol("term", false),
                    new Symbol("simpleExpression*", false));    //видаляємо ліву рекурсію
            cfGR.createRule("simpleExpression*",                // використовуючи simpleExpression*
                    new Symbol("addingOperator", false),
                    new Symbol("term", false),
                    new Symbol("simpleExpression*", false));
            cfGR.createEmptyRule("simpleExpression*");

            cfGR.createRule("signOperator",
                    new Symbol("PLUS", true));
            cfGR.createRule("signOperator",
                    new Symbol("MINUS", true));

            cfGR.createRule("addingOperator",
                    new Symbol("PLUS", true));
            cfGR.createRule("addingOperator",
                    new Symbol("MINUS", true));
            cfGR.createRule("addingOperator",
                    new Symbol("OR", true));

            cfGR.createRule("relationalOperator",
                    new Symbol("EQUAL", true));
            cfGR.createRule("relationalOperator",
                    new Symbol("NOTEQUAL", true));
            cfGR.createRule("relationalOperator",
                    new Symbol("LT", true));
            cfGR.createRule("relationalOperator",
                    new Symbol("GT", true));
            cfGR.createRule("relationalOperator",
                    new Symbol("LE", true));
            cfGR.createRule("relationalOperator",
                    new Symbol("GE", true));

            cfGR.createRule("multiplyingOperator",
                    new Symbol("STAR", true));
            cfGR.createRule("multiplyingOperator",
                    new Symbol("DIV", true));
            cfGR.createRule("multiplyingOperator",
                    new Symbol("SLASH", true));
            cfGR.createRule("multiplyingOperator",
                    new Symbol("MOD", true));
            cfGR.createRule("multiplyingOperator",
                    new Symbol("AND", true));

            cfGR.createRule("term",
                    new Symbol("factor", false),
                    new Symbol("term*", false));
            cfGR.createRule("term*",
                    new Symbol("multiplyingOperator", false),
                    new Symbol("factor", false),
                    new Symbol("term*", false));
            cfGR.createEmptyRule("term*");

            cfGR.createRule("factor",
                    new Symbol("numeral", false));
            cfGR.createRule("factor",
                    new Symbol("real", false));
            cfGR.createRule("factor",
                    new Symbol("bool", false));
            cfGR.createRule("factor",
                    new Symbol("line", false));
            cfGR.createRule("factor",
                    new Symbol("var", false));
            cfGR.createRule("factor",
                    new Symbol("LPAREN", true),
                    new Symbol("expression", false),
                    new Symbol("RPAREN", true));
            cfGR.createRule("factor",
                    new Symbol("NOT", true),
                    new Symbol("factor", false));
            cfGR.createRule("factor",
                    new Symbol("functionCall", false));

            cfGR.createRule("numeral",
                    new Symbol("NUMERAL", true));

            cfGR.createRule("real",
                    new Symbol("REAL", true));

            cfGR.createRule("bool",
                    new Symbol("TRUE", true));
            cfGR.createRule("bool",
                    new Symbol("FALSE", true));

            cfGR.createRule("line",
                    new Symbol("LINE", true));

        } catch (IsNotInAlphabetException e) {
            e.printStackTrace();
        }
        parser = new PascalParser(cfGR);
    }

    private static final PascalParser parser;

    private PascalParser(Grammar grammar) {
        super(grammar);
    }

    public static PascalParser instance() {
        return parser;
    }

    public Program parseInAST(ArrayList<Lexeme> lexemes) {
        Node tree = parser.parse(lexemes);
        return new Program(tree.getChildNodes().get(1).getName(),
                findDefBlockAt(tree.searchChildNode("varDefPart")),
                findStatementBlockAt(tree.searchChildNode("compoundStatement")));
    }

    private Statement findStatementAt(Node node) {
        Statement result = null;
        node = node.getChildNodes().get(0);
        if (node.getName() != null) {
            switch (node.getName()) {
                case "functionCall":
                    result = findFunctionCallAt(node);
                    break;
                case "ifStatement":
                    result = findIfAt(node);
                    break;
                case "assignmentStatement":
                    result = findAssignmentAt(node);
                    break;
                case "compoundStatement":
                    result = findStatementBlockAt(node);
                    break;
                case "whileStatement":
                    result = findWhileStAt(node);
                    break;
                case "forStatement":
                    result = findForStAt(node);
                    break;
                case "repeatStatement":
                    result = findRepeatStAt(node);
                    break;
                case ";":
                    result = new EmptyStatement();
                    break;
            }
        } else {
            result = new EmptyStatement();
        }
        return result;
    }

    private DefinitionBlock findDefBlockAt(Node node) {
        ArrayList<Definition> definitions = new ArrayList<>();
        Node childNode;
        do {
            childNode = node.searchChildNode("varDef");
            String name = childNode.getChildNodes().get(0).getName();
            Type type = null;
            switch (childNode.searchChildNode("namedType").getChildNodes().get(0).getName()) {
                case "integer":
                    type = IntegerType.instance;
                    break;
                case "float":
                    type = FloatType.instance;
                    break;
                case "boolean":
                    type = BooleanType.instance;
                    break;
                case "string":
                    type = StringType.instance;
                    break;
            }
            definitions.add(new Definition(name, type));
            node = node.searchChildNode("nt5");
        } while(node.hasChildNodes());
        return new DefinitionBlock(definitions);
    }

    private StatementBlock findStatementBlockAt(Node node) {
        ArrayList<Statement> statements = new ArrayList<>();
        statements.add(findStatementAt(node.searchChildNode("statement")));
        node = node.searchChildNode("nt3");
        while (node.hasChildNodes()) {
            statements.add(findStatementAt(node.searchChildNode("statement")));
            node = node.searchChildNode("nt3");
        }
        return new StatementBlock(statements);
    }

    private FunctionCall findFunctionCallAt(Node node) {
        StringBuilder name = new StringBuilder(node.getChildNodes().get(0).getName());
        name.deleteCharAt(name.length()-1);                 //видаляємо ліву дужку - '('
        ArrayList<Expr> args = new ArrayList<>();
        node = node.searchChildNode("actualParams");
        while (node.hasChildNodes()) {
            args.add(findExprAt(node.searchChildNode("expression")));
            node = node.searchChildNode("nt6");
        }
        return new FunctionCall(name.toString(), args);
    }

    private Assignment findAssignmentAt(Node node) {
        return new Assignment(findVarAt(node.searchChildNode("var")),
                findExprAt(node.searchChildNode("expression")));
    }

    private IfStatement findIfAt(Node node) {
        Expr condition = findExprAt(node.searchChildNode("expression"));
        Statement thenClause = findStatementAt(node.searchChildNode("statement"));
        Node childNode = node.searchChildNode("nt1");
        if (childNode.hasChildNodes()) {
            Statement elseClause = findStatementAt(childNode.searchChildNode("statement"));
            return new IfStatement(condition, thenClause, elseClause);
        }
        return new IfStatement(condition, thenClause);
    }

    private WhileStatement findWhileStAt(Node node) {
        return new WhileStatement(findExprAt(node.searchChildNode("expression")),
                findStatementAt(node.searchChildNode("statement")));
    }

    private ForStatement findForStAt(Node node) {
        return new ForStatement(findVarAt(node.searchChildNode("var")),
                new IntegerConst(Integer.parseInt(node.getChildNodes().get(3).getName())),
                new IntegerConst(Integer.parseInt(node.getChildNodes().get(5).getName())),
                findStatementAt(node.searchChildNode("statement"))
        );
    }

    private RepeatStatement findRepeatStAt(Node node) {
        return new RepeatStatement(findStatementAt(node.searchChildNode("statement")),
                findExprAt(node.searchChildNode("expression")));
    }

    private Expr findExprAt(Node node) {
        Expr result = null;
        Node childNode;
        switch (node.getName()) {
            case "simpleExpression":
                Expr term1;
                childNode = node.searchChildNode("signOperator");
                if (childNode != null) {
                    term1 = new UnaryOp(childNode.
                            getChildNodes().get(0).getName(), findTermAt(node.searchChildNode("term")));
                } else {
                    term1 = findTermAt(node.searchChildNode("term"));
                }
                childNode = node.searchChildNode("simpleExpression*");
                if (childNode.hasChildNodes()) {
                    result = findAddingExprAt(childNode, term1);
                } else {
                    result = term1;
                }
                break;
            case "expression":
                childNode = node.searchChildNode("nt2");
                if (childNode.hasChildNodes()) {
                    result = new BinaryOp(
                            findExprAt(node.searchChildNode("simpleExpression")),
                            childNode.searchChildNode("relationalOperator").
                            getChildNodes().get(0).getName(),
                            findExprAt(childNode.searchChildNode("simpleExpression")));
                } else {
                    result = findExprAt(node.searchChildNode("simpleExpression"));
                }
                break;
        }
        return result;
    }

    private Expr findAddingExprAt(Node node, Expr leftTerm) {
        Expr result = new BinaryOp(leftTerm, node.searchChildNode("addingOperator").
                getChildNodes().get(0).getName(),
                findTermAt(node.searchChildNode("term")));
        Node childNode = node.searchChildNode("simpleExpression*");
        if (childNode.hasChildNodes()) {
            result = findAddingExprAt(childNode, result);
        }
        return result;
    }

    private Expr findTermAt(Node node) {
        Expr result = null;
        if (node.getName().equals("term")) {
            Expr term1 = findTermAt(node.searchChildNode("factor"));
            node = node.searchChildNode("term*");
            if (node.hasChildNodes()) {
                result = findMultiplyingTerm(node, term1);
            } else {
                result = term1;
            }
        } else if (node.getName().equals("factor")) {
            ArrayList<Node> childNodes = node.getChildNodes();
            if (childNodes.size() == 1) {
                Node childNode = childNodes.get(0);
                if (childNode.getName().equals("var")) {
                    result = findVarAt(childNode);
                }
                switch (childNode.getName()) {
                    case "numeral":
                        result = new IntegerConst(Integer.parseInt(
                                childNode.getChildNodes().get(0).getName()));
                        break;
                    case "real":
                        result = new FloatConst(Float.parseFloat(
                                childNode.getChildNodes().get(0).getName()));
                        break;
                    case "bool":
                        result = new BooleanConst(Boolean.valueOf(
                                childNode.getChildNodes().get(0).getName()));
                        break;
                    case "line":
                        StringBuilder name = new StringBuilder(
                                childNode.getChildNodes().get(0).getName());
                        name.deleteCharAt(0).deleteCharAt(name.length()-1); // видаляємо ' '
                        result = new StringConst(name.toString());
                        break;
                    case "var":
                        result = findVarAt(childNode);
                        break;
                    case "functionCall":
                        result = findFunctionCallAt(childNode);
                        break;
                }
            } else if (childNodes.size() == 2) {
                result = new UnaryOp(childNodes.get(0).getName(),
                        findTermAt(childNodes.get(1)));
            } else if (childNodes.size() == 3) {
                result = findExprAt(childNodes.get(1));
            }
        };
        return result;
    }

    private Expr findMultiplyingTerm(Node node, Expr leftTerm) {
        Expr result = new BinaryOp(leftTerm, node.searchChildNode("multiplyingOperator")
                .getChildNodes().get(0).getName(),
                findTermAt(node.searchChildNode("factor")));
        Node childNode = node.searchChildNode("term*");
        if (childNode.hasChildNodes()) {
            result = findMultiplyingTerm(childNode, result);
        }
        return result;
    }

    private Var findVarAt(Node node) {
        Var result;
        String name = node.getChildNodes().get(0).getName();
        node = node.searchChildNode("nt4");
        if (node.hasChildNodes()) {
            result = new ArrayVar(name,
                    findExprAt(node.searchChildNode("expression")));
        } else {
            result = new Var(name);
        }
        return result;
    }
}
