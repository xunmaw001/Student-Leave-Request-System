package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.entity.BanjiEntity;

import com.service.BanjiService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 班级表
 * 后端接口
 * @author
 * @email
 * @date 2021-02-23
*/
@RestController
@Controller
@RequestMapping("/banji")
public class BanjiController {
    private static final Logger logger = LoggerFactory.getLogger(BanjiController.class);

    @Autowired
    private BanjiService banjiService;

    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params){
        logger.debug("Controller:"+this.getClass().getName()+",page方法");
        PageUtils page = banjiService.queryPage(params);
        return R.ok().put("data", page);
    }
    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        logger.debug("Controller:"+this.getClass().getName()+",info方法");
        BanjiEntity banji = banjiService.selectById(id);
        if(banji!=null){
            return R.ok().put("data", banji);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody BanjiEntity banji, HttpServletRequest request){
        logger.debug("Controller:"+this.getClass().getName()+",save");
        Wrapper<BanjiEntity> queryWrapper = new EntityWrapper<BanjiEntity>()
            .eq("name", banji.getName())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        BanjiEntity banjiEntity = banjiService.selectOne(queryWrapper);
        if(banjiEntity==null){
            banjiService.insert(banji);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody BanjiEntity banji, HttpServletRequest request){
        logger.debug("Controller:"+this.getClass().getName()+",update");
        //根据字段查询是否有相同数据
        Wrapper<BanjiEntity> queryWrapper = new EntityWrapper<BanjiEntity>()
            .notIn("id",banji.getId())
            .eq("name", banji.getName())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        BanjiEntity banjiEntity = banjiService.selectOne(queryWrapper);
        if(banjiEntity==null){
            banjiService.updateById(banji);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }


    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        logger.debug("Controller:"+this.getClass().getName()+",delete");
        banjiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}

