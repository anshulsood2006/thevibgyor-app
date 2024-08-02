package com.arsoft.projects.thevibgyor.backend.repository.impl;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.app.Menu;
import com.arsoft.projects.thevibgyor.backend.repository.HeaderMenuRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.arsoft.projects.thevibgyor.common.constant.Collection.COLLECTION_MENU_HEADERS;
import static com.arsoft.projects.thevibgyor.common.constant.Collection.COLLECTION_SUB_MENU_HEADERS;

@Slf4j
public class HeaderMenuRepositoryImpl implements HeaderMenuRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Menu> findByRolesContaining(List<Role> roles) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> menuCollection = database.getCollection(COLLECTION_MENU_HEADERS);
        //creating query where role_ids in given list of role ids
        Document menuCollectionQuery = new Document("role_ids", new Document("$in", roles.stream().map(Role::getId).toList()));
        List<Menu> menuList = new ArrayList<>();
        for (Document doc : menuCollection.find(menuCollectionQuery)) {
            Menu menu = mongoTemplate.getConverter().read(Menu.class, doc);
            MongoCollection<Document> subMenuCollection = database.getCollection(COLLECTION_SUB_MENU_HEADERS);
            List<Menu> subMenus = menu.getSubMenuIds().stream().map(id -> {
                Document subMenuCollectionQuery = new Document("id", id);
                Document subMenuCollectionQueryDoc = subMenuCollection.find(subMenuCollectionQuery).first();
                assert subMenuCollectionQueryDoc != null;
                return mongoTemplate.getConverter().read(Menu.class, subMenuCollectionQueryDoc);
            }).toList();
            menu.setSubMenu(subMenus);
            log.info(String.format("Connecting to database: '%s' and querying collection '%s' via query '%s'", database.getName(), COLLECTION_MENU_HEADERS, menuCollectionQuery));
            menuList.add(menu);
        }
        return menuList;
    }
}
