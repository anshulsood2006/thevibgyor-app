package com.arsoft.projects.thevibgyor.dictionary.service;

import com.arsoft.projects.thevibgyor.dictionary.model.Word;

import java.util.List;

public interface DictionaryService {
    boolean addWord(Word word);
    List<Word> getAllWords(String letter);


    //boolean updateWord(int id, Word word);

    //boolean deleteWord(int id);

    //boolean getWord(int id);
}
