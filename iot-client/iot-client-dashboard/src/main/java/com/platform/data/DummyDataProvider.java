package com.platform.data;

import com.google.common.collect.Multimap;
import com.platform.domain.User;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * A dummy implementation for the backend API.
 */
public class DummyDataProvider implements DataProvider {

    // TODO: Get API key from http://developer.rottentomatoes.com
    private static final String ROTTEN_TOMATOES_API_KEY = null;

    /* List of countries and cities for them */
    private static Multimap<String, String> countryToCities;
    private static Date lastDataUpdate;

    private static Random rand = new Random();

    /**
     * Initialize the data for this application.
     */
    public DummyDataProvider() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {
            lastDataUpdate = new Date();
        }
    }

    /* JSON utility method */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

//    /* JSON utility method */
//    private static JsonObject readJsonFromUrl(String url) throws IOException {
//        InputStream is = new URL(url).openStream();
//        try {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
//                    Charset.forName("UTF-8")));
//            String jsonText = readAll(rd);
//            JsonElement jelement = new JsonParser().parse(jsonText);
//            JsonObject jobject = jelement.getAsJsonObject();
//            return jobject;
//        } finally {
//            is.close();
//        }
//    }
//
//    /* JSON utility method */
//    private static JsonObject readJsonFromFile(File path) throws IOException {
//        BufferedReader rd = new BufferedReader(new FileReader(path));
//        String jsonText = readAll(rd);
//        JsonElement jelement = new JsonParser().parse(jsonText);
//        JsonObject jobject = jelement.getAsJsonObject();
//        return jobject;
//    }

    /**
     * =========================================================================
     * Countries, cities, theaters and rooms
     * =========================================================================
     */

    static List<String> theaters = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("Threater 1");
            add("Threater 2");
            add("Threater 3");
            add("Threater 4");
            add("Threater 5");
            add("Threater 6");
        }
    };

    static List<String> rooms = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("Room 1");
            add("Room 2");
            add("Room 3");
            add("Room 4");
            add("Room 5");
            add("Room 6");
        }
    };



    @Override
    public User authenticate(String userName, String password) {
        User user = new User();
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setRole("admin");
        String email = user.getFirstName().toLowerCase() + "."
                + user.getLastName().toLowerCase() + "@"
                + "iot".toLowerCase() + ".com";
        user.setEmail(email.replaceAll(" ", ""));
        user.setLocation("-");

        return user;
    }

    @Override
    public int getUnreadNotificationsCount() {
        return 0;
    }


    private Date getDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }


    @Override
    public double getTotalSum() {
        double result = 0;
//        for (Transaction transaction : transactions.values()) {
//            result += transaction.getPrice();
//        }
        return result;
    }


}
