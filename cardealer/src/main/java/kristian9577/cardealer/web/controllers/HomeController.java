package kristian9577.cardealer.web.controllers;


import kristian9577.cardealer.web.annotations.PageTitle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView getIndex() {
        return super.view("home/index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView getHome() {
        return super.view("home/home");
    }

//    @GetMapping("/login")
//    public ModelAndView getHomeAfterRegister() {
//        return super.redirect("/home");
//    }

}
