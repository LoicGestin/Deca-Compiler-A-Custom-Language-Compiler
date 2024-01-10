package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabelTable {
    private final Map<String, ArrayList<Label>> map = new HashMap<>();

    public Label addLabel(String type) {
        if (map.containsKey(type)) {
            ArrayList<Label> labelArrayList = map.get(type);
            Label lab = new Label(type, labelArrayList.size());
            labelArrayList.add(lab);
            return lab;

        } else {
            ArrayList<Label> newArrayLab = new ArrayList<>();
            Label lab = new Label(type, 0);
            newArrayLab.add(lab);
            map.put(type, newArrayLab);
            return lab;
        }
    }
}
