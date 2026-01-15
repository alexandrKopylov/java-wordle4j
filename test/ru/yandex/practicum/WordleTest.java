package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private  PrintWriter log;
    private WordleDictionary dictionary;
    private  WordleGame game;
    private StringWriter stringWriter;


    @BeforeEach
    void settingGame() {

        // Настраиваем логгер для проверки вывода
        stringWriter = new StringWriter();
        log = new PrintWriter(stringWriter);

        List<String> wordList = Arrays.asList(
                "квота", "злюка", "лапка", "накал", "лежак", "мялка"
        );
        dictionary = new WordleDictionary(wordList,log);
        game = new WordleGame(dictionary, log);
        game.setMysteriousWord("лежак");
    }

   // WordleDictionary
    @Test
     void testRandomWordReturnsValidWord() {
        String result = dictionary.randomWord();
        assertTrue(dictionary.getWords().contains(result));
    }

    // WordleGame
    @Test
    void testCheckEachLetter_ExactMatch() {
        game.setTryingGuessWord("лежак");
        String result = game.checkEachLetter();
        assertEquals("+++++", result);
        assertTrue(stringWriter.toString().contains("список букв которых не должно быть в слове:"));
    }
    @Test
    void testCheckEachLetter_LetterInWrongPosition() {
        game.setTryingGuessWord("лояка");
        String result = game.checkEachLetter();
        //лояка
        //+--^^
        assertEquals("+--^^", result);
        assertTrue(game.getCharactersNoInMysteriousWord().contains('о'));
        assertTrue(game.getCharactersNoInMysteriousWord().contains('я'));
       assertEquals(2,game.getCharactersNoInMysteriousWord().size());
    }
    @Test
    void testCheckEachLetter_NoMatch() {
        game.setTryingGuessWord("вишня");

        String result = game.checkEachLetter();
        assertEquals("-----", result);
        assertTrue(game.getCharactersNoInMysteriousWord().containsAll(
                Arrays.asList('в', 'и', 'ш', 'н', 'я')));
    }


    @Test
    void testСheckPlayerWin_ExactMatch() {
        game.setTryingGuessWord("лежак");
        boolean result = game.checkPlayerWin();
        assertEquals(true, result);
    }

    @Test
    void testСheckPlayerWin_NoMatch() {
        game.setTryingGuessWord("злюка");
        boolean result = game.checkPlayerWin();
        assertEquals(false, result);
    }
    @Test
    void testHintWordComputer_FirstTry() {
        game.setAnswerTemplate("^^^^^");
        String hint = game.hintWordComputer();
        assertTrue(dictionary.getWords().contains(hint));
    }
    @Test
    void testHintWordComputer_SecondTry() {
        //лежак
      //  "квота", "злюка", "лапка", "накал", "лежак", "мялка"
        game.setAnswerTemplate("+----");
        game.setPreviousTryingGuessWord("лотус");
        String hint = game.hintWordComputer();
        assertEquals(2, dictionary.getWords().size());
        assertTrue(dictionary.getWords().containsAll(
                Arrays.asList("лапка", "лежак")));
    }

    @Test
   void testCheckWordtemplate_FirstTry () {
        assertTrue(game.checkWordtemplate("^^^^^", "злюка", null));
    }
//    @Test
//    void testCheckWordtemplate_SecondTry () {
//        assertTrue(game.checkWordtemplate("^^^^^", "злюка", null));
//    }

}
