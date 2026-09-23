# Java Task Tracker (v0.5)

A console-based task management application built in Java.

## Features
- Create, update, delete tasks
- Assign tasks to people
- Filter overdue tasks (`filter --overdue`)
- Sorting (`list --sort date|title|status|priority|deadline|assignee`)
- Undo last deleted task
- Delete confirmation before removing
- Search tasks by keyword
- Task statistics with progress bars (`stats`)
- Colored output (like Git)
- Smart suggestions for typos (`Did you mean 'create'?`)
- JSON persistence (`tasks.json`)
- Autosave after every command
- `desc <id>` command for full task details
- Dynamic table width

## Technologies
- Java 26
- Gradle
- Jackson (JSON serialization)
- File-based storage (`tasks.json`)
- ANSI colors for console output
- Stream API for data processing

## How to run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew clean run --console=plain
```

## Version 0.5 UPDATE!

## Features

- Logging (SLF4J + Logback)
- Unit tests (JUnit 5, 45 tests)
- Code coverage (JaCoCo)

## Technologies

- SLF4J + Logback (logging)
- JUnit 5 (testing)
- JaCoCo (code coverage)

## Bugs

Now each command has a recognizable color - red for ERRORs, orange for WARN and yellow for INFO. You can try it!
