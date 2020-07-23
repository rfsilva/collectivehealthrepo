package br.com.collectivehealth.pollmanagement.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.collectivehealth.pollmanagement.dto.UserDTO;
import com.collectivehealth.pollmanagement.form.UserForm;
import com.collectivehealth.pollmanagement.repository.UserRepository;
import com.collectivehealth.pollmanagement.service.UserService;
import com.collectivehealth.pollmanagement.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void authenticateUserService() {
        Mockito.doReturn(Optional.ofNullable(null)).when(userRepository).findByUsername(Mockito.anyString());
        UserService userService = new UserServiceImpl(userRepository);
        UserDTO userDTO = userService.authenticate(UserForm.builder().username("abcde").password("12345").build());
        assertThat(userDTO.hasErrors()).isTrue();
        assertThat(userDTO.getError().getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
