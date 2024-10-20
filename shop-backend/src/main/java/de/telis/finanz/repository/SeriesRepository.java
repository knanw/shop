package de.telis.finanz.repository;

import de.telis.finanz.entity.Book;
import de.telis.finanz.entity.Series;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@Transactional
@RequestScoped
public class SeriesRepository implements PanacheRepository<Series> {

    public Series addBookToSeries(Long seriesId, Book book) {
        Series series = findById(seriesId);
        series.addBook(book);
        return getEntityManager().merge(series);
    }

    public Series removeBookFromSeries(Long seriesId, Book book) {
        Series series = findById(seriesId);
        series.removeBook(book);
        return getEntityManager().merge(series);
    }

    public Series save(Series s) {
        if (s.getId() == null || s.getId() == 0) {
            getEntityManager().persist(s);
        } else {
            s = getEntityManager().merge(s);
        }
        return s;
    }
}
