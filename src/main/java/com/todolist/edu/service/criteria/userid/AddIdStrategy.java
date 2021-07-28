
package com.todolist.edu.service.criteria.userid;

import com.todolist.edu.domain.User;
import com.todolist.edu.service.dto.UserDTO;
import tech.jhipster.service.Criteria;
/**
 *
 * @author felipe
 */
public interface AddIdStrategy {
    
    
    public void addUserId(UserDTO userDTO);
    
    public void addUserIdFilter(Criteria criteria, Class clazz, User user) throws Exception;
    
}
