export interface BookDTO {
    id: number | null;
    title: string | null;
    isbn: string | null;
    description: string | null;
    publisher: string | null;
    publishedDate: string | null;
}
export interface SeriesDTO {
    id: number;
    name: string;
    genre: string;
    description: string;
    books: Array<BookDTO[]>;
}