package com.nfl07;

import static dev.tamboui.toolkit.Toolkit.*;

import com.nfl07.service.TodoService;
import com.nfl07.storage.Storage;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.widgets.input.TextInputState;
import dev.tamboui.style.Color;
import java.util.List;
import java.util.stream.Collectors;

public class App extends ToolkitApp {
  TextInputState inputState = new TextInputState();
  TodoService service;
  private ListElement<?> myList = list().id("list");

  public App(TodoService service) {
    this.service = service;
    myList.items(loadData());
  }

  private List<String> loadData() {
    return service.load().stream()
        .map(todo -> (todo.getCompleted() ? "[x] " : "[ ] ") + todo.getTitle())
        .collect(Collectors.toList());
  }

  @Override
  protected Element render() {
    return column(
        myList
            .fill()
            .focusable()
            .onKeyEvent(
                event -> {
                  if (event.isChar('a')) {
                    service.completeById(myList.selected());
                    myList.items(loadData());
                    return EventResult.HANDLED;
                  }
                  if (event.isChar('d')) {
                    service.deleteById(myList.selected());
                    myList.items(loadData());
                    return EventResult.HANDLED;
                  }
                  return EventResult.UNHANDLED;
                }),
        textInput(inputState)
            .id("input")
            .rounded()
            .borderColor(Color.WHITE)
            .focusedBorderColor(Color.CYAN)
            .focusable()
            .placeholder("Enter title...")
            .onSubmit(
                () -> {
                  service.create(inputState.text());
                  myList.items(loadData());
                  inputState.clear();
                }));
  }

  public static void main(String[] args) throws Exception {
    Storage database = new Storage();
    // Server webServer = Server.createWebServer("-web", "-webAllowOthers",
    // "-webPort", "7777").start();
    database.connect();

    TodoService service = new TodoService(database);
    new App(service).run();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      database.close();
    }));
    while (true) {
      Thread.sleep(1000);
    }
  }
}
