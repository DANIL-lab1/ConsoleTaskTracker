package tasktracker.ui.commands;

import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import tasktracker.ui.TablePrinter;
import tasktracker.util.ColorUtils;

import java.util.List;
import java.util.Map;

public class FilterCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
        
        String statusStr = ArgumentParser.getValue(parsedArgs, "status");
        String assigneeName = ArgumentParser.getValue(parsedArgs, "assignee");
        boolean overdue = ArgumentParser.hasFlag(parsedArgs, "overdue");
        
        List<Task> tasks;
        String header;
        
        // ===== Filter by overdue =====
        if (overdue) {
            tasks = service.getOverdueTasks();
            header = "Overdue tasks (" + tasks.size() + "):";
        }
        // ===== Filter by status =====
        else if (statusStr != null && !statusStr.isEmpty()) {
            Status status;
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                ColorUtils.printError("Invalid status. Use: NEW, IN_PROGRESS, or DONE");
                return;
            }
            tasks = service.getTasksByStatus(status);
            header = "Tasks with status '" + status + "' (" + tasks.size() + "):";
        }
        // ===== Filter by assignee =====
        else if (assigneeName != null && !assigneeName.isEmpty()) {
            tasks = service.getAllTasks().stream()
                .filter(task -> task.getAssignee() != null &&
                    task.getAssignee().getName().equalsIgnoreCase(assigneeName))
                .toList();
            header = "Tasks assigned to '" + assigneeName + "' (" + tasks.size() + "):";
        }
        // ===== No filter =====
        else {
            ColorUtils.printError("Specify --status, --assignee, or --overdue");
            System.out.println("Usage: " + getUsage());
            return;
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
        return "Filter tasks by status, assignee, or overdue";
    }
    
    @Override
    public String getUsage() {
        return "filter [--status NEW|IN_PROGRESS|DONE] [--assignee \"Name\"] [--overdue]";
    }
    
    @Override
    public String getCategory() {
        return "SEARCH & FILTER";
    }
}