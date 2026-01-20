package ru.yandex.practicum.exceptions;

public class WordleEmptyCandidatesException extends WordleGameException {
    public WordleEmptyCandidatesException(String message) {
        super(message);
    }
}
