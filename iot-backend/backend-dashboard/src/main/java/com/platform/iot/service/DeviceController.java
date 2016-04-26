package com.platform.iot.service;

/**
 * Created by ioan.vranau on 1/4/2016.
 */

import com.platform.iot.model.Device;
import com.platform.iot.model.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DeviceController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/devices")
    public
    @ResponseBody
    List<Device> devices(@RequestParam(value = "name", defaultValue = "World") String name) {
        return Arrays.asList(new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Android Phone", "svg-1", "I love cheese, especially airedale queso. Cheese and biscuits halloumi cauliflower cheese cottage cheese swiss boursin fondue caerphilly. Cow port-salut camembert de normandie macaroni cheese feta who moved my cheese babybel boursin. Red leicester roquefort boursin squirty cheese jarlsberg blue castello caerphilly chalk and cheese. Lancashire.", null),
                new Device(counter.incrementAndGet(), "Smart Clock", "svg-2", "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. De carne lumbering animata corpora quaeritis. Summus brains sit\u200B\u200B, morbo vel maleficia? De apocalypsi gorger omero undead survivor dictum mauris.", null));
    }

    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<Device> addDevice(@RequestBody Device device) {
        //
        // Code processing the input parameters
        //
        if (device != null) {
            System.out.println(device);
        }
        return new ResponseEntity<Device>(device, HttpStatus.OK);
    }
}