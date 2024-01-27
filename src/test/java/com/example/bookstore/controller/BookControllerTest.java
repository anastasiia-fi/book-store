package com.example.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @SneakyThrows
    @AfterEach
    void afterAll(
            @Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-all-books.sql"));
        }
    }

    @Test
    @DisplayName("Create a new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void createBook_WithValidData_ShouldCreateValidBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Test book",
                "Test Author",
                "0123456789",
                BigDecimal.valueOf(100),
                "Description",
                "Cover Image",
                Set.of(1L)
        );
        Category category = new Category();
        category.setId(1L);

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.title());
        expected.setAuthor(requestDto.author());
        expected.setIsbn(requestDto.isbn());
        expected.setPrice(requestDto.price());
        expected.setDescription(requestDto.description());
        expected.setCoverImage(requestDto.coverImage());
        expected.setCategoryIds(requestDto.categoryIds());

        String jsonRequest;
        try {
            jsonRequest = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't read request", e);
        }

        MvcResult result = mockMvc.perform(
                post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(actual.getTitle(), expected.getTitle());
        assertEquals(actual.getAuthor(), expected.getAuthor());
        assertEquals(actual.getPrice(), expected.getPrice());
        assertEquals(actual.getDescription(), expected.getDescription());
        assertEquals(actual.getCoverImage(), expected.getCoverImage());
        assertEquals(actual.getCategoryIds(), expected.getCategoryIds());
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/insert-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void findAllBooks_WithValidData_ShouldReturnThreeBooks() throws Exception {

        BookDto a = new BookDto();
        a.setId(1L);
        a.setTitle("BookA");
        a.setAuthor("AuthorA");
        a.setIsbn("0123456789");
        a.setPrice(BigDecimal.valueOf(100.01));
        a.setDescription("DescriptionA");
        a.setCategoryIds(Set.of(1L));

        BookDto b = new BookDto();
        b.setId(2L);
        b.setTitle("BookB");
        b.setAuthor("AuthorB");
        b.setIsbn("9123456780");
        b.setPrice(BigDecimal.valueOf(200.01));
        b.setDescription("DescriptionB");
        b.setCategoryIds(Set.of(2L));

        BookDto c = new BookDto();
        c.setId(3L);
        c.setTitle("BookC");
        c.setAuthor("AuthorC");
        c.setIsbn("0923456781");
        c.setPrice(BigDecimal.valueOf(300.01));
        c.setDescription("DescriptionC");
        c.setCategoryIds(Set.of(1L));

        List<BookDto> expected = List.of(a, b, c);

        MvcResult result = mockMvc.perform(
                        get("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<BookDto>>(){});
        assertEquals(3, actual.size());
        assertThat(actual.contains(expected.get(0)));
        assertThat(actual.contains(expected.get(1)));
        assertThat(actual.contains(expected.get(2)));
    }

    @Test
    @DisplayName("Get book by id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/insert-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void getBookById_WithValidData_ShouldReturnValidBook() throws Exception {
        long id = 1L;

        MvcResult result = mockMvc.perform(
                        get("/api/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("title", "BookA")
                .hasFieldOrPropertyWithValue("author", "AuthorA")
                .hasFieldOrPropertyWithValue("isbn", "0123456789")
                .hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(100.01))
                .hasFieldOrPropertyWithValue("description", "DescriptionA");
    }

    @Test
    @DisplayName("Get book by invalid id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/insert-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void getBookById_WithInvalidData_ShouldThrowException() throws Exception {
        long id = 100L;

        MvcResult result = mockMvc.perform(
                        get("/api/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(result.getResolvedException().getMessage(), ("Can't find book with id " + id));
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/insert-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteBookById_WithValidData_ShouldDeleteBook() throws Exception {
        long id = 1L;
        MvcResult result = mockMvc.perform(
                        delete("/api/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Delete book without admin role")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(
            scripts = "classpath:database/add-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/insert-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteBookById_WithoutAdminRole_ShouldThrowException() throws Exception {
        long id = 1L;
        MvcResult result = mockMvc.perform(
                        delete("/api/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
