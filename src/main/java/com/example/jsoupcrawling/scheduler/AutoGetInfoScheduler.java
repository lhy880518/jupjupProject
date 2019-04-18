package com.example.jsoupcrawling.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class AutoGetInfoScheduler {

    final String resource = "/src/main/resources/static/secureInfo.properties";

    @Scheduled(cron = "* * * * * *")
    public void getJupJup() throws IOException {
        log.info("============getJupJup is Call");
//        Properties properties = new Properties();
//
//        try {
//            properties.load(new FileReader(resource));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String connUrl = properties.getProperty("jupjup.site.url");
//        Document doc = Jsoup.connect(connUrl).get();
//        Elements titles = doc.select("div.f_s span");
//        Elements names = doc.select("div.name span.txt");
//        Elements manageNos = doc.select("div.name a");
//        Elements dates = doc.select("div.receipt_date span.date");
//        List<String> messageList = new ArrayList<String>();
//
//        if(titles.size() > 0){
//            for(int i=0 ; i < titles.size() ; i++){
//                messageList.add(String.format("오늘의 줍줍이~ %s건\n위치 및 물건명 : %s\n접수기간 : %s\n호갱노노 연결 : %s\n상세파일 다운로드 : test"
//                        , String.valueOf(titles.size())
//                        , titles.get(i).text() + " " + names.get(i).text()
//                        , dates.get(i).text()
//                        , "https://hogangnono.com/search?q="+(names.get(i).text().replaceAll("　",""))
//                ));
//            }
//        }else{
//            messageList.add("줍줍이 없다");
//        }
//
//        for(String message : messageList){
//            log.info("message = {}",message);
//        }
    }
}
