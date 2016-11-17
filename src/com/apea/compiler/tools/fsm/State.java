package com.apea.compiler.tools.fsm;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Представляє стан ДКА.
 * @see DFSM
 */
public class State<S, M> {

    /** Унікальний ідентифікатор стану(номер) */
    private int id;

    /** Ознака стану. */
    private M mark;

    /** Переходи до інших станів. */
    private HashMap<S, State<S, M>> transitions;

    public State(int id, M mark) {
        setId(id);
        setMark(mark);
        transitions = new HashMap<S, State<S,M>>();
    }

    /**
     * Робить перехід по сигналу
     * {@code signal}(умова переходу) до {@code nxtState}.
     * Якщо перехід по вказаному сигналу уже присутній,
     * то змінює його на новий.
     * @return попередній стан, асоційований з {@code signal},
     * або {@code null}, якщо такого стану не було.
     */
    public State<S,M> makeTransition(S signal, State nxtState) {
        return transitions.put(signal, nxtState);
    }

    /**
     * Прибирає перехід по умові переходу {@code signal}.
     * @return прибраний перехід, або {@code null} якщо пере
     * хід був відсутній.
     */
    public State<S, M> removeTransition(S signal) {
        return transitions.remove(signal);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public M getMark() {
        return mark;
    }

    public void setMark(M mark) {
        this.mark = mark;
    }

    /**
     * Повертає стан до якому можна перейти за умовою {@code signal}.
     * Якщо такого стану немає, повертає {@code null}.
     */
    public State<S, M> transitionWith(S signal) {
        return transitions.get(signal);
    }

    public boolean haveNoTransitions() {
        return transitions.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
                "S"+getId()+"["+getMark().toString()+ "]:");
        Iterator<S> iter =
                transitions.keySet().iterator();
        State state;
        S signal;
        while (iter.hasNext()) {
            signal = iter.next();
            state = transitions.get(signal);
            result.append("\n("+signal.toString()+")" +
                    "\t\t->\tS"+state.getId()+"["+state.getMark()
                    .toString()+ "]");
        }
        result.append(".\n");
        return result.toString();
    }
}
