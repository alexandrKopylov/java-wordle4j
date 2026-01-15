package ru.yandex.practicum;

import java.io.PrintWriter;
import java.nio.file.Path;
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
    private PrintWriter log;
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






    public boolean checkValidWord() throws InputExeption {
        if (tryingGuessWord.length() != 5) {
            throw  new InputExeption("Ошибка: слово должно быть из 5-ти букв");
        }
        if (tryingGuessWord.matches(".*[a-zA-Z].*")) {
            throw new InputExeption("Ошибка: в слове не должно быть английских букв");
        }
        if (!checkWordInDictionary()) {
            throw new InputExeption("Ошибка: слово должно быть из словаря");
        }
        if (!checkWordtemplate(answerTemplate, tryingGuessWord, previousTryingGuessWord)) {
            throw  new InputExeption( "Ошибка: слово должно соответсвовать предыдущему шаблону " + answerTemplate);
        }
        return true;
    }

    private boolean checkWordInDictionary() {
        return dictionary.getWords().contains(tryingGuessWord);
    }

    private boolean checkWordtemplate(String answerTemplate, String tryingGuessWord, String previousTryingGuessWord) {
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
             log.println("список слов в словаре после фильтрции = " + newListWord.size());
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

    private String checkEachLetter() {
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
        log.println("список букв которых не должно быть в слове:" );
        log.println(charactersNoInMysteriousWord);
        return String.valueOf(chars);
    }


}
