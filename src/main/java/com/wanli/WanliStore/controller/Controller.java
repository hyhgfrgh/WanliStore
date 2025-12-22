package com.wanli.WanliStore.controller;

import com.fasterxml.jackson.core.JsonPointer;
import com.wanli.WanliStore.entity.GoodInfo;
import com.wanli.WanliStore.entity.Users;
import com.wanli.WanliStore.repository.GoodsRepository;
import com.wanli.WanliStore.repository.UsersRepository;
import com.wanli.WanliStore.utils.Result;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController()
@CrossOrigin(origins = "*")

public class Controller {
    @Autowired
    private GoodsRepository goodsRepo;
    @Autowired
    private UsersRepository userRepo;

    @GetMapping("/api/add")
    public void add(String name, BigDecimal price, Integer stock, String category, String img_url,String introduce){
        GoodInfo u = new GoodInfo();
        u.name = name;u.stock = stock;
        u.price = price;u.category = category;
        u.img_url = img_url;u.introduce = introduce;
        goodsRepo.save(u);
    }

    @GetMapping("/api/del")
    @Transactional

    public void del(Long id){
        goodsRepo.deleteById(id);
    }

    @GetMapping("/api/delAll")
    public void delAll(){
        goodsRepo.deleteAll();
    }

    @GetMapping("/api/list")
    public Result<List<GoodInfo>> list(){
        return Result.success(goodsRepo.findAll());
    }

    @GetMapping("/api/update")
    public void update(Long id,String name, BigDecimal price, Integer stock, String category, String img_url,String introduce){
        Optional<GoodInfo> u = goodsRepo.findById(id);
        if(u.isPresent()){
            GoodInfo good = u.get();
            if(!(name == null || name.isEmpty())) good.name = name;
            if(!(introduce == null || introduce.isEmpty())) good.introduce = introduce;
            if(!(stock == null)) good.stock = stock;
            if(!(price == null)) good.price = price;
            if(!(category == null || category.isEmpty())) good.category = category;
            if(!(img_url == null || img_url.isEmpty())) good.img_url = img_url;
            goodsRepo.save(good);
        }
    }

    @GetMapping("/api/find")
    public GoodInfo find(Long id){
        Optional<GoodInfo> u = goodsRepo.findById(id);
        return u.orElseGet(GoodInfo::new);
    }

    @GetMapping("/api/auth/login")
    public Result<String> login( String username,String password) {
        if(username == null || username.isEmpty())
            return Result.fail("用户名不能为空");
        if(password == null || password.isEmpty())
            return Result.fail("密码不能为空");
        Users user = userRepo.findByUsername(username);
        if(user == null)
            return Result.fail("用户不存在");

        // 校验密码,后续补充加密密码的校验
        if(!password.equals(user.password))
            return Result.fail("密码错误");

        return Result.success("登录成功");
    }
    @GetMapping("/api/register")
    public Result<String> register(String username, String password, String nickname) throws Exception {
        if(userRepo.findByUsername(username) != null)
            return Result.fail("用户名已存在！");
        Users user = new Users();
        user.nickname = nickname;
        user.password = password;
        user.username = username;
        userRepo.save(user);
        return Result.success("注册成功！");
    }

    @GetMapping("/api/showUsers")
    public Result<List<Users>> ShowUsers(String username, String password, String nickname) throws Exception {
        return Result.success(userRepo.findAll());
    }
    @GetMapping("/api/delAllUser")
    public Result<String> DelAllUser() throws Exception {
        userRepo.deleteAll();
        return Result.success("全部删除完毕");
    }
}
