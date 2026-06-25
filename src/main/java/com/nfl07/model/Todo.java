package com.nfl07.model;

public class Todo {
  public String title;
  public boolean completed;
  public int id;

  public Todo(String title) {
    this.title = title;
    this.completed = false;
  }

  public Todo(int id, String title, boolean completed) {
    this.title = title;
    this.id = id;
    this.completed = completed;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setCompleted() {
    if (this.completed) {
      this.completed = false;
    } else {
      this.completed = true;
    }
  }

  public String getTitle() {
    return this.title;
  }

  public boolean getCompleted() {
    return this.completed;
  }
}
