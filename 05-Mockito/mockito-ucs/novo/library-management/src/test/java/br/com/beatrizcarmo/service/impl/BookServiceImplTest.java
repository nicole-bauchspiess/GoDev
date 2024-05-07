package br.com.beatrizcarmo.service.impl;

import br.com.beatrizcarmo.dto.BookDto;
import br.com.beatrizcarmo.dto.mapper.BookMapper;
import br.com.beatrizcarmo.exceptions.NotFoundException;
import br.com.beatrizcarmo.exceptions.WrongParametersException;
import br.com.beatrizcarmo.repository.BookRepository;
import br.com.beatrizcarmo.repository.UserRepository;
import org.apache.tomcat.jni.Local;
import org.junit.Before;
import org.junit.Test;
import br.com.beatrizcarmo.models.Book;
import br.com.beatrizcarmo.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


public class BookServiceImplTest {


    @InjectMocks //só vai ter um injectMock que é a classe que efetivamente estamos testando
    public BookServiceImpl service;

    @Mock //pode ter vários, vai injetar as dependencias mocks que a nossa classe injectMock precisa
    BookRepository repository;

    @Captor
    ArgumentCaptor<Book> bookArgumentCaptor;

    @Mock
    UserRepository userRepository;

    @Mock
    BookMapper mapper;

    @Mock
    BookDto bookDto;

    @Before
    public void setUp() {
        initMocks(this);
    }

    //exemplo1
    @Test
    public void checkBookingPossibility_shouldBePossible() {
        Book book = new Book();
        User user = new User();
        user.setIsPunished(false);
        book.setIsBorrowed(false);

        boolean result = service.checkBookingPossibility(user, book);
        Assertions.assertTrue(result);
        ;
    }

