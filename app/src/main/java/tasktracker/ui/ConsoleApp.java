package tasktracker.ui;

import tasktracker.service.TaskService;
import java.util.Scanner;

public class ConsoleApp {
    private final TaskService service;
    private final CommandDispatcher dispatcher;
    private final Scanner scanner;
    private boolean running;
    
    public ConsoleApp(TaskService service){
        this.service = service;
        this.dispatcher = new CommandDispatcher();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start(){
        printWelcome();
        
        while (running){
            System.out.print("task-cli> ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit")){
                running = false;
            }
            
            dispatcher.execute(input, service);
        }
        
        System.out.println("Goodbye!");
        scanner.close();
    }
    
    private void printWelcome(){
        System.out.println("========================================");
        System.out.println("  JAVA TASK TRACKER   ");
        System.out.println("========================================");
        System.out.println("Type 'help' for available commands.");
        System.out.println("========================================\n");
    }
}
