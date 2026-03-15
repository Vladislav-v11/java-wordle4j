package ru.yandex.practicum.exceptions;

public class DictionaryUploadException extends RuntimeException {
    public DictionaryUploadException(String message) {
        super(message);
    }
}
