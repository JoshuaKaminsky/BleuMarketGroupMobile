package com.mobile.android.client.utilities;

/**
 * Created by Josh on 10/12/15.
 */
public class StringUtilities {

    public static String Join(String joinWith, String[] source) {
        if(source == null)
            return "";

        StringBuilder builder = new StringBuilder();

        for (String item : source) {

            if (builder.length() > 0)
                builder.append(joinWith);

            builder.append(item);
        }

        return builder.toString();
    }

    public static boolean IsNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
