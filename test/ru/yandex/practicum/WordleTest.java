package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private static PrintWriter log;
    private WordleDictionary dictionary;

    @BeforeAll
    static void loggingStart() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void settingDictionary() {
        List<String> wordList = Arrays.asList(
                "бобер", "кошка", "какао", "лимон", "груша", "пилон"
        );
        dictionary = new WordleDictionary(wordList,log);
    }

    //WordleDictionary Test
//
//    @Test
//    void testNormalize(){
//        assertEquals("бобер", WordleDictionary.normalize("БОБЁР"));
//        assertEquals("бобер", WordleDictionary.normalize("бобёр"));
//        assertEquals("лимон", WordleDictionary.normalize("Лимон"));
//        assertEquals("лимон", WordleDictionary.normalize("лимон"));
//    }
//
//    @Test
//    void testContainsWord() {
//        assertTrue(dictionary.contains("бобер"));
//        assertTrue(dictionary.contains("БОБЁР"));
//        assertTrue(dictionary.contains("Лимон"));
//        assertTrue(dictionary.contains("лимон"));
//    }
}
