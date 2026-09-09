package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import tasktracker.ui.CommandDispatcher;

public class HelpCommand implements ICommand {
    
    private final CommandDispatcher dispatcher;
    
    public HelpCommand(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    @Override
    public void execute(String[] args, TaskService service) {
        System.out.println("\n  Available Commands:");
        System.out.println("=" .repeat(60));
        System.out.printf("%-15s | %-40s%n", "Command", "Description");
        System.out.println("-".repeat(60));
        
        for (String commandName : dispatcher.getCommandNames()) {
            ICommand command = dispatcher.getCommand(commandName);
            System.out.printf("%-15s | %-40s%n", 
                commandName, 
                command.getDescription());
        }
        
        System.out.println("\n  Detailed Usage:");
        System.out.println("=".repeat(60));
        for (String commandName : dispatcher.getCommandNames()) {
            ICommand command = dispatcher.getCommand(commandName);
            System.out.printf("%-15s | %-40s%n", 
                commandName, 
                command.getUsage());
        }
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
}