package model;

public class DataChecker {
    public static String copyWithChecking(String words, char check, boolean isName) {
        if (words.isEmpty()) {
            return null;
        }
        else {
            for (char c : words.toCharArray()) {
                if (c == check) {
                    System.out.printf("This word have a '%c'\n", check);
                    return null;
                }

                if (isName) {
                    if (words.matches("\\W")) {
                        System.out.println("Name can't have any special characters!");
                        return null;
                    }

                    if (words.matches(".*\\d+.*")) {
                        System.out.println("Name can't have any number!");
                        return null;
                    }


                    words = words.toUpperCase();
                }
            }
        }

        return words;
    }

    public static String copyNumber(String words, char check) {
        if (copyWithChecking(words, check, false) == null) {
            return null;
        }

        return words.replaceAll("\\D", "");
    }

    public static String copyNumberWIthReplace(String words, String check, String replace) {
        String[] arr = words.split(check);
        return String.format("%s%s%s%s%s", arr[2], replace, arr[1], replace, arr[0]);
    }
}
