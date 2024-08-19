package com.example.demoapp.database;

import com.example.demoapp.utils.IFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDB implements CommandLineRunner {
//    @Qualifier("jsonFileReader")
//    @Qualifier("csvFileReader")
    @Qualifier("excelFileReader")
    @Autowired
    private IFileReader fileReader;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Doc du lieu tu file");
//        BookDB.books = fileReader.readFile("book.json");
//        BookDB.books = fileReader.readFile("book.csv");
        BookDB.books = fileReader.readFile("book.xlsx");
        System.out.println("Tong so book = " +BookDB.books.size());
    }
}