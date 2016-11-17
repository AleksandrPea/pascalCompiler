package com.apea.compiler.lexical;

import com.apea.compiler.tools.fsm.DFSM;
import com.apea.compiler.tools.fsm.State;
import com.apea.compiler.tools.grammars.Grammar;
import com.apea.compiler.tools.grammars.RegularGrammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Клас представляє лексичний аналізатор на основі скінченного
 * детермінованого автомату.
 */
public class LexicalAnalyzer extends DFSM<Character, String> {

    /**
     * Розільники. Використовуються для визначення
     * меж лексем. Кожний роздільник може бути лексемою,
     * крім пробілів.<br>
     * За замовчуванням містить: {@code ' ', '\n', '\t', '\r'}
     */
    private HashSet<Character> delimiters;

    /** Ключові слова. */
    private HashSet<String> keywords;

    /** Вихідний текст. */
    private String text;

    /** Номер поточного рядка у тексті. */
    private int line;

    /** Номер поточного стовпця у тексті. */
    private int col;

    /**
     * Поточна позиція у вихідному тексті
     * з якої буде починатися пошук методом {@link #findLexeme()}.
     */
    private int offset;

    public LexicalAnalyzer(RegularGrammar grammar,
                           Character[] delims, String[] kwords) {
        super();
        delimiters = new HashSet<>();
        keywords = new HashSet<>();
        for (Character delim: delims) {
            delimiters.add(delim);
        }
        delimiters.add(' ');
        delimiters.add('\n');
        delimiters.add('\t');
        delimiters.add('\r');
        for (String kword: kwords) {
            keywords.add(kword);
        }
        makeFSM(grammar);
    }

    /** Розбиває вихідний текст {@code text} на лексеми. */
    public ArrayList<Lexeme> makeLexemes(String text) {
        this.text = text;
        line = 1;
        col = 1;
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        offset = 0;
        skipWhiteSpaces();
        while(offset < text.length()) {
            lexemes.add(findLexeme());
            skipWhiteSpaces();
        }
        return lexemes;
    }

    /**
     * Шукає лексему у тексті {@link #text}.
     * Пробіли та переводи рядка пропускаються.
     * @return знайдену лексему.
     */
    private Lexeme findLexeme() {
        int i = offset;
        resetToFirstState();
        StringBuilder lexemeText = new StringBuilder();
        Character currentChar = null;
        // повторюємо цикл тільки за умов якщо: 1)не дійшли до кінця тексту і
        // 2)переход по наступному символу є, або
        // 3)якщо ні поточний символ ні наступний не є роздільниками. Якщо друга
        // умова не виконується, а перша і третя виконуються - це означає помилку.
        while (i < text.length() && (canMoveWith(text.charAt(i)) ||
                    !delimiters.contains(text.charAt(i)) &&
                            !delimiters.contains(currentChar)))
        {
            currentChar = text.charAt(i++);
            if (!canMoveWith(currentChar)) {
                throw new LexicalException("Cannot find lexeme at line " +
                        line + " col " + col);
            }
            moveWith(currentChar);
            lexemeText.append(currentChar);
        }
        String name = new String(lexemeText);
        String type;
        if (keywords.contains(name)) {
            type = name.toUpperCase();
        } else {
            type = getCurrentState().getMark();
        }
        Lexeme lexeme = new Lexeme(name, type, line, col);
        consumeInput(i);
        return lexeme;
    }

    /**
     * Поправляє {@link #col}, {@link #line} та {@link #offset}.
     * У вихідному тексті аналізуються
     * символ, починаючи з {@link #offset}
     * до {@code len}(не включаючи).
     */
    private void consumeInput(int len) {
        for (int i = offset; i < len; i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        }
        offset = len;
    }

    /** пропускає символи {@code ' ', '\n', '\t', '\r'.} */
    private void skipWhiteSpaces() {
        int i = offset;
        //
        while (i < text.length() &&
                (text.charAt(i) == ' ' ||
                        text.charAt(i) == '\n' ||
                        text.charAt(i) == '\t' ||
                        text.charAt(i) == '\r')){
            i++;
        }
        consumeInput(i);
    }

    /** Будує автомат на основі регулярної граматики. */
    private void makeFSM(RegularGrammar grammar) {
        setFirstState(new State<>(0, "%start%"));
        String[] nterms = grammar.getNonterminals();
        for (int i = 1; i <= nterms.length; i++) {
            addState(new State<>(i, nterms[i-1]));
        }
        List<Grammar.Rule> rules = grammar.getRulesList();
        State<Character, String> state;
        for (Grammar.Rule rule : rules) {
            RegularGrammar.RegularRule regRule =
                    (RegularGrammar.RegularRule) rule;
            state = searchState(regRule.getRightNT());
            if (state == null) {
                state = getStatesList().get(0);      // стартовий стан
            }
            if (state.makeTransition(
                    regRule.getRightT().charAt(0),
                    searchState(rule.getLeftNT())) != null) {
                System.err.println("There was state rewriting with mark "
                        + state.getMark());
            }
        }
    }
}
