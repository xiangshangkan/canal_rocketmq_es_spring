package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {

	}

	void test1() {
		BeanFactory bf = new XmlBeanFactory(new ClassPathResource("myTestBean.xml"));
		MyTestBean myTestBean = (MyTestBean)bf.getBean("myTestBean");
		System.out.print(myTestBean.getTestStr());
	}
}
