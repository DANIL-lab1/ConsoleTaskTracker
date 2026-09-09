package tasktracker.ui;

import tasktracker.model.Task;
import tasktracker.model.Status;
import java.util.List;

public class TablePrinter {
    
    private static final int ID_WIDTH = 36;
    private static final int TITLE_WIDTH = 30;
    private static final int STATUS_WIDTH = 12;
    private static final int ASSIGNEE_WIDTH = 12;
    
    private static final String SEPARATOR = "+" + "-".repeat(ID_WIDTH + 2) +
                                            "+" + "-".repeat(TITLE_WIDTH + 2) +
                                            "+" + "-".repeat(STATUS_WIDTH + 2) +
                                            "+" + "-".repeat(ASSIGNEE_WIDTH + 2) + "+";
    
    private static final String HEADER_FORMAT = "| %-" + ID_WIDTH + "s | %-" + TITLE_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + ASSIGNEE_WIDTH + "s |\n";
    private static final String ROW_FORMAT = "| %-" + ID_WIDTH + "s | %-" + TITLE_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + ASSIGNEE_WIDTH + "s |\n";
    
    public static void printTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("  No tasks to display.");
            return;
        }
        
        System.out.println(SEPARATOR);
        System.out.printf(HEADER_FORMAT, "ID", "Title", "Status", "Assignee");
        System.out.println(SEPARATOR);
        
        for (Task task : tasks) {
            String id = task.getId().toString().substring(0, 8) + "...";
            String title = truncate(task.getTitle(), TITLE_WIDTH);
            String status = formatStatus(task.getStatus());
            String assignee = task.getAssignee() != null ? task.getAssignee().getName() : "-";
            
            System.out.printf(ROW_FORMAT, id, title, status, assignee);
        }
        
        System.out.println(SEPARATOR);
    }
    
    private static String formatStatus(Status status) {
        if (status == null) return "-";
        switch (status) {
            case NEW: return "NEW";
            case IN_PROGRESS: return "IN PROGRESS";
            case DONE: return "DONE";
            default: return status.toString();
        }
    }
    
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}