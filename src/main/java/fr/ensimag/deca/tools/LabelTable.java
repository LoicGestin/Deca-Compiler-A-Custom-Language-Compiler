package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;
import java.util.Map;

public class LabelTable {
    private final Map<String, Integer> map = new HashMap<>();

    public Label addLabel(String type) {
        if (map.containsKey(type)) {
            map.replace(type, map.get(type) + 1);
            return new Label(type, map.get(type));

        } else {
            Label lab = new Label(type, 1);
            map.put(type, 1);
            return lab;
        }
    }

    public Label addLabel(String type, Boolean duplicate) {
        if (duplicate){
            Label lab = new Label(type, 1);
            map.put(type, 1);
            return lab;
        }
        return addLabel(type);
    }


}
