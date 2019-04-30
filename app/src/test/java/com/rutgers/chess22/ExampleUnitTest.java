package com.rutgers.chess22;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import model.game.Game;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static final String FILEPATH = "testgame.txt";

    public static void main(String[] args) throws IOException {
        Game game = new Game();
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}