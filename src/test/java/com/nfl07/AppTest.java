package com.nfl07;

import com.nfl07.model.Todo;
import com.nfl07.service.TodoService;
import com.nfl07.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

  private TodoService todoService;

  @BeforeEach
  void setUp() {
    Storage testStorage = new Storage("jdbc:h2:mem:minimal_test;DB_CLOSE_DELAY=-1");
    testStorage.connect();

    todoService = new TodoService(testStorage);
  }

  @Test
  void getAndAdd() {
    todoService.create("learn java");

    List<Todo> list = todoService.load();

    assertEquals(1, list.size(), "there is only one item in memory supposedly");
    assertEquals("learn java", list.get(0).getTitle());
    assertFalse(list.get(0).getCompleted(), "new todo must be not completed (false)");
  }
}
