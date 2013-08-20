package com.github.riking.dropcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util {
    public static List<String> getPartialMatches(Collection<String> choices, String partial) {
        ArrayList<String> ret = new ArrayList<String>(4);
        for (String choice : choices) {
            if (choice.startsWith(partial)) {
                ret.add(choice);
            }
        }
        return ret;
    }
}
