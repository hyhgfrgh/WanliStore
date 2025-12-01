package com.wanli.hellospringboot.controller;

import com.wanli.hellospringboot.entity.GoodInfo;
import com.wanli.hellospringboot.entity.User;
import com.wanli.hellospringboot.repository.GoodsRepository;
import com.wanli.hellospringboot.repository.UsersRepository;
import com.wanli.hellospringboot.utils.Encrypt;
import com.wanli.hellospringboot.utils.Result;
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
    public Boolean login(){





        return true;
    }
    @GetMapping("/api/register")
    public Result<String> register(String username, String password, String nickname) throws Exception {
        if(userRepo.findByUsername(username) != null)
            return Result.fail("用户已存在！");
        User user = new User();
        user.nickname = nickname;
        user.password = Encrypt.hash(password, "MD5");
        user.username = username;
        userRepo.save(user);
        return Result.success("注册成功！");
    }

}
