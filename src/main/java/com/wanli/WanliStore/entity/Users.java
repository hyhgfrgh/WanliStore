package com.wanli.WanliStore.entity;


import jakarta.persistence.*;

@Entity
public class Users {
    // 不要用user命名，不然创建不出来表
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String username;
    public String password;
    public String nickname;
    public String avatar_url;
    public String email;
    public String start_time;

    public Users() {}
}


