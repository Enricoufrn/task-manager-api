package com.example.taskmanagerapi.services;

import com.example.taskmanagerapi.domain.User;
import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import com.example.taskmanagerapi.domain.enumerations.Role;
import com.example.taskmanagerapi.dtos.UserDTO;
import com.example.taskmanagerapi.exceptions.ResourceException;
import com.example.taskmanagerapi.helper.MessageHelper;
import com.example.taskmanagerapi.helper.PasswordHelper;
import com.example.taskmanagerapi.helper.UserHelper;
import com.example.taskmanagerapi.repositories.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private final IUserRepository userRepository;
    private final UserHelper userHelper;
    private final PasswordHelper passwordHelper;
    private final MessageHelper messageHelper;

    public UserService(IUserRepository userRepository, UserHelper userHelper, PasswordHelper passwordHelper, MessageHelper messageHelper) {
        this.userRepository = userRepository;
        this.userHelper = userHelper;
        this.passwordHelper = passwordHelper;
        this.messageHelper = messageHelper;
    }
    public UserDTO save(UserDTO userDTO){
        User user = userDTO.toEntity();
        if (user.getId() != null){
            User foundedUser = this.findById(user.getId());
            user.setPassword(foundedUser.getPassword());
        }else{
            user.setPassword(this.passwordHelper.generatePassword(user.getPassword()));
        }
        User saved = this.save(user);
        return new UserDTO(saved);
    }
    private User save(User user){
        return this.userRepository.save(user);
    }
    public List<UserDTO> findAll(){
        List<User> users = this.userRepository.findAll();
        return users.stream().map(UserDTO::new).toList();
    }
    public UserDTO findUserById(UUID id){
        User user = this.findById(id);
        return new UserDTO(user);
    }
    private User findById(UUID id){
        return this.userRepository.findById(id).orElseThrow(() ->
                new ResourceException(this.messageHelper.getMessage(MessageCode.USER_NOT_FOUND), "id: "+ id, HttpStatus.NOT_FOUND));
    }
    public User findByLogin(String login) {
        return this.userRepository.findByLogin(login).orElseThrow(() ->
                new ResourceException(this.messageHelper.getMessage(MessageCode.USER_NOT_FOUND), "login: "+ login, HttpStatus.NOT_FOUND));
    }

    public void delete(UUID id){
        if (id == null)
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.BAD_REQUEST), "id: "+ id, HttpStatus.BAD_REQUEST);
        User loggedUser = this.userHelper.getLoggedUser();
        if (loggedUser.getId().equals(id))
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.USER_DELETE_SELF), "id: "+ id, HttpStatus.BAD_REQUEST);
        if (!loggedUser.getRole().equals(Role.ROLE_ADMIN)){
            throw new ResourceException(this.messageHelper.getMessage(MessageCode.PERMISSION_DENIED), "", HttpStatus.FORBIDDEN);
        }
        User user = this.findById(id);
        this.userRepository.delete(user);
    }
}
