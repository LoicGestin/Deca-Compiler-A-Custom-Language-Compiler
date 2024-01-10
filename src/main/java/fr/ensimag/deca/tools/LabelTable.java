package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabelTable {
    private final Map<String, ArrayList<Label>> map = new HashMap<>();

    public Label create(String type) {
        if (map.containsKey(type)) {
            ArrayList<Label> labelArrayList= map.get(type);
            Label lastLab = labelArrayList.get(labelArrayList.size()-1);
            Label lab = new Label(type, lastLab.getTypeIndex()+1);
            return lab;

        } else {
            ArrayList<Label> newArrayLab = new ArrayList<Label>();
            Label lab = new Label(type,0);
            newArrayLab.add(lab);
            map.put(type, newArrayLab);
            return lab;
        }
    }
}
