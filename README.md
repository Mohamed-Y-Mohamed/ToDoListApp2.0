# To-Do List Application

This Java-based To-Do List application allows users to create, edit, delete, and manage tasks with additional security features. The application uses JavaFX for the user interface and includes persistent storage with encryption to ensure data security.

## Features

- **Add Task:** Users can add tasks with a title, description, and due date.
- **Edit Task:** Users can edit existing tasks.
- **Delete Task:** Users can delete tasks.
- **Persistent Storage:** Tasks are saved to a file and loaded when the application is launched.
- **Data Security:** Tasks are encrypted before being saved to the file.

## Requirements

- Java 11 or higher
- JavaFX 11 or higher

## How to Run

1. **Download or Clone the Repository:**
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Navigate to the `out` Folder:**
   The pre-built application can be found in the `out` folder of the Java project.

3. **Run the Application:**
    Open a terminal or command prompt and navigate to the `out` folder, then run the following command:
    ```sh
    java -jar ToDoListApp.jar
    ```

    Ensure that the `tasks.ser` and `key.ser` files (if they exist) are located in the same directory as the `ToDoListApp.jar` file to enable proper loading and saving of tasks.

## Notes

- The application will save tasks to a file named `tasks.ser` in the same directory as the executable jar file.
- The encryption key is stored in a file named `key.ser` in the same directory as the executable jar file.
- Ensure that the `tasks.ser` file is not empty to load tasks upon application startup.

## Troubleshooting

- **File Not Found Exception:**
  Ensure that the `tasks.ser` and `key.ser` files are in the correct directory and have the necessary read/write permissions.

- **Decryption Errors:**
  If you encounter decryption errors, ensure that the encryption key (`key.ser`) matches the key used to encrypt the tasks.

For further issues, please check the console output for detailed error messages.

## License

This project is licensed under the MIT License. See the LICENSE file for details.


app 
