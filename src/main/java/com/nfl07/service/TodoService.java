package com.nfl07.service;

import com.nfl07.model.Todo;
import com.nfl07.storage.Storage;
import java.util.List;

public class TodoService {
  Storage database;

  public TodoService(Storage database) {
    this.database = database;
  }

  public String create(String title) {
    if (title.isBlank()) {
      return "Title must be filled!";
    } else {
      Todo newTodo = new Todo(title);
      database.save(newTodo.title, false);
      return "Tode created";
    }
  }

  public List<Todo> load() {
    return database.getAllTasks();
  }

  public void dataInit() {
    database.init();
  }

  public void completeById(int id) {
    id += 1;
    if (database.isEmpty()) {
      return;
    } else {
      if (database.getCompleted(id)) {
        database.updateComplete(false, id);
      } else {
        database.updateComplete(true, id);
      }
    }
  }

  public void deleteById(int id) {
    id += 1;
    if (database.isEmpty()) {
      return;
    } else {
      database.delete(id);
    }
  }
}
