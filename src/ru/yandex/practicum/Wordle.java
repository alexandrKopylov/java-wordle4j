package ru.yandex.practicum;

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
            System.out.println( "Ошибка IOException. " + e.getMessage());
            log.println("Ошибка IOException. " + e.getMessage());
        } catch (DictionaryException e) {
            System.out.println( e.getMessage());
            log.println(e.getMessage());
        } catch (Exception e) {
            System.out.println( e.getMessage());
            log.println( e.getMessage());
        } finally {
            if (log != null) {
                log.close();
            }
        }
    }


    public void run(WordleGame game, PrintWriter log) {

            game.setMysteriousWord(game.getDictionary().randomWord());
            log.println("Компьютер загадал слово из 5 букв -> " + game.getMysteriousWord());
            System.out.println("Компьютер загадал слово из 5 букв ");
            System.out.println("Чтобы отгадать слово у Вас есть 6 попыток.");
            boolean computerWin = false;
            boolean playerWin = false;

            while (!computerWin && !playerWin) {
                ++steps;
                boolean validWord = false;

                while (!validWord) {
                    System.out.println();
                    System.out.println(steps + "-я  попытка, введите слово  (ENTER - подсказка компьютера, exit - выход из игры)  ");
                    log.println();
                    log.println(steps + "-я  попытка ");
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
                        log.println("проверка слова игрока на валидность -> " + game.getTryingGuessWord());
                    } catch (InputExeption e) {
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
                System.out.println("загаданное слово было -> " + game.getMysteriousWord());
                log.println("игрок не угадал слово -> " + game.getMysteriousWord());
            }


    }

}
