// src/main/java/mongodb/MongoDbUtil.java
package mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbUtil {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "testdb";
    private static final MongoClient CLIENT = MongoClients.create(URI);
    private static final MongoDatabase DATABASE = CLIENT.getDatabase(DB_NAME);

    /** Lấy về collection theo tên */
    public static MongoCollection<Document> getCollection(String name) {
        return DATABASE.getCollection(name);
    }

    /** Đóng client khi không còn dùng nữa */
    public static void close() {
        CLIENT.close();
    }
}