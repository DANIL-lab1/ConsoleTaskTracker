package tasktracker.ui;

import tasktracker.service.TaskService;
import tasktracker.ui.commands.*;
import java.util.*;

public class CommandDispatcher {
    private final Map<String, ICommand> commands = new HashMap<>();
    
    public CommandDispatcher(){
        registerCommand("create", new CreateCommand());
        registerCommand("list", new ListCommand());
        registerCommand("update", new UpdateCommand());
        registerCommand("delete", new DeleteCommand());
        registerCommand("assign", new AssignCommand());
        registerCommand("search", new SearchCommand());
        registerCommand("filter", new FilterCommand());
        registerCommand("clear", new ClearCommand());
        registerCommand("help", new HelpCommand(this));
        registerCommand("exit", new ExitCommand());
    }
    
    private void registerCommand(String name, ICommand command){
        commands.put(name.toLowerCase(), command);
    }
    
    public void execute(String input, TaskService service){
        if (input == null || input.trim().isEmpty()){}
        
        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();
        
        ICommand command = commands.get(commandName);
        
        if (command == null){
            System.out.println("Unknown command: '" + commandName + "'");
            System.out.println("Type 'help' for available commands.");
        }
        
        try {
            command.execute(parts, service);
        } catch (Exception e){
            System.out.println("Error executing command: " + e.getMessage());
        }
    }
    
    public ICommand getCommand(String name){
        return commands.get(name.toLowerCase());
    }
    
    public Set<String> getCommandNames(){
        return commands.keySet();
    }
}
