package kristian9577.cardealer.web.controllers;


import kristian9577.cardealer.error.OfferNotFoundException;
import kristian9577.cardealer.error.OfferWithThisVehicleAlreadyExistsException;
import kristian9577.cardealer.services.OfferService;
import kristian9577.cardealer.services.models.OfferServiceModel;
import kristian9577.cardealer.web.annotations.PageTitle;
import kristian9577.cardealer.web.models.OfferAddModel;
import kristian9577.cardealer.web.models.OfferViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offers")
public class OfferController extends BaseController {

    private ModelMapper modelMapper;
    private OfferService offerService;

    public OfferController(ModelMapper modelMapper, OfferService offerService) {
        this.modelMapper = modelMapper;
        this.offerService = offerService;
    }

    @GetMapping("/add-offer")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Offer")
    public ModelAndView getAddOffer() {
        return super.view("offers/add-offer");
    }

    @PostMapping("/add-offer")
    @PageTitle("Add Offer")
    public ModelAndView addOfferConfirm(@ModelAttribute OfferAddModel offerAddModel, Principal principal) {
        OfferServiceModel serviceModel = this.modelMapper.map(offerAddModel, OfferServiceModel.class);
        String username = principal.getName();

        this.offerService.create(serviceModel, username);
        return super.redirect("/offers/all-offer");
    }

    @GetMapping("/my-offer")
    @PageTitle("My Offer")
    public ModelAndView getMyOffer(ModelAndView modelAndView, Principal principal) {
        List<OfferViewModel> offerViewModels = offerService.findOfferByUsername(principal.getName())
                .stream()
                .map(o -> this.modelMapper.map(o, OfferViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("offers", offerViewModels);

        return view("offers/my-offer", modelAndView);
    }

    @GetMapping("/all-offer")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("All Offer")
    public ModelAndView getAllOffer(ModelAndView modelAndView) {
        List<OfferViewModel> offerViewModels = offerService.findAll()
                .stream()
                .map(event -> this.modelMapper.map(event, OfferViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("offers", offerViewModels);


        return super.view("offers/all-offer", modelAndView);
    }

    @GetMapping("/delete-offer/{id}")
    @PageTitle("Delete Offer")
    public ModelAndView getDeleteOffer(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "offer") OfferAddModel offer) {
        OfferServiceModel offerServiceModel = this.offerService.findById(id);
        offer = this.modelMapper.map(offerServiceModel, OfferAddModel.class);

        modelAndView.addObject("offer", offer);
        modelAndView.addObject("id", id);

        return super.view("offers/delete-offer", modelAndView);
    }

    @PostMapping("/delete-offer/{id}")
    @PageTitle("Delete Offer")
    public ModelAndView deleteOfferConfirm(@PathVariable String id) {
        this.offerService.deleteOffer(id);

        return super.redirect("/offers/my-offer");
    }

    @GetMapping("/edit-offer/{id}")
    @PageTitle("Edit Offer")
    public ModelAndView getEditOffer(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "offer") OfferAddModel offer) {
        OfferServiceModel offerServiceModel = this.offerService.findById(id);
        offer = this.modelMapper.map(offerServiceModel, OfferAddModel.class);

        modelAndView.addObject("offer", offer);
        modelAndView.addObject("id", id);

        return super.view("offers/edit-offer", modelAndView);
    }

    @PostMapping("/edit-offer/{id}")
    @PageTitle("Edit Offer")
    public ModelAndView editOfferConfirm(@PathVariable String id, @ModelAttribute OfferViewModel model) {
        this.offerService.editOffer(id, this.modelMapper.map(model, OfferServiceModel.class));

        return super.redirect("/offers/my-offer/");
    }

    @ExceptionHandler({OfferWithThisVehicleAlreadyExistsException.class})
    public ModelAndView handleOfferWithThisVehicleExist(OfferWithThisVehicleAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView("error.html");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }

    @ExceptionHandler({OfferNotFoundException.class})
    public ModelAndView handleOfferNotFound(OfferNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }
}
