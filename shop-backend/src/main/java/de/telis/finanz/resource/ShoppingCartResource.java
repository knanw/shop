package de.telis.finanz.resource;

import de.telis.finanz.entity.Book;
import de.telis.finanz.entity.Series;
import de.telis.finanz.repository.SeriesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/shop")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartResource {

    @Inject
    SeriesRepository seriesRepository;

    final static double BOOK_PRICE = 8.0;
    final static double[] DISCOUNTS = {0, 0.05, 0.10, 0.20, 0.25};

    @POST
    @Path("/calculate")
    @Produces(MediaType.TEXT_PLAIN)
    public Response calculateOptimalPrice(List<Book> basket) {

        if (basket == null || basket.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Map<Series, List<Book>> sortedBasket = getBooksBySeries(basket);



        double totalPrice = 0.0;

        for (Map.Entry<Series, List<Book>> entry : sortedBasket.entrySet()) {
            totalPrice += calculateBasketPrice(entry.getValue());
        }

        return Response.ok(String.format("Der Gesamtpreis des Warenkorbs beträgt: %.2f EUR%n", totalPrice)).build();
    }

    public static double calculateBasketPrice(List<Book> books) {
        if (books.isEmpty()) {
            return 0.0;
        }

        Map<Long, Integer> bookCounts = new HashMap<>();
        for (Book book : books) {
            bookCounts.put(book.getId(), bookCounts.getOrDefault(book.getId(), 0) + 1);
        }

        List<Integer> counts = new ArrayList<>(bookCounts.values());
        return calculateMinimumPrice(counts, 0);
    }

    public static double calculateMinimumPrice(List<Integer> books, double accumulatedPrice) {
        if (books.isEmpty() || books.stream().allMatch(count -> count == 0)) {
            return accumulatedPrice;
        }

        double minPrice = Double.MAX_VALUE;

        for (int setSize = 1; setSize <= 5; setSize++) {
            List<Integer> remainingBooks = new ArrayList<>(books);
            Set<Integer> bookSet = new HashSet<>();

            for (int i = 0; i < remainingBooks.size() && bookSet.size() < setSize; i++) {
                if (remainingBooks.get(i) > 0 && bookSet.add(i)) {
                    remainingBooks.set(i, remainingBooks.get(i) - 1);
                }
            }

            if (!bookSet.isEmpty()) {
                double discount = DISCOUNTS[bookSet.size() - 1];
                double setPrice = bookSet.size() * BOOK_PRICE * (1 - discount);
                minPrice = Math.min(minPrice, calculateMinimumPrice(remainingBooks, accumulatedPrice + setPrice));
            }
        }
        return minPrice;
    }

    public Map<Series, List<Book>> getBooksBySeries(List<Book> basket) {
        Map<Series, List<Book>> booksBySeries = new HashMap<>();
        List<Book> booksWithoutSeries = new ArrayList<>(basket);

        for (Series series : seriesRepository.listAll()) {
            List<Book> seriesBooks = series.getBooks();
            for (Book book : seriesBooks) {
                long countInBasket = basket.stream().filter(b -> b.equals(book)).count();
                if (countInBasket > 0) {
                    booksBySeries.computeIfAbsent(series, k -> new ArrayList<>())
                            .addAll(Collections.nCopies((int) countInBasket, book));
                    booksWithoutSeries.removeIf(b -> b.equals(book));
                }
            }
        }

        booksBySeries.put(null, booksWithoutSeries);
        return booksBySeries;
    }

}