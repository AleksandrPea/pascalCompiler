package com.apea.compiler.syntactic;

import com.apea.compiler.lexical.Lexeme;
import com.apea.compiler.tools.grammars.Grammar;
import com.apea.compiler.tools.grammars.Symbol;

import java.util.*;

/**
 * Клас представляє синтаксичний аналізатор на основі LL(1)
 * граматики.
 */
public class Parser {
    private Grammar grammar;
    private HashMap<String, HashSet<String>> firstSet;
    private HashMap<String, HashSet<String>> followSet;
    private Stack<Symbol> stack;
    /** Індекс правила(вик. при побудові дерева). */
    private int ruleIndex;
    /** Індекс лексем(вик. при побудові дерева). */
    private int lexemeIndex;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        firstSet = new HashMap<>();
        followSet = new HashMap<>();
        stack = new Stack<>();
        fillSets();
    }

    public Node parse(ArrayList<Lexeme> lexemes) {
        // використані правила
        ArrayList<Grammar.Rule> usedRules = new ArrayList<>();
        stack.push(new Symbol(grammar.getAxiom(), false));
        Symbol symbol;
        Grammar.Rule[] rules;
        Grammar.Rule rule;
        for (Lexeme lexeme : lexemes) {
            while (stack.peek().isNonTerminal()) {
                rules = grammar.searchRules(stack.peek().getValue());
                boolean flag = false;
                for (int i = 0; i < rules.length && !flag; i++) {
                    if (!rules[i].isEmptyRule()) {
                        symbol = rules[i].getRightSymbol(0);
                        if (symbol.isTerminal() && lexeme.type.equals(symbol.getValue()) ||
                                symbol.isNonTerminal()
                                        && firstSet.get(symbol.getValue()).contains(lexeme.type)) {
                            stack.pop();
                            for (int j = rules[i].getRightLength() - 1; j >= 0; j--) {
                                stack.push(rules[i].getRightSymbol(j));
                            }
                            usedRules.add(rules[i]);
                            flag = true;
                        }
                        if (!flag) {
                            symbol = stack.peek();
                            rule = grammar.searchEmptyRuleFor(symbol.getValue());
                            if (rule != null
                                    && followSet.get(symbol.getValue()).contains(lexeme.type)) {
                                stack.pop();
                                usedRules.add(rule);
                                flag = true;
                            }
                        }
                    }
                }
                if (!flag) {
                    throwException(lexeme);
                }
            }
            if (!stack.peek().getValue().equals(lexeme.type)) {
                throwException(lexeme);
            }
            stack.pop();           // верхівка стеку співпадає з поточною лексемою
        }
        while (stack.size() != 0) {
            symbol = stack.peek();
            rule = grammar.searchEmptyRuleFor(symbol.getValue());
            if (rule != null
                    && followSet.get(symbol.getValue()).contains(null)) {
                stack.pop();
                usedRules.add(rule);
            } else {
                throw new ParsingException("Parsing error");
            }
        }
        lexemeIndex = ruleIndex = 0;
        return makeNode(usedRules, lexemes);
    }

    /**
     * Рекурсивно будує дерево, починаючи з індексів {@link #lexemeIndex},
     * {@link #ruleIndex} у колекціях {@code lexemes}, {@code usedRules} відповідно.
     */
    private Node makeNode(ArrayList<Grammar.Rule> usedRules,
                          ArrayList<Lexeme> lexemes) {
        Grammar.Rule rule = usedRules.get(ruleIndex++);
        Symbol symbol;
        Node result = new Node(rule.getLeftNT());
        for (int i = 0; i < rule.getRightLength(); i++) {
            symbol = rule.getRightSymbol(i);
            if (symbol != null) {
                if (symbol.isTerminal()) {
                    result.addNode(new Node(lexemes.get(lexemeIndex++).text));
                } else {
                    result.addNode(makeNode(usedRules, lexemes));
                }
            } else {
                result.addNode(new Node(null));
            }
        }
        return result;
    }

    /** Заповнює множини {@link #firstSet} та {@link #followSet}. */
    private void fillSets() {
        Grammar modifiedGrammar = grammar.deleteChainRules();
        List<Grammar.Rule> rules = modifiedGrammar.getRulesList();
        for (Grammar.Rule rule : rules) {
            firstSet.put(rule.getLeftNT(), new HashSet<String>());
            followSet.put(rule.getLeftNT(), new HashSet<String>());
        }
        for (Grammar.Rule rule :rules) {
            if (firstSet.get(rule.getLeftNT()).isEmpty()) {
                findFirstsFor(rule.getLeftNT(), modifiedGrammar);
            }
        }
        for (Grammar.Rule rule : rules) {
            if (followSet.get(rule.getLeftNT()).isEmpty()) {
                findFollowsFor(rule.getLeftNT(), modifiedGrammar);
            }
        }
    }

    /**
     * Заповнює множину початкових(first) рядків({@link #firstSet}) для нетермінала
     * {@code nonterm}. Якщо для цього необхідна множина початкових рядків іншого
     * нетермінала, то заповнює і її, при умові,що вони була пуста.
     * @param modifiedGrammar модифікована граматика(видалені ланцюгові правила).
     *                        Необхідна тільки для побудови множин {@link #firstSet},
     *                        та {@link #followSet}.
     */
    private void findFirstsFor(String nonterm, Grammar modifiedGrammar) {
        HashSet<String> mainSet = firstSet.get(nonterm);
        HashSet<String> set;
        Grammar.Rule[] rules = modifiedGrammar.searchRules(nonterm);
        Symbol symbol;
        for (Grammar.Rule rule : rules) {
            if (!rule.isEmptyRule()) {
                int i = 0;
                symbol = rule.getRightSymbol(i);
                if (symbol.isTerminal()) {
                    if (!mainSet.add(symbol.getValue())) {
                        throw new ParsingException("Ambiguity of firsts for "
                                + nonterm);
                    }
                } else {
                    set = firstSet.get(symbol.getValue());
                    if (set.isEmpty()) {
                        findFirstsFor(symbol.getValue(), modifiedGrammar);
                    }
                    for (String first : set) {
                        if (!mainSet.add(first)) {
                            throw new ParsingException("Ambiguity of firsts for "
                                    + nonterm);
                        }
                    }
                }
            }
        }
    }

    /**
     * Заповнює множину слідуючих(follow) рядків({@link #followSet}) для нетермінала
     * {@code nonterm}. Якщо для цього необхідна множина слідуючих рядків іншого
     * нетермінала, то заповнює і її, при умові,що вони була пуста.
     * @param modifiedGrammar модифікована граматика(видалені ланцюгові правила).
     *                        Необхідна тільки для побудови множин {@link #firstSet},
     *                        та {@link #followSet}.
     */
    private void findFollowsFor(String nonterm, Grammar modifiedGrammar) {
        HashSet<String> mainSet = followSet.get(nonterm);
        HashSet<String> set;
        List<Grammar.Rule> rulesList = modifiedGrammar.getRulesList();
        Symbol ntSymbol = new Symbol(nonterm, false);
        Symbol symbol;
        for (Grammar.Rule rule : rulesList) {
           if (rule.contains(ntSymbol)) {
               int i = 0;
               while (i < rule.getRightLength()
                       && !rule.getRightSymbol(i).equals(ntSymbol)) {
                   i++;
               }
               // випадок правила виду B -> a nonterm b
               if (i < rule.getRightLength()-1) {
                   symbol = rule.getRightSymbol(i+1);
                   if (symbol.isTerminal()) {
                       mainSet.add(symbol.getValue());
                   } else {
                       mainSet.addAll(firstSet.get(symbol.getValue()));
                       // випадок для правил виду B -> ""
                       if (modifiedGrammar.hasEmptyRuleFor(symbol.getValue())) {
                           set = followSet.get(symbol.getValue());
                           if (set.isEmpty()) {
                               findFollowsFor(symbol.getValue(), modifiedGrammar);
                           }
                           mainSet.addAll(set);
                       }
                   }
                   // випадок правила виду B -> a nonterm
               } else {
                   set = followSet.get(rule.getLeftNT());
                   if (set.isEmpty()) {
                       findFollowsFor(rule.getLeftNT(), modifiedGrammar);
                   }
                   mainSet.addAll(set);
               }
           }
        }
        if (modifiedGrammar.getAxiom().equals(nonterm)) {
            // для початкового нетерміналу додається пустий символ
            mainSet.add(null);
        }
    }

    private void throwException(Lexeme lexeme) {
        throw new ParsingException("\nParsing error at line " + lexeme.line +
                " col " + lexeme.col + "; found : \"" + lexeme.text +
                "\", expected syntax : \"" + stack.peek()+"\"");
    }
}