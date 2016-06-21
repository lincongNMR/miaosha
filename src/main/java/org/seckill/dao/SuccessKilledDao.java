package org.seckill.dao;

import com.sun.net.httpserver.Authenticator;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by User on 2016/6/5.
 */
public interface SuccessKilledDao {

    int insert(SuccessKilled successKilled);

    int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

}
