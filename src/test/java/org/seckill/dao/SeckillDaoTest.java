package org.seckill.dao;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2016/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest extends TestCase {

    @Autowired
    private SeckillDao seckillDao;

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }
    @Test
    public void testReduceNumber() throws Exception {
        long seckillId = 1L;
        int c = this.seckillDao.reduceNumber(seckillId,new Date());
        System.out.println(c);
    }
    @Test
    public void testQueryById() throws Exception {
        long id = 1;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }
    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(1,100);
        for(Seckill seckill: seckills){
            System.out.println(seckill);
        }
    }
}