package de.telis.finanz.repository;

import de.telis.finanz.entity.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@Transactional
@RequestScoped
public class BookRepository implements PanacheRepository<Book> {
    public Book save(Book b) {
       if (b.getId() == null || b.getId() == 0) {
        getEntityManager().persist(b);
    } else {
        b = getEntityManager().merge(b);
    }
        return b;
}

}
