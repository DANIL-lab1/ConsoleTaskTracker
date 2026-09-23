package tasktracker.ui;

import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;
import java.util.Scanner;

public class ConsoleApp {
    
    private static final int WELCOME_WIDTH = 45;
    private static final String WELCOME_LINE = "=".repeat(WELCOME_WIDTH);
    
    private final TaskService service;
    private final CommandDispatcher dispatcher;
    private final Scanner scanner;
    private boolean running;
    
    public ConsoleApp(TaskService service) {
        this.service = service;
        this.dispatcher = new CommandDispatcher();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        printWelcome();
        
        while (running) {
            System.out.print(ColorUtils.CYAN + "task-cli> " + ColorUtils.RESET);
            
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                    break;
                }
                
                if (!input.isEmpty()) {
                    dispatcher.execute(input, service);
                }
            } else {
                System.out.println(ColorUtils.warning("No input available. Exiting..."));
                break;
            }
        }
        
        System.out.println(ColorUtils.info("Goodbye!"));
        scanner.close();
    }
    
    private void printWelcome() {
        String title = "JAVA TASK TRACKER";
        
        System.out.println(ColorUtils.cyan(WELCOME_LINE));
        System.out.println(ColorUtils.cyan(ColorUtils.center(ColorUtils.BOLD + title + ColorUtils.RESET, WELCOME_WIDTH)));
        System.out.println(ColorUtils.cyan(WELCOME_LINE));
        System.out.println(ColorUtils.center("Type " + ColorUtils.bold("'help'") + " for available commands.", WELCOME_WIDTH));
        System.out.println(ColorUtils.cyan(WELCOME_LINE));
        System.out.println();
    }
}