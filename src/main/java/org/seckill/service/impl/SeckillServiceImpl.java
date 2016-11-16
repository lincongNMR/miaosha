package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by linyitian on 2016/6/22.
 */
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = RuntimeException.class)
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5
    private final String slat = "asfasdfasfasdfasdfasdfasdfkjzxl;cjv;lajks;f";

    public List<Seckill> getSeckillList() {
        return this.seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return this.seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(null==seckill){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = this.getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    public String getMD5(long seckillId){
        String base = seckillId + "/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


//    @Transactional(rollbackFor = RuntimeException.class,isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRED)
    public void test1() throws Exception{
        this.successKilledDao.updateTest();
        int age = this.successKilledDao.queryAge(1);
        logger.info("age ===="+ age);

        throw new RuntimeException("rollBack?");

    }

    @Override
    public void test() throws Exception{
        test1();

    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if(md5==null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        //执行秒杀逻辑 ：１.　减库存　　２.　记录购买行为
        try{
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount<=0){
                //did not make it
                throw new SeckillCloseException("seckill is closed");
            }else{
                //record purchase behaviour
                int insertCount = this.successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount<=0){
                    throw new RepeatKillException("repeat kill by "+userPhone);
                }else{
                    SuccessKilled successKilled = this.successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error : "+e.getMessage());
        }
    }
}