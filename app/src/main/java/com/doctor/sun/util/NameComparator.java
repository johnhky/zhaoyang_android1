package com.doctor.sun.util;

import java.util.Comparator;

/**
 * Created by rick on 26/12/2015.
 */
public class NameComparator implements Comparator<NameComparator.Name> {

    @Override
    public int compare(Name lhs, Name rhs) {
        String lhsName = lhs.getName();
        if (lhs.getName() == null) {
            lhsName = "";
        }
        String lhsString = CharacterParser.getInstance().getSpelling(lhsName);
        String rhsName = rhs.getName();
        if (rhs.getName() == null) {
            rhsName = "";
        }
        String rhsString = CharacterParser.getInstance().getSpelling(rhsName);
        return lhsString.toUpperCase().compareTo(rhsString.toUpperCase());
    }

    public interface Name {
        String getName();
    }
}
