package de.telis.finanz.resource;

import de.telis.finanz.entity.Book;
import de.telis.finanz.entity.Series;
import de.telis.finanz.repository.SeriesRepository;
import de.telis.finanz.resource.dto.SeriesDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ShoppingCarResourceTest {

    @Inject
    ShoppingCartResource shoppingCartResource;

    @InjectMock
    SeriesRepository seriesRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCalculateOptimalPrice_NoBooks() {
        List<Book> basket = Collections.emptyList();
        Response response = shoppingCartResource.calculateOptimalPrice(basket);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCalculateBasketPrice_NoBooks() {
        List<Book> books = Collections.emptyList();
        double price = ShoppingCartResource.calculateBasketPrice(books);

        assertEquals(0.0, price);
    }

    @Test
    public void testCalculateMinimumPrice_EmptyBooks() {
        List<Integer> books = Collections.emptyList();
        double price = ShoppingCartResource.calculateMinimumPrice(books, 0.0);

        assertEquals(0.0, price);
    }

    @Test
    public void testCalculateMinimumPrice_SingleBook() {
        List<Integer> books = Collections.singletonList(1);
        double price = ShoppingCartResource.calculateMinimumPrice(books, 0.0);

        assertEquals(8.0, price);
    }

    @Test
    public void testGetBooksBySeries() {
        Series series1 = new Series();
        series1.setId(1L);
        series1.setName("Series 1");
        series1.setBooks(new ArrayList<>());

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Book 3");

        series1.setBooks(Arrays.asList(book1, book2));
        Mockito.when(seriesRepository.listAll()).thenReturn(List.of(series1));

        List<Book> basket = Arrays.asList(book1, book2, book3);

        Map<Series, List<Book>> sortedBooks = shoppingCartResource.getBooksBySeries(basket);

        assertTrue(sortedBooks.containsKey(null));
        assertTrue(sortedBooks.containsKey(series1));
        assertEquals(2, sortedBooks.size());

        assertEquals(1, sortedBooks.get(null).size());
        assertEquals(book3, sortedBooks.get(null).getFirst());

        assertEquals(2, sortedBooks.get(series1).size());
        assertEquals(book2, sortedBooks.get(series1).getLast());
    }

    @Test
    void testGetBooksNotNull() {
        Series series1 = new Series();
        series1.setId(1L);
        series1.setName("Series 1");
        assertNotNull(SeriesDTO.from(series1).getBooks(), "Books list should not be null");
    }

    @Test
    void testGetBooksInitialSizeZero() {
        Series series1 = new Series();
        series1.setId(1L);
        series1.setName("Series 1");
        assertEquals(0, SeriesDTO.from(series1).getBooks().size(), "Books list should be empty initially");
    }

}