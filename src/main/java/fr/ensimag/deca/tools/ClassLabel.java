package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;
import java.util.Map;

public class ClassLabel {
    private final Map<String, Label> mapLabel = new HashMap<>();

    public Label addLabel(String name) {
        if (mapLabel.containsKey(name)) {
            return mapLabel.get(name);
        } else {
            Label lab = new Label(name);
            mapLabel.put(name, lab);
            return lab;
        }
    }
}
