package edu.icet.crm.util;

import edu.icet.crm.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory = createSession();

    private static SessionFactory createSession() {
        StandardServiceRegistry build = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

       Metadata metadata = new MetadataSources(build)
                .addAnnotatedClass(CustomerEntity.class)
                .addAnnotatedClass(EmployeeEntity.class)
                .addAnnotatedClass(OrderDetailsEntity.class)
                .addAnnotatedClass(OrderEntity.class)
                .addAnnotatedClass(ProductEntity.class)
                .addAnnotatedClass(RoleEntity.class)
                .addAnnotatedClass(SupplierEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }

    public static Session getSession(){
        return sessionFactory.openSession();
    }
}
