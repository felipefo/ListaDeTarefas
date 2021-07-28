package com.todolist.edu.service.criteria.userid;

import com.todolist.edu.domain.Authority;
import com.todolist.edu.domain.User;
import com.todolist.edu.repository.UserRepository;
import com.todolist.edu.security.AuthoritiesConstants;
import com.todolist.edu.service.mapper.UserMapper;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tech.jhipster.service.Criteria;
import com.todolist.edu.service.dto.UserDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author felipe
 */
@Repository
public class UserOwnerRestrictions {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserOwnerRestrictions(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }
    
    
    @Transactional
    public boolean checkOwnerDTOId(Long userDTOId) throws Exception {
        User user = getCurrentUser();
        Set<Authority> authories = user.getAuthorities();
        for (Authority aut : authories) {
            if (aut.getName().equalsIgnoreCase(AuthoritiesConstants.ADMIN)) {
                return true;//admin can do everything.  
            }
        }
        if(user.getId().compareTo(userDTOId) == 0)
            return true;
        return false;
    }
    

    @Transactional
    public void setUserOwnerIDFilter(Criteria criteria , Class clazz, AddIdStrategy strategy) throws Exception {

        User user = getCurrentUser();
        Set<Authority> authories = user.getAuthorities();
        for (Authority aut : authories) {
            if (aut.getName().equalsIgnoreCase(AuthoritiesConstants.ADMIN)) {
                return;
            }
        }
        strategy.addUserIdFilter(criteria, clazz, user);
    }

    @Transactional
    private User getCurrentUser() throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        Optional<User> user = userRepository.findOneByLogin(username);
        if (!user.isPresent()) {
            throw new Exception("user not found " + username);
        }
        return user.get();
    }

    @Transactional
    public void setDTOUserId(AddIdStrategy strategy) throws Exception {
        User user = getCurrentUser();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        strategy.addUserId(userDTO);
    }
}
