package tasktracker.ui;

import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;
import tasktracker.ui.commands.*;
import java.util.*;

public class CommandDispatcher {
    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandDispatcher() {
        registerCommand("create", new CreateCommand());
        registerCommand("list", new ListCommand());
        registerCommand("update", new UpdateCommand());
        registerCommand("delete", new DeleteCommand());
        registerCommand("undo", new UndoCommand());
        registerCommand("desc", new DescCommand());
        registerCommand("assign", new AssignCommand());
        registerCommand("search", new SearchCommand());
        registerCommand("filter", new FilterCommand());
        registerCommand("stats", new StatsCommand());
        registerCommand("clear", new ClearCommand());
        registerCommand("help", new HelpCommand(this));
        registerCommand("exit", new ExitCommand());
    }

    private void registerCommand(String name, ICommand command) {
        commands.put(name.toLowerCase(), command);
    }

    public void execute(String input, TaskService service) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();

        ICommand command = commands.get(commandName);

        if (command == null) {
        String suggestion = findClosestCommand(commandName);
        ColorUtils.printError("Unknown command: '" + commandName + "'");
        if (suggestion != null) {
            System.out.println(ColorUtils.warning("TIP: Did you mean '" + suggestion + "'?"));
        } else {
            System.out.println(ColorUtils.dim("Type 'help' for available commands."));
        }
        return;
    }

        try {
            command.execute(parts, service);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ICommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    public Set<String> getCommandNames() {
        return commands.keySet();
    }

    private String findClosestCommand(String input) {
        String bestMatch = null;
        int bestDistance = Integer.MAX_VALUE;
        int threshold = 3;

        for (String cmd : commands.keySet()) {
            int distance = levenshteinDistance(input, cmd);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestMatch = cmd;
            }
        }

        return (bestDistance <= threshold) ? bestMatch : null;
    }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(
                        Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1),
                        dp[i-1][j-1] + cost
                    );
                }
            }
        }

        return dp[a.length()][b.length()];
    }
}