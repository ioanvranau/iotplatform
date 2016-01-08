package com.platform.iot.service;

/**
 * Created by ioan.vranau on 1/4/2016.
 */

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.platform.iot.domain.Greeting;
import com.platform.iot.domain.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping("/users")
    public @ResponseBody
    List<User> users(@RequestParam(value = "name", defaultValue = "World") String name) {
        return Arrays.asList(new User(counter.incrementAndGet(),"Ionut", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire."),
                new User(counter.incrementAndGet(),"George Duke", "svg-2", "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. De carne lumbering animata corpora quaeritis. Summus brains sit\u200B\u200B, morbo vel maleficia? De apocalypsi gorger omero undead survivor dictum mauris."));
    }
}