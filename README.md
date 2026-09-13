# Java Task Tracker (v0.2)

A console-based task management application built in Java.

## Features
- Create, update, delete tasks
- Assign tasks to people
- Filter tasks by status (NEW, IN_PROGRESS, DONE)
- Search tasks by keyword
- Data persistence via InMemoryRepository

## Technologies
- Java 26
- Gradle
- InMemory data storage

## How to run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew clean run --console=plain
```

## version 0.2 UPDATE!

## Features
- Task statistics with progress bars (`stats`)
- Colored output (like Git)
- Smart suggestions for typos (`Did you mean 'create'?`)

## Technologies

- ANSI colors for console output
- Stream API for data processing
