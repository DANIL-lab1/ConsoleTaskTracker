package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import tasktracker.ui.CommandDispatcher;
import tasktracker.util.ColorUtils;
import java.util.*;

public class HelpCommand implements ICommand {
    
    private static final int WIDTH = 60;
    private static final String LINE = "=".repeat(WIDTH);
    
    private final CommandDispatcher dispatcher;
    
    private static final List<String> CATEGORY_ORDER = List.of(
        "TASK MANAGEMENT",
        "SEARCH & FILTER",
        "UTILITY",
        "OTHER"
    );
    
    public HelpCommand(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    @Override
    public void execute(String[] args, TaskService service) {
        Map<String, List<String>> byCategory = new LinkedHashMap<>();
        for (String category : CATEGORY_ORDER) {
            byCategory.put(category, new ArrayList<>());
        }
        
        for (String commandName : dispatcher.getCommandNames()) {
            ICommand command = dispatcher.getCommand(commandName);
            String category = command.getCategory();
            byCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(commandName);
        }
        
        for (List<String> commands : byCategory.values()) {
            Collections.sort(commands);
        }
        
        printHeader();
        
        for (Map.Entry<String, List<String>> entry : byCategory.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            
            System.out.println("  " + ColorUtils.helpCategory(entry.getKey()));
            
            for (String commandName : entry.getValue()) {
                ICommand command = dispatcher.getCommand(commandName);
                String paddedName = String.format("%-14s", commandName);
                System.out.println("    " + ColorUtils.helpCommand(paddedName) + 
                    " " + ColorUtils.helpDescription(command.getDescription()));
                
                if (commandName.equals("create")) {
                    System.out.println("    " + " ".repeat(14) + " " + 
                        ColorUtils.dim("Optional: --status, --priority, --deadline"));
                    System.out.println("    " + " ".repeat(14) + " " + 
                        ColorUtils.dim("(defaults: NEW, MEDIUM, none)"));
                }
            }
            System.out.println();
        }
        
        printDetailedUsage(byCategory);
        printTips();
    }
    
    private void printHeader() {
        String title = "JAVA TASK TRACKER - HELP";
        
        System.out.println();
        System.out.println(ColorUtils.helpTitle(LINE));
        System.out.println(ColorUtils.helpTitle(ColorUtils.center(ColorUtils.BOLD + title + ColorUtils.RESET, WIDTH)));
        System.out.println(ColorUtils.helpTitle(LINE));
        System.out.println();
    }
    
    private void printDetailedUsage(Map<String, List<String>> byCategory) {
        System.out.println("  " + ColorUtils.helpCategory("DETAILED USAGE"));
        System.out.println();

        for (List<String> commands : byCategory.values()) {
            for (String commandName : commands) {
                ICommand command = dispatcher.getCommand(commandName);
                System.out.println("  " + ColorUtils.helpCommand(commandName));
                String usage = command.getUsage();
                if (commandName.equals("create")) {
                    System.out.println("    " + ColorUtils.helpUsage("create --title \"Task name\" --desc \"Description\" --assignee \"Name\""));
                    System.out.println("    " + ColorUtils.helpUsage("Optional: --status NEW|IN_PROGRESS|DONE --priority HIGH|MEDIUM|LOW --deadline YYYY-MM-DD"));
                } else {
                    System.out.println("    " + ColorUtils.helpUsage(usage));
                }
                System.out.println();
            }
        }
    }
    
    private void printTips() {
        System.out.println("  " + ColorUtils.helpCategory("TIPS"));
        System.out.println("    " + ColorUtils.helpDescription("  Press UP/DOWN to navigate command history"));
        System.out.println("    " + ColorUtils.helpDescription("  Use quotes for values with spaces: --title \"My Task\""));
        System.out.println("    " + ColorUtils.helpDescription("  IDs can be shortened: b8921e19 instead of full UUID"));
        System.out.println();
    }
    
    @Override
    public String getDescription() {
        return "Show this help message";
    }
    
    @Override
    public String getUsage() {
        return "help";
    }
    
    @Override
    public String getCategory() {
        return "UTILITY";
    }
}