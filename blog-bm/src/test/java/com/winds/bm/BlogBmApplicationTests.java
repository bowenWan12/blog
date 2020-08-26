package com.winds.bm;

import com.winds.bm.entity.User;
import com.winds.bm.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogBmApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    public void testDao(){
        User userById = userService.findUserById(1L);
        System.out.println(userById);
    }
    @Test
    public void contextLoads() {
    }

}
