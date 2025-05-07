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
