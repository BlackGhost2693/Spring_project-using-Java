package kristian9577.cardealer.web.controllers;

import kristian9577.cardealer.error.UserNotFoundException;
import kristian9577.cardealer.services.UserService;
import kristian9577.cardealer.services.models.RoleServiceModel;
import kristian9577.cardealer.services.models.UserServiceModel;
import kristian9577.cardealer.validation.user.UserEditValidator;
import kristian9577.cardealer.web.annotations.PageTitle;
import kristian9577.cardealer.web.models.RegisterUserModel;
import kristian9577.cardealer.web.models.UserAllViewModel;
import kristian9577.cardealer.web.models.UserEditViewModel;
import kristian9577.cardealer.web.models.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private ModelMapper modelMapper;
    private UserService userService;
    private UserEditValidator userEditValidator;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService, UserEditValidator userEditValidator) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userEditValidator = userEditValidator;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView register() {
        return super.view("users/register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public String registerConfirm(@ModelAttribute RegisterUserModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            return "redirect:/users/register";
        }
        this.userService.register(this.modelMapper.map(model, UserServiceModel.class));

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView login() {
        return super.view("users/login");
    }


    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Profile")
    public ModelAndView profile(ModelAndView modelAndView, Principal principal) {

        UserServiceModel userServiceModel = this.userService.findUserByUserName(principal.getName());
        UserProfileViewModel model = this.modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("model", model);

        return super.view("users/profile", modelAndView);
    }

    @GetMapping("/edit-profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView, @ModelAttribute(name = "model") UserEditViewModel model) {
        UserServiceModel userServiceModel = this.userService.findUserByUserName(principal.getName());
        model = this.modelMapper.map(userServiceModel, UserEditViewModel.class);
        model.setPassword(null);
        modelAndView.addObject("model", model);

        return super.view("users/edit-profile", modelAndView);
    }

    @PostMapping("/edit-profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView editProfileConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") UserEditViewModel model, BindingResult bindingResult) {
        this.userEditValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            model.setOldPassword(null);
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);

            return super.view("users/edit-profile", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(model, UserServiceModel.class);
        this.userService.editUserProfile(userServiceModel, model.getOldPassword());

        return super.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UserAllViewModel> users = this.userService.findAllUsers()
                .stream()
                .map(u -> {
                    UserAllViewModel user = this.modelMapper.map(u, UserAllViewModel.class);
                    Set<String> authorities = u.getAuthorities()
                            .stream()
                            .map(RoleServiceModel::getAuthority)
                            .collect(Collectors.toSet());
                    user.setAuthorities(authorities);

                    return user;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return super.view("users/all-users", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.setUserRole(id, "user");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable String id) {
        this.userService.setUserRole(id, "moderator");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.setUserRole(id, "admin");

        return super.redirect("/users/all");
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ModelAndView handleOfferNotFound(UserNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }
}
