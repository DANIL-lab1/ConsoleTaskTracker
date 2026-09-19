# Java Task Tracker (v0.4)

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

## Technologies
- Java 26
- Gradle
- **Jackson** (JSON serialization)
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

## Version 0.4 UPDATE!

## Features

- JSON persistence (`tasks.json`)
- Autosave after every command
- `desc <id>` command for full task details
- Dynamic table width

## Technologies

Now saving in file is available with json-files format.
