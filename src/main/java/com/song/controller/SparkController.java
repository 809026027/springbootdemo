package com.song.controller;

import com.song.service.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by feng on 2019/8/31.
 */
@RestController
public class SparkController {
    @Autowired
    private SparkService sparkService;

    @RequestMapping("/demo/top10")
    @ResponseBody
    public Map<String, Object> calculateTopTen() {
        return sparkService.calculateTopTen();
    }

    @RequestMapping("/demo/exercise")
    public void exercise() {
        sparkService.sparkExerciseDemo();
    }

    @RequestMapping("/demo/stream")
    public void streamingDemo() throws InterruptedException {
        sparkService.sparkStreaming();
    }

    @RequestMapping("/demo/db2db")
    public void sparkMySqltoMySql() throws InterruptedException {
        sparkService.sparkMySql();
    }
}
