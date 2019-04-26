package com.example.jsoupcrawling.service.impl;

import com.example.jsoupcrawling.service.messageSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Service
@Slf4j
public class messageSendServiceImpl implements messageSendService {

    @Autowired
    Environment environment;

    @Override
    public void send(String message) {

        String hostNameUrl = environment.getProperty("host.name.url");
        String requestUrl= environment.getProperty("request.url");
        String requestUrlType = environment.getProperty("request.url.type");
        String accessKey = environment.getProperty("access.key");
        String secretKey = environment.getProperty("secret.key");
        String serviceId = environment.getProperty("service.id");
        String method = environment.getProperty("method");
        String sendPhoneNumber = environment.getProperty("send.phone.number");
        String timestamp = Long.toString(System.currentTimeMillis()); 	// current timestamp (epoch)
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // String으로 body data 생성
        StringBuilder body = new StringBuilder();
        body.append("{\r\n");
        body.append("  \"type\": \"SMS\",\r\n");
        body.append("  \"contentType\": \"COMM\",\r\n");
        body.append("  \"countryCode\": \"82\",\r\n");
        body.append("  \"from\": \"");
        body.append(sendPhoneNumber);
        body.append("\",\r\n");
        body.append("  \"subject\": \"Daily 줍줍\",\r\n");
        body.append("  \"content\": \"");
        body.append(message);
        body.append("\",\r\n");
        body.append("  \"messages\": [\r\n");
        body.append("    {");
        body.append("  		\"subject\": \"Daily 줍줍\",\r\n");
        body.append("  		\"content\": \"");
        body.append(message);
        body.append("\",\r\n");
        body.append("  		\"to\": \"");
        body.append(sendPhoneNumber);
        body.append("\"\r\n");
        body.append("		}\r\n");
        body.append("  ]\r\n");
        body.append("}");

        try {

            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
            con.setRequestMethod(method);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.write(body.toString().getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==202) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            log.info("response.toString() = {}",response.toString());

        } catch (Exception e) {
            log.error("Exception = {}",e);
        }
    }

    public static String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";                    // one space
        String newLine = "\n";                 // new line
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = java.util.Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            encodeBase64String = e.toString();
        }

        return encodeBase64String;
    }
}
