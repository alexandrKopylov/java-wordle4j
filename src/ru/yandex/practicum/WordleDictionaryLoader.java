package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private  int numberLettersInWord;
    private  PrintWriter log;

    public WordleDictionaryLoader( PrintWriter log , int numberLettersInWord) {
        this.numberLettersInWord = numberLettersInWord;
        this.log = log;
    }

    public WordleDictionary load(String file) throws IOException {
        log.println("Загрузка словаря из " + file);
        List<String> words = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == numberLettersInWord ) {
                    words.add(line.toLowerCase().replace("ё","е"));
                }
            }
        }
        log.println("Cловарь из 5 букв, размер = " + words.size() + " слов");

        if (words.isEmpty()) {
            throw new DictionaryException("Словарь  из 5 букв пуст");
        }
        return new WordleDictionary(words, log);
    }
}
