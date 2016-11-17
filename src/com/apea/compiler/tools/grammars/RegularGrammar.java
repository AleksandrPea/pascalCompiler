package com.apea.compiler.tools.grammars;

/** Представляє лівосторонню регулярну граматику.*/
public class RegularGrammar extends Grammar {
    
    public RegularGrammar(String[] terminals,
                   String[] nonterminals) {
        super(terminals, nonterminals);
    }

    /** Створює регулярне правило виду A -> Bc. */
    public void createRule(String leftNT, String rightNT, String rightT)
            throws IsNotInAlphabetException {
        if (containsNonterminal(leftNT) && containsNonterminal(rightNT)
                && containsTerminal(rightT)) {
                rulesList.add(new RegularRule(leftNT, rightNT, rightT));
        } else {throw new IsNotInAlphabetException();}
    }

    /**
     * Створює регулярне правило виду A -> c.
     * Для таких правил метод {@link RegularRule#getRightNT}
     * повертає {@code null}.
     */
    public void createRule(String leftNT,String rightT)
            throws IsNotInAlphabetException {
        if (containsNonterminal(leftNT) && containsTerminal(rightT)) {
            rulesList.add(new RegularRule(leftNT, rightT));
        } else {throw new IsNotInAlphabetException();}
    }

    /** Представляє лівостороннє регулярне правило.*/
    public static class RegularRule extends Grammar.Rule{

        /** Правило виду A -> Bc.*/
        protected RegularRule(String leftNT, String rightNT, String rightT) {
            super(leftNT, new Symbol[]{new Symbol(rightNT, false),
                    new Symbol(rightT, true)});
        }

        /** Правило виду A -> c.*/
        protected RegularRule(String leftNT,String rightT) {
            super(leftNT, new Symbol[]{new Symbol(rightT, true)});
        }

        /**@return  правий термінал. */
        public String getRightT() {
            String result = null;
            if (getRightLength() == 1) {
                result = getRightSymbol(0).getValue();
            } else if (getRightLength() == 2) {
                result = getRightSymbol(1).getValue();
            }
            return result;
        }

        /**
         * @return правий нетермінал. Якщо
         * він відсутній, то повертає null.
         */
        public String getRightNT() {
            String result = null;
            if (getRightLength() == 2) {
                result = getRightSymbol(0).getValue();
            }
            return result;
        }
    }
}
