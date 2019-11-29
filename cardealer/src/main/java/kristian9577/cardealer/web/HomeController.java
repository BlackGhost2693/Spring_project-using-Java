package kristian9577.cardealer.web;

import kristian9577.cardealer.services.OfferService;
import kristian9577.cardealer.web.base.BaseController;
import kristian9577.cardealer.web.models.OfferViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView getIndex() {
        return super.view("home/index");
    }


    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getHome() {
        return super.view("home/home");
    }
    @GetMapping("/login")
    public ModelAndView getHomeAfterRegister() {
        return super.redirect("/home");
    }

//    @GetMapping("/home")
//    public List<OfferViewModel> getHome() {
//        return this.offerService.findAll()
//                .stream()
//                .map(offer -> this.modelMapper.map(offer, OfferViewModel.class))
//                .collect(Collectors.toList());
//    }
}
