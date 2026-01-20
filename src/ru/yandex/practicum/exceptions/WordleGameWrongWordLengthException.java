package ru.yandex.practicum.exceptions;

public class WordleGameWrongWordLengthException extends WordleGameException {
    public WordleGameWrongWordLengthException(String message) {
        super(message);
    }
}
