package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2016/6/5.
 */
@Component("seckillDao")
public interface SeckillDao {

    int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);

    Seckill queryById(long seckillId);

    List<Seckill> queryAll(@Param("offset")int offset,@Param("limit") int limit);

}
