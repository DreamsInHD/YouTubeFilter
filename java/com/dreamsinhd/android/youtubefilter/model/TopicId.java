package com.dreamsinhd.android.youtubefilter.model;

import java.util.HashMap;
import java.util.Map;

public class TopicId {
    public static final Map<String, String> topics = createMap();
    private static Map<String, String> createMap() {
        Map<String, String> ids = new HashMap<>();
        ids.put("Music", "/m/04rlf");
        ids.put("Classical music", "/m/0ggq0m");
        ids.put("Country", "/m/01lyv");
        ids.put("Independent music", "/m/05rwpb");
        ids.put("Rock music", "/m/06by7");
        ids.put("Rhythm and blues", "/m/06j6l");
        ids.put("Action-adventure game", "/m/02ntfj");
        ids.put("Simulation video game", "/m/021bp2");
        ids.put("Sports", "/m/06ntj");
        ids.put("Boxing", "/m/01cgz");
        ids.put("Ice hockey", "/m/03tmr");
        ids.put("Motorsport", "/m/0410tth");
        ids.put("American football", "/m/0jm_");
        ids.put("Baseball", "/m/018jz");
        ids.put("Football", "/m/02vx4");
        ids.put("Entertainment", "/m/02jjt");
        ids.put("Humor", "/m/09kqc");
        ids.put("Movies", "/m/02vxn");
        ids.put("Performing arts", "/m/05qjc");
        ids.put("Professional wrestling", "/m/066wd");
        ids.put("TV shows", "/m/0f2f9");
        ids.put("Lifestyle", "/m/019_rr");
        ids.put("Fashion", "m/032tl");
        ids.put("Fitness", "/m/027x7n");
        ids.put("Food", "/m/02wbm");
        ids.put("Hobby", "/m/03glg");
        ids.put("Pets", "/m/068hy");
        ids.put("Physical attractiveness", "/m/041xxh");
        ids.put("Technology", "/m/07c1v");
        ids.put("Tourism", "/m/07bxq");
        ids.put("Vehicles", "/m/07yv9");
        ids.put("Society", "/m/098wr");
        ids.put("Business", "/m/09s1f");
        ids.put("Health", "/m/0kt51");
        ids.put("Military", "/m/01h6rj");
        ids.put("Politics", "/m/05qt0");
        ids.put("Knowledge", "/m/01k8wb");
        return ids;
    }
}
