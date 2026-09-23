package tasktracker.ui.commands;

import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.TablePrinter;
import java.util.List;
import tasktracker.util.ColorUtils;

public class SearchCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            ColorUtils.printError("ERROR: Search keyword required.");
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
            ColorUtils.printError("ERROR: Search keyword cannot be empty.");
            return;
        }
        
        List<Task> tasks = service.searchTasks(keyword);
        
        System.out.println("Search results for '" + keyword + "' (" + tasks.size() + "):");
        
        if (tasks.isEmpty()) {
            ColorUtils.printInfo("No tasks found.");
            return;
        }
        
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
    
    @Override
    public String getCategory() {
        return "SEARCH & FILTER";
    }
}