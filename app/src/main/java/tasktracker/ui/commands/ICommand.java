package tasktracker.ui.commands;

import tasktracker.service.TaskService;

public interface ICommand {
    void execute(String args[], TaskService service);
    String getDescription();
    String getUsage();
}
