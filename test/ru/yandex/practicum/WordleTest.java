package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.WordNotFoundException;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void makeGame() {
        log = new PrintWriter(System.out, true);
        List<String> words = Arrays.asList("дождь", "мотор", "амеба");
        dictionary = new WordleDictionary(words, log);
    }

    @DisplayName("Проверка создания новой игры")
    @Test
    void testCreateNewGame() {
        game = new WordleGame(dictionary, log);
        assertNotNull(game.getAnswer());
        assertEquals(6, game.getSteps());
        assertFalse(game.isGameOver());
        assertFalse(game.isWon());
    }

    @DisplayName("Проверка форматирования слов")
    @Test
    void testNormalize() {
        assertEquals("мотор", WordleDictionary.normalizeWord("МОТОР"));
        assertEquals("амеба", WordleDictionary.normalizeWord("Амёба"));
        assertEquals("лодка", WordleDictionary.normalizeWord("  лодка   "));
    }

    @DisplayName("Проверка словаря")
    @Test
    void testContainsWord() {
        assertTrue(dictionary.containsWord("мотор"));
        assertTrue(dictionary.containsWord("АМЁБА"));
        assertFalse(dictionary.containsWord("лодка"));
    }

    @DisplayName("Проверка создания исключения при отсутствии слова в словаре")
    @Test
    void testWordNotFoundException() {
        game = new WordleGame(dictionary, log, "мотор");
        assertThrows(WordNotFoundException.class, () -> {
            game.makeGuess("арбуз");
        });
    }

    @DisplayName("Проверка совпадений букв")
    @Test
    void testcompareWords() {
        assertEquals("+++++", WordleDictionary.compareWords("мотор", "мотор"));
        assertEquals("-+^++", WordleDictionary.compareWords("мотор", "топор"));
        assertEquals("-----", WordleDictionary.compareWords("абвгд", "ежзик"));
    }

    @DisplayName("Проверка верного предположения")
    @Test
    void testCorrectGuess() throws InvalidWordException, WordNotFoundException {
        game = new WordleGame(dictionary, log, "мотор");
        String result = game.makeGuess("мотор");
        assertEquals("+++++", result);
        assertTrue(game.isWon());
        assertTrue(game.isGameOver());
        assertEquals(5, game.getSteps());
    }
    @DisplayName("Проверка неверного предположения")
    @Test
    void testWrongGuess() throws InvalidWordException, WordNotFoundException {
        game = new WordleGame(dictionary, log, "мотор");
        String result = game.makeGuess("дождь");
        assertNotEquals("+++++", result);
        assertFalse(game.isWon());
        assertFalse(game.isGameOver());
        assertEquals(5, game.getSteps());
    }

    @DisplayName("Проверка окончания игры после траты всех попыток")
    @Test
    void testGameOverAfterSixAttempts() throws InvalidWordException, WordNotFoundException {
        game = new WordleGame(dictionary, log, "мотор");
        for (int i = 0; i < 6; i++) {
            game.makeGuess("дождь");
            if (i < 5) {
                assertFalse(game.isGameOver(), "Игра должна продолжаться после " + (i + 1) + " попытки");
            } else {
                assertTrue(game.isGameOver(), "Игра должна окончиться после 6 попыток");
                assertFalse(game.isWon(), "Нельзя победить после траты всех попыток");
            }
        }
        assertEquals(0, game.getSteps());
    }

    @DisplayName("Проверка счетчика попыток")
    @Test
    void testGetHint() throws WordNotFoundException {
        game = new WordleGame(dictionary, log, "мотор");
        assertEquals(5, game.getCountHints());
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(4, game.getCountHints());
    }
}
