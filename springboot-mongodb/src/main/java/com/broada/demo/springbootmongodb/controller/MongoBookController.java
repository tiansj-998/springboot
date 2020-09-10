package com.broada.demo.springbootmongodb.controller;

import com.broada.demo.springbootmongodb.entity.MongoBook;
import com.broada.demo.springbootmongodb.service.MongoBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author tsj
 * @Date 2020/9/2 10:01
 */
@RestController
@RequestMapping(value = "/mongoBook")
public class MongoBookController {
    @Autowired
    private MongoBookService mongoBookService;

    @PostMapping("/save")
    public String saveObj(@RequestBody MongoBook book) {
        return mongoBookService.saveObj(book);
    }

    @GetMapping("/findOneByName")
    public MongoBook findOneByName(@RequestParam String name) {
        return mongoBookService.getBookByName(name);
    }

    @GetMapping("/findAll")
    public List<MongoBook> findAll() {
        return mongoBookService.findAll();
    }

    @PostMapping("/update")
    public String update(@RequestBody MongoBook book) {
        return mongoBookService.updateBook(book);
    }

    @PostMapping("/delOne")
    public String delOne(@RequestBody MongoBook book) {
        return mongoBookService.deleteBook(book);
    }

    @GetMapping("/findlikes")
    public List<MongoBook> findByLikes(@RequestParam String search) {
        return mongoBookService.findByLikes(search);
    }

    @GetMapping("/test")
    public List<MongoBook> testQ(){
        return mongoBookService.testQuery();
    }

}
