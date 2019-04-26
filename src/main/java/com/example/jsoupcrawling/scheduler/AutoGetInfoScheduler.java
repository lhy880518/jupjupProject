package com.example.jsoupcrawling.scheduler;

import com.example.jsoupcrawling.service.messageSendService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AutoGetInfoScheduler {

    @Autowired
    Environment environment;

    @Autowired
    private messageSendService messageSendService;

    /**
     * 오전 11시 마다 JupJup관련 sms를 발송합니다. 월~금만 사용
     *
     * For example, suites in JUnit 4 are built using RunWith, and a custom runner named Suite:
     *
     * <pre>
     * &#064;RunWith(Suite.class)
     * &#064;SuiteClasses({ATest.class, BTest.class, CTest.class})
     * public class ABCSuite {
     * }
     * </pre>
     *
     * @since 4.0
     */
    @Scheduled(cron = "* * 11 ? * MON-FRI")
    public void getJupJup() throws IOException {
        log.info("============getJupJup is Call");

        String connUrl = environment.getProperty("jupjup.site.url");
        Document doc = Jsoup.connect(connUrl).get();
        Elements titles = doc.select("div.f_s span");
        Elements names = doc.select("div.name span.txt");
        Elements manageNos = doc.select("div.name a");
        Elements dates = doc.select("div.receipt_date span.date");
        List<String> messageList = new ArrayList<String>();

        if(titles.size() > 0){
            for(int i=0 ; i < titles.size() ; i++){
                messageList.add(String.format("오늘의 줍줍이~ %s건\\n위치 및 물건명 : %s\\n접수기간 : %s\\n호갱노노 연결 : %s"
                        , String.valueOf(titles.size())
                        , titles.get(i).text() + " " + names.get(i).text()
                        , dates.get(i).text()
                        , "https://hogangnono.com/search?q="+(names.get(i).text().replaceAll("　",""))
                ));
            }
        }else{
            messageList.add("줍줍이 없다");
        }

        for(String message : messageList){
            log.info("message = {}",message);
            messageSendService.send(message);
        }
    }
}
