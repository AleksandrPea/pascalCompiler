package com.apea.compiler.lexical;

import com.apea.compiler.tools.grammars.IsNotInAlphabetException;
import com.apea.compiler.tools.grammars.RegularGrammar;

public class PascalAnalyzer extends LexicalAnalyzer {

    static {
        String[] nterms = {"ID", "NUMERAL", "REAL", "LINE", "FUNCTION",
                "SEMICOLON", "COLON","COMMA","DOT", "LPAREN", "RPAREN",
                "LBRAC", "RBRAC",
                "EQUAL", "NOTEQUAL", "LT", "GT", "LE","GE",
                "STAR", "SLASH", "PLUS", "MINUS",
                "ASSIGMENT", "QUOTE",
                "notClosedLine"
        };
        String[] terms = {"a","b","c","d","e","f", "g", "h", "i","j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z",
                "A","B","C","D","E","F", "G", "H", "I","J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z",
                "0","1","2","3","4","5","6","7","8","9",
                ";", ":", ",", ".", "(", ")", "<", ">", "*", "/", "+", "-",
                "=", "\'", "!", "\"", "#", "$", "%", "&", "?", "@", "[",
                "]", "\\", "^", "_","`", "{", "}", "|", "~", " "
        };
        Character[] delimiters = {'(', ')', '*', '=', ';', '+', '-','/','>','<','.', '[',']',','};
        String[] keywords = {
                "program", "var","integer", "float","boolean","string", "if", "then", "else",
                "begin", "end", "while", "do", "for", "to", "repeat", "until",
                "true", "false", "or", "and", "div", "mod", "not"};
        RegularGrammar gr = new RegularGrammar(terms, nterms);
        try {
            String buf;
            char i = 32;
            for (; i <= 38; i++) {
                buf = Character.toString(i);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } // i == 39
            i++;      // пропускаємо символ '
            for (; i <= 47; i++) {
                buf = Character.toString(i);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } // i == 48
            for (; i <= 57; i++) {
                // додаємо цифри
                buf = Character.toString(i);
                gr.createRule("ID", "ID", buf);
                gr.createRule("NUMERAL", "NUMERAL", buf);
                gr.createRule("NUMERAL", buf);
                gr.createRule("REAL", "REAL", buf);
                gr.createRule("REAL", "DOT", buf);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } //i == 58
            for (; i <= 64; i++) {
                buf = Character.toString(i);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } //i == 65
            for(; i <= 90; i++) {
                // додаємо букви верхнього регістру
                buf = Character.toString(i);
                gr.createRule("ID", "ID", buf);
                gr.createRule("ID", buf);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } //i == 91
            for (; i <= 96; i++) {
                buf = Character.toString(i);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } //i == 97
            for (; i <= 122; i++) {
                // додаємо букви нижнього регістру
                buf = Character.toString(i);
                gr.createRule("ID", "ID", buf);
                gr.createRule("ID", buf);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            } //i == 123
            for(; i <= 126; i++) {
                buf = Character.toString(i);
                gr.createRule("notClosedLine", "notClosedLine", buf);
                gr.createRule("notClosedLine", "QUOTE", buf);
            }
            gr.createRule("REAL", "NUMERAL", ".");
            gr.createRule("LINE", "notClosedLine", "\'");
            gr.createRule("FUNCTION", "ID", "(");
            gr.createRule("SEMICOLON", ";");
            gr.createRule("COLON", ":");
            gr.createRule("COMMA", ",");
            gr.createRule("DOT", ".");
            gr.createRule("LPAREN", "(");
            gr.createRule("RPAREN", ")");
            gr.createRule("LBRAC", "[");
            gr.createRule("RBRAC", "]");
            gr.createRule("EQUAL", "=");
            gr.createRule("NOTEQUAL", "LT", ">");
            gr.createRule("LT", "<");
            gr.createRule("LE", "LT", "=");
            gr.createRule("GT", ">");
            gr.createRule("GE", "GT", "=");
            gr.createRule("STAR", "*");
            gr.createRule("SLASH", "/");
            gr.createRule("PLUS", "+");
            gr.createRule("MINUS", "-");
            gr.createRule("ASSIGMENT", "COLON", "=");
            gr.createRule("QUOTE", "\'");
        } catch (IsNotInAlphabetException e) {
            e.printStackTrace();
        }
        analyzer = new PascalAnalyzer(gr, delimiters, keywords);
    }

    private static final PascalAnalyzer analyzer;

    public PascalAnalyzer(RegularGrammar grammar, Character[] delims, String[] kwords) {
        super(grammar, delims, kwords);
    }

    public static PascalAnalyzer instance() {
        return analyzer;
    }
}