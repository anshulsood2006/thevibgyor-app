package com.arsoft.projects.thevibgyor.dictionary.repository;

import com.arsoft.projects.thevibgyor.dictionary.model.Language;
import com.arsoft.projects.thevibgyor.dictionary.model.Word;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;

import java.util.List;

public interface DictionaryRepository {
    boolean addWord(Word word);
    List<Word> getAllWords(String letter);
    ObjectId getWord(String text, Language language);
    UpdateResult updateWord(ObjectId objectId, Word word);
}
