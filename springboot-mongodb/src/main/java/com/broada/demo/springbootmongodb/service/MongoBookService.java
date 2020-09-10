package com.broada.demo.springbootmongodb.service;

import com.broada.demo.springbootmongodb.entity.MongoBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author tsj
 * @Date 2020/9/2 9:58
 */
@Service
public class MongoBookService {

    private static final Logger logger = LoggerFactory.getLogger(MongoBookService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存对象
     * @param book
     * @return
     */
    public String saveObj(MongoBook book) {
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());
        mongoTemplate.save(book);
        logger.info("添加book成功,bookName:{}",book.getName());
        return "添加成功";
    }

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    public MongoBook getBookByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, MongoBook.class);
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public MongoBook getBookById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, MongoBook.class);
    }

    /**
     * 查询所有
     * @return
     */
    public List<MongoBook> findAll() {
        List<MongoBook> books = mongoTemplate.findAll(MongoBook.class);
        return books;
    }

    /**
     * 更新对象
     *
     * @param book
     * @return
     */
    public String updateBook(MongoBook book) {
        Query query = new Query(Criteria.where("_id").is(book.getId()));
        Update update = new Update().set("publish", book.getPublish()).set("info", book.getInfo()).set("updateTime",new Date());
        // updateFirst 更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, MongoBook.class);
        // updateMulti 更新查询返回结果集的全部
        // mongoTemplate.updateMulti(query,update,Book.class);
        // upsert 更新对象不存在则去添加
        // mongoTemplate.upsert(query,update,Book.class);
        return "update " + getBookById(book.getId()).getName() + " success";
    }

    /***
     * 删除对象
     * @param book
     * @return
     */
    public String deleteBook(MongoBook book) {
        mongoTemplate.remove(book);
        return "success";
    }

    /**
     * 模糊查询
     * @param search
     * @return
     */
    public List<MongoBook> findByLikes(String search){
        Query query = new Query();
        Pattern pattern = Pattern.compile("^" + search + ".*$" , Pattern.CASE_INSENSITIVE);
        Criteria condition1 = Criteria.where("name").regex(pattern);
        query.addCriteria(condition1);
        List<MongoBook> mongoBooks = mongoTemplate.find(query, MongoBook.class);
        logger.info(mongoBooks.toString());
        //根据条件查询并删除数据
        //List<MongoBook> lists = mongoTemplate.findAllAndRemove(query, MongoBook.class);
        return mongoBooks;
    }

    public List<MongoBook> testQuery(){
        Query q = new Query(Criteria.where("price").gt(25));
        List<MongoBook> mongoBooks = mongoTemplate.find(q, MongoBook.class);
        return mongoBooks;
    }
}
