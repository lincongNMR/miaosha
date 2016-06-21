package org.seckill.dao;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by User on 2016/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Test
    public void testInsert() throws Exception {
        System.out.println(this.successKilledDao.insertSuccessKilled(2L,1L));

    }

    public void testIsertSuccessKilled() throws Exception {

    }

    public void testQueryByIdWithSeckill() throws Exception {

    }
}