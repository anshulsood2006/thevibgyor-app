package com.arsoft.projects.thevibgyor.dictionary.service.impl;

import com.arsoft.projects.thevibgyor.dictionary.model.Word;
import com.arsoft.projects.thevibgyor.dictionary.repository.DictionaryRepository;
import com.arsoft.projects.thevibgyor.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public boolean addWord(Word word) {
        return dictionaryRepository.addWord(word);
    }

    @Override
    public List<Word> getAllWords(String letter) {
        return dictionaryRepository.getAllWords(letter);
    }
}
