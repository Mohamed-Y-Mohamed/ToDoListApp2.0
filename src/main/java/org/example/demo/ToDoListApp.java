package org.example.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.Task;
import org.example.demo.TaskStorage;

import java.time.LocalDate;
import java.util.List;

public class ToDoListApp extends Application {

    private ListView<Task> listView;
    private ObservableList<Task> taskList;

    @Override
    public void start(Stage primaryStage) {
        taskList = FXCollections.observableArrayList(TaskStorage.loadTasks());
        listView = new ListView<>(taskList);

        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Task Description");

        DatePicker dueDatePicker = new DatePicker(LocalDate.now());

        Button addButton = new Button("Add Task");
        addButton.setOnAction(e -> addTask(titleField, descriptionArea, dueDatePicker));

        Button editButton = new Button("Edit Task");
        editButton.setOnAction(e -> editTask());

        Button deleteButton = new Button("Delete Task");
        deleteButton.setOnAction(e -> deleteTask());

        HBox inputBox = new HBox(10, titleField, dueDatePicker, addButton, editButton, deleteButton);
        VBox layout = new VBox(10, inputBox, descriptionArea, listView);
        layout.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List Application");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> TaskStorage.saveTasks(taskList));
    }

    private void addTask(TextField titleField, TextArea descriptionArea, DatePicker dueDatePicker) {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        if (!title.isEmpty() && dueDate != null) {
            Task task = new Task(title, description, dueDate);
            taskList.add(task);
            titleField.clear();
            descriptionArea.clear();
            dueDatePicker.setValue(LocalDate.now());
        }
    }

    private void editTask() {
        Task selectedTask = listView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TextInputDialog dialog = new TextInputDialog(selectedTask.getTitle());
            dialog.setTitle("Edit Task");
            dialog.setHeaderText("Edit Task Title");
            dialog.setContentText("New title:");

            dialog.showAndWait().ifPresent(selectedTask::setTitle);

            TextArea descriptionArea = new TextArea(selectedTask.getDescription());
            descriptionArea.setPromptText("Edit Description");
            descriptionArea.setPrefRowCount(3);

            Dialog<ButtonType> descriptionDialog = new Dialog<>();
            descriptionDialog.setTitle("Edit Description");
            descriptionDialog.getDialogPane().setContent(descriptionArea);
            descriptionDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            descriptionDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedTask.setDescription(descriptionArea.getText());
                }
            });

            DatePicker dueDatePicker = new DatePicker(selectedTask.getDueDate());

            Dialog<ButtonType> dueDateDialog = new Dialog<>();
            dueDateDialog.setTitle("Edit Due Date");
            dueDateDialog.getDialogPane().setContent(dueDatePicker);
            dueDateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dueDateDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedTask.setDueDate(dueDatePicker.getValue());
                }
            });

            listView.refresh();
        }
    }

    private void deleteTask() {
        Task selectedTask = listView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
        }
    }

}
