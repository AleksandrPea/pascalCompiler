package com.apea.compiler.types;

import java.util.*;

/**
 * Таблиця операцій, що ставить у відповідність одному об'єкту {@link String}
 * множину об'єктів {@link Type}.
 */
public class OperatorTable {

    private HashMap<String, HashSet<OperatorType>> innerTable;

    public OperatorTable() {
        innerTable = new HashMap<>();
    }

    /** @return Чи був доданий {@code value}. */
    public boolean add(String name, OperatorType value) {
        HashSet<OperatorType> set = innerTable.get(name);
        if (set == null) {
            set = new HashSet<>();
            innerTable.put(name, set);
        }
        return set.add(value);
    }

    /** @return Чи змінилася таблиця після додання елементів. */
    public boolean addAll(OperatorTable typeTable) {
        boolean result = false;
        HashMap<String, HashSet<OperatorType>> map = typeTable.innerTable;
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            HashSet<OperatorType> set = innerTable.get(key);
            if (set == null) {
                set = new HashSet<>();
                innerTable.put(key, set);
            }
            result = set.addAll(map.get(key)) || result;
        }
        return result;
    }

    public OperatorType search(String key, List<Type> argTypes) {
        OperatorType result = null;
        Iterator<OperatorType> iter = getSet(key).iterator();
        while (iter.hasNext() && result == null) {
            OperatorType opType = iter.next();
            if (opType.argTypes.equals(argTypes)) {
                result = opType;
            }
        }
        return result;
    }

    public boolean containsKey(String key) {
        return innerTable.containsKey(key);
    }

    public OperatorType search(String key, Type... argTypes) {
        return search(key, Arrays.asList(argTypes));
    }

    public HashSet<OperatorType> getSet(String key) {
        return innerTable.get(key);
    }


}
