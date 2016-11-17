package com.apea.compiler.tools.grammars;

/**
 * Представляє символ в правилі. Може бути терміналом,
 * нетерміналом.
 */
public class Symbol {

    /** Значення символу. */
    private String value;

    /** Чи є символ терміналом. */
    private boolean isTerminal;

    public Symbol(String value, boolean isTerminal) {
        this.value = value;
        this.isTerminal = isTerminal;
    }

    public String getValue() {
        return value;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public boolean isNonTerminal() {
        return !isTerminal;
    }

    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;

        Symbol symbol = (Symbol) o;

        if (isTerminal() != symbol.isTerminal()) return false;
        return getValue().equals(symbol.getValue());

    }
}
