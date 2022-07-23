package fr.minemobs.minemobsutils.utils;

public class WordUtils {

    private WordUtils() {}

    public static String capitalize(String str) {
        String[] words = str.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }

}
