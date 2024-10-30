package com.huarui.pachong.pachong;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class getdatas {
    private static final String BASE_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
    private static final String API_URL = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=181&type=all&web_location=333.934&w_rid=d9c2e6b911c1757fae2d2a2c78971e53&wts=1728559155";
    private List<dataobject> dataList = new ArrayList<>();

    // 随机UA
    private static String generateRandomUserAgent() {
        Random random = new Random();
        char randomLetter = (char) ('a' + random.nextInt(26));
        int position = random.nextInt(BASE_USER_AGENT.length());
        return BASE_USER_AGENT.substring(0, position) + randomLetter + BASE_USER_AGENT.substring(position);
    }

    public class dataobject {
        private String title;
        private String author;
        private String tname;
        private int viewCount;
        private int danmakuCount;

        public dataobject(String title, String author, String tname, int viewCount, int danmakuCount) {
            this.title = title;
            this.author = author;
            this.tname = tname;
            this.viewCount = viewCount;
            this.danmakuCount = danmakuCount;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getTname() { return tname; }
        public int getViewCount() { return viewCount; }
        public int getDanmakuCount() { return danmakuCount; }
    }

    // 获取数据
    public List<dataobject> fetchData() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", generateRandomUserAgent());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            JSONObject data = new JSONObject(responseBuilder.toString()).getJSONObject("data");
            JSONArray videoList = data.getJSONArray("list");

            for (int i = 0; i < videoList.length(); i++) {
                JSONObject video = videoList.getJSONObject(i);
                String title = video.getString("title");
                String author = video.getJSONObject("owner").getString("name");
                String tname = video.getString("tname");
                int viewCount = video.getJSONObject("stat").getInt("view");
                int danmakuCount = video.getJSONObject("stat").getInt("danmaku");

                dataList.add(new dataobject(title, author, tname, viewCount, danmakuCount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public dataobject getHighestViewCountVideo() {
        return dataList.stream()
                .max((v1, v2) -> Integer.compare(v1.getViewCount(), v2.getViewCount()))
                .orElse(null);
    }

    public dataobject getHighestDanmakuCountVideo() {
        return dataList.stream()
                .max((v1, v2) -> Integer.compare(v1.getDanmakuCount(), v2.getDanmakuCount()))
                .orElse(null);
    }

    // 获取出现最多的分区
    public String getMostFrequentCategory() {
        Map<String, Integer> categoryCountMap = new HashMap<>();

        // 统计每个分区的出现次数
        for (dataobject video : dataList) {
            String tname = video.getTname();
            categoryCountMap.put(tname, categoryCountMap.getOrDefault(tname, 0) + 1);
        }

        // 找到出现次数最多的分区
        String mostFrequentCategory = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : categoryCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentCategory = entry.getKey();
            }
        }

        return mostFrequentCategory;
    }
}
