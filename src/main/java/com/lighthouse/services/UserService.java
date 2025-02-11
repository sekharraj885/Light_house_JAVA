package com.lighthouse.services;

import com.lighthouse.DTOs.UserDTO;
import com.lighthouse.models.User;
import com.lighthouse.repositories.UserRepository;
import com.lighthouse.utils.Query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(User user){
        User createdUser = userRepository.save(user);
        return new UserDTO(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getEmail().toLowerCase(Locale.ROOT),
                createdUser.getRole(),
                createdUser.getCreatedBy(),
                createdUser.getCreatedOn(),
                createdUser.getLastUpdatedBy(),
                createdUser.getLastUpdatedOn()
        );
    }

    public Page<UserDTO> getAlluser(String firstName,String lastName, String email, String role, int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdOn").descending());
        Specification<User> spec = UserQuery.filterUsers(firstName, lastName, email, role);
        Page<User> userList = userRepository.findAll(spec, pageable);
        return userList.map(user->
                new UserDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedBy(),
                        user.getCreatedOn(),
                        user.getLastUpdatedBy(),
                        user.getLastUpdatedOn()
                ));
    }

    public Optional<UserDTO> getUserById(UUID id){
        return  userRepository.findById(id).map(user->
                new UserDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedBy(),
                        user.getCreatedOn(),
                        user.getLastUpdatedBy(),
                        user.getLastUpdatedOn()
                ));
    }

    public UserDTO updateUser(UUID id, User user){
        User existingUser = userRepository.findById(id)
                   .orElseThrow(()-> new RuntimeException("Invalid User Id"));

       if(user.getFirstName() !=null){
           existingUser.setFirstName(user.getFirstName());
       }
        if(user.getLastName() !=null){
            existingUser.setLastName(user.getLastName());
        } if(user.getRole() !=null){
            existingUser.setRole(user.getRole());
        } if(user.getPassword() !=null){
            existingUser.setPassword(user.getPassword());
        }
        User updatedUser = userRepository.save(existingUser);
        return new UserDTO(
                updatedUser.getId(),
                updatedUser.getEmail().toLowerCase(Locale.ROOT),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getRole(),
                updatedUser.getCreatedBy(),
                updatedUser.getCreatedOn(),
                updatedUser.getLastUpdatedBy(),
                updatedUser.getLastUpdatedOn()
        );

    }

}
