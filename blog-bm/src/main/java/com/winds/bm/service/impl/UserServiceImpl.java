package com.winds.bm.service.impl;

import com.winds.bm.entity.User;
import com.winds.bm.repository.UserRepository;
import com.winds.bm.service.UserService;
import com.winds.common.util.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public User getUserById(Long id) {


        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public Long saveUser(User user) {

        Map<String, String> saltPassWdMap = PasswordHelper.encryptPassword(user.getPassword());
        int index = new Random().nextInt(6) + 1;
        String avatar = "/static/user/user_" + index + ".png";

        user.setAvatar(avatar);
        user.setSalt(saltPassWdMap.get("salt"));
        user.setPassword(saltPassWdMap.get("passwd"));
        return userRepository.save(user).getId();
    }


    @Override
    @Transactional
    public Long updateUser(User user) {
        User oldUser = userRepository.findById(user.getId()).get();
        oldUser.setNickname(user.getNickname());
        return oldUser.getId();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
