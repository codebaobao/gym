package com.portal.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.portal.common.Role;
import com.portal.entity.User;

@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, String>{

    User findById(String id);

    void deleteById(String id);

    User findByName(String name);
    
    Page<User> findByRole(Pageable pageable, Role role);
}
