## Djurinschi Andrei I2302 
### Spring Boot Hibernate

### Цель:

Разработай Spring Boot приложение для управления библиотекой, где будут 3 контроллера, 3 сервиса и 5 взаимосвязанных сущностей. Приложение должно поддерживать CRUD-операции через REST API и использовать Hibernate для работы с базой данных.
API должен работать с DTO а не с entity 
Author:
Один автор может написать много книг (One-to-Many).
Publisher:
Один издатель может издать много книг (One-to-Many).
Book:
Каждая книга принадлежит одному автору (Many-to-One).
Каждая книга имеет одного издателя (Many-to-One).
Каждая книга может принадлежать к нескольким категориям (Many-to-Many).
Category:
Одна категория может быть связана с несколькими книгами (Many-to-Many).
Library:
Библиотека содержит коллекцию книг в виде списка (ElementCollection).

### Выполнение

Hibernate — это фреймворк для Java, предназначенный для работы с базами данных. Он реализует подход ORM (Object-Relational Mapping) — отображение объектов на реляционные таблицы. Hibernate позволяет разработчику взаимодействовать с базой данных, используя обычные Java-классы, без необходимости вручную писать SQL-запросы.
Зачем нужен Hibernate
Упрощение доступа к данным
Вместо SQL-запросов вы работаете с Java-объектами, а Hibernate автоматически преобразует их в SQL и обратно.

Автоматическое управление связями
Например, @OneToMany, @ManyToOne — Hibernate сам позаботится о JOIN-операциях и каскадных действиях.

Кэширование данных
Повышает производительность, уменьшая количество обращений к БД.

Поддержка транзакций
Интеграция с JTA, Spring, и другими менеджерами транзакций.

Портируемость
SQL-запросы Hibernate пишет сам, и они адаптированы под конкретную СУБД (MySQL, PostgreSQL, Oracle и т.д.).

Миграции схем (через Hibernate tools или в связке с Liquibase/Flyway)
Может автоматически создавать таблицы и изменять схему базы по аннотациям классов.


Конфигурационный файл с информацией о соединении с БД, классами, описывающими структуру итаблиц в БД
```xml
<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 5.3//EN"
        "http://hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.connection.url">jdbc:postgresql://localhost:5433/lab02</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">pass</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <mapping class="lab02.libraryhibernate.entities.Author"/>
        <mapping class="lab02.libraryhibernate.entities.Book"/>
        <mapping class="lab02.libraryhibernate.entities.Publisher"/>
        <mapping class="lab02.libraryhibernate.entities.Category"/>
    </session-factory>
</hibernate-configuration>
```

Класс, регистрирующий бин для управления сессиями на основе конфигурационного файла... 
```java
package lab02.libraryhibernate.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateConfig {
    @Bean
    public SessionFactory createSessionFactory() {
        return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }
}
```

Пример использования сессий:

```java
@Repository
public class BookDao {

    private final SessionFactory sessionFactory;

    public BookDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Book> getAllBooks() {
        try(Session session = sessionFactory.openSession()) {
            String query = "select b from Book b left join fetch b.categories";
            return session.createQuery(query, Book.class).getResultList();
        }
    }
// Остальные методы
```
В DAO классах с использованием блока try-with-resiurces я окрываю сессию с бд для получения информации о всех книгах, включая их категории, после чего сессия сама закрывается высвобождаю ресурсы

Сервис класс книги

```java
@Service
public class BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final CategoryDao categoryDao;
    private final PublisherDao publisherDao;
    private final BookMapper bookMapper;

    public BookService(BookDao bookDao, AuthorDao authorDao, CategoryDao categoryDao, PublisherDao publisherDao, BookMapper bookMapper) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.categoryDao = categoryDao;
        this.publisherDao = publisherDao;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> getAllBooks(){
        List<Book> books = bookDao.getAllBooks();
        List<BookDto> BookDtos = new ArrayList<>();
        for (Book book : books){
            BookDtos.add(bookMapper.mapToDto(book));
        }
        return BookDtos;
    }
```

Поля DAO помимо BOOKDAO я использую в дугих методах, здесь показан именно метод получения всех книг из бд

класс маппер для киги:

```java
package lab02.libraryhibernate.mappers;

import lab02.libraryhibernate.dao.AuthorDao;
import lab02.libraryhibernate.dao.CategoryDao;
import lab02.libraryhibernate.dao.PublisherDao;
import lab02.libraryhibernate.dtos.BookDto;
import lab02.libraryhibernate.entities.Book;
import lab02.libraryhibernate.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BookMapper {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private PublisherDao publisherDao;

    public BookDto mapToDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getPublisher().getId(), getIds(book));
    }

    public Book mapToEntity(BookDto dto) {
        return new Book(dto.getTitle(), authorDao.getAuthor(dto.getAuthorId()), publisherDao.getPublisher(dto.getPublisherId()), getCategories(dto));
    }

    private List<Long> getIds(Book book) {
        List<Category> categories = book.getCategories();
        List<Long> ids = new ArrayList<>();
        for (Category category : categories) {
            ids.add(category.getId());
        }
        return ids;
    }

    private List<Category> getCategories(BookDto dto) {
        List<Long> ids = dto.getCategoryIds();
        List<Category> categories = new ArrayList<>();
        for(Long id : ids) {
            categories.add(categoryDao.getCategory(id));
        }
        return categories;
    }
}

```

Контроллер
```java
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }
// другие методы контроллера
```

Для тестов я испольщую сваггер, очень удобную для меня технологию. Выше представлен метод по получению всех книг из БД, возвращающий информацию о них в виде JSON-a

### Выводы

Hibernate — это мощный инструмент для Java-разработчиков, который автоматизирует работу с базами данных, избавляя от необходимости вручную писать SQL-запросы. Он упрощает разработку, улучшает читаемость кода, обеспечивает переносимость между СУБД и помогает реализовывать сложные связи между сущностями. Использование Hibernate особенно оправдано в крупных проектах, где требуется надёжное и масштабируемое управление данными.





