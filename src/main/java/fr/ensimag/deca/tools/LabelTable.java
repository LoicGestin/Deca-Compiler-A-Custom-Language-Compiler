package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabelTable {
    private final Map<String, Integer> map = new HashMap<>();

    public Label addLabel(String type) {
        if (map.containsKey(type)) {
            map.replace(type, map.get(type)+1);
            Label lab = new Label(type, map.get(type));
            return lab;

        } else {
            Label lab = new Label(type,0);
            map.put(type, 0);
            return lab;
        }
    }
}
