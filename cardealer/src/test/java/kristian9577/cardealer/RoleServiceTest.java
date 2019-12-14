package kristian9577.cardealer;

import kristian9577.cardealer.base.BaseTest;
import kristian9577.cardealer.data.models.Role;
import kristian9577.cardealer.data.repository.RoleRepository;
import kristian9577.cardealer.services.RoleService;
import kristian9577.cardealer.services.models.RoleServiceModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoleServiceTest extends BaseTest {
    @MockBean
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;
//    @Test
//    public void seedRolesInBase_whenNoRoles_ShouldSeed(){
//        Mockito.when(roleRepository.count()).thenReturn(0L);
//        this.roleService.seedRolesInDb();
//
//        ArgumentCaptor<Role> argument = ArgumentCaptor.forClass(Role.class);
//        Mockito.verify(roleRepository).saveAndFlush(argument.capture());
//
//
//        List<Role> roles = argument.getAllValues();
//        Assert.assertEquals(4,roles.size());
//        Assert.assertEquals("ROLE_USER",roles.get(0).getAuthority().toString());
//        Assert.assertEquals("ROLE_MODERATOR",roles.get(1).getAuthority().toString());
//        Assert.assertEquals("ROLE_ADMIN",roles.get(2).getAuthority().toString());
//        Assert.assertEquals("ROLE_ROOT",roles.get(3).getAuthority().toString());
//
//    }
    @Test
   public void findAllRole_whenRolesExists_shouldReturnCorrect(){
        Role role1=new Role("ROLE_USER");
        Role role2=new Role("ROLE_MODERATOR");
        Role role3=new Role("ROLE_ADMIN");
        Role role4=new Role("ROLE_ROOT");
        List<Role> roles=new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        roles.add(role3);
        roles.add(role4);

        Mockito.when(roleRepository.findAll()).thenReturn(roles);
        Set<RoleServiceModel>result=roleService.findAllRoles();

        Assert.assertEquals(roles.size(),result.size());
    }

    @Test
   public void findByAuthority_whenValidInput_shouldReturnCorrect(){
        String authority="moderator";
        Role role=new Role();
        role.setAuthority("ROLE_Moderator");
        Mockito.when(roleRepository.findByAuthority(authority)).thenReturn(role);
        RoleServiceModel result=roleService.findByAuthority(authority);

        Assert.assertEquals(role.getAuthority(),result.getAuthority());
    }
}