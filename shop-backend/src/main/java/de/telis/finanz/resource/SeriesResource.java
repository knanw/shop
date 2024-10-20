package de.telis.finanz.resource;


import de.telis.finanz.entity.Book;
import de.telis.finanz.entity.Series;
import de.telis.finanz.repository.BookRepository;
import de.telis.finanz.repository.SeriesRepository;
import de.telis.finanz.resource.dto.BookDTO;
import de.telis.finanz.resource.dto.SeriesDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/series")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeriesResource {
    protected Logger log = Logger.getLogger(SeriesResource.class);

    @Inject
    SeriesRepository seriesRepository;
    @Inject
    BookResource bookResource;
    @Inject
    BookRepository bookRepository;

    @GET
    public Response getAll() {
        return Response.ok(seriesRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        if (id == null) {
            return Response.status(404).build();
        }

        return Response.ok(SeriesDTO.from(seriesRepository.findById(id))).build();
    }

    @POST
    public Response post(SeriesDTO series) {
        try {

            Series s = new Series();
            s.setName(series.getName());
            s.setGenre(series.getGenre());
            s.setDescription(series.getDescription());

            seriesRepository.save(s);
            if (series.getBooks() != null) {
                for (BookDTO b : series.getBooks()) {
                    Book book = bookRepository.findById(b.getId());

                    if (book != null) {
                        s.addBook(book);
                        bookRepository.save(book);
                    } else {
                        log.warn("Book with ID " + b.getId() + " not found.");
                        return Response.status(404, "Book with ID " + b.getId() + " not found.").build();
                    }
                }
            }

            seriesRepository.save(s);

            return Response.ok(s).build();

        } catch (Exception ex) {
            log.info(ex);
            return Response.status(403, ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response put(@PathParam("id") Long id, Series series) {
        if (!id.equals(series.getId())) {
            throw new IllegalArgumentException("Ids not equal");
        }

        Series oldSeries = seriesRepository.findById(id);
        if (oldSeries == null) {
            throw new NotFoundException("Series not found");
        }

        System.out.println("serie:");
        System.out.println(series.getId());
        System.out.println(series.getName());

        Series newSeries = seriesRepository.save(series);
        return Response.ok(newSeries).build();
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Series series = seriesRepository.findById(id);
        if (series == null) {
            throw new NotFoundException("Series not found");
        }
        seriesRepository.delete(series);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Series addBookToSeries(@PathParam("id") Long seriesId, Book book) {
        System.out.println("addBookToSeries");
        return seriesRepository.addBookToSeries(seriesId, book);
    }

    @PUT
    @Path("/{id}/book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Series removeBookFromSeries(@PathParam("id") Long seriesId, Book book) {
        return seriesRepository.removeBookFromSeries(seriesId, book);
    }
}
