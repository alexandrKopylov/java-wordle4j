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
                "квота", "злюка", "лапка", "накал", "лежак", "мялка"
        );
        dictionary = new WordleDictionary(wordList,log);
    }

   // WordleDictionary Test

    @Test
     void testRandomWord_returnsValidWord() {
        String result = dictionary.randomWord();
        assertTrue(dictionary.getWords().contains(result));
        assertFalse(dictionary.getWordsPlayerEntered().isEmpty());
    }

//    @Test
//    void testRandomWord_doesNotReturnDuplicate() {
//        // Дано
//        String firstWord = wordleDictionary.randomWord();
//
//        // Когда
//        String secondWord = wordleDictionary.randomWord();
//
//        // Тогда
//        assertNotEquals(firstWord, secondWord);
//    }
//
//    @Test
//    void testRandomWord_allWordsUsed() {
//        // Используем все слова
//        for (String word : testWords) {
//            wordleDictionary.randomWord();
//        }
//
//        // Проверяем, что все слова были использованы
//        assertEquals(testWords.size(), wordleDictionary.getWordsPlayerEntered().size());
//    }
//
//    @Test
//    void testRandomWord_emptyList() {
//        // Дано
//        WordleDictionary emptyDict = new WordleDictionary(new ArrayList<>(), mockLog);
//
//        // Тогда
//        Exception exception = assertThrows(NoSuchElementException.class, () -> {
//            emptyDict.randomWord();
//        });
//
//        assertTrue(exception.getMessage().contains("Список слов пуст"));
//    }
//
//    @Test
//    void testRandomWord_logging() {
//        // Когда
//        wordleDictionary.randomWord();
//
//        // Тогда
//        verify(mockLog, atLeastOnce()).println(Mockito.contains("Выбрано слово:"));
//    }
//}
//Объяснение тестов:
//testRandomWord_returnsValidWord
//
//Проверяет, что возвращаемое слово есть в списке
//
//Убеждается, что слово добавлено в список использованных
//
//testRandomWord_doesNotReturnDuplicate
//
//Проверяет, что метод не возвращает одно и то же слово дважды
//
//testRandomWord_allWordsUsed
//
//Имитирует использование всех слов
//
//Проверяет корректность подсчета использованных слов
//
//        testRandomWord_emptyList
//
//Тестирует обработку пустого списка слов
//
//Проверяет выброс исключения
//
//        testRandomWord_logging
//
//Проверяет корректность логирования
//
//Использует Mockito для проверки вызовов метода log
//

}
