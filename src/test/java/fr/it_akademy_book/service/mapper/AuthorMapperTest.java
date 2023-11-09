package fr.it_akademy_book.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AuthorMapperTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    public void setUp() {
        authorMapper = new AuthorMapperImpl();
    }
}
