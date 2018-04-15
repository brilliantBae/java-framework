# Simple Web Application Server

HTTP 요청을 처리하는 웹서버를 구현한 프로그램입니다.

## 시작하기

### Prerequisites

프로젝트를 실행시키기 위한 도구 및 프로그램
* [IntelliJ](https://www.jetbrains.com/idea/download/#section=mac)
* [Eclipse(STS)](https://spring.io/tools/sts/all)
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## 실행화면

* 서버 시작은 WebServer 클래스가 담당
* 클라이언트의 요청을 받고 응답을 보내는 모든 작업은 RequestHandler 클래스가 담당

ex) 클라이언트로부터 http://localhost:8080/index.html 요청을 받았을때 요청 메세지 전체를 출력해주고 응답으로 알맞은 페이지를 보내줍니다.

![RequestMessage](https://user-images.githubusercontent.com/23162178/38776375-6f7cf500-40d0-11e8-9ddf-c5bc20a71bfb.png)

응답으로 온 index.html 파일

![index.html](https://user-images.githubusercontent.com/23162178/38776413-c94e03ee-40d0-11e8-9505-0102e49813d9.png)

## Running the tests

테스트 실행방법(Shortcuts)
* IntelliJ : ⇧+ ⌃ + R
* Eclipse(STS) : ⌥ ⌘ X + T

### End-to-End 테스트

* `HTTPRequestTest` : 클라이언트로부터 HTTP 요청이 왔을 때 웹 서버가 요청을 올바르게 인지하고 있는지 확인하기 위한 테스트.

ex)
 `printAllHeader_get()` : HTTP get 요청이 왔을 때 요청 헤더가 올바르게 출력되는지 확인하기 위한 테스트.

```
    @Test
    public void printAllHeader_get() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("localhost:8080",  httpRequest.getHeader("Host"));
        assertEquals("keep-alive",  httpRequest.getHeader("Connection"));
        assertEquals("*/*",  httpRequest.getHeader("Accept"));

    }
```

* HTTPResponseTest : 웹서버가 클라이언트의 요청을 처리하여 알맞은 응답을 보내는지 확인하기 위한 테스트.


ex) `createDynamicHTML()` : 웹서버가 응답으로 동적인 HTML 을 생성해서 보내는지 확인하는 테스트.

```
    @Test
    public void createDynamicHTML() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("HttpResponse.txt"));
        byte[] body = response.createDynamicHTML("./webapp/user/list_static.html", users);
        response.responseBody(body);
    }

```
테스트 실행 후 HttpResponse.txt 에 해당 HTML 출력확인 가능.

## 사용된 도구

* [Maven](https://maven.apache.org/) - 의존성 관리 프로그램

## 프로젝트 관련 지식, 진행과정 관련 글
* https://medium.com/@jwb8705/web-http-%EC%9D%B4%ED%95%B4-82552992365e
* https://medium.com/@jwb8705/web-servlet-introduction-15a900f42f87
* https://medium.com/@jwb8705/web-servlet-servlet-container-b6c3a4c6549f

## 라이센스

이 프로젝트는 MIT 허가서를 사용합니다 - [LICENSE](https://github.com/brilliantBae/java-was/blob/master/LICENSE) 파일에서 자세히 알아보세요.

---

