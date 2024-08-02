package com.arsoft.projects.thevibgyor.dictionary.repository.impl;

import com.arsoft.projects.thevibgyor.dictionary.constant.DictionaryCollection;
import com.arsoft.projects.thevibgyor.dictionary.model.Input;
import com.arsoft.projects.thevibgyor.dictionary.model.Language;
import com.arsoft.projects.thevibgyor.dictionary.model.Meaning;
import com.arsoft.projects.thevibgyor.dictionary.model.Word;
import com.arsoft.projects.thevibgyor.dictionary.repository.DictionaryRepository;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DictionaryRepositoryImpl implements DictionaryRepository {

    private static final String COLUMN_DEFAULT_LANGUAGE = "ENGLISH";
    private static final String COLUMN_ID = "_id";
    private static final String KEY_SET = "$set";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean addWord(Word word) {
        String letter = word.getInput().getText().substring(0, 1);
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(DictionaryCollection.COLLECTION_DICTIONARY_PREFIX + letter.toUpperCase());
        Document document = getDocumentForWord(word);
        ObjectId id = getWord(word.getInput().getText(), word.getInput().getLanguage());
        if (id != null) {
            UpdateResult result = updateWord(id, word);
        } else {
            collection.insertOne(document);
        }
        return true;
    }

    @Override
    public List<Word> getAllWords(String letter) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(DictionaryCollection.COLLECTION_DICTIONARY_PREFIX + letter.toUpperCase());
        FindIterable<Document> documents = collection.find();
        Function<Document, List<Meaning>> getMeanings = document -> {
            List<Meaning> meanings = new ArrayList<>();
            for (Map.Entry<String, Object> entry : document.entrySet()) {
                String key = entry.getKey();
                if (!key.equalsIgnoreCase(COLUMN_ID)) {
                    String value = (String) entry.getValue();
                    meanings.add(Meaning.builder()
                            .language(Language.valueOf(key))
                            .meaning(value)
                            .build());
                }
            }
            return meanings;
        };
        List<Word> words = new ArrayList<>();
        documents.forEach(document -> {
            words.add(Word.builder()
                    .input(Input.builder()
                            .language(Language.ENGLISH)
                            .text(document.getString(COLUMN_DEFAULT_LANGUAGE))
                            .build())
                    .meanings(getMeanings.apply(document))
                    .build());

        });
        return words;
    }

    public ObjectId getWord(String text, Language language) {
        MongoDatabase database = mongoTemplate.getDb();
        String letter = text.substring(0, 1);
        MongoCollection<Document> collection = database.getCollection(DictionaryCollection.COLLECTION_DICTIONARY_PREFIX + letter.toUpperCase());
        Document query = new Document(language.name().toUpperCase(), text);
        Document result = collection.find(query).first();
        if (result != null) {
            return result.getObjectId(COLUMN_ID);
        }
        return null;
    }

    @Override
    public UpdateResult updateWord(ObjectId objectId, Word word) {
        MongoDatabase database = mongoTemplate.getDb();
        String letter = word.getInput().getText().substring(0, 1);
        MongoCollection<Document> collection = database.getCollection(DictionaryCollection.COLLECTION_DICTIONARY_PREFIX + letter.toUpperCase());
        Document query = new Document(COLUMN_ID, objectId);
        Document updateOperation = new Document(KEY_SET, getDocumentForWord(word));
        return collection.updateOne(query, updateOperation);
    }

    private static Document getDocumentForWord(Word word) {
        Document document = new Document(COLUMN_DEFAULT_LANGUAGE, word.getInput().getText());
        for (Meaning meaning : word.getMeanings()) {
            document.append(meaning.getLanguage().toString(), meaning.getMeaning());
        }
        return document;
    }
}
