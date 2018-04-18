package core.di.factory.example;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.View;

public class MockView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

    }
}
