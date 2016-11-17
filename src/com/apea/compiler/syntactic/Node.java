package com.apea.compiler.syntactic;

import java.util.ArrayList;
import java.util.Iterator;

/** Представляє вершину в дереві синтаксичного розбору. */
public class Node {

    private String name;
    private ArrayList<Node> childNodes;

    public Node(String name) {
        childNodes = new ArrayList<>();
        this.name = name;
    }

    /**
     * Шукає серед безпосередніх нащадків
     * вершину з ім'ям {@code name}. Якщо такої немає,
     * повертає {@code null}.
     */
    public Node searchChildNode(String name) {
        Node result = null;
        int i = 0;
        while (i < childNodes.size() && result == null) {
            if (name.equals(childNodes.get(i).getName())) {
                result = childNodes.get(i);
            }
            i++;
        }
        return result;
    }

    /** @return чи має нащадків. */
    public boolean hasChildNodes() {
        return childNodes.get(0).getName() != null;
    }

    public void addNode(Node node) {
        childNodes.add(node);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Node> getChildNodes() {
        return childNodes;
    }

    public String toString(int idents) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= idents; i++) {
            result.append("\t");
        }
        result.append(getName());
        Iterator<Node> iter = childNodes.iterator();
        Node node;
        if (!childNodes.isEmpty()) {
            idents++;
            result.append(" ->");
        }
        while (iter.hasNext()) {
            node = iter.next();
            result.append("\n" + node.toString(idents));
        }
        return result.toString();
    }
}
