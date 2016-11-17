package com.apea.compiler.tools.grammars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Grammar {

    /** Стартовий нетермінал. */
    private String axiom;

    /**
     * Множина терміналів.
     * По замовчуванню містить
     * пустий символ "".
     */
    private HashSet<String> terminals;

    /** Множина нетерміналів. */
    private HashSet<String> nonterminals;

    /** Перелік правил. */
    protected ArrayList<Rule> rulesList;

    /** Перший нетермінал приймається за стартовий. */
    public Grammar(String[] terminals,
                   String[] nonterminals) {
        this.terminals = new HashSet<>();
        this.nonterminals = new HashSet<>();
        for (String terminal : terminals) {
            this.terminals.add(terminal);
        }
        for (String nonterminal : nonterminals) {
            this.nonterminals.add(nonterminal);
        }
        axiom = nonterminals[0];
        rulesList = new ArrayList<Rule>();
    }

    public void createRule(String leftNT, Symbol... rightSide)
            throws IsNotInAlphabetException {
        boolean flag = true;
        int i = 0;
        while (i < rightSide.length && flag) {
            if (!containsSymbol(rightSide[i])) {
                flag = false;
            }
            i++;
        }
        if (containsNonterminal(leftNT) && flag) {
            rulesList.add(new Rule(leftNT, rightSide));
        } else {throw new IsNotInAlphabetException();}
    }

    /** Створює правило виду: {@code leftNT -> ""}(пустий символ).*/
    public void createEmptyRule(String leftNT)
            throws IsNotInAlphabetException {
        if (containsNonterminal(leftNT)) {
            rulesList.add(new Rule(leftNT, null));
        } else {throw new IsNotInAlphabetException();}
    }

    public boolean containsNonterminal(String symbol) {
        return nonterminals.contains(symbol);
    }

    public boolean containsTerminal(String symbol) {
        return terminals.contains(symbol);
    }

    public boolean containsSymbol(Symbol symbol) {
        boolean result;
        if (symbol.isTerminal()) {
            result = containsTerminal(symbol.getValue());
        } else {
            result = containsNonterminal(symbol.getValue());
        }
        return result;
    }

    /**
     * @return правила, які зліва мають нетермінал {@code leftNT}.
     * Якщо таких правил немає, повертає пустий масив.
     */
    public Rule[] searchRules(String leftNT) {
        ArrayList<Rule> rules = new ArrayList<>();
        for (Rule rule : rulesList) {
            if (rule.getLeftNT().equals(leftNT)) {
                rules.add(rule);
            }
        }
        if (!rules.isEmpty()) {
            Object[] objects = rules.toArray();
            Rule[] result = new Rule[objects.length];
            for (int i = 0; i < objects.length; i++) {
                result[i] = (Rule) objects[i];
            }
            return result;
        } else {
            return new Rule[0];
        }
    }

    /**
     * Якщо граматика має пусте правило для нетермінала
     * {@code leftNT}, то повертає його. Якщо такого немає,
     * повертає null.
     */
    public Rule searchEmptyRuleFor(String leftNT) {
        Rule result = null;
        Rule[] rules = searchRules(leftNT);
        int i = 0;
        while (i < rules.length && result == null) {
            if (rules[i].isEmptyRule()) {
                result = rules[i];
            }
            i++;
        }
        return result;
    }

    /** @return чи є правило виду {@code leftNT -> ""}. */
    public boolean hasEmptyRuleFor(String leftNT) {
        return searchEmptyRuleFor(leftNT) != null;
    }

    /** Видаляє ланцюгові правила та повертає перетворену граматику. */
    public Grammar deleteChainRules() {
        Grammar result = new Grammar(getTerminals(), getNonterminals());
        result.axiom = this.axiom;
        result.getRulesList().addAll(rulesList);
        Rule[] rules;
        Rule rule;
        Symbol symbol;
        int j = 0;
        while (j < result.getRulesList().size()) {
            rule = result.getRulesList().get(j);
            symbol = rule.getRightSymbol(0);
            if (rule.getRightLength() == 1
                    && symbol!= null
                    && symbol.isNonTerminal()) {
                rules = result.searchRules(symbol.getValue());
                for (int i = 0; i< rules.length; i++) {
                    try {
                        if (rules[i].isEmptyRule()) {
                            result.createEmptyRule(rule.getLeftNT());
                        } else {
                            result.createRule(rule.getLeftNT(), rules[i].rightSide);
                        }
                    } catch (IsNotInAlphabetException e) {
                        e.printStackTrace();
                    }
                }
                result.getRulesList().remove(rule);
            } else {
                j++;
            }
        }
        return result;
    }

    public String getAxiom() {
        return axiom;
    }

    public void setAxiom(String axiom)
            throws IsNotInAlphabetException {
        if (containsNonterminal(axiom)) {
            this.axiom = axiom;
         } else {
            throw new IsNotInAlphabetException();
        }
    }

    public String[] getTerminals() {
        String[] array = new String[terminals.size()];
        return terminals.toArray(array);
    }

    public String[] getNonterminals() {
        String[] array = new String[nonterminals.size()];
        return nonterminals.toArray(array);
    }

    public ArrayList<Rule> getRulesList() {
        return rulesList;
    }

    /** Представляє КВ правило.*/
    public static class Rule{

        /** Ліва частина правила(нетермінал). */
        private String leftNT;

        /**
         * Права частина правила.
         * Кожний елемент масиву
         * це нетермінал або термінал.
         * Може містити пустий символ.*/
        private Symbol[] rightSide;

        protected Rule(String leftNT, Symbol[] rightSide) {
            this.leftNT = leftNT;
            this.rightSide = rightSide;
        }

        /** @return чи містить права частина правила {@code symbol}. */
        public boolean contains(Symbol symbol) {
            boolean result = false;
            for (int i = 0; i < getRightLength() && !result; i++) {
                if (symbol.equals(getRightSymbol(i))) {
                    result = true;
                }
            }
            return result;
        }

        /**
         * @param index номер символу
         * @return термінал або нетермінал(символ) з
         * правої частини правила. Якщо правило виду
         * A -> "", то повертає {@null} при будь-якому індексі.
         */
        public Symbol getRightSymbol(int index) {
            if (rightSide != null) {
                return rightSide[index];
            } else {
                return null;
            }
        }

        public String getLeftNT() {
            return leftNT;
        }

        /** Повертає кількість символів у правій частині правила. */
        public int getRightLength() {
            if (rightSide != null) {
                return rightSide.length;
            } else return 1;
        }

        /** @return чи є правило правилом виду A -> "". */
        public boolean isEmptyRule() {
            return rightSide == null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Rule)) return false;

            Rule rule = (Rule) o;

            if (!leftNT.equals(rule.leftNT)) return false;
            return Arrays.equals(rightSide, rule.rightSide);

        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(getLeftNT());
            result.append(" -> ");
            if (!isEmptyRule()) {
                for (Symbol symbol : rightSide) {
                    result.append(symbol + " ");
                }
                result.deleteCharAt(result.length() - 1);
            }
            return result.toString();
        }
    }
}

