package com.nfl07;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.nfl07.service.TodoService;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCreate() {
        TodoService service = new TodoService();
        // String name = System.getProperty("name");
        // String category = System.getProperty("category");

        service.create();
    }
}
