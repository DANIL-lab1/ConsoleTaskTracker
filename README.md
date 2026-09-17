# Java Task Tracker (v0.3)

A console-based task management application built in Java.

## Features
- Create, update, delete tasks
- Assign tasks to people
- Filter tasks by status (NEW, IN_PROGRESS, DONE)
- Search tasks by keyword
- Data persistence via InMemoryRepository
- Task statistics with progress bars (`stats`)
- Colored output (like Git)
- Smart suggestions for typos (`Did you mean 'create'?`)

## Technologies
- Java 26
- Gradle
- InMemory data storage
- ANSI colors for console output
- Stream API for data processing

## How to run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew clean run --console=plain
```

## Version 0.3 UPDATE!

## Features

# New fields for creating tasks:
- Task priorities (`HIGH`, `MEDIUM`, `LOW`)
- Deadlines (`--deadline YYYY-MM-DD`)

# New commands to use:
- Filter overdue tasks (`filter --overdue`)
- Sorting (`list --sort date|title|status|priority|deadline|assignee`)
- Undo last deleted task
- Delete confirmation before removing
