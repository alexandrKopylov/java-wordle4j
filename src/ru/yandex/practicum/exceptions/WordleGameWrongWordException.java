package ru.yandex.practicum.exceptions;

public class WordleGameWrongWordException extends WordleGameException {
    public WordleGameWrongWordException(String message) {
        super(message);
    }
}
