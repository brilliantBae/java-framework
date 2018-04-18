package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import next.controller.AddAnswerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JsonView implements View {
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        // 모델에 담긴 데이터를 꺼내 req 에 전달한다.
        model.forEach((k, v) -> req.setAttribute(k, v));
        writeJson(res, getJson(model));
    }

    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
    }

    private String getJson(Map<String, ?> model) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(model);
    }
}
