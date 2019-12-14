package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.data.models.User;
import kristian9577.cardealer.data.repository.UsersRepository;
import kristian9577.cardealer.services.UserService;
import kristian9577.cardealer.services.models.UserServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest extends BaseTest {
    private List<User> usersList;

    @Autowired
    private UserService service;
    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private UsersRepository usersRepository;

    @Before
    public void setupTest() {
        usersList = new ArrayList<>();
        when(usersRepository.findAll())
                .thenReturn(usersList);
    }
    @Test
    public void findAllEvents_whenNoEvents_returnEmpty() {
        usersList.clear();
        List<UserServiceModel> allEvents = service.findAllUsers();
        Assert.assertTrue(allEvents.isEmpty());
    }

    @Test
    public void findOneEvents_whenOneEvents_IdAdd() {
        usersList.clear();

        User user = new User();

        usersList.add(user);

        List<UserServiceModel> results = service.findAllUsers();
        assertEquals(1, results.size());
    }
    @Test
    public void findOneEvents_whenOneEvents_IdAdd1() {
        usersList.clear();

        User user = new User();
        user.setUsername("name");

        usersList.add(user);
        assertThrows(Exception.class,
                () -> service.findUserByUserName(""));
    }
    @Test
    public void findOneEvents_whenOneEvents_IdAdd11() {
        usersList.clear();

        User user = new User();
        user.setUsername("name");

        usersList.add(user);
        UserServiceModel serviceModel=modelMapper.map(user,UserServiceModel.class);
        serviceModel.setUsername("name");

        assertThrows(Exception.class,
                () -> service.register(serviceModel));
    }
    @Test
    public void findOneEvents_whenOneEvents_Edit() {
        usersList.clear();

        User user = new User();
        user.setUsername("name");

        usersList.add(user);
        UserServiceModel serviceModel=modelMapper.map(user,UserServiceModel.class);
        serviceModel.setUsername("name");

        assertThrows(Exception.class,
                () -> service.editUserProfile(serviceModel,"1"));
    }

    @Test
    public void findOneEvents_whenOneEvents_loadByUsername() {
        usersList.clear();
        assertThrows(Exception.class,
                () -> service.loadUserByUsername("name"));
    }

}
