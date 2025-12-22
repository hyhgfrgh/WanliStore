package com.wanli.WanliStore.repository;

import com.wanli.WanliStore.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<Users,Long> {

    Users findByUsername(String username);

}
