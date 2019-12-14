package kristian9577.cardealer.services.impl;

import kristian9577.cardealer.data.models.User;
import kristian9577.cardealer.data.repository.UsersRepository;
import kristian9577.cardealer.error.UserAlreadyExistException;
import kristian9577.cardealer.services.LogService;
import kristian9577.cardealer.services.RoleService;
import kristian9577.cardealer.services.UserService;
import kristian9577.cardealer.services.models.LogServiceModel;
import kristian9577.cardealer.services.models.UserServiceModel;
import kristian9577.cardealer.validation.user.UserRegisterValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final LogService logService;
    private final UsersRepository usersRepository;
    private final ModelMapper mapper;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRegisterValidator userRegisterValidator;

    @Autowired
    public UserServiceImpl(LogService logService, UsersRepository usersRepository, ModelMapper mapper, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRegisterValidator userRegisterValidator) {
        this.logService = logService;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRegisterValidator = userRegisterValidator;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        this.roleService.seedRolesInDb();
        if (this.usersRepository.count() == 0) {
            userServiceModel.setAuthorities(this.roleService.findAllRoles());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());

            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }
        User user = this.usersRepository
                .findByUsernameOrEmail(userServiceModel.getUsername(), userServiceModel.getEmail())
                .orElse(null);

        if (user != null) {
            throw new UserAlreadyExistException("User already exists");
        }
        user = mapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(user.getUsername());
        logServiceModel.setDescription("User registered");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);

        return this.mapper.map(this.usersRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel findUserByUserName(String username) {
        return this.usersRepository
                .findByUsername(username)
                .map(u -> this.mapper.map(u, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword) {
        User user = this.usersRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        if (!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password!");
        }

        user.setPassword(userServiceModel.getPassword() != null ?
                this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()) :
                user.getPassword());
        user.setEmail(userServiceModel.getEmail());
        this.usersRepository.flush();

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(user.getUsername());
        logServiceModel.setDescription("User profile edited");
        logServiceModel.setTime(LocalDateTime.now());

        this.logService.seedLogInDB(logServiceModel);
        return this.mapper.map(this.usersRepository.saveAndFlush(user), UserServiceModel.class);
    }

//    @Override
//    public UserServiceModel findById(String id) throws Exception {
//        return this.usersRepository
//                .findById(id)
//                .map(u -> this.mapper.map(u, UserServiceModel.class))
//                .orElseThrow(() -> new Exception("Invalid user"));
//    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.usersRepository.findAll()
                .stream()
                .map(u -> this.mapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    @Override
    public void setUserRole(String id, String role) {
        User user = this.usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id!"));

        UserServiceModel userServiceModel = this.mapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        switch (role) {
            case "user":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                break;
            case "moderator":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }

        this.usersRepository.saveAndFlush(this.mapper.map(userServiceModel, User.class));
    }
}
