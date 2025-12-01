package com.wanli.hellospringboot.repository;

import com.wanli.hellospringboot.entity.GoodInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<GoodInfo,Long> {


    void deleteByName(String name);

    List<GoodInfo> findByName(String name);
}
