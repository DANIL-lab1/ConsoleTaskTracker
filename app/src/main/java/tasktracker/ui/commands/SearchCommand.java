package tasktracker.ui.commands;

import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.TablePrinter;
import java.util.List;

public class SearchCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            System.out.println("  Error: Search keyword required.");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        StringBuilder keywordBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) keywordBuilder.append(" ");
            keywordBuilder.append(args[i]);
        }
        
        String keyword = keywordBuilder.toString()
            .replaceAll("^\"|\"$", "")
            .trim();
        
        if (keyword.isEmpty()) {
            System.out.println("  Error: Search keyword cannot be empty.");
            return;
        }
        
        List<Task> tasks = service.searchTasks(keyword);
        
        if (tasks.isEmpty()) {
            System.out.println("  No tasks found containing '" + keyword + "'");
            return;
        }
        
        System.out.println("  Found " + tasks.size() + " task(s) containing '" + keyword + "':");
        TablePrinter.printTasks(tasks);
    }
    
    @Override
    public String getDescription() {
        return "Search tasks by keyword";
    }
    
    @Override
    public String getUsage() {
        return "search \"keyword\"";
    }
}