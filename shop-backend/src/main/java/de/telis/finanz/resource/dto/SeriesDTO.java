package de.telis.finanz.resource.dto;

import de.telis.finanz.entity.Series;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Data
public class SeriesDTO {
    public SeriesDTO() {}

    private Long id;
    private String name;
    private String genre;
    private String description;
    private List<BookDTO> books;

    public static SeriesDTO from(Series series) {
        return series == null ? null: SeriesDTO.builder()
                .id(series.getId())
                .name(series.getName())
                .genre(series.getGenre())
                .description(series.getDescription())
                .books(series.getBooks() != null ? series.getBooks().stream().map(BookDTO::from).collect(Collectors.toList()): new ArrayList<>())
                .build();
    }
}
