package tasktracker.util;

public class ColorUtils {
    
    // ========== ANSI Escape Codes ==========
    
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    
    // ========== Standard Colors ==========
    
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String GRAY = "\u001B[90m";
    
    // ========== Bright Colors ==========
    
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    
    // ========== Extended Colors (256-color mode) ==========
    
    public static final String ORANGE = "\u001B[38;5;208m";
    
    public static final String DARK_ORANGE = "\u001B[38;5;202m";
    
    public static final String LIGHT_ORANGE = "\u001B[38;5;214m";
    
    // ========== Background Colors ==========
    
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_ORANGE = "\u001B[48;5;208m";
    
    // ========== Helper methods ==========
    
    public static String success(String text) {
        return GREEN + text + RESET;
    }
    
    public static String error(String text) {
        return RED + text + RESET;
    }
    
    public static String warning(String text) {
        return ORANGE + text + RESET;  // ← Оранжевый для warning
    }
    
    public static String info(String text) {
        return CYAN + text + RESET;
    }
    
    public static String bold(String text) {
        return BOLD + text + RESET;
    }
    
    public static String dim(String text) {
        return DIM + text + RESET;
    }
    
    public static String gray(String text) {
        return GRAY + text + RESET;
    }
    
    public static String orange(String text) {
        return ORANGE + text + RESET;
    }
    
    public static String cyan(String text) {
        return CYAN + text + RESET;
    }
    
    public static String green(String text){
        return GREEN + text + RESET;
    }
    
    // ========== Status colors ==========
    
    public static String colorStatus(String status) {
        if (status == null) return "-";
        switch (status.toUpperCase()) {
            case "NEW": 
                return BLUE + status + RESET;
            case "IN_PROGRESS":
            case "IN PROGRESS": 
                return ORANGE + status + RESET;  // ← Оранжевый для "в работе"
            case "DONE": 
                return GREEN + status + RESET;
            default: 
                return status;
        }
    }
    
    // ========== Tag colors ==========
    
    public static String colorTag(String tag) {
        if (tag == null) return "";
        int hash = Math.abs(tag.hashCode());
        String[] colors = {RED, GREEN, ORANGE, BLUE, PURPLE, CYAN};
        return colors[hash % colors.length] + "#" + tag + RESET;
    }
    
     // ========== Message helpers ==========
    
    public static void printSuccess(String message) {
        System.out.println(GREEN + "[OK] " + message + RESET);
    }
    
    public static void printError(String message) {
        System.out.println(RED + "[ERROR] " + message + RESET);
    }
    
    public static void printWarning(String message) {
        System.out.println(ORANGE + "[WARN] " + message + RESET);  // ← Оранжевый
    }
    
    public static void printInfo(String message) {
        System.out.println(CYAN + "[INFO] " + message + RESET);
    }
    
    public static void printTip(String message) {
        System.out.println(DIM + "[TIP] " + message + RESET);
    }
    
    // ========== Utility ==========
    
    public static String stripAnsi(String text) {
        if (text == null) return "";
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }
    
    public static boolean supportsAnsi() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String term = System.getenv("WT_SESSION");
            if (term != null) return true;
            return false;
        }
        return true;
    }
    
    public static String center(String text, int width) {
        if (text == null) return "";
        String stripped = stripAnsi(text);
        int textLength = stripped.length();
        if (textLength >= width) return text;

        int totalPadding = width - textLength;
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }
    
    // ========== Help-related helpers ==========

    public static String helpTitle(String text) {
        return BOLD + CYAN + text + RESET;
    }

    public static String helpCategory(String text) {
        return BOLD + ORANGE + text + RESET;
    }

    public static String helpCommand(String text) {
        return BOLD + GREEN + text + RESET;
    }

    public static String helpDescription(String text) {
        return DIM + text + RESET;
    }

    public static String helpUsage(String text) {
        return GRAY + text + RESET;
    }
}