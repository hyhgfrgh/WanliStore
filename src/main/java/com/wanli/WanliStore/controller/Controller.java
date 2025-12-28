package com.wanli.WanliStore.controller;

import com.fasterxml.jackson.core.JsonPointer;
import com.wanli.WanliStore.entity.GoodInfo;
import com.wanli.WanliStore.entity.Users;
import com.wanli.WanliStore.repository.GoodsRepository;
import com.wanli.WanliStore.repository.UsersRepository;
import com.wanli.WanliStore.utils.Result;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void add(Long id,String name, BigDecimal price, Integer stock, String category, String img_url,String introduce){
        GoodInfo u = new GoodInfo();
        u.belongTo = id;
        u.name = name;u.stock = stock;
        u.price = price;u.category = category;
        u.img_url = img_url;u.introduce = introduce;
        goodsRepo.save(u);
    }

    @GetMapping("/api/del")
    @Transactional
    public Result<String> del(Long id){
        goodsRepo.deleteById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/api/delAll")
    public Result<String> delAll(){
        goodsRepo.deleteAll();
        return Result.success("所有商品以全部删除");
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
    public Result<Users> login( String username,String password) {
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

        return Result.success(user);
    }
    @GetMapping("/api/register")
    public Result<String> register(String username, String password, String nickname,String start_time) throws Exception {
        if(userRepo.findByUsername(username) != null)
            return Result.fail("用户名已存在！");
        Users user = new Users();
        user.nickname = nickname;
        user.password = password;
        user.username = username;
        user.start_time = start_time;
        user.golds = BigDecimal.valueOf(0);
        userRepo.save(user);
        return Result.success("注册成功！");
    }
    @GetMapping("/api/remove")
    public Result<String> remove(Long id) {
        userRepo.deleteById(id);
        return Result.success("注销成功！");
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
    @GetMapping("/api/editUser")
    public Result<String> editUser(Long id,String avatar_url,String email,String nickname){
        Optional<Users> u = userRepo.findById(id);
        if(u.isPresent()){
            Users user = u.get();
            if(!(nickname == null || nickname.isEmpty())) user.nickname = nickname;
            user.avatar_url = avatar_url;
            if(!(email == null || email.isEmpty())) user.email = email;
            userRepo.save(user);
        }
        return Result.success("修改成功");
    }
    @GetMapping("/api/findUserGoods")
    public Result<List<GoodInfo>> findUserGoods(Long id){

        List<GoodInfo> goods = goodsRepo.findByBelongTo(id);

        return Result.success(goods);
    }

    @GetMapping("/api/topup")
    public Result<BigDecimal> topUp(Long user_id,String passwd){
        Optional<Users> user = userRepo.findById(user_id);
        if(user.isEmpty()) return Result.fail("用户不存在!");
        Users u = user.get();
        if(u.golds == null){
            u.golds = BigDecimal.ZERO;
        }
        switch (passwd) {
            case "wanli666" -> u.golds = u.golds.add(BigDecimal.valueOf(666666));
            case "wanli777" -> u.golds = u.golds.add(BigDecimal.valueOf(7777777));
            case "wanli888" -> u.golds = u.golds.add(BigDecimal.valueOf(88888888));
            default -> {
                return Result.fail("无效卡密");
            }
        }
        userRepo.save(u);
        return Result.success(u.golds);
    }



    @GetMapping("/api/transact")
    @Transactional
    public Result<String> findUserGoods(Long id_seller,Long id_buyer,Long id_good,Integer nums_buy){
        if(nums_buy == null || nums_buy <= 0){
            return Result.fail("购买数量不合法");
        }
        Optional<GoodInfo> good = goodsRepo.findById(id_good);
        if(good.isEmpty()){
            return Result.fail("商品不存在");
        }
        GoodInfo sellerGood = good.get();
        if(!sellerGood.belongTo.equals(id_seller)){
            return Result.fail("该商品不属于卖家，禁止交易");
        }
        if(sellerGood.stock == null || sellerGood.stock < nums_buy){
            return Result.fail("您所购买的商品存货不足以您的要求");
        }

        Optional<Users> sellerOpt = userRepo.findById(id_seller);
        Optional<Users> buyerOpt  = userRepo.findById(id_buyer);

        if(sellerOpt.isEmpty() || buyerOpt.isEmpty()){
            return Result.fail("交易双方账户异常");
        }

        Users seller = sellerOpt.get();
        Users buyer  = buyerOpt.get();

        if(buyer.golds == null) buyer.golds = BigDecimal.ZERO;
        if(seller.golds == null) seller.golds = BigDecimal.ZERO;

        BigDecimal totalPrice = sellerGood.price.multiply(new BigDecimal(nums_buy));

        if(buyer.golds.compareTo(totalPrice) < 0){
            return Result.fail("余额不足，请充值");
        }

        buyer.golds = buyer.golds.subtract(totalPrice);
        seller.golds = seller.golds.add(totalPrice);

        userRepo.save(buyer);
        userRepo.save(seller);

        sellerGood.stock -= nums_buy;
        goodsRepo.save(sellerGood);

        GoodInfo buyerGood = new GoodInfo();
        buyerGood.name = sellerGood.name;
        buyerGood.introduce = sellerGood.introduce;
        buyerGood.price = sellerGood.price;
        buyerGood.category = sellerGood.category;
        buyerGood.img_url = sellerGood.img_url;
        buyerGood.stock = nums_buy;
        buyerGood.belongTo = id_buyer;
        goodsRepo.save(buyerGood);

        return Result.success("购买成功");
    }



}