    @Test
    public void checkBookingPossibility_bookIsBorredBeNull() {
        Book book = new Book();
        User user = new User();
        user.setIsPunished(false);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.checkBookingPossibility(user, book));

    }

    @Test
    public void calcuateDiscountBasedOnPercentage_shouldReturnFice() {
        Book book = new Book();
        book.setCost(50.0f);
        Double discount = service.calculateDiscountBasedOnPercentage(book, 10.0);
        assertThat(discount).isEqualTo((5.0));
    }


    @Test
    public void verifyifBookIsBorowed_shouldBookIsPresent() {
        Book book = new Book();
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");
        book.setIsBorrowed(false);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        boolean result = service.verifyIfBookIsBorrowed(id);

        assertThat(result).isEqualTo(false);
    }

    @Test
    public void verifyifBookIsBorowed_shouldNotBePresent() {
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");

        when(repository.findById(id)).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> service.verifyIfBookIsBorrowed(id));

        assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

    }

    @Test
    public void verifiyIfIsPossibleToBuyBookWitgValue_shouldBookIsPresentAndValueGraterBookCost() {
        Book book = new Book();
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");
        book.setCost(50.0f);

        when(repository.findBookById(id)).thenReturn(Optional.of(book));

        boolean result = service.verifyIfIsPossibleToBuyBookWithValue(id, 60.0f);
        assertThat(result).isTrue();

    }

    @Test
    public void verifiyIfIsPossibleToBuyBookWitgValue_shouldBookIsPresentAndValueLowerrBookCost() {
        Book book = new Book();
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");
        book.setCost(50.0f);

        when(repository.findBookById(id)).thenReturn(Optional.of(book));

        boolean result = service.verifyIfIsPossibleToBuyBookWithValue(id, 40.0f);
        assertThat(result).isFalse();

    }

    @Test
    public void verifiyIfIsPossibleToBuyBookWitgValue_shoudBookIsNotPresent() {
        Book book = new Book();
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");

        book.setCost(50.0f);

        when(repository.findBookById(id)).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> service.verifyIfIsPossibleToBuyBookWithValue(id, 50f));
        assertThat(exception).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");
    }

    @Test
    public void deleteBook() {
        Book book = new Book();
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");
        when(repository.findById(id)).thenReturn(Optional.of(book));

        service.deletBook(id);
        verify(repository).delete(book); //verifica se o metodo delete do mock repository foi chamado
    }

    @Test
    public void deleteBookNotPresent() {
        UUID id = UUID.fromString("89cd6d14-bff1-4d7c-8638-b884b21b334");
        when(repository.findById(id)).thenReturn(Optional.empty());


        Throwable throwable = catchThrowable(() -> service.deletBook(id));
        verify(repository).findById(id);

        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

    }

    //*********************FIM EXEMPLOS *******************************


    //JUNIT 1.1
    @Test
    @DisplayName("OK-> Deveria retornar usuários responsáveis pelo empréstimo")
    public void getUsersResponsibleForBorrowed() {
        List<Book> books = adicionaLivros();

        List<User> novaLista = service.getUsersResponsibleForBorrowed(books);
        assertThat(novaLista.get(0)).isEqualTo(books.get(0).getUser());
        assertThat(novaLista.get(1)).isEqualTo(books.get(1).getUser());
    }

    //JUNIT 1.2
    @Test
    @DisplayName("OK-> Deveria retornar a lista vazia quando nao tem livro emprestado")
    public void getUsersResponsibleForBorrowed_empty() {
        Book book = new Book();
        List<Book> books = new ArrayList<>();
        books.add(book);
        book.setIsBorrowed(false);
        List<User> novaLista = service.getUsersResponsibleForBorrowed(books);
        assertThat(novaLista.size()).isEqualTo(0);
    }


    //JUNIT 2.1
    @Test
    @DisplayName("OK-> Deveria retornar a quantidade de livros emprestados")
    public void countNumberOfBorrowedBooks() {
        List<Book> books = adicionaLivros();
        assertThat(2L).isEqualTo(service.countNumberOfBorrowedBooks(books));
    }

    //JUNIT 3.1
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando não lista estiver nula")
    public void calculateTotalCostOfBooks_listOfBooksIsNull(){
        List<Book> books = null;
        Throwable throwable = catchThrowable(()-> service.calculateTotalCostOfBooks(books));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum livro foi encontrado");
    }

    //JUNIT 3.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando não lista estiver vazia")
    public void calculateTotalCostOfBooks_listOfBooksIsEmpty(){
        List<Book> books = new ArrayList<>();
        Throwable throwable = catchThrowable(()-> service.calculateTotalCostOfBooks(books));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhum livro foi encontrado");
    }

    //JUNIT 3.3
    @Test
    @DisplayName("OK-> Deveria somar o preço de todos os livros")
    public void calculateTotalCostOfBooks_shouldReturn500(){
        List<Book> books = getNewBooks();
        assertThat(service.calculateTotalCostOfBooks(books)).isEqualTo(500f);
    }

    //JUNIT 3.4
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar somar o preço de todos os livros e o custo estiver zerado")
    public void calculateTotalCostOfBooks_bookCostIsZero(){
        List<Book> books = getNewBooks();
        books.get(0).setCost(null);

        Throwable throwable = catchThrowable(()-> service.calculateTotalCostOfBooks(books));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Livro cadastrado sem preço");
    }

    //JUNIT 4.1
    @Test
    @DisplayName("OK-> Deveria retornar 100 ao tentar pegar o maior valor dos livros")
    public void getMaxBooksCost_shuldReturn100() {
        Book book = new Book();
        book.setCost(50f);
        Book b1 = new Book();
        b1.setCost(100f);
        List<Book> lista = new ArrayList<>();
        lista.add(b1);
        lista.add(book);
        Assertions.assertEquals(100, service.getMaxBooksCost(lista), 1.00);
    }

    //JUNIT 4.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar pegar o maior valor dos livros e o custo estiver zerado")
    public void getMaxBooksCost_costIsZero() {
        List<Book> lista = adicionaLivros();

        lista.get(0).setCost(0.0f);
        lista.get(1).setCost(0.0f);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> service.getMaxBooksCost(lista));
        Assertions.assertEquals("Nenhum preço cadastrado", exception.getMessage());
    }


    //JUNIT 5.1
    @Test
    public void getNumberOfYearsReleased_yearIsNull(){
        Book book = new Book();
        Throwable throwable = catchThrowable(()-> service.getNumberOfYearsReleased(book));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Ano de lançamento não encontrado");

    }

    //JUNIT 5.2
    @Test
    public void getNumberOfYearsReleased_yearIsAfterThanNow(){
        Book book = new Book();
        book.setYearEdition(LocalDate.now().plusYears(2));
        Throwable throwable = catchThrowable(()-> service.getNumberOfYearsReleased(book));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Ano de lançamento depois de hoje");

    }

    //JUNIT 5.3
    @Test
    public void getNumberOfYearsReleased(){
        Book book = new Book();
        LocalDate data = LocalDate.now().minusYears(2);
        book.setYearEdition(data);
        assertThat(service.getNumberOfYearsReleased(book)).isEqualTo(2);
    }


    //JUNIT 6
    @Test
    @DisplayName("Deveria retornar lista de usuarios com data de devolução atrasada ")
    public void getUsersWithBookWithLateDevolutionDate(){
        User user = new User();
        User user2 = new User();
        Book book = new Book();
        Book book2 = new Book();

        book.setUser(user);
        book2.setUser(user2);
        book.setDevolutionDate(LocalDate.now().minusMonths(2));
        book2.setDevolutionDate(LocalDate.now().minusMonths(3));

        List<Book> lista = new ArrayList<>();
        lista.add(book);
        lista.add(book2);

        List<User> users = new ArrayList<>();
        users.add(book.getUser());
        users.add(book2.getUser());

        assertThat(service.getUsersWithBookWithLateDevolutionDate(lista)).isEqualTo(users);
    }

    //JUNIT 6 -> teste vai falhar
    @Test
    @DisplayName("OK-> Não deveria retornar lista de usuários quando a data não estiver atrasada")
    public void getUsersWithBookWithLateDevolutionDate_(){
        User user = new User();
        User user2 = new User();
        Book book = new Book();
        Book book2 = new Book();

        book.setUser(user);
        book2.setUser(user2);
        book.setDevolutionDate(LocalDate.now().plusMonths(2));
        book2.setDevolutionDate(LocalDate.now().plusMonths(3));

        List<Book> lista = new ArrayList<>();
        lista.add(book);
        lista.add(book2);

        List<User> users = new ArrayList<>();
        users.add(book.getUser());
        users.add(book2.getUser());

        assertThat(service.getUsersWithBookWithLateDevolutionDate(lista).size()).isEqualTo(0);
    }

    //JUNIT 6.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando o livro nao tiver usuário informado")
    public void getUsersWithBookWithLateDevolutionDate_dateIsNull(){
        Book book = new Book();
        book.setName("BookName");
        book.setDevolutionDate(LocalDate.now().plusMonths(3));
        List<Book> lista = new ArrayList<>();
        lista.add(book);

        Throwable throwable = catchThrowable(()-> service.getUsersWithBookWithLateDevolutionDate(lista));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro BookName possui data de devolução mas não tem usuário relacionado.");
    }

    //JUNIT 7
    @Test
    @DisplayName("OK-> Deveria retornar q quantidade de livros alugado pelo usuario")
    public void getNumberOfBooksRentedByUser(){
        User user = new User();
        List<Book> books = getNewBooks();
        books.forEach(b-> b.setUser(user));

        assertThat(service.getNumberOfBooksRentedByUser(books,user)).isEqualTo(10);
    }

    //JUNIT 7.1
    @Test
    @DisplayName("Not OK-> Deveria retornar zero quando o usuario informado nao tiver livros alugados")
    public void getNumberOfBooksRentedByUser_shouldReturn0(){
        User user = new User();
        User user2 = new User();
        List<Book> books = getNewBooks();
        books.forEach(b-> b.setUser(user));

        assertThat(service.getNumberOfBooksRentedByUser(books,user2)).isEqualTo(0);
    }






