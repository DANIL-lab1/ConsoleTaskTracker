package tasktracker.ui.commands;

import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.TablePrinter;
import java.util.List;

import tasktracker.ui.ArgumentParser;
import java.util.Comparator;
import java.util.Map;
import tasktracker.util.ColorUtils;

public class ListCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        List<Task> tasks = service.getAllTasks();
        
        // Parsing sort option
        Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
        String sortBy = ArgumentParser.getValue(parsedArgs, "sort", "none");
        
        String header;
        switch (sortBy.toLowerCase()) {
            case "date":
                tasks.sort(Comparator.comparing(Task::getCreatedAt).reversed());
                header = "All tasks (sorted by date, newest first):";
                break;
            case "title":
                tasks.sort(Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER));
                header = "All tasks (sorted by title):";
                break;
            case "status":
                tasks.sort(Comparator.comparing(Task::getStatus));
                header = "All tasks (sorted by status):";
                break;
            case "priority":
                tasks.sort(Comparator.comparing(Task::getPriority));
                header = "All tasks (sorted by priority, HIGH first):";
                break;
            case "deadline":
                tasks.sort(Comparator.comparing(
                    task -> task.getDeadline() != null ? task.getDeadline() : java.time.LocalDateTime.MAX,
                    Comparator.naturalOrder()
                ));
                header = "All tasks (sorted by deadline, soonest first):";
                break;
            case "assignee":
                tasks.sort(Comparator.comparing(
                    task -> task.getAssignee() != null ? task.getAssignee().getName() : "ZZZ",
                    String.CASE_INSENSITIVE_ORDER
                ));
                header = "All tasks (sorted by assignee):";
                break;
            default:
                header = "All tasks (" + tasks.size() + "):";
        }
        
        System.out.println(header);
        
        if (tasks.isEmpty()) {
            ColorUtils.printInfo("No tasks found.");
            return;
        }
        
        TablePrinter.printTasks(tasks);
    }
    
    @Override
    public String getDescription() {
        return "List all tasks (with optional --sort)";
    }
    
    @Override
    public String getUsage() {
        return "list [--sort date|title|status|assignee]";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}