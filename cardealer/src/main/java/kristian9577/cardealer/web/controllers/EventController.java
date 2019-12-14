package kristian9577.cardealer.web.controllers;

import kristian9577.cardealer.error.EventNameAlreadyExistsException;
import kristian9577.cardealer.error.EventNotFoundException;
import kristian9577.cardealer.services.CloudinaryService;
import kristian9577.cardealer.services.EventService;
import kristian9577.cardealer.services.models.EventAddServiceModel;
import kristian9577.cardealer.web.annotations.PageTitle;
import kristian9577.cardealer.web.models.EventAddModel;
import kristian9577.cardealer.web.models.EventViewModel;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {

    private ModelMapper modelMapper;
    private CloudinaryService cloudinaryService;
    private EventService eventService;

    @Autowired
    public EventController(ModelMapper modelMapper, CloudinaryService cloudinaryService, EventService eventService) {
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.eventService = eventService;
    }

    @GetMapping("/add-events")
    @PageTitle("Add Event")
    public ModelAndView getAddEvent() {
        return super.view("events/add-events");
    }

    @PostMapping("/add-events")
    @PageTitle("Add Event")
    public ModelAndView getAddEventConfirm(@ModelAttribute EventAddModel model, Principal principal) throws IOException {
        EventAddServiceModel serviceModel = this.modelMapper.map(model, EventAddServiceModel.class);
        serviceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImageUrl()));
        String username = principal.getName();
        this.eventService.createEvent(serviceModel, username);
        return super.redirect("/events/all-events");
    }

    @GetMapping("/all-events")
    @PageTitle("All Event")
    public ModelAndView getAllEvent(ModelAndView modelAndView) {
        modelAndView.addObject("events", this.eventService.findAllEvents()
                .stream()
                .map(event -> this.modelMapper.map(event, EventViewModel.class))
                .collect(Collectors.toList()));

        return super.view("events/all-events", modelAndView);
    }

    @GetMapping("/my-events")
    @PageTitle("My Event")
    public ModelAndView getMyEvents(ModelAndView modelAndView, Principal principal) {
        List<EventViewModel> eventViewModels = eventService.findEventByUsername(principal.getName())
                .stream()
                .map(event -> this.modelMapper.map(event, EventViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("events", eventViewModels);

        return view("events/my-events", modelAndView);
    }

    @GetMapping("/edit-events/{id}")
    @PageTitle("Edit Event")
    public ModelAndView getEditEvent(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") EventAddModel model) {
        EventAddServiceModel eventServiceModel = this.eventService.findById(id);
        model = this.modelMapper.map(eventServiceModel, EventAddModel.class);

        modelAndView.addObject("model", model);
        modelAndView.addObject("id", id);

        return super.view("events/edit-events", modelAndView);
    }

    @PostMapping("/edit-events/{id}")
    @PageTitle("Edit Event")
    public ModelAndView editEventConfirm(@PathVariable String id, @ModelAttribute EventViewModel model) {

        this.eventService.editEvent(id, this.modelMapper.map(model, EventAddServiceModel.class));

        return super.redirect("/events/detail-events/" + id);
    }

    @GetMapping("/delete-events/{id}")
    @PageTitle("Delete Event")
    public ModelAndView getDeleteEvent(@PathVariable String id, ModelAndView modelAndView, @ModelAttribute(name = "model") EventAddModel model) {
        EventAddServiceModel eventServiceModel = this.eventService.findById(id);
        model = this.modelMapper.map(eventServiceModel, EventAddModel.class);

        modelAndView.addObject("model", model);
        modelAndView.addObject("id", id);

        return super.view("events/delete-events", modelAndView);
    }

    @PostMapping("/delete-events/{id}")
    @PageTitle("Delete Event")
    public ModelAndView deleteEventConfirm(@PathVariable String id) {
        this.eventService.deleteEvent(id);

        return super.redirect("/events/my-events");
    }

    @GetMapping("/detail-events/{id}")
    @PageTitle("Details Event")
    public ModelAndView detailsEvent(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("model", this.modelMapper.map(this.eventService.findById(id), EventViewModel.class));

        return super.view("events/detail-events", modelAndView);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<EventViewModel> fetchAllEvents() {
        return this.eventService.findAllEvents()
                .stream()
                .sorted((e1, e2) -> {
                    DateTime firstDate = DateTime.parse(e1.getDate());
                    DateTime secondDate = DateTime.parse(e2.getDate());
                    return firstDate.compareTo(secondDate);
                })
                .map(q -> this.modelMapper.map(q, EventViewModel.class))
                .collect(Collectors.toList());
    }

    @ExceptionHandler({EventNotFoundException.class})
    public ModelAndView handleEventNotFound(EventNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }

    @ExceptionHandler({EventNameAlreadyExistsException.class})
    public ModelAndView handleEventExist(EventNameAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }
}

