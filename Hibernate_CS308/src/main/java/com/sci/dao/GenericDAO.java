package com.sci.dao;

import com.sci.criteria.FilterQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenericDAO<T, ID extends Serializable> {

    private static final Logger logger = LoggerFactory.getLogger(GenericDAO.class);
    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> getAll(int offset, int limit) {
        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception ex) {
            logger.error("Error fetching all entities", ex);
            return new ArrayList<>();
        }
    }

    public Optional<T> read(ID id) {
        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        } catch (Exception ex) {
            logger.error("Error reading entity with ID: " + id, ex);
            return Optional.empty();
        }
    }

    public ID create(T entity) {
        Transaction transaction = null;
        ID id = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.persist(entity);
            id = (ID) session.getIdentifier(entity);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error creating entity", ex);
        }

        return id;
    }

    public void update(T entity) {
        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating entity", ex);
        }
    }

    public void delete(ID id) {
        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();

            T entity = session.load(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting entity with ID: " + id, ex);
        }
    }

    public List<T> findByFilter(List<FilterQuery> filterQueries, boolean useOr) {
        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);

            List<Predicate> predicates = new ArrayList<>();
            for (FilterQuery query : filterQueries) {
                switch (query.getOp()) {
                    case Equal:
                        predicates.add(cb.equal(root.get(query.getAttributeName()), query.getAttributeValue()));
                        break;
                    case GreaterThan:
                        predicates.add(cb.greaterThan(root.get(query.getAttributeName()), (Comparable) query.getAttributeValue()));
                        break;
                    case LessThan:
                        predicates.add(cb.lessThan(root.get(query.getAttributeName()), (Comparable) query.getAttributeValue()));
                        break;
                    case NotEqual:
                        predicates.add(cb.notEqual(root.get(query.getAttributeName()), query.getAttributeValue()));
                        break;
                    case IsNull:
                        predicates.add(cb.isNull(root.get(query.getAttributeName())));
                        break;
                    case GreaterThanOrEqual:
                        predicates.add(cb.greaterThanOrEqualTo(root.get(query.getAttributeName()), (Comparable) query.getAttributeValue()));
                        break;
                    case LessThanOrEqual:
                        predicates.add(cb.lessThanOrEqualTo(root.get(query.getAttributeName()), (Comparable) query.getAttributeValue()));
                        break;
                    case Like:
                        predicates.add(cb.like(root.get(query.getAttributeName()), String.format("%%%s%%", query.getAttributeValue())));
                        break;
                    case Between:
                        List<Comparable> values = (List<Comparable>) query.getAttributeValue();
                        predicates.add(cb.between(root.get(query.getAttributeName()), values.get(0), values.get(1)));
                        break;
                    case In:
                        List<Object> inQuery = (List<Object>) query.getAttributeValue();
                        predicates.add(root.get(query.getAttributeName()).in(inQuery));
                        break;
                    default:
                        logger.warn("Unsupported filter operation: " + query.getOp());
                        break;
                }
            }

            Predicate combinedPredicate = useOr ? cb.or(predicates.toArray(new Predicate[0])) : cb.and(predicates.toArray(new Predicate[0]));
            criteriaQuery.select(root).where(combinedPredicate);
            Query<T> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception ex) {
            logger.error("Error fetching entities by filter", ex);
            return new ArrayList<>();
        }
    }

    public List<T> findWithCustomPredicate(PredicateBuilder<T> predicateBuilder) {
        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);

            Predicate predicate = predicateBuilder.build(cb, root);
            criteriaQuery.select(root).where(predicate);

            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            logger.error("Error fetching entities with custom predicate", ex);
            return new ArrayList<>();
        }
    }

    public interface PredicateBuilder<T> {
        Predicate build(CriteriaBuilder cb, Root<T> root);
    }
}
