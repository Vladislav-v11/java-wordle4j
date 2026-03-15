package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryUploadException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static ru.yandex.practicum.WordleDictionary.normalizeWord;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary loadDictionary(String fileName) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 5) {
                    words.add(normalizeWord(line));
                }
            }

            if (words.isEmpty()) {
                throw new DictionaryUploadException("В словаре нет слов из 5 букв");
            }
            log.println("Создан список из " + words.size() + " слов");
            return new WordleDictionary(words, log);
        } catch (IOException e) {
            log.println("Ошибка чтения файла" + e.getMessage());
            throw e;
        }
    }
}