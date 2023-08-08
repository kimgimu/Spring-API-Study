package com.example2.openAPI2;

// 네이버 개발자 센터에서 검색기능을 구현.
// 1. 네이버개발자센터 회원가입
// 2. 어플케이션 등록 
//   Client ID : lXF9D0J81ibDjCsxzkKE
//   Client Secret : rodfxhs30y
//네이버 검색 API 예제 - 블로그 검색
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiExamSearchBlog {

	public static void main(String[] args) {

		String clientId = "lXF9D0J81ibDjCsxzkKE"; // 애플리케이션 클라이언트 아이디
		String clientSecret = "rodfxhs30y"; // 애플리케이션 클라이언트 시크릿

		// 검색어
		String text = null;
		try {

			// 검색어를 url뒤에 붙여서 보낼때 인코딩
			text = URLEncoder.encode("강동원", "UTF-8");

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패", e);
		}

		String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text; // JSON 결과
		// String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text;
		// // XML 결과

		// 요청하기 전에 어떤 id로 접속할 지 헤더파일을 만들어서 저장한다.
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);
		String responseBody = get(apiURL, requestHeaders);

		// 검색한 내용을 출력한다.
		// System.out.println(responseBody);

		// json 파싱
		/*
		 * 
		 * <!-- 메이븐으로 설정
		 * https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple -->
		 * <dependency> <groupId>com.googlecode.json-simple</groupId>
		 * <artifactId>json-simple</artifactId> <version>1.1.1</version> </dependency>
		 * 
		 * 
		 * 
		 */
		
		// JsonObject  map 형식으로 데이터가 감싸져 있을 경우 {}
		// JsonArray  배열 형식으로 데이터가 감싸져 있을 경우 []
		
		// 가장 큰 JSONObject를 가져옵니다.
		JSONObject jObject = new JSONObject(responseBody);
		
		// 배열을 가져옵니다.
		JSONArray jArray = jObject.getJSONArray("items");
		
		//System.out.println(jArray);
		
		// 배열의 모든 아이템을 출력합니다. 
		// 반복문을 이용해서 for each 문 작성! 
		// 반복문을 이용해서 인덱스 번호를 작성 !
		
		JSONObject obj = jArray.getJSONObject(0);
		
		String title = obj.getString("title");
		String originallink = obj.getString("originallink");
		String link = obj.getString("link");
		String description = obj.getString("description");	

		System.out.println("title : " + title);
		System.out.println("originallink : " + originallink);
		System.out.println("link : " + link);
		System.out.println("description : " + description);
	}

	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);

		try {
			con.setRequestMethod("GET"); // 전송방식을 설정하는 메서드
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			// 응답
			// 실제 전송이 잘 되어서 응답이 온다면 정상 코드 200 getResponseCode()
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 오류 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			// String str ="hello"
			// String str2 = "hello world" 불변

			// StringBuilder 변경가능한 객체 한번 만들어놓으면 추가하거나 삭제하거나 수정이 가능하다.

			// 응답받은 내용을 꺼내서 저장하기 위한 변수
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
		}
	}
}
