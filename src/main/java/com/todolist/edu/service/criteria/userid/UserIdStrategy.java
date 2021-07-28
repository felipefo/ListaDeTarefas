
package com.todolist.edu.service.criteria.userid;

import com.todolist.edu.domain.User;
import com.todolist.edu.service.dto.UserDTO;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.LongFilter;

/**
 *
 * @author felipe
 */
public class UserIdStrategy implements AddIdStrategy{
    private Object dto;
    private Class clazz;
    
    public UserIdStrategy( Object dto, Class clazz){
        this.dto = dto;
        this.clazz = clazz;
    }
    
    @Override
    public void addUserId(UserDTO userDTO) {
        try {
            Method sumInstanceMethod =  clazz.getMethod("setDono", UserDTO.class); 
            sumInstanceMethod.invoke(clazz.cast(this.dto), userDTO);
        } catch (Exception ex) {
            Logger.getLogger(UserIdStrategy.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

    @Override
    public void addUserIdFilter(Criteria criteria, Class clazz, User user) throws Exception {

        LongFilter filter = new LongFilter();
        filter.setEquals(user.getId());
        Method sumInstanceMethod =  clazz.getMethod("setDonoId", LongFilter.class);
        sumInstanceMethod.invoke(clazz.cast(criteria), filter);
        
    }

    
}