//**********************MOCKITO*************************


    //MOCKITO 1

    @Test
    @DisplayName("OK-> Livro deveria retornar como 'emprestado' após ser vinculado ao usuário")
    public void lendBookToUser_shouldSetBookToBorrowed() {
        User user = new User();
        Book book = new Book();
        UUID id = UUID.randomUUID();
        book.setIsBorrowed(false);
        UUID userId = UUID.randomUUID();
        user.setIsPunished(false);


        when(repository.findById(id)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        service.lendBookToUser(userId, id);
        verify(repository).save(book);

        assertTrue(book.getIsBorrowed());
    }

    //MOCKITO 1.1
    @Test
    @DisplayName("OK-> Deveria emprestar o livro para o usuário")
    public void lendBookToUser_shouldLendTheBookToUser() {
        User user = new User();
        Book book = new Book();
        UUID id = UUID.randomUUID();
        book.setIsBorrowed(false);
        UUID userId = UUID.randomUUID();
        user.setIsPunished(false);


        when(repository.findById(id)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        service.lendBookToUser(userId, id);
        verify(repository).save(book);

        assertAll(
                ()-> assertEquals(user, book.getUser()),
                ()-> assertFalse(book.getIsBorrowed())
        );

    }

    //MOCKITO 1.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando tentar emprestar um livro que não existe")
    public void lendBookToUser_bookNotFound() {
        Book book = new Book();
        UUID id = UUID.randomUUID();
        book.setIsBorrowed(true);
        UUID userId = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> service.lendBookToUser(userId, id));
        Assertions.assertEquals("The object was not found", exception.getMessage());
    }

    //MOCKITO 1.3
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando tentar emprestar livro e não localizar o usuário")
    public void lendBookToUser_userNotFound() {
        Book book = new Book();
        User user = new User();

        UUID id = UUID.randomUUID();
        book.setIsBorrowed(true);
        UUID userId = UUID.randomUUID();
        user.setIsPunished(false);

        when(repository.findById(id)).thenReturn(Optional.of(book));
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> service.lendBookToUser(userId, id));
        Assertions.assertEquals("The object was not found", exception.getMessage());
    }

    //MOCKITO 1.4
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando tenta emprestar livro que já está emprestado")
    public void lendBookToUser_bookIsBorred() {
        Book book = new Book();
        User user = new User();

        UUID id = UUID.randomUUID();
        book.setIsBorrowed(true);
        UUID userId = UUID.randomUUID();
        user.setIsPunished(false);

        when(repository.findById(id)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        book.setIsBorrowed(true);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> service.lendBookToUser(userId, id));
        Assertions.assertEquals("Livro já foi emprestado", exception.getMessage());
    }

    //MOCKITO 1.5
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando tenta emprestar livro ao usuário que está punido")
    public void lendBookToUser_userIsPunished() {
        Book book = new Book();
        User user = new User();

        UUID id = UUID.randomUUID();
        book.setIsBorrowed(false);
        UUID userId = UUID.randomUUID();
        user.setIsPunished(true);

        when(repository.findById(id)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> service.lendBookToUser(userId, id));
        Assertions.assertEquals("O usuário não está autorizado para pegar novos livros", exception.getMessage());
    }

    //MOCKITO 2.1
    @Test
    @DisplayName("OK-> Deveria atualizar o preço do livro")
    public void updateBookPriceAccordingYearEdition_shouldUpdate() {
        Book book = new Book();
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(book));

        book.setYearEdition(LocalDate.of(2022, 10, 10));
        book.setCost(50f);

        service.updateBookPriceAccordingYearEdition(id);

        verify(repository).save(bookArgumentCaptor.capture());
        Book result = bookArgumentCaptor.getValue();

        assertAll(
                ()-> assertThat(result).isNotNull(),
                ()-> assertEquals(book, result),
                ()-> assertThat(book.getCost()).isEqualTo(49f)
        );
    }

    //ano atual -> nao tem desconto
    //custo <= 0 -> validar

    //MOCKITO 2.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar alterar o preço de um livro que não existe")
    public void updateBookPriceAccordingYearEdition_bookNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> service.updateBookPriceAccordingYearEdition(id));
        Assertions.assertEquals("The object was not found", exception.getMessage());
    }

    //MOCKITO 2.3
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar alterar o preço de acordo com o ano de lançamento, se o ano não for informado")
    public void updateBookPriceAccordingYearEdition_dateIsnull() {
        Book book = new Book();
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(book));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> service.updateBookPriceAccordingYearEdition(id));
        Assertions.assertEquals("Ano de lançamento não encontrado", exception.getMessage());
    }

    //MOCKITO 2.4
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar alterar o preço de acordo com o ano de lançamento, se o custo original estiver nulo")
    public void updateBookPriceAccordingYearEdition_costIsnull() {
        Book book = new Book();
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(book));
        book.setYearEdition(LocalDate.of(2022, 10, 10));
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> service.updateBookPriceAccordingYearEdition(id));
        Assertions.assertEquals("Custo do livro não encontrado", exception.getMessage());
    }

    //MOCKITO 2.4 -> teste vai falhar
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar alterar o preço de acordo com o ano de lançamento, se o custo original estiver zero")
    public void updateBookPriceAccordingYearEdition_costIsZero() {
        Book book = new Book();
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(book));
        book.setYearEdition(LocalDate.of(2022, 10, 10));
        book.setCost(0f);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> service.updateBookPriceAccordingYearEdition(id));
        Assertions.assertEquals("Custo do livro não encontrado", exception.getMessage());
    }



    //MOCKITO 3.1
    @Test
    @DisplayName("OK-> Deveria inserir o livro")
    public void insertBook_shouldInsert(){
        BookDto bookDto1 = getBooksDto().get(0);
        Book book1 = getNewBooks().get(0);

        when(mapper.toEntity(bookDto1)).thenReturn(book1);
        when(repository.save(book1)).thenReturn(book1);
        when(mapper.toDto(book1)).thenReturn(bookDto1);

        verify(repository).save(book1);
    }

    //MOCKITO 3.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar inserir o livro com nome nulo")
    public void insertBook_nameIsEmpty(){
        BookDto bookDto1 = new BookDto();
        bookDto1.name="";
        Throwable throwable = catchThrowable(()-> service.insertBook(bookDto1));
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

    }

    //MOCKITO 3.3
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar inserir o livro com autor estiver nulo")
    public void insertBook_authorIsEmpty(){
        BookDto bookDto1 = new BookDto();
        bookDto1.name="name";
        bookDto1.author="";
        Throwable throwable = catchThrowable(()-> service.insertBook(bookDto1));
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

    }

    //MOCKITO 4
    @Test
    @DisplayName("OK-> Deveria retornar a lista de livros")
    public void getBooks_shouldReturnList() {
        List<Book> lista = getNewBooks();
        List<BookDto> bookDtos = getBooksDto();

        when(repository.findAll()).thenReturn(lista);
        when(mapper.toDto(lista)).thenReturn(bookDtos);
        service.getBooks();
        verify(repository).findAll();
    }

    //MOCKITO 5.1
    @Test
    public void getBookById_shouldReturnDTO() {
        Book book1 = getNewBooks().get(0);
        BookDto bookDto = getBooksDto().get(0);

        when(repository.findById(book1.getId())).thenReturn(Optional.of(book1));
        when(mapper.toDto(book1)).thenReturn(bookDto);

        service.getBookById(book1.getId());
        verify(repository).findById(book1.getId());
    }


    //MOCKITO 5.2
    @Test
    public void getBookById_bookNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> service.getBookById(id));
        verify(repository).findById(id);
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");
    }


    //MOCKITO 6
    @Test
    public void updateBook_nameIsNull() {
        UUID id = UUID.randomUUID();
        BookDto bookDto1 = new BookDto();

        Throwable throwable = catchThrowable(() -> service.updateBook(bookDto1, id));
        assertThat(throwable).isInstanceOf(WrongParametersException.class).hasMessage("The parameters are wrong");
    }

    //MOCKITO 6.1
    @Test
    public void updateBook_nameIsEmpty() {
        UUID id = UUID.randomUUID();
        BookDto bookDto1 = new BookDto();
        bookDto1.name = "";

        Throwable throwable = catchThrowable(() -> service.updateBook(bookDto1, id));
        assertThat(throwable).isInstanceOf(WrongParametersException.class).hasMessage("The parameters are wrong");
    }



    //MOCKITO 6.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando o autor estiver em branco")
    public void updateBook_authorIsEmpty() {
        UUID id = UUID.randomUUID();
        BookDto bookDto1 = new BookDto();
        bookDto1.name = "nome";
        bookDto1.author = "";

        Throwable throwable = catchThrowable(() -> service.updateBook(bookDto1, id));
        assertThat(throwable).isInstanceOf(WrongParametersException.class).hasMessage("The parameters are wrong");
    }

    //MOCKITO 6.3
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando o autor estiver em branco")
    public void updateBook_authorIsNull() {
        UUID id = UUID.randomUUID();
        BookDto bookDto1 = new BookDto();
        bookDto1.name = "nome";
        bookDto1.author = null;

        Throwable throwable = catchThrowable(() -> service.updateBook(bookDto1, id));
        assertThat(throwable).isInstanceOf(WrongParametersException.class).hasMessage("The parameters are wrong");
    }

    //MOCKITO 6.4
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção quando nao encontrar o livro")
    public void updateBook_bookNotFound() {
        UUID id = UUID.randomUUID();
        BookDto bookDto1 = new BookDto();
        bookDto1.name = "nome";
        bookDto1.author = "autor";

        when(repository.findById(id)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> service.updateBook(bookDto1, id));
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");
    }

    //MOCKITO 6.5
    @Test
    @DisplayName("OK-> Deveria atualizar o livro")
    public void updateBook_shouldUpdate() {

        BookDto bookDto1 = getBooksDto().get(1);
        Book book1 = getNewBooks().get(0);
        when(repository.findById(book1.getId())).thenReturn(Optional.of(book1));
        when(mapper.toDto(book1)).thenReturn(bookDto1);
        when(repository.save(book1)).thenReturn(book1);

        verify(repository).save(book1);
    }


    //MOCKITO 7.1
    @Test
    @DisplayName("OK-> Deveria remover os emprestimos do usuário")
    public void removeUserLoans_shouldRemoveLoans() {
        User user = new User();
        List<Book> books = getNewBooks();

        Book book = books.get(0);
        book.setIsBorrowed(true);
        book.setUser(user);
        UUID id = UUID.randomUUID();

        when(repository.findAll()).thenReturn(books);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        service.removeUserLoans(id);

        verify(repository).findAll();
        verify(repository).save(book);


        assertNull(book.getUser());
        assertNull(book.getDevolutionDate());
        //verificar outros atributos do livro
        //ter mais de um livro
    }

    //MOCKITO 7 -> NAO ESTÁ SETANDO O LIVRO COMO NAO EMPRESTADO
    @Test
    @DisplayName("OK-> Deveria deixar o livro como nao emprestado após remover o empréstimo do usuário")
    public void removeUserLoans_isBorrowedShouldBeFalse() {
        User user = new User();
        List<Book> books = getNewBooks();

        Book book = books.get(0);
        book.setIsBorrowed(true);
        book.setUser(user);
        UUID id = UUID.randomUUID();

        when(repository.findAll()).thenReturn(books);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        service.removeUserLoans(id);
        assertFalse(book.getIsBorrowed());
    }

    //MOCKITO 7.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar tirar o emprestimo do usuário que nao existe")
    public void removeUserLoans_userIsNull(){
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(()-> service.removeUserLoans(id));
        assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("The object was not found");

    }

    //MOCKITO 7.3
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar tirar o emprestimo do usuário que nao tem livro vinculado")
    public void removeUserLoans_userBooksIsEmpty() {
        User user = new User();
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Throwable throwable = catchThrowable(() -> service.removeUserLoans(id));
        verify(repository).findAll();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Não há nenhum livro emprestado para esse usuário");
    }


    //MOCKITO 8
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar calcular multa quando o isBorrowed está false")
    public void calculatePenaltyAfterSixMonths_borrowedIsFalse(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();

        when(repository.findAll()).thenReturn(listaLivros);

        listaLivros.get(0).setUser(user);
        listaLivros.get(0).setIsBorrowed(false);
        user.setIsPunished(true);

        Throwable throwable = catchThrowable(()-> service.calculatePenaltyAfterSixMonths(user));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro está associado ao usuário, mas não está emprestado");
    }

    //MOCKITO 8.1
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar calcular multa quando o isBorrowed está nulo")
    public void calculatePenaltyAfterSixMonths_borrowedIsNull(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();

        when(repository.findAll()).thenReturn(listaLivros);

        listaLivros.get(0).setUser(user);
        listaLivros.get(0).setIsBorrowed(null);
        user.setIsPunished(true);

        Throwable throwable = catchThrowable(()-> service.calculatePenaltyAfterSixMonths(user));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro está associado ao usuário, mas não está emprestado");
    }


    //MOCKITO 8.2
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar calcular multa quando a data de devolução está nula")
    public void calculatePenaltyAfterSixMonths_devolutionDateIsNull(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();

        when(repository.findAll()).thenReturn(listaLivros);

        Book book1 = listaLivros.get(0);
        book1.setUser(user);
        book1.setIsBorrowed(true);
        user.setIsPunished(true);
        book1.setDevolutionDate(null);

        Throwable throwable = catchThrowable(()-> service.calculatePenaltyAfterSixMonths(user));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro está associado ao usuário, mas não tem data de devolução");
    }

    //MOCKITO 8.3

    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar calcular multa quando o custo está nulo")
    public void calculatePenaltyAfterSixMonths_costIsNull(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();

        when(repository.findAll()).thenReturn(listaLivros);

        Book book1 = listaLivros.get(0);
        book1.setUser(user);
        book1.setIsBorrowed(true);
        user.setIsPunished(true);
        book1.setCost(null);
        book1.setDevolutionDate(LocalDate.now());
        Throwable throwable = catchThrowable(()-> service.calculatePenaltyAfterSixMonths(user));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro não possui custo");
    }

    //MOCKITO 8.3 -> teste vai falhar
    @Test
    @DisplayName("Not OK-> Deveria lançar exceção ao tentar calcular multa quando o custo está zero")
    public void calculatePenaltyAfterSixMonths_costIs0(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();
        user.setIsPunished(true);
        when(repository.findAll()).thenReturn(listaLivros);

        Book book1 = listaLivros.get(0);
        book1.setUser(user);
        book1.setIsBorrowed(true);
        book1.setDevolutionDate(LocalDate.now());
        book1.setCost(0f);


        Throwable throwable = catchThrowable(()-> service.calculatePenaltyAfterSixMonths(user));
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("O livro não possui custo");
    }

    //MOCKITO 8.4
    @Test
    @DisplayName("OK-> Deveria retornar 100 reais de multa quando o custo do livro for 50.0")
    public void calculatePenaltyAfterSixMonths_shouldReturn100(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();
        when(repository.findAll()).thenReturn(listaLivros);

        user.setIsPunished(true);

        Book book1 = listaLivros.get(0);
        book1.setUser(user);
        book1.setIsBorrowed(true);
        book1.setDevolutionDate(LocalDate.now().minusMonths(8));

        assertThat(service.calculatePenaltyAfterSixMonths(user)).isEqualTo(100f);
    }

    //MOCKITO 8.5
    @Test
    @DisplayName("OK-> Deveria retornar 0 reais de multa quando não passou 6 meses da data de devolução")
    public void calculatePenaltyAfterSixMonths_shouldReturn0(){
        List<Book> listaLivros = getNewBooks();
        User user = new User();
        when(repository.findAll()).thenReturn(listaLivros);

        user.setIsPunished(true);

        Book book1 = listaLivros.get(0);
        book1.setUser(user);
        book1.setIsBorrowed(true);
        book1.setDevolutionDate(LocalDate.now().minusMonths(2));

        assertThat(service.calculatePenaltyAfterSixMonths(user)).isEqualTo(0f);
    }

    private List<Book> getNewBooks() {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            book.setName("Livro " + i);
            book.setAuthor("Autor " + i);
            book.setDescription("Descrição " + i);
            book.setCost(50f);
            book.setIsBorrowed(false);
            book.setYearEdition(LocalDate.now());
            books.add(book);
        }
        return books;
    }

    private List<BookDto> getBooksDto() {
        List<BookDto> books = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            BookDto bookDto = new BookDto();
            bookDto.name = "Livro " + i;
            bookDto.author = "Autor " + i;
            bookDto.description = "Descrição " + i;
            bookDto.cost = 50f;
            bookDto.isBorrowed = false;
            bookDto.yearEdition = LocalDate.now();
            books.add(bookDto);
        }
        return books;


    }

    private List<Book> adicionaLivros() {
        User user = new User();
        List<User> users = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        Book book1 = new Book();
        User user1 = new User();

        book.setIsBorrowed(true);
        book1.setIsBorrowed(true);

        book1.setUser(user1);
        book.setUser(user);

        books.add(book);
        books.add(book1);
        users.add(book1.getUser());
        users.add(book.getUser());
        return books;
    }

}