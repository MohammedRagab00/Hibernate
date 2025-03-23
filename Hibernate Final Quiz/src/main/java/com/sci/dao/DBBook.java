package com.sci.dao;


import com.sci.criteria.FilterQuery;
import com.sci.models.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DBBook {
    public List<Book> getByFilter(List<FilterQuery> filterQueries) {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            // To be edited in other relations CRUD OPs:
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cr = cb.createQuery(Book.class);
            Root<Book> root = cr.from(Book.class);

            Predicate[] predicates = new Predicate[filterQueries.size()];
            for (int i = 0; i < filterQueries.size(); i++) {
                switch (filterQueries.get(i).getOp()) {
                    case Equal:
                        predicates[i] =
                                cb.equal(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        filterQueries.get(i).getAttributeValue());
                        break;
                    case GreaterThan:
                        predicates[i] =
                                cb.greaterThan(root.get(filterQueries.
                                                get(i).getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case LessThan:
                        predicates[i] =
                                cb.lessThan(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case NotEqual:
                        predicates[i] =
                                cb.notEqual(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case IsNull:
                        predicates[i] =
                                cb.isNull(root.get(filterQueries.get(i).
                                        getAttributeName())
                                );
                        break;
                    case GreaterThanOrEqual:
                        predicates[i] =
                                cb.greaterThanOrEqualTo(
                                        root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case LessThanOrEqual:
                        predicates[i] =
                                cb.lessThanOrEqualTo(
                                        root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        (Comparable) filterQueries.get(i).
                                                getAttributeValue());
                        break;
                    case Like:
                        /* <===> Where attribute like '%string%', can be customized. */
                        predicates[i] =
                                cb.like(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        "%" + (filterQueries.get(i).
                                                getAttributeValue()) + "%");
                        break;
                    case Between:
                        List<Comparable> values =
                                (List<Comparable>) filterQueries.get(i).
                                        getAttributeValue();
                        predicates[i] =
                                cb.between(root.get(filterQueries.get(i).
                                                getAttributeName()),
                                        values.get(0), values.get(1));
                        break;
                    case In:
                        List<Object> inQuery = (List<Object>)
                                filterQueries.get(i).getAttributeValue();
                        predicates[i] =
                                root.get(filterQueries.get(i).getAttributeName()).
                                        in(inQuery);
                        break;
                    default:
                        break;
                }
            }

            //This is their default, We need a more complex query
            cr.select(root).where(predicates);

            Query<Book> query = session.createQuery(cr);
            return query.getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return new ArrayList<>();
    }

    public List<Book> get() {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

<<<<<<< HEAD:Template/src/main/java/com/sci/dao/DBRoom.java
            return session.createQuery("FROM Room", Room.class).getResultList();
=======
            //noinspection unchecked
            return session.createQuery("FROM Book ").list();
>>>>>>> 2761105d78135125d9e54ac15b7fdcf8d2ce19b0:Hibernate Final Quiz/src/main/java/com/sci/dao/DBBook.java

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    public Book read(Integer bookId) {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            return session.get(Book.class, bookId);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }


    public Integer create(Book book) {

        Transaction transaction = null;
        Integer bookId = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

<<<<<<< HEAD:Template/src/main/java/com/sci/dao/DBRoom.java
            session.persist(room);
            roomNumber = room.getRoomNumber();
=======
            bookId = (Integer) session.save(book);
>>>>>>> 2761105d78135125d9e54ac15b7fdcf8d2ce19b0:Hibernate Final Quiz/src/main/java/com/sci/dao/DBBook.java

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }

        return bookId;
    }
<<<<<<< HEAD:Template/src/main/java/com/sci/dao/DBRoom.java

    public void update(Room room) {

        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            session.merge(room);

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    public void delete(String roomNumber) {

        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            Room room = read(roomNumber);

            session.remove(room);

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

=======
>>>>>>> 2761105d78135125d9e54ac15b7fdcf8d2ce19b0:Hibernate Final Quiz/src/main/java/com/sci/dao/DBBook.java
}
