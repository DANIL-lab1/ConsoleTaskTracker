package tasktracker.ui.commands;

import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsCommand implements ICommand{
    private static final int WIDTH = 50;
    private static final String LINE = "=".repeat(WIDTH);
    private static final int BAR_LENGTH = 10;
    
    @Override 
    public void execute(String[] args, TaskService service){
        List<Task> tasks = service.getAllTasks();
        
        if (tasks.isEmpty()){
            ColorUtils.printInfo("No tasks to show statistics.");
            return;
        }
        
        printHeader();
        printTotal(tasks);
        printByStatus(tasks);
        printByAssignee(tasks);
        //printByDate(tasks);
        printFooter();
    }
    
    private void printHeader(){
        System.out.println();
        System.out.println(ColorUtils.cyan(LINE));
        System.out.println(ColorUtils.cyan(ColorUtils.center(ColorUtils.BOLD + "TASK STATISTICS" + ColorUtils.RESET, WIDTH)));
        System.out.println(ColorUtils.cyan(LINE));
        System.out.println();
    }  
    
    private void printTotal(List<Task> tasks) {
        System.out.println("  Total tasks:     " + ColorUtils.bold(String.valueOf(tasks.size())));
        System.out.println();
    }
    
    private void printByStatus(List<Task> tasks) {
        System.out.println("  " + ColorUtils.bold("By Status:"));

        long total = tasks.size();
        Map<Status, Long> byStatus = tasks.stream()
            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        for (Status status : Status.values()) {
            long count = byStatus.getOrDefault(status, 0L);
            String bar = buildBar(count, total);
            String statusColored = ColorUtils.colorStatus(padStatus(status.toString(), 12));

            System.out.printf("    %s %s  %2d  (%2d%%)%n",
                statusColored,
                bar,
                count,
                percent(count, total)
            );
        }
        System.out.println();
    }
    
    private void printByAssignee(List<Task> tasks) {
        System.out.println("  " + ColorUtils.bold("By Assignee:"));
        
        Map<String, Long> byAssignee = tasks.stream()
            .collect(Collectors.groupingBy(
                task -> task.getAssignee() != null ? task.getAssignee().getName() : "Unassigned",
                Collectors.counting()
            ));
        
        byAssignee.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> {
                String name = entry.getKey();
                if (name.equals("Unassigned")) {
                    name = ColorUtils.dim(name);
                } else {
                    name = ColorUtils.green(name);
                }
                System.out.printf("    %-20s %d tasks%n", name, entry.getValue());
            });
        System.out.println();
    }
    
    /*private void printByDate(List<Task> tasks) {
        System.out.println("  " + ColorUtils.bold("By Date:"));
        
        LocalDate today = LocalDate.now();
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        
        long todayCount = tasks.stream()
            .filter(t -> t.getCreatedAt().toLocalDate().equals(today))
            .count();
        
        long weekCount = tasks.stream()
            .filter(t -> t.getCreatedAt().isAfter(weekAgo))
            .count();
        
        System.out.printf("    Created today:     %d%n", todayCount);
        System.out.printf("    Created this week: %d%n", weekCount);
        System.out.println();
    }*/
    
    private void printFooter() {
        System.out.println(ColorUtils.cyan(LINE));
        System.out.println();
    }
    
    private String padStatus(String status, int width) {
        if (status.length() >= width) return status;
        return status + " ".repeat(width - status.length());
    }
    
    private String buildBar(long value, long total) {
        if (total == 0) {
            return "[" + ColorUtils.gray("-".repeat(BAR_LENGTH)) + "]";
        }

        int filled = (int) Math.round((double) value / total * BAR_LENGTH);
        int empty = BAR_LENGTH - filled;

        return "[" + 
            ColorUtils.green("#".repeat(filled)) + 
            ColorUtils.gray("-".repeat(empty)) + 
            "]";
    }
    
    private long percent(long value, long total) {
        return total > 0 ? Math.round((double) value / total * 100) : 0;
    }
    
    @Override
    public String getDescription() {
        return "Show task statistics";
    }
    
    @Override
    public String getUsage() {
        return "stats";
    }
    
    @Override
    public String getCategory() {
        return "UTILITY";
    }
}
