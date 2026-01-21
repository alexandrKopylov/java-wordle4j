package ru.yandex.practicum;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;
    private StringWriter stringWriter;

    @BeforeEach
    void settingGame() {
        stringWriter = new StringWriter();
        log = new PrintWriter(stringWriter);
        List<String> wordList = Arrays.asList(
                "квота", "злюка", "лапка", "накал", "лежак", "мялка"
        );
        dictionary = new WordleDictionary(wordList, log);
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
        assertEquals(2, game.getCharactersNoInMysteriousWord().size());
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
    void testCheckPlayerWin_ExactMatch() {
        game.setTryingGuessWord("лежак");
        boolean result = game.checkPlayerWin();
        assertTrue(result);
    }

    @Test
    void testCheckPlayerWin_NoMatch() {
        game.setTryingGuessWord("злюка");
        boolean result = game.checkPlayerWin();
        assertFalse(result);
    }

    @Test
    void testHintWordComputer_FirstTry() {
        game.setAnswerTemplate("^^^^^");
        String hint = game.hintWordComputer();
        assertTrue(dictionary.getWords().contains(hint));
    }

    @Test
    void testHintWordComputer_SecondTry() {
        game.setAnswerTemplate("+----");
        game.setPreviousTryingGuessWord("лотус");

        game.hintWordComputer();
        assertEquals(2, dictionary.getWords().size());
        assertTrue(dictionary.getWords().containsAll(
                Arrays.asList("лапка", "лежак")));
    }

    @Test
    void testCheckWordtemplate_FirstTryValid() {
        assertTrue(game.checkWordtemplate("^^^^^", "злюка", null));
    }

    @Test
    void testCheckWordtemplate_SecondTryValid() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.addAll(List.of('з', 'ю'));
        game.setCharactersNoInMysteriousWord(characterSet);
        assertTrue(game.checkWordtemplate("-^-^^", "мялка", "злюка"));
    }

    @Test
    void testCheckWordtemplate_ThirdTryNoValid() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.addAll(List.of('з', 'ю', 'м', 'я'));
        game.setCharactersNoInMysteriousWord(characterSet);
        // буквы м не должно быть в слове
        assertFalse(game.checkWordtemplate("--^^^", "калым", "мялка"));
    }

    @Test
    void testCheckWordtemplate_FourthTryNoValid() {
        // буквы л  должно быть в слове в неопределенном месте
        assertFalse(game.checkWordtemplate("--^^^", "актер", "мялка"));
    }

    @Test
    void testCheckWordtemplate_FiveTryNoValid() {
        // буквы л  должно быть точно на первом буквой, буквы к а  - должны присутвовать в слове
        assertFalse(game.checkWordtemplate("+^-^^", "накал", "лапка"));
    }

    @Test
    void checkWordInDictionary_ReturnTrue() {
        game.setTryingGuessWord("мялка");
        assertTrue(game.checkWordInDictionary());
    }

    @Test
    void checkWordInDictionary_ReturnFalse() {
        game.setTryingGuessWord("бабка");
        assertFalse(game.checkWordInDictionary());
    }

    @Test
    void checkValidWord_ReturnTrue() throws WordleGameWrongWordException, WordleGameWrongWordLengthException, WordleGameNoSuchWordException, WordleEmptyCandidatesException {
        game.setTryingGuessWord("злюка");
        game.setAnswerTemplate("--^^^");
        game.setPreviousTryingGuessWord("мялка");
        assertTrue(game.checkValidWord());
    }

    @Test
    void checkValidWord_LongWordReturnWordleGameException() {
        game.setTryingGuessWord("кузнец");
        assertThrows(WordleGameException.class, () -> {
            game.checkValidWord();
        });
    }

    @Test
    void checkValidWord_WordContainsLatinSimbolReturnWordleGameException() {
        game.setTryingGuessWord("fargo");
        assertThrows(WordleGameException.class, () -> {
            game.checkValidWord();
        });
    }

    @Test
    void checkValidWord_WordNoDictionaryReturnWordleGameException() {
        game.setTryingGuessWord("бабка");
        assertThrows(WordleGameException.class, () -> {
            game.checkValidWord();
        });
    }

    @Test
    void checkValidWord_WordMatchPreviousPatternReturnWordleGameException() {
        game.setTryingGuessWord("кочка");
        game.setAnswerTemplate("--^^^");
        game.setPreviousTryingGuessWord("мялка");
        assertThrows(WordleGameException.class, () -> {
            game.checkValidWord();
        });
    }

}
