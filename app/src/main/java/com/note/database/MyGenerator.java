package com.note.database;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by ankit on 24/02/18.
 */


public class MyGenerator {

    public static void main(String[] args){

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1,"com.note.database.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static void addTables(final Schema schema) {
        addUserEntities(schema);
        // addPhonesEntities(schema);
    }

    private static Entity addUserEntities(final Schema schema) {
        Entity user = schema.addEntity("Note");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("title");
        user.addStringProperty("text");
        user.addStringProperty("created_at");
        user.addStringProperty("image_path");
        return user;
    }
}
