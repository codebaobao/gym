package com.portal.webapp.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.portal.webapp.entity.User;

@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, String>{

    User findById(String id);

    void deleteById(String id);

    User findByName(String name);
}
