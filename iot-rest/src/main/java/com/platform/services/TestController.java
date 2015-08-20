package com.platform.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vranau on 7/31/2015.
 */
@RestController
public class TestController {
    /**
     * Executes really fast.
     *
     * @return a name of the executing thread
     */
    @RequestMapping("/fast")
    public String fast() {
        System.out.println("I am here!!");
        return Thread.currentThread().getName();
    }

}
