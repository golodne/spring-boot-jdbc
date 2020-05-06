package ru.db.springbootjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.db.springbootjdbc.dao.PersonDao;
import ru.db.springbootjdbc.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringBootJdbcApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringBootJdbcApplication.class, args);
		//for expire
		ApplicationContext context = SpringApplication.run(SpringBootJdbcApplication.class);
		PersonDao dao = context.getBean(PersonDao.class);
		System.out.println("count records in db: " + dao.count());

		Person person1 = dao.getById(1);
		System.out.println("person with id= 1 " + person1);

		List<String> emails = new ArrayList<>();
		emails.addAll(Arrays.asList("t1@mail.com", "t2@mail.com"));
		dao.insert(new Person(3, "Ivan", emails));

		Person person2 = dao.getById(3);
		System.out.println("person with id= 3 " + person2);
	}
}
