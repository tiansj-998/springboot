package com.broada.demo.springbootmongodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.addEachToSet;

/**
 * @Author tsj
 * @Date 2020/9/3 11:28
 */
public class MongoTemplateTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoTemplateTest.class);

    private MongoDatabase db;
    private MongoCollection<Document> doc;
    private MongoClient client;

    @Before
    public void init(){
        client = new MongoClient("127.0.0.1",27017);
        db = client.getDatabase("test");
        doc = db.getCollection("users");
    }

    /**
     * 测试插入数据
     */
    @Test
    public void testInsert() {
        Document doc1 = new Document();
        doc1.append("username", "tom");
        doc1.append("country", "England");
        doc1.append("age", 18);
        doc1.append("length", 1.77f);
        doc1.append("salary", new BigDecimal("6585.23"));
        Map<String, String> address1 = new HashMap<>();
        address1.put("aCode", "0000");
        address1.put("add", "xxx000");
        doc1.append("address", address1);
        Map<String, Object> favorites1 = new HashMap<>();
        favorites1.put("movies", Arrays.asList("aa", "bb"));
        favorites1.put("cities", Arrays.asList("广州", "深圳"));
        doc1.append("favorites", favorites1);

        Document doc2 = new Document();
        doc2.append("username", "tony");
        doc2.append("country", "USA");
        doc2.append("age", 28);
        doc2.append("length", 1.75f);
        doc2.append("salary", new BigDecimal("3476.23"));
        Map<String, String> address2 = new HashMap<>();
        address2.put("aCode", "0000");
        address2.put("add", "xxx000");
        doc2.append("address", address2);
        Map<String, Object> favorites2 = new HashMap<String, Object>();
        favorites2.put("movies", Arrays.asList("cc", "dd"));
        favorites2.put("cities", Arrays.asList("南宁", "衡阳"));
        doc2.append("favorites", favorites2);

        doc.insertMany(Arrays.asList(doc1, doc2));
    }

    /**
     * 删除测试
     */
    @Test
    public void testDelete() {
        // delete from users where username = 'iuv'
        DeleteResult deleteMany = doc.deleteMany(eq("username", "iuv"));
        logger.info("删除的行数为：{}", deleteMany.getDeletedCount());

        // delete from users where age >8 and age <25
        DeleteResult deleteMany2 = doc.deleteMany(and(gt("age", 8), lt("age", 25)));
        logger.info("删除的行数为：{}", deleteMany2.getDeletedCount());

        //删除名字为tom和tony的
        /*DeleteResult deleteMany3 = doc.deleteMany(in("username", "tom","tony"));
        logger.info("删除的行数为：{}", deleteMany3.getDeletedCount());*/
    }

    /**
     * 更新测试
     */
    @Test
    public void testUpdate() {
        // update users set age = 6 where username = 'tom'
        UpdateResult updateNum = doc.updateMany(eq("username", "tom"), new Document("$set", new Document("age", 6)));
        logger.info("更新的行数为：{}", updateNum.getModifiedCount());

        // update users set favorites.movies add "蜘蛛侠","钢铁侠" where
        // favorites.cities has "深圳"
        UpdateResult updateNum2 = doc.updateMany(eq("favorites.cities", "深圳"),
                addEachToSet("favorites.movies", Arrays.asList("蜘蛛侠", "钢铁侠")));
        logger.info("更新的行数为：{}", updateNum2.getModifiedCount());
    }

    /**
     * 查询测试
     */
    @Test
    public void testFind() {
        final List<Document> ret = new ArrayList<>();
        Block<Document> printBlock = t -> {
            logger.info("{}", t.toJson());
            ret.add(t);
        };

        // select * from users where favorites.cities has "深圳"、"广州"
        FindIterable<Document> find = doc.find(all("favorites.cities", Arrays.asList("深圳", "广州")));
        find.forEach(printBlock);
        logger.info(String.valueOf(ret.size()));
        ret.removeAll(ret);// 将集合清空

        // select * from users where username like '%om%' and (country=Englan or
        // country=USA)
        String regexStr = ".*om.*";
        Bson regex = regex("username", regexStr);
        Bson or = or(eq("country", "England"), eq("country", "USA"));
        FindIterable<Document> find2 = doc.find(and(regex, or));
        find2.forEach(printBlock);
        logger.info(String.valueOf(ret.size()));
    }



}
