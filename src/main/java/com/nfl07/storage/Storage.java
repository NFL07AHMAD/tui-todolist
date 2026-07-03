package com.nfl07.storage;

import com.nfl07.model.Todo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class Storage {
  String dbUrl;
  Connection connection;

  public Storage() {
    Properties prop = new Properties();
    try (InputStream input = Storage.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        throw new RuntimeException("Maaf, config.properties tidak ditemukan!");
      }
      prop.load(input);
      // Ambil URL yang sudah disuntikkan oleh Maven
      this.dbUrl = prop.getProperty("database.url");
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public Storage(String url) {
    dbUrl = url;
  }

  public void connect() {
    try {
      // 1. Membuat koneksi ke H2
      this.connection = DriverManager.getConnection(dbUrl);
      System.out.println("Koneksi ke H2 Database berhasil.");
      // 2. Langsung panggil metode init setelah koneksi siap
      init();

    } catch (SQLException e) {
      System.err.println("Gagal mengoneksikan atau menginisialisasi database!");
      e.printStackTrace();
    }
  }

  public void init() {
    try (Statement stmt = this.connection.createStatement()) {
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS tasks (id INTEGER AUTO_INCREMENT PRIMARY KEY, title"
              + " VARCHAR(255) NOT NULL, completed BOOLEAN NOT NULL)");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void save(String title, boolean completed) {
    try {
      PreparedStatement ps = this.connection.prepareStatement("INSERT INTO tasks (title, completed) VALUES (?, ?)");
      ps.setString(1, title);
      ps.setBoolean(2, completed);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateComplete(boolean completed, int id) {
    try {
      PreparedStatement ps = this.connection.prepareStatement("UPDATE tasks SET completed = ? WHERE id = ?");
      ps.setBoolean(1, completed);
      ps.setInt(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Todo> get() {
    List<Todo> todos = new ArrayList<>();

    try {
      PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM tasks");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Todo todo = new Todo(rs.getInt("id"), rs.getString("title"), rs.getBoolean("completed"));

        todos.add(todo);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return todos;
  }

  public boolean getCompleted(int id) {
    boolean completed = false;

    try {
      PreparedStatement ps = connection.prepareStatement("SELECT completed FROM tasks WHERE id = ?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        completed = rs.getBoolean("completed");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return completed;
  }

  public boolean isEmpty() {
    boolean exist = true;

    try {
      PreparedStatement stmt = connection.prepareStatement("SELECT EXISTS (SELECT 1 FROM tasks)");
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        // Ingat pelajaran sebelumnya, rs.getBoolean otomatis membaca nilai
        // boolean/bit/tinyint
        boolean adaData = rs.getBoolean(1);

        if (!adaData) {
          exist = true;
        } else {
          exist = false;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return exist;
  }

  public void close() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
        System.out.println("\nDatabase H2 berhasil diputus dengan aman.");
      }
    } catch (SQLException e) {
      System.err.println("Gagal menutup koneksi database!");
      e.printStackTrace();
    }
  }
}
