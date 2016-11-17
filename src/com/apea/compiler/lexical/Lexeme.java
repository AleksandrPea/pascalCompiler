package com.apea.compiler.lexical;

/** Клас, який представляє лексему. */
public class Lexeme {

    public final String text;
    public final String type;
    public final int line;
    public final int col;
    public final int endcol;

    public Lexeme(String text, String type, int line, int col) {
        this.text = text;
        this.type = type;
        this.line = line;
        this.col = col;
        this.endcol = col + text.length();
    }

    @Override
    public int hashCode() {
        return type.hashCode() + text.hashCode() + line + col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lexeme) {
            Lexeme that = (Lexeme)obj;
            return
                    this.type.equals(that.type) &&
                            this.text.equals(that.text) &&
                            this.line == that.line &&
                            this.col == that.col;
        } else {
            return false;
        }
    }

}
