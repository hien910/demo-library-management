package com.example.demolibrarymanagement;

import com.example.demolibrarymanagement.model.entity.Author;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.Category;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.repository.AuthorRepository;
import com.example.demolibrarymanagement.repository.BookRepository;
import com.example.demolibrarymanagement.repository.CategoryRepository;
import com.example.demolibrarymanagement.repository.UserRepository;
import com.example.demolibrarymanagement.service.IBookedBookService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest

class DemoLibraryManagementApplicationTests {
	@Autowired
	private  BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private  CategoryRepository categoryRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void createBook(){
		List<Author> authorList = authorRepository.findAll();
		List<Category> categoryList = categoryRepository.findAll();
		Faker faker = new Faker();

		Random random = new Random();
		for (int i = 0; i < 30; i++) {
			Book book = Book.builder()
					.createdAt(new Date())
					.updatedAt(new Date())
					.title(faker.book().title())
					.quantity(random.nextInt(10)+10)
					.author(authorList.get(random.nextInt(authorList.size())))
					.category(categoryList.get(random.nextInt(categoryList.size())))
					.build();
			bookRepository.save(book);
		}
	}

	@Test
	void update_book() {
		Faker faker =new Faker();
		List<Book> books = bookRepository.findAll();
		books.forEach(b -> {
			b.setCode(faker.code().asin());
			bookRepository.save(b);
		});
	}


	@Test
	void update_password() {
		List<User> userList = userRepository.findAll();
		userList.forEach(user -> {
			user.setPassword(passwordEncoder.encode("123"));
			userRepository.save(user);
		});
	}

}
