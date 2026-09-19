package tasktracker.ui;

import tasktracker.model.Priority;
import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.util.ColorUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TablePrinter {
    
    private static final int MAX_ID_WIDTH = 40;
    private static final int MAX_TITLE_WIDTH = 30;
    private static final int MAX_STATUS_WIDTH = 12;
    private static final int MAX_PRIORITY_WIDTH = 10;
    private static final int MAX_DEADLINE_WIDTH = 12;
    private static final int MAX_ASSIGNEE_WIDTH = 20;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void printTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            ColorUtils.printInfo("No tasks to display.");
            return;
        }
        
        List<String> ids = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> statuses = new ArrayList<>();
        List<String> priorities = new ArrayList<>();
        List<String> deadlines = new ArrayList<>();
        List<String> assignees = new ArrayList<>();
        
        for (Task task : tasks) {
            ids.add(task.getId().toString());
            titles.add(task.getTitle() != null ? task.getTitle() : "");
            statuses.add(formatStatusPlain(task.getStatus()));
            priorities.add(task.getPriority() != null ? task.getPriority().toString() : "-");
            deadlines.add(formatDeadlinePlain(task));
            assignees.add(task.getAssignee() != null ? task.getAssignee().getName() : "-");
        }
        
        int idWidth = calcWidth("ID", ids, MAX_ID_WIDTH);
        int titleWidth = calcWidth("Title", titles, MAX_TITLE_WIDTH);
        int statusWidth = calcWidth("Status", statuses, MAX_STATUS_WIDTH);
        int priorityWidth = calcWidth("Priority", priorities, MAX_PRIORITY_WIDTH);
        int deadlineWidth = calcWidth("Deadline", deadlines, MAX_DEADLINE_WIDTH);
        int assigneeWidth = calcWidth("Assignee", assignees, MAX_ASSIGNEE_WIDTH);
        
        String separator = "+" +
            "-".repeat(idWidth + 2) + "+" +
            "-".repeat(titleWidth + 2) + "+" +
            "-".repeat(statusWidth + 2) + "+" +
            "-".repeat(priorityWidth + 2) + "+" +
            "-".repeat(deadlineWidth + 2) + "+" +
            "-".repeat(assigneeWidth + 2) + "+";
        
        System.out.println(ColorUtils.gray(separator));
        System.out.printf("| %-" + idWidth + "s | %-" + titleWidth + "s | %-" + statusWidth + "s | %-" + priorityWidth + "s | %-" + deadlineWidth + "s | %-" + assigneeWidth + "s |\n",
            "ID", "Title", "Status", "Priority", "Deadline", "Assignee");
        System.out.println(ColorUtils.gray(separator));
        
        for (Task task : tasks) {
            String id = task.getId().toString();
            String title = truncate(task.getTitle(), titleWidth);
            String status = formatStatus(task.getStatus());
            String priority = formatPriority(task.getPriority());
            String deadline = formatDeadline(task);
            String assignee = task.getAssignee() != null 
                ? truncate(task.getAssignee().getName(), assigneeWidth) 
                : "-";
            
            System.out.println("| " + padColored(ColorUtils.gray(id), idWidth) +
                " | " + String.format("%-" + titleWidth + "s", title) +
                " | " + padColored(status, statusWidth) +
                " | " + padColored(priority, priorityWidth) +
                " | " + padColored(deadline, deadlineWidth) +
                " | " + String.format("%-" + assigneeWidth + "s", assignee) + " |");
        }
        
        System.out.println(ColorUtils.gray(separator));
    }
    
    // ===== Helpers =====
    
    private static int calcWidth(String header, List<String> values, int maxWidth) {
        int max = header.length();
        for (String value : values) {
            if (value != null && value.length() > max) {
                max = value.length();
            }
        }
        return Math.min(max, maxWidth);
    }
    
    private static String formatStatusPlain(Status status) {
        if (status == null) return "-";
        switch (status) {
            case NEW: return "NEW";
            case IN_PROGRESS: return "IN PROGRESS";
            case DONE: return "DONE";
            default: return status.toString();
        }
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
    
    private static String formatDeadlinePlain(Task task) {
        if (task.getDeadline() == null) return "-";
        return task.getDeadline().format(DATE_FORMAT);
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