package com.example.jsoupcrawling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:static/properties/secureInfo.properties")
public class JsoupcrawlingApplicationTests {

    @Value("${host.name.url}")
    private String hostNameUrl;

    @Value("${request.url}")
    private String requestUrl;

    @Value("${request.url.type}")
    private String requestUrlType;

    @Value("${access.key}")
    private String accessKey;

    @Value("${secret.key}")
    private String secretKey;

    @Value("${service.id}")
    private String serviceId;

    @Value("${method}")
    private String method;

    @Value("${send.phone.number}")
    private String sendPhoneNumber;

    @Test
    public void SendSmsTest() {

        String hostNameUrl = this.hostNameUrl;
        String requestUrl= this.requestUrl;
        String requestUrlType = this.requestUrlType;
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;
        String serviceId = this.serviceId;
        String method = this.method;
        String sendPhoneNumber = this.sendPhoneNumber;
        String timestamp = Long.toString(System.currentTimeMillis()); 	// current timestamp (epoch)
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;
        System.out.println(apiUrl);

        // JSON 을 활용한 body data 생성
		/*
		JSONObject bodyJson = new JSONObject();
		JSONObject toJson = new JSONObject();
	    JSONArray  toArr = new JSONArray();

	    toJson.put("subject","");				// 메시지 제목 * LMS Type에서만 사용할 수 있습니다.
	    toJson.put("content","");				// 메시지 내용 * Type별로 최대 byte 제한이 다릅니다.* SMS: 80byte / LMS: 2000byte
	    toJson.put("to","");					// 수신번호 목록  * 최대 50개까지 한번에 전송할 수 있습니다.
	    toArr.add(toJson);

	    bodyJson.put("type","");				// 메시지 Type (sms | lms)
	    bodyJson.put("contentType","");			// 메시지 내용 Type (AD | COMM) * AD: 광고용, COMM: 일반용 (default: COMM) * 광고용 메시지 발송 시 불법 스팸 방지를 위한 정보통신망법 (제 50조)가 적용됩니다.
	    bodyJson.put("countryCode","");			// 국가 전화번호
	    bodyJson.put("from","");				// 발신번호 * 사전에 인증/등록된 번호만 사용할 수 있습니다.
	    bodyJson.put("subject","");				// 메시지 제목 * LMS Type에서만 사용할 수 있습니다.
	    bodyJson.put("content","");				// 메시지 내용 * Type별로 최대 byte 제한이 다릅니다.* SMS: 80byte / LMS: 2000byte
	    bodyJson.put("messages", toArr);


	    String body = bodyJson.toJSONString();
	    */

        // String으로 body data 생성
//        StringBuilder body = new StringBuilder();
//        body.append("{\r\n");
//        body.append("  \"type\": \"SMS\",\r\n");
//        body.append("  \"contentType\": \"COMM\",\r\n");
//        body.append("  \"countryCode\": \"82\",\r\n");
//        body.append("  \"from\": \"");
//        body.append(sendPhoneNumber);
//        body.append("\",\r\n");
//        body.append("  \"subject\": \"Daily 줍줍\",\r\n");
//        body.append("  \"content\": \"");
//        body.append("message");
//        body.append("\",\r\n");
//        body.append("  \"messages\": [\r\n");
//        body.append("    {");
//        body.append("  		\"subject\": \"Daily 줍줍\",\r\n");
//        body.append("  		\"content\": \"");
//        body.append("message");
//        body.append("\",\r\n");
//        body.append("  		\"to\": \"");
//        body.append(sendPhoneNumber);
//        body.append("\"\r\n");
//        body.append("		}\r\n");
//        body.append("  ]\r\n");
//        body.append("}");
//
//        try {
//
//            URL url = new URL(apiUrl);
//
//            HttpURLConnection con = (HttpURLConnection)url.openConnection();
//            con.setUseCaches(false);
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setRequestProperty("content-type", "application/json");
//            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
//            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
//            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
//            con.setRequestMethod(method);
//            con.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//
//            wr.write(body.toString().getBytes());
//            wr.flush();
//            wr.close();
//
//            int responseCode = con.getResponseCode();
//            BufferedReader br;
//            System.out.println(responseCode);
//            if(responseCode==202) { // 정상 호출
//                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            } else {  // 에러 발생
//                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            }
//
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = br.readLine()) != null) {
//                response.append(inputLine);
//            }
//            br.close();
//
//            System.out.println(response.toString());
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
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
