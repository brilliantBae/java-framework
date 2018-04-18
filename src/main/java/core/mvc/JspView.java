package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {

    private String url;

    public JspView(String url) {
        this.url = url;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        model.forEach((k, v) -> req.setAttribute(k, v));
        //redirect
        if (this.url.startsWith("redirect:")) {
            res.sendRedirect(this.url.substring(9));
            return;
        }
        // forward
        RequestDispatcher rd = req.getRequestDispatcher(this.url);
        rd.forward(req, res);
    }
}
