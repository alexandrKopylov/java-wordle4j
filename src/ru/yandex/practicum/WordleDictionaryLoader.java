package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private final int numberLettersInWord;
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log, int numberLettersInWord) {
        this.numberLettersInWord = numberLettersInWord;
        this.log = log;
    }

    public WordleDictionary load(String file) throws IOException {
        log.println(String.format("Загрузка словаря из  %s", file));
        List<String> words = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == numberLettersInWord) {
                    words.add(line.toLowerCase().replace("ё", "е"));
                }
            }
        }
        log.println(String.format("Cловарь из 5 букв, размер = %s слов", words.size()));
        if (words.isEmpty()) {
            throw new DictionaryException("Словарь  из 5 букв пуст");
        }
        return new WordleDictionary(words, log);
    }
}
