package ru.yandex.practicum.exceptions;

public class WordleGameNoSuchWordException extends WordleGameException {
    public WordleGameNoSuchWordException(String message) {
        super(message);
    }
}
