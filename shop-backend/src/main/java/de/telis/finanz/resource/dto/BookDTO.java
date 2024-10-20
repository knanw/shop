package de.telis.finanz.resource.dto;

import de.telis.finanz.entity.Book;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Data
public class BookDTO {
    public BookDTO() {}

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String description;
    private LocalDate publishedDate;
    public static BookDTO from(Book book) {
        return book == null ? null: BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .publishedDate(book.getPublishedDate() != null ? book.getPublishedDate(): null)
                .description(book.getDescription())
                .build();
    }

    public static Book off(BookDTO dto) {
        if (dto == null) { return null; };
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setIsbn(dto.getIsbn());
        book.setPublishedDate(dto.getPublishedDate() != null ? dto.getPublishedDate(): null);
        book.setDescription(dto.getDescription());
        return book;
    }
}
