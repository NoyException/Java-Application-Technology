package cn.noy.javahw.util;

public class CaseConverter {
    public static String toCamelCase(String s) {
        return toCamelCase(s, false);
    }

    public static String toCamelCase(String s, boolean firstCharUppercase) {
        if(s.contains("_") || s.contains("-")){
            String[] parts = s.split("[_-]");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (i == 0 && !firstCharUppercase) {
                    sb.append(part.toLowerCase());
                } else {
                    sb.append(part.substring(0, 1).toUpperCase());
                    sb.append(part.substring(1).toLowerCase());
                }
            }
            return sb.toString();
        }
        return firstCharUppercase ? s.substring(0, 1).toUpperCase() + s.substring(1) :
                s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
