package ru.yandex.practicum;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
    private Path pathLog;
    private String answer = "^^^^^";
    private String tryingGuessWord;
    private String previousTryingGuessWord;
    private int steps = 0;
    private WordleDictionary dictionary;
    private Random random = new Random();
    private Scanner scanner = new Scanner(System.in);
    private List<String> wordsPlayerEntered = new ArrayList<>();


    public WordleGame(Path pathLog, WordleDictionary dictionary) {
        this.pathLog = pathLog;
        this.dictionary = dictionary;
    }

    public void run() {
        mysteriousWord = guessWord();
        System.out.println("Компьютер загадал слово из 5 букв " + mysteriousWord);
        System.out.println("Чтобы отгадать слово у Вас есть 6 попыток.");
        boolean computerWin = false;
        boolean playerWin = false;

        while (!computerWin && !playerWin) {
            ++steps;
            boolean validWord = false;

            while (!validWord) {
                System.out.println();
                System.out.println(steps + "-я  попытка, введите слово  (ENTER - подсказка компьютера, exit - выход из игры)  ");
                tryingGuessWord = scanner.nextLine();

                if (tryingGuessWord.equalsIgnoreCase("exit")) {
                    System.out.println("Выход");
                    System.exit(0);
                } else if (tryingGuessWord.isEmpty()) {
                    tryingGuessWord = wordComputer();
                    System.out.println(tryingGuessWord);
                }
                validWord = checkValidWord();
            }

            playerWin = checkWordPlayer();
            if (playerWin) {
                System.out.println("Вы угадали слово");
            } else {
                System.out.println(answer);
            }
            if (steps == 6) {
                computerWin = true;
                System.out.println("Попытки закончились  :( ");
            }
            previousTryingGuessWord = tryingGuessWord;
            wordsPlayerEntered.add(tryingGuessWord);

        }

        if (playerWin) {
            System.out.println("\nВы выиграли !!!");
        } else {
            System.out.println("\nВы не угодали слово за 6 попыток.");
            System.out.println("загаданное слово было -> " + mysteriousWord);
        }
    }

    private boolean checkValidWord() {
        if (tryingGuessWord.length() != 5) {
            System.out.println("слово должно быть 5-ти букв");
            return false;
        }
        return true;
    }

    private String wordComputer() {
        List<String> newListWord = new ArrayList<>();
        String word;
        if (answer.equals("^^^^^") || answer.equals("-----")) {
            word = guessWord();
        } else {
            for (int i = 0; i < dictionary.getWords().size(); i++) {
                if (answer.charAt(0) == '+') {
                    char simbolDictionaryWord = dictionary.getWords().get(i).charAt(0);
                    char simbolMysteriousWord = mysteriousWord.charAt(0);
                    if (simbolDictionaryWord != simbolMysteriousWord) {
                        continue;
                    }
                }
                if (answer.charAt(1) == '+') {
                    char simbolDictionaryWord = dictionary.getWords().get(i).charAt(1);
                    char simbolMysteriousWord = mysteriousWord.charAt(1);
                    if (simbolDictionaryWord != simbolMysteriousWord) {
                        continue;
                    }
                }

                if (answer.charAt(2) == '+') {
                    char simbolDictionaryWord = dictionary.getWords().get(i).charAt(2);
                    char simbolMysteriousWord = mysteriousWord.charAt(2);
                    if (simbolDictionaryWord != simbolMysteriousWord) {
                        continue;
                    }
                }

                if (answer.charAt(3) == '+') {
                    char simbolDictionaryWord = dictionary.getWords().get(i).charAt(3);
                    char simbolMysteriousWord = mysteriousWord.charAt(3);
                    if (simbolDictionaryWord != simbolMysteriousWord) {
                        continue;
                    }
                }
                if (answer.charAt(4) == '+') {
                    char simbolDictionaryWord = dictionary.getWords().get(i).charAt(4);
                    char simbolMysteriousWord = mysteriousWord.charAt(4);
                    if (simbolDictionaryWord != simbolMysteriousWord) {
                        continue;
                    }
                }
                if (answer.charAt(0) == '^') {
                    if (!dictionary.getWords().get(i).contains(String.valueOf(previousTryingGuessWord.charAt(0)))) {
                        continue;
                    }
                }
                if (answer.charAt(1) == '^') {
                    if (!dictionary.getWords().get(i).contains(String.valueOf(previousTryingGuessWord.charAt(1)))) {
                        continue;
                    }
                }
                if (answer.charAt(2) == '^') {
                    if (!dictionary.getWords().get(i).contains(String.valueOf(previousTryingGuessWord.charAt(2)))) {
                        continue;
                    }
                }
                if (answer.charAt(3) == '^') {
                    if (!dictionary.getWords().get(i).contains(String.valueOf(previousTryingGuessWord.charAt(3)))) {
                        continue;
                    }
                }
                if (answer.charAt(4) == '^') {
                    if (!dictionary.getWords().get(i).contains(String.valueOf(previousTryingGuessWord.charAt(4)))) {
                        continue;
                    }
                }
                String str = dictionary.getWords().get(i);
                newListWord.add(str);
            }
            dictionary.setWords(newListWord);
            System.out.println("список слов = " + newListWord.size());
            word = guessWord();
        }
        return word;
    }

    private boolean checkWordPlayer() {
        if (tryingGuessWord.equals(mysteriousWord)) {
            return true;
        }
        answer = checkEachLetter();
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
            }
        }
        return String.valueOf(chars);
    }

    private String guessWord() {
        String word;
        int size = dictionary.getWords().size();
        while (true) {
            int indexWord = random.nextInt(0, size );
            word = dictionary.getWords().get(indexWord);
            if (!wordsPlayerEntered.contains(word)){
                break;
            }
        }
        return word;
    }
}
