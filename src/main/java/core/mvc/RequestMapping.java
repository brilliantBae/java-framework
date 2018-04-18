package core.mvc;

import next.controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    // match URL with Controller
    Map<String, Controller> controllers;

    public RequestMapping(){
        controllers = new HashMap<>();
        controllers.put("/", new HomeController());
        controllers.put("/users/create", new CreateUserController());
        controllers.put("/users", new ListUserController());
        controllers.put("/users/login", new LoginController());
        controllers.put("/users/logout", new LogoutController());
        controllers.put("/users/profile", new ProfileController());
        controllers.put("/users/update", new UpdateUserController());
        controllers.put("/users/form", new FowardController("/users/form"));
        controllers.put("/users/loginForm", new FowardController("/users/loginForm"));
    }

    public Controller find(String url) {
        return controllers.get(url);
    }


}
