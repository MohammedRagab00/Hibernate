package com.sci.dao;

import com.sci.models.Region;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DBRegion {

    public List<Region> get() {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            return session.createQuery("FROM Region", Region.class).getResultList();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    public Region read(Integer regionId) {

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            return session.get(Region.class, regionId);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }


    public Integer create(Region region) {

        Transaction transaction = null;
        int regionId = 0;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            session.persist(region);
            regionId = region.getRegionId();

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }

        return regionId;
    }

    public void update(Region region) {

        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            session.merge(region);

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

    public void delete(Integer regionId) {

        Transaction transaction = null;

        try (Session session = DBConfig.SESSION_FACTORY.openSession()) {

            transaction = session.beginTransaction();

            Region region = read(regionId);

            session.remove(region);

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println(ex.getMessage());
        }
    }

}
