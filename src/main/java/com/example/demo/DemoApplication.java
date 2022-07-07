package com.example.demo;

import com.example.demo.output.EsUnifiedOrderOutput;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
	@Autowired
	private EsUnifiedOrderService esUnifiedOrderService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		// test1();

		// test2();


	}

	/**
	 * 测试容器及Bean加载过程
	 */
	public static void test1() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfigration.class);
		System.out.print(ctx.getBean(String.class));
		/*BeanFactory bf = new XmlBeanFactory(new ClassPathResource("myTestBean.xml"));
		MyTestBean myTestBean = (MyTestBean)bf.getBean("myTestBean");
		System.out.print(myTestBean.getTestStr());*/

		/*ApplicationContext ctx = new ClassPathXmlApplicationContext("myTestBean.xml");
		MyTestBean myTestBean = (MyTestBean)ctx.getBean("myTestBean");
		System.out.print(myTestBean.getTestStr());*/
	}

	/**
	 * 测试jdbc
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void test2() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest", "root", "root");
			Statement statement = connection.createStatement();
			statement.executeUpdate("insert into test(name)" + "values('曹操')");

			ResultSet resultSet = statement.executeQuery("select * from test");
			System.out.print(resultSet.getString("name"));
			connection.close();

		} catch (Exception e) {
			System.out.print(e.toString());
		} finally {
		}
	}
	@RequestMapping("/hello")
	public String hello() {
		return "hello world";
	}

	@RequestMapping("/testEss")
	public List<EsUnifiedOrderOutput> testEs(
			@RequestBody EsUnifiedOrderInput input
	) throws IOException {
		return esUnifiedOrderService.pageListEsUnifiedOrders(input);
	}

}
