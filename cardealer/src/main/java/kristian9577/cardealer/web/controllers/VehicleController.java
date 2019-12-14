package kristian9577.cardealer.web.controllers;

import kristian9577.cardealer.error.VehicleNotFoundException;
import kristian9577.cardealer.services.VehicleService;
import kristian9577.cardealer.services.models.VehicleAddServiceModel;
import kristian9577.cardealer.web.annotations.PageTitle;
import kristian9577.cardealer.web.models.VehicleAddModel;
import kristian9577.cardealer.web.models.VehicleViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vehicles")
public class VehicleController extends BaseController {

    private ModelMapper modelMapper;
    private VehicleService vehicleService;


    @Autowired
    public VehicleController(ModelMapper modelMapper, VehicleService vehicleService) {
        this.modelMapper = modelMapper;
        this.vehicleService = vehicleService;

    }

    @GetMapping("/add-vehicle")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Vehicle")
    public ModelAndView getAddVehicle() {
        return super.view("vehicles/add-vehicle");
    }

    @PostMapping("/add-vehicle")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add Vehicle")
    public ModelAndView getAddVehicleConfirm(@ModelAttribute(name = "model") VehicleAddModel model, Principal principal) {
        VehicleAddServiceModel serviceModel = this.modelMapper.map(model, VehicleAddServiceModel.class);
        String username = principal.getName();

        this.vehicleService.create(serviceModel, username);

        return super.redirect("/vehicles/my-vehicle");
    }

    @GetMapping("/my-vehicle")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My Vehicle")
    public ModelAndView getMyVehicle(ModelAndView modelAndView, Principal principal) {
        List<VehicleViewModel> vehicleViewModel = vehicleService.findVehicleByUsername(principal.getName())
                .stream()
                .map(event -> this.modelMapper.map(event, VehicleViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("vehicles", vehicleViewModel);

        return view("vehicles/my-vehicle", modelAndView);
    }

    @GetMapping("/all-vehicles")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("All Vehicle")
    public ModelAndView getAllVehicles(ModelAndView modelAndView) {
        modelAndView.addObject("vehicles", this.vehicleService.findAllVehicles()
                .stream()
                .map(v -> this.modelMapper.map(v, VehicleViewModel.class))
                .collect(Collectors.toList()));

        return super.view("vehicles/all-vehicles", modelAndView);
    }

    @GetMapping("/details-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Details Vehicle")
    public ModelAndView detailsEvent(@PathVariable String id, ModelAndView modelAndView) throws Exception {
        modelAndView.addObject("vehicle", this.modelMapper.map(this.vehicleService.findById(id), VehicleViewModel.class));

        return super.view("vehicles/details-vehicle", modelAndView);
    }

    @GetMapping("/delete-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Delete Vehicle")
    public ModelAndView getDeleteVehicle(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "vehicle") VehicleAddModel vehicle) throws Exception {
        VehicleAddServiceModel vehicleAddServiceModel = this.vehicleService.findById(id);
        vehicle = this.modelMapper.map(vehicleAddServiceModel, VehicleAddModel.class);

        modelAndView.addObject("vehicle", vehicle);
        modelAndView.addObject("id", id);

        return super.view("vehicles/delete-vehicle", modelAndView);
    }

    @PostMapping("/delete-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Delete Vehicle")
    public ModelAndView deleteVehicleConfirm(@PathVariable String id) throws Exception {
        this.vehicleService.deleteVehicle(id);

        return super.redirect("/vehicles/my-vehicle");
    }

    @GetMapping("/edit-vehicle/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Vehicle")
    public ModelAndView getEditVehicle(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "vehicle") VehicleAddModel model) throws Exception {
        VehicleAddServiceModel serviceModel = this.vehicleService.findById(id);
        model = this.modelMapper.map(serviceModel, VehicleAddModel.class);

        modelAndView.addObject("vehicle", model);
        modelAndView.addObject("id", id);

        return super.view("vehicles/edit-vehicle", modelAndView);
    }

    @PostMapping("/edit-vehicle/{id}")
    @PageTitle("Edit Vehicle")
    public ModelAndView editVehicleConfirm(@PathVariable String id, @ModelAttribute VehicleViewModel model) {
        this.vehicleService.editVehicle(id, this.modelMapper.map(model, VehicleAddServiceModel.class));

        return super.redirect("/vehicles/details-vehicle/" + id);
    }

    @GetMapping("/fetch")
    @ResponseBody
    public List<VehicleViewModel> fetchVehicles(Principal principal) throws InterruptedException {
        Thread.sleep(2000);
        List<VehicleViewModel> vehicles = this.vehicleService.findVehicleByUsername(principal.getName())
                .stream()
                .map(c -> this.modelMapper.map(c, VehicleViewModel.class))
                .collect(Collectors.toList());

        return vehicles;
    }

    @ExceptionHandler({VehicleNotFoundException.class})
    public ModelAndView handleOfferNotFound(VehicleNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }
}
