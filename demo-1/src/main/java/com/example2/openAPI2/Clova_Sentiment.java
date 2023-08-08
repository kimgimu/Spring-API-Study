package com.example2.openAPI2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class Clova_Sentiment {

    @GetMapping("/sentiment")
    public String sentiment(){

        //아이디 패스워드
        String clientId = "fzvnjqv0cs";
        String clientSecret = "DIF8oSalVCUOJev9BwimDBE3SVzLYdGnjuMweyD";

        StringBuffer response = new StringBuffer();

        try{
            String content = "{\"content\":\"싸늘하다. 가슴에 비수가 날아와 꽂힌다.\"}";
            String apiURL = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";

            //주어진 url 주소에 대해 새 url 객체를 생성한다
            //url 형식이 잘못된 경우에는 에러를 발생할 수 있다
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //전송방식 //대문자로 작성!
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID",clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY",clientSecret);
            con.setRequestProperty("Content-Type","application/json");

            //쿼리 날리기
            String postParams = "content=" + content;

            //urlconnerction 이 서버에 데이터를 보내는데 사용할 수 있는지 여부를 설정한다
            con.setDoOutput(true);
            //urlconnerction 서버에서 콘텐츠를 읽는데 사용할 수 있는지 여부를 설정한다
            con.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);

            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            BufferedReader br;
            if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { //오류 발생시
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }

            String inputLine;

            while ((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }

            br.close();
            System.out.println(response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

        return response.toString();
    }

}
