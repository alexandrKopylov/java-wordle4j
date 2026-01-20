package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryException;
import ru.yandex.practicum.exceptions.WordleGameException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
    private static final String DICTIONARY = "words_ru.txt";
    private static final String LOG = "log.txt";
    private static final int numberLettersInWord = 5;
    private Scanner scanner = new Scanner(System.in);
    private int steps = 0;

    public static void main(String[] args) {
        PrintWriter log = null;
        try {
            log = new PrintWriter(LOG, StandardCharsets.UTF_8);
            WordleDictionaryLoader loader = new WordleDictionaryLoader(log, numberLettersInWord);
            WordleDictionary dictionary = loader.load(DICTIONARY);
            WordleGame game = new WordleGame(dictionary, log);
            Wordle wordle = new Wordle();
            wordle.run(game, log);
        } catch (IOException e) {
            System.out.println(String.format("Ошибка IOException. %s", e.getMessage()));
            log.println(String.format("Ошибка IOException. %s", e.getMessage()));
        } catch (DictionaryException e) {
            System.out.println(e.getMessage());
            log.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.println(e.getMessage());
        } finally {
            if (log != null) {
                log.close();
            }
        }
    }


    public void run(WordleGame game, PrintWriter log) {

        game.setMysteriousWord(game.getDictionary().randomWord());
        log.println(String.format("загаданное слово  -> %s", game.getMysteriousWord()));
        System.out.println("Компьютер загадал слово из 5 букв ");
        System.out.println("Чтобы отгадать слово у Вас есть 6 попыток.");
        boolean computerWin = false;
        boolean playerWin = false;

        while (!computerWin && !playerWin) {
            ++steps;
            boolean validWord = false;
            StringBuilder sb = new StringBuilder();
            while (!validWord) {
                System.out.println();
                sb.setLength(0);
                sb.append(steps);
                sb.append("-я  попытка, введите слово  (ENTER - подсказка компьютера, exit - выход из игры)  ");

                System.out.println(sb.toString());
                log.println();
                log.println(sb.toString());
                game.setTryingGuessWord(scanner.nextLine());

                if (game.getTryingGuessWord().equalsIgnoreCase("exit")) {
                    System.out.println("Выход");
                    log.println("игрок выбрал exit ");
                    System.exit(0);
                } else if (game.getTryingGuessWord().isEmpty()) {
                    game.setTryingGuessWord(game.hintWordComputer());
                    System.out.println("(подсказка)");
                    log.println("игрок выбрал подсказку  ");
                    System.out.println(game.getTryingGuessWord());
                }
                try {
                    validWord = game.checkValidWord();
                    sb.setLength(0);
                    sb.append("проверка слова игрока на валидность -> ");
                    sb.append(game.getTryingGuessWord());
                    log.println(sb.toString());
                } catch (WordleGameException e) {
                    log.println(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }

            playerWin = game.checkPlayerWin();
            if (playerWin) {
                System.out.println("Вы угадали слово");
                log.println("игрок угадал слово");
            } else {
                System.out.println(game.getAnswerTemplate());
                log.println("результат угадывания :");
                log.println(game.getTryingGuessWord());
                log.println(game.getAnswerTemplate());
            }
            if (steps == 6 && !playerWin) {
                computerWin = true;
                System.out.println("Попытки закончились  :( ");
                log.println("Попытки закончились  :( ");
            }
            game.setPreviousTryingGuessWord(game.getTryingGuessWord());
            game.getDictionary().getWordsPlayerEntered().add(game.getTryingGuessWord());
        }

        if (playerWin) {
            System.out.println("\nВы выиграли !!!");
            log.println("игрок выиграл");
        } else {
            System.out.println("\nВы не угодали слово за 6 попыток.");
            System.out.println(String.format("загаданное слово было -> %s", game.getMysteriousWord()));
            log.println(String.format("игрок не угадал слово -> %s", game.getMysteriousWord()));
        }
    }
}
