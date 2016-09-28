package org.seckill.service.impl;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by linyitian on 2016/6/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest extends TestCase {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> list = this.seckillService.getSeckillList();

        logger.info("list={}",list);
    }
    @Test
    public void testGetById() throws Exception {
        long id = 1;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }
    @Test
    public void testExportSeckillUrl() throws Exception {
        long id = 1;
        Exposer exposer = this.seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
    }
    @Test
    public void testExecuteSeckill() throws Exception {
        long id = 1;
        long phone = 18211099999L;
        String md5 = this.seckillService.getMD5(id);

        SeckillExecution seckillExecution = this.seckillService.executeSeckill(id,phone,md5);

        logger.info("SeckillExecution={}",seckillExecution);

    }
}