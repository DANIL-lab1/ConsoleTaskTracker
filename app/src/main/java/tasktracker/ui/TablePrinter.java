package tasktracker.ui;

import tasktracker.model.Priority;
import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.util.ColorUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TablePrinter {
    
    private static final int ID_WIDTH = 36;
    private static final int TITLE_WIDTH = 25;
    private static final int STATUS_WIDTH = 12;
    private static final int PRIORITY_WIDTH = 8;
    private static final int DEADLINE_WIDTH = 12;
    private static final int ASSIGNEE_WIDTH = 12;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final String SEPARATOR = 
        "+" + "-".repeat(ID_WIDTH + 2) +
        "+" + "-".repeat(TITLE_WIDTH + 2) +
        "+" + "-".repeat(STATUS_WIDTH + 2) +
        "+" + "-".repeat(PRIORITY_WIDTH + 2) +
        "+" + "-".repeat(DEADLINE_WIDTH + 2) +
        "+" + "-".repeat(ASSIGNEE_WIDTH + 2) + "+";
    
    public static void printTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            ColorUtils.printInfo("No tasks to display.");
            return;
        }
        
        // Header
        System.out.println(ColorUtils.gray(SEPARATOR));
        System.out.printf("| %-" + ID_WIDTH + "s | %-" + TITLE_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + PRIORITY_WIDTH + "s | %-" + DEADLINE_WIDTH + "s | %-" + ASSIGNEE_WIDTH + "s |\n",
            "ID", "Title", "Status", "Priority", "Deadline", "Assignee");
        System.out.println(ColorUtils.gray(SEPARATOR));
        
        for (Task task : tasks) {
            String id = task.getId().toString();
            String title = truncate(task.getTitle(), TITLE_WIDTH);
            String status = formatStatus(task.getStatus());
            String priority = formatPriority(task.getPriority());
            String deadline = formatDeadline(task);
            String assignee = task.getAssignee() != null 
                ? truncate(task.getAssignee().getName(), ASSIGNEE_WIDTH) 
                : "-";
            
            System.out.println("| " + padColored(ColorUtils.gray(id), ID_WIDTH) +
                " | " + String.format("%-" + TITLE_WIDTH + "s", title) +
                " | " + padColored(status, STATUS_WIDTH) +
                " | " + padColored(priority, PRIORITY_WIDTH) +
                " | " + padColored(deadline, DEADLINE_WIDTH) +
                " | " + String.format("%-" + ASSIGNEE_WIDTH + "s", assignee) + " |");
        }
        
        System.out.println(ColorUtils.gray(SEPARATOR));
    }
    
    private static String formatStatus(Status status) {
        if (status == null) return "-";
        switch (status) {
            case NEW: return ColorUtils.BLUE + "NEW" + ColorUtils.RESET;
            case IN_PROGRESS: return ColorUtils.ORANGE + "IN PROGRESS" + ColorUtils.RESET;
            case DONE: return ColorUtils.GREEN + "DONE" + ColorUtils.RESET;
            default: return status.toString();
        }
    }
    
    private static String formatPriority(Priority priority) {
        if (priority == null) return "-";
        switch (priority) {
            case HIGH: return ColorUtils.RED + "HIGH" + ColorUtils.RESET;
            case MEDIUM: return ColorUtils.ORANGE + "MEDIUM" + ColorUtils.RESET;
            case LOW: return ColorUtils.GRAY + "LOW" + ColorUtils.RESET;
            default: return priority.toString();
        }
    }
    
    private static String formatDeadline(Task task) {
        if (task.getDeadline() == null) return "-";
        
        String dateStr = task.getDeadline().format(DATE_FORMAT);
        
        if (task.isOverdue() && task.getStatus() != Status.DONE) {
            return ColorUtils.RED + dateStr + ColorUtils.RESET;
        } else if (task.getStatus() == Status.DONE) {
            return ColorUtils.GREEN + dateStr + ColorUtils.RESET;
        }
        return dateStr;
    }
    
    private static String padColored(String coloredText, int width) {
        String plainText = ColorUtils.stripAnsi(coloredText);
        int padding = width - plainText.length();
        if (padding < 0) padding = 0;
        return coloredText + " ".repeat(padding);
    }
    
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}