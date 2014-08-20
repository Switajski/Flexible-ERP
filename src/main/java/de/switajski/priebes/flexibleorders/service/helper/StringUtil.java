package de.switajski.priebes.flexibleorders.service.helper;

import java.util.Collection;
import java.util.Iterator;

public class StringUtil {

    public static String concatWithCommas(Collection<String> strings){
        StringBuilder builder = new StringBuilder();
        Iterator<String> itr = strings.iterator();
        while (itr.hasNext()){
            builder.append(itr.next().toString());
            if (itr.hasNext())
                builder.append(", ");
        }
        return builder.toString();
    }
}
