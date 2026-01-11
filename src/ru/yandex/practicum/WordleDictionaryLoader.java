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
    private final int numberLettersInWord;
    private final Path pathFileDictionary;
    private final Path pathLog;

    public WordleDictionaryLoader(int numberLettersInWord, Path pathFileDictionary, Path pathLog) {
        this.numberLettersInWord = numberLettersInWord;
        this.pathFileDictionary = pathFileDictionary;
        this.pathLog = pathLog;
    }

    public List<String> read() {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(pathFileDictionary.toFile()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == numberLettersInWord ) {
                    result.add(line.toLowerCase().replace("ё","е"));
                }
            }
        } catch (IOException e) {
            try (FileWriter fileWriter = new FileWriter(pathLog.toFile()); ){
                fileWriter.write("Произошла ошибка во время чтения файла.\n");
                fileWriter.write (e.getMessage());

            } catch (IOException ex) {
                System.out.println("не получилось записать log в log-файл");
            }
        }
        return result;
    }


}
