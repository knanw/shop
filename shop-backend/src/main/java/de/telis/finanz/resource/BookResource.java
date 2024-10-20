package de.telis.finanz.resource;

import de.telis.finanz.entity.Book;
import de.telis.finanz.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/book")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    protected Logger log = Logger.getLogger(BookResource.class);

    @Inject
    BookRepository bookRepository;

    public BookResource() {
    }

    @GET
    public Response getAll() {
        return Response.ok(bookRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return Response.ok(bookRepository.listAll()).build();
    }

    @POST
    public Response post(Book book) {
        try {
            bookRepository.save(book);
            return Response.ok(book).build();
        } catch (Exception ex) {
            log.info(ex);
            return Response.status(403, ex.getCause().getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response put(@PathParam("id") Long id, Book book) {

        if (!id.equals(book.getId())) {
            throw new IllegalArgumentException("Ids not equal");
        }

        Book oldBook = bookRepository.findById(id);
        if (oldBook == null) {
            throw new NotFoundException("Book not found");
        }

        Book newBook = bookRepository.save(book);
        return Response.ok(newBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        bookRepository.delete(book);
        return Response.ok().build();
    }

}
