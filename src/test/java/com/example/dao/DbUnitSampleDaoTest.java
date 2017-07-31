package com.example.dao;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.example.entity.DbUnitSample;

import test_utils.DbMode;
import test_utils.DbUnitTestHelper;
import test_utils.H2DatabaseServer;

@SpringBootTest //(classes = DbUnitSampleDaoTest.TestConfig.class)
@DirtiesContext
public class DbUnitSampleDaoTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    
    @Autowired
    private DbUnitSampleDao dao;

    public static final int port = H2DatabaseServer.getRandomPort();

     @ClassRule
     public static H2DatabaseServer server = new H2DatabaseServer(port);
    
     @Rule
     public DbUnitTestHelper helper = new DbUnitTestHelper(server.getUrl(), DbMode.NONE);

     //普通のテスト
    @Test
    public void test_findAll() {
        List<DbUnitSample> samples = dao.findAll();
        assertThat(samples.size(), greaterThan(0));

        DbUnitSample sample = samples.get(0);
        assertThat(sample, notNullValue());
        assertThat(sample.getId(), is(1));
        assertThat(sample.getFirstName(), is("yushi"));
        assertThat(sample.getLastName(), is("koga"));
    }

    //普通のテスト
    @Test
    public void test_find() {
        DbUnitSample sample = dao.find(1);
        assertThat(sample, notNullValue());
        assertThat(sample.getFirstName(), is("yushi"));
        assertThat(sample.getLastName(), is("koga"));
    }

    @Test
    public void test_save() {
        final DbUnitSample sample = new DbUnitSample();
        sample.setFirstName("tomonori");
        sample.setLastName("okanoya");
        LocalDateTime current = LocalDateTime.now();
        sample.setCreatedAt(current);
        sample.setCreatedBy("junit");
        sample.setCreatedAt(current);
        sample.setCreatedBy("junit");

        dao.save(sample);

    }
    
    @Configuration
    public static class TestConfig {
        @Bean
        @Primary
        public DataSource dataSource() {
            return new DriverManagerDataSource(server.getUrl(),"sa","as");
        }
        
        @Bean
        @Primary
        public Dialect dialect() {
            return new H2Dialect();
        }
    }
}
