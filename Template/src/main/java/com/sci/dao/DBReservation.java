package com.sci.dao;


import com.sci.criteria.FilterQuery;
import com.sci.models.Reservation;
import com.sci.models.ReservationCompositeKey;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DBReservation {
    public List<Reservation> getByFilter(List<FilterQuery> filterQueries) {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            // To be edited in other relations CRUD OPs:
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Reservation> cr = cb.createQuery(Reservation.class);
            Root<Reservation> root = cr.from(Reservation.class);

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

            Query<Reservation> query = session.createQuery(cr);
            return query.getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return new ArrayList<>();
    }

    public List<Reservation> get() {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            return session.createQuery("FROM Reservation", Reservation.class).getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    public Reservation read(String bookingNumber, String roomNumber, Date dateReserved) {
        ReservationCompositeKey key = new ReservationCompositeKey(bookingNumber, roomNumber, dateReserved);

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            return session.get(Reservation.class, key);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }


    public ReservationCompositeKey create(Reservation reservation) {

        Transaction transaction = null;
        ReservationCompositeKey key = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            session.persist(reservation);
            key = new ReservationCompositeKey(reservation.getBookingNumber(), reservation.getRoomNumber(), reservation.getDateReserved());

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }

        return key;
    }

    public void update(Reservation reservation) {

        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            session.merge(reservation);

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    public void delete(String bookingNumber, String roomNumber, Date dateReserved) {

        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            Reservation reservation = read(bookingNumber, roomNumber, dateReserved);

            session.remove(reservation);

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

}
