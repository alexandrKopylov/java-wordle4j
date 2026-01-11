package ru.yandex.practicum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {


    public static void main(String[] args) {
        int numberLettersInWord = 5;
        Path pathFileDictionary = Paths.get("words_ru.txt");
        Path pathLog = Paths.get("log.txt");
        try {
            Files.createFile(pathLog);
        } catch (IOException e) {
            System.out.println("не могу создать файл log.txt");

        }

        WordleDictionaryLoader wdl = new WordleDictionaryLoader(numberLettersInWord, pathFileDictionary, pathLog);
        //List<String> list = wdl.read() ;
        WordleDictionary wordleDictionary = new WordleDictionary( wdl.read() , pathLog );
        WordleGame wordleGame = new WordleGame(pathLog, wordleDictionary);
        wordleGame.run();


    }


}
