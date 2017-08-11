package entity;

import com.seveniu.AuthConfig;
import com.seveniu.entity.User;
import com.seveniu.entity.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by seveniu on 7/29/17.
 * *
 */
@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = FrameworkConfig.class)
//@ContextConfiguration(classes = FrameworkConfig.class, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest(classes = AuthConfig.class)
//@ActiveProfiles("test")
public class UserServiceImplTest {
    @Autowired
    UserService userService;

    @Test
    public void createUser() throws Exception {
    }

    @Test
    public void findAll() throws Exception {
        Page<User> userPage = userService.findAll(new PageRequest(1, 10));
        System.out.println(userPage);
    }

}