package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在“使用者”角度设计接口
 * 1.方法定义粒度
 * 2.参数
 * 3.返回类型
 * Created by linyitian on 2016/6/22.
 */
public interface SeckillService {
    List<Seckill> getSeckillList();

    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
            throws SeckillException,SeckillCloseException,RepeatKillException;

    String getMD5(long seckillId);
}
