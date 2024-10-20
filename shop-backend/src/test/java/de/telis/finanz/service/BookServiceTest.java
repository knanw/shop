package de.telis.finanz.service;

import de.telis.finanz.entity.Book;
import de.telis.finanz.repository.BookRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
@QuarkusTest
public class BookServiceTest {


    @InjectMock
    BookRepository bookRepository;

    @Test
    void getAllBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setIsbn("1234567890");
        book1.setAuthor("author1");
        book1.setPublisher("Publisher 1");
        book1.setDescription("Description 1");
        book1.setPublishedDate(LocalDate.now());
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setIsbn("12345678");
        book2.setAuthor("author2");
        book2.setPublisher("Publisher 2");
        book2.setDescription("Description 2");
        book2.setPublishedDate(LocalDate.now());

        List<Book> booksExpected = List.of(book1, book2);
        Mockito.when(bookRepository.listAll()).thenReturn(List.of(book1, book2));
        List<Book> books = bookRepository.listAll();

        Assertions.assertEquals(booksExpected, books);
    }
}
