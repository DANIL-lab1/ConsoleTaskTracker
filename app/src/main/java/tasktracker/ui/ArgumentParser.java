package tasktracker.ui;

import java.util.*;

public class ArgumentParser {

    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> result = new HashMap<>();
        String fullCommand = String.join(" ", args);
        
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "--(\\w+)\\s+(?:\"([^\"]*)\"|(\\S+))"
        );
        java.util.regex.Matcher matcher = pattern.matcher(fullCommand);
        
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if (value == null) {
                value = matcher.group(3);
            }
            result.put(key, value);
        }
        
        return result;
    }
    
    public static String getValue(Map<String, String> args, String key) {
        return args.get(key);
    }

    public static String getValue(Map<String, String> args, String key, String defaultValue) {
        return args.getOrDefault(key, defaultValue);
    }
}