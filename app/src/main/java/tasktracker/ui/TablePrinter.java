package tasktracker.ui;

import tasktracker.model.Task;
import tasktracker.model.Status;
import tasktracker.util.ColorUtils;
import java.util.List;

public class TablePrinter {
    
    private static final int ID_WIDTH = 36;
    private static final int TITLE_WIDTH = 30;
    private static final int STATUS_WIDTH = 12;
    private static final int ASSIGNEE_WIDTH = 15;
    
    private static final String SEPARATOR = "+" + "-".repeat(ID_WIDTH + 2) +
                                            "+" + "-".repeat(TITLE_WIDTH + 2) +
                                            "+" + "-".repeat(STATUS_WIDTH + 2) +
                                            "+" + "-".repeat(ASSIGNEE_WIDTH + 2) + "+";
    
    private static final String HEADER_FORMAT = "| %-" + ID_WIDTH + "s | %-" + TITLE_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + ASSIGNEE_WIDTH + "s |\n";
    private static final String ROW_FORMAT = "| %-" + ID_WIDTH + "s | %-" + TITLE_WIDTH + "s | %-" + STATUS_WIDTH + "s | %-" + ASSIGNEE_WIDTH + "s |\n";
    
    public static void printTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            ColorUtils.printInfo("No tasks to display.");
            return;
        }
        
        System.out.println(ColorUtils.gray(SEPARATOR));
        System.out.printf(ColorUtils.bold(HEADER_FORMAT), "ID", "Title", "Status", "Assignee");
        System.out.println(ColorUtils.gray(SEPARATOR));
        
        for (Task task : tasks) {
            String id = task.getId().toString();
            String title = truncate(task.getTitle(), TITLE_WIDTH);
            String status = formatStatus(task.getStatus());
            String assignee = task.getAssignee() != null 
                ? truncate(task.getAssignee().getName(), ASSIGNEE_WIDTH) 
                : "-";
            
            String idColored = ColorUtils.gray(String.format("%-" + ID_WIDTH + "s", id));
            String titleColored = String.format("%-" + TITLE_WIDTH + "s", title);
            String statusColored = padColored(status, STATUS_WIDTH);
            String assigneeColored = String.format("%-" + ASSIGNEE_WIDTH + "s", assignee);
            
            System.out.println("| " + idColored + " | " + titleColored + " | " + statusColored + " | " + assigneeColored + " |");
        }
        
        System.out.println(ColorUtils.gray(SEPARATOR));
    }

    
    private static String formatStatus(Status status) {
        if (status == null) return "-";
        switch (status) {
            case NEW: return ColorUtils.BLUE + "NEW" + ColorUtils.RESET;
            case IN_PROGRESS: return ColorUtils.YELLOW + "IN PROGRESS" + ColorUtils.RESET;
            case DONE: return ColorUtils.GREEN + "DONE" + ColorUtils.RESET;
            default: return status.toString();
        }
    }
    
    private static String padColored(String coloredText, int width) {
        String plainText = coloredText.replaceAll("\u001B\\[[;\\d]*m", "");
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