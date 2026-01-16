package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;
    private PrintWriter log;
    private Random random = new Random();
    private List<String> wordsPlayerEntered = new ArrayList<>();

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.words = words;
        this.log = log;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<String> getWordsPlayerEntered() {
        return wordsPlayerEntered;
    }

    public String randomWord() {
        String word;
        int size = words.size();
        while (true) {
            int indexWord = random.nextInt(0, size);
            word = words.get(indexWord);
            if (!wordsPlayerEntered.contains(word)) {
                break;
            }
        }
        return word;
    }
}
