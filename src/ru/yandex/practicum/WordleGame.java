package ru.yandex.practicum;

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
    private Path pathLog;
    private String answerTemplate = "^^^^^";
    private String tryingGuessWord;
    private String previousTryingGuessWord;
    private int steps = 0;
    private WordleDictionary dictionary;
    private Random random = new Random();
    private Scanner scanner = new Scanner(System.in);
    private List<String> wordsPlayerEntered = new ArrayList<>();
    private Set<Character> charactersNoInMysteriousWord = new HashSet<>();


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
                    System.out.println("(подсказка)");
                    System.out.println(tryingGuessWord);
                }
                validWord = checkValidWord();
            }

            playerWin = checkPlayerWin();
            if (playerWin) {
                System.out.println("Вы угадали слово");
            } else {
                System.out.println(answerTemplate);
            }
            if (steps == 6 && !playerWin) {
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
            System.out.println("слово должно быть из 5-ти букв");
            return false;
        }
        if (tryingGuessWord.matches(".*[a-zA-Z].*")) {
            System.out.println("в слове не должно быть английских букв");
            return false;
        }
        if (!checkWordInDictionary()) {
            System.out.println("слово должно быть из словаря");
            return false;
        }
        if (!checkWordtemplate(answerTemplate, tryingGuessWord, previousTryingGuessWord)) {
            System.out.println("слово должно соответсвовать предыдущему шаблону " + answerTemplate);
            return false;
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


    private String wordComputer() {
        List<String> newListWord = new ArrayList<>();
        String word;
        if (answerTemplate.equals("^^^^^")) {
            word = guessWord();
        } else {
            for (int i = 0; i < dictionary.getWords().size(); i++) {
                if (checkWordtemplate(answerTemplate, dictionary.getWords().get(i), previousTryingGuessWord)) {
                    newListWord.add(dictionary.getWords().get(i));
                }
            }
            dictionary.setWords(newListWord);
            // System.out.println("список слов = " + newListWord.size());
            word = guessWord();
        }
        return word;
    }


    private boolean checkPlayerWin() {
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
        return String.valueOf(chars);
    }

    private String guessWord() {
        String word;
        int size = dictionary.getWords().size();
        while (true) {
            int indexWord = random.nextInt(0, size);
            word = dictionary.getWords().get(indexWord);
            if (!wordsPlayerEntered.contains(word)) {
                break;
            }
        }
        return word;
    }
}
