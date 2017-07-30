//package com.example.dao;
//
//import static org.hamcrest.Matchers.greaterThan;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.junit.Assert.assertThat;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.junit.ClassRule;
//import org.junit.Rule;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.rules.SpringClassRule;
//import org.springframework.test.context.junit4.rules.SpringMethodRule;
//
//import com.example.entity.Sample;
//
//@SpringBootTest
//public class SampleDaoTest {
//    @ClassRule
//    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
//
//    @Rule
//    public final SpringMethodRule springMethodRule = new SpringMethodRule();
//    
//    @Autowired
//    private SampleDao dao;
//
////    public static final int port = H2DatabaseServer.getRandomPort();
//
//    // @ClassRule
//    // public static H2DatabaseServer server = new
//    // H2DatabaseServer(DbUnitSampleTest.class, port);
//    //
//    // @Rule
//    // public DbUnitTestHelper helper = new DbUnitTestHelper(this.getClass(),
//    // port);
//
//    @Test
//    public void test_findAll() {
//        List<Sample> samples = dao.findAll();
//        assertThat(samples.size(), greaterThan(0));
//
//        Sample sample = samples.get(0);
//        assertThat(sample, notNullValue());
//        assertThat(sample.getId(), is(1));
//        assertThat(sample.getName(), is("hoge"));
//    }
//
//    @Test
//    public void test_find() {
//        Sample sample = dao.find(1);
//        assertThat(sample, notNullValue());
//        assertThat(sample.getName(), is("hoge"));
//    }
//
//    @Test
//    public void test_save() {
//        final Sample sample = new Sample();
//        sample.setName("hoge fuga foobar");
//        sample.setCreatedAt(LocalDateTime.now());
//        sample.setVersion(0);
//
//        dao.save(sample);
//
//    }
//}
