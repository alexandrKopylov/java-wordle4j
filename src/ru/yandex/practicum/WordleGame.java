package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordleEmptyCandidatesException;
import ru.yandex.practicum.exceptions.WordleGameNoSuchWordException;
import ru.yandex.practicum.exceptions.WordleGameWrongWordException;
import ru.yandex.practicum.exceptions.WordleGameWrongWordLengthException;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String mysteriousWord;
    private final PrintWriter log;
    private String answerTemplate = "^^^^^";
    private String tryingGuessWord;
    private String previousTryingGuessWord;
    private WordleDictionary dictionary;
    private Set<Character> charactersNoInMysteriousWord = new HashSet<>();


    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
    }

    public String getMysteriousWord() {
        return mysteriousWord;
    }

    public void setCharactersNoInMysteriousWord(Set<Character> charactersNoInMysteriousWord) {
        this.charactersNoInMysteriousWord = charactersNoInMysteriousWord;
    }

    public Set<Character> getCharactersNoInMysteriousWord() {
        return charactersNoInMysteriousWord;
    }

    public void setAnswerTemplate(String answerTemplate) {
        this.answerTemplate = answerTemplate;
    }

    public void setMysteriousWord(String mysteriousWord) {
        this.mysteriousWord = mysteriousWord;
    }

    public String getAnswerTemplate() {
        return answerTemplate;
    }

    public String getTryingGuessWord() {
        return tryingGuessWord;
    }

    public void setTryingGuessWord(String tryingGuessWord) {
        this.tryingGuessWord = tryingGuessWord;
    }

    public void setPreviousTryingGuessWord(String previousTryingGuessWord) {
        this.previousTryingGuessWord = previousTryingGuessWord;
    }

    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public boolean checkValidWord() throws WordleGameWrongWordLengthException,
            WordleGameWrongWordException,
            WordleGameNoSuchWordException,
            WordleEmptyCandidatesException {
        if (tryingGuessWord.length() != 5) {
            throw new WordleGameWrongWordLengthException("Ошибка: слово должно быть из 5-ти букв");
        }
        if (tryingGuessWord.matches(".*[a-zA-Z].*")) {
            throw new WordleGameWrongWordException("Ошибка: в слове не должно быть английских букв");
        }
        if (!checkWordInDictionary()) {
            throw new WordleGameNoSuchWordException("Ошибка: слово должно быть из словаря");
        }
        if (!checkWordtemplate(answerTemplate, tryingGuessWord, previousTryingGuessWord)) {
            throw new WordleEmptyCandidatesException(String.format("Ошибка: слово должно соответсвовать предыдущему шаблону %s", answerTemplate));
        }
        return true;
    }

    public boolean checkWordInDictionary() {
        return dictionary.getWords().contains(tryingGuessWord);
    }

    public boolean checkWordtemplate(String answerTemplate, String tryingGuessWord, String previousTryingGuessWord) {
        if (answerTemplate.equals("^^^^^")) {
            return true;
        }
        for (int i = 0; i < answerTemplate.length(); i++) {
            if (answerTemplate.charAt(i) == '+') {
                char simbolTryingGuessWord = tryingGuessWord.charAt(i);
                char simbolPreviousTryingGuessWord = previousTryingGuessWord.charAt(i);
                if (simbolTryingGuessWord != simbolPreviousTryingGuessWord) {
                    return false;
                }
            } else if (answerTemplate.charAt(i) == '^') {
                if (!tryingGuessWord.contains(String.valueOf(previousTryingGuessWord.charAt(i)))) {
                    return false;
                }
            }
        }

        for (Character simbol : charactersNoInMysteriousWord) {
            if (tryingGuessWord.contains(String.valueOf(simbol))) {
                return false;
            }
        }
        return true;
    }

    public String hintWordComputer() {
        List<String> newListWord = new ArrayList<>();
        String word;
        if (answerTemplate.equals("^^^^^")) {
            word = dictionary.randomWord();
        } else {
            for (int i = 0; i < dictionary.getWords().size(); i++) {
                if (checkWordtemplate(answerTemplate, dictionary.getWords().get(i), previousTryingGuessWord)) {
                    newListWord.add(dictionary.getWords().get(i));
                }
            }
            dictionary.setWords(newListWord);
            log.println(String.format("список слов в словаре после фильтрции = %d", newListWord.size()));
            word = dictionary.randomWord();
        }
        return word;
    }

    public boolean checkPlayerWin() {
        if (tryingGuessWord.equals(mysteriousWord)) {
            return true;
        }
        answerTemplate = checkEachLetter();
        return false;
    }

    public String checkEachLetter() {
        char[] arrayChar = tryingGuessWord.toCharArray();
        char[] chars = new char[arrayChar.length];
        for (int i = 0; i < arrayChar.length; i++) {
            if (arrayChar[i] == mysteriousWord.charAt(i)) {
                chars[i] = '+';
            } else if (mysteriousWord.contains(String.valueOf(tryingGuessWord.charAt(i)))) {
                chars[i] = '^';
            } else {
                chars[i] = '-';
                charactersNoInMysteriousWord.add(arrayChar[i]);
            }
        }
        log.println("список букв которых не должно быть в слове:");
        log.println(charactersNoInMysteriousWord);
        return String.valueOf(chars);
    }
}
