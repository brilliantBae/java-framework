package next.model;

public class Response {
    private String result;

    private Response(String result) {
        this.result = result;
    }

    public static Response isNotAjax(String html) {
        return new Response(html);
    }

    public static Response isAjax() {
        return new Response(null);
    }

    public String getResult() {
        return result;
    }
}

