package edu.icet.crm.util;

import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.UserEntity;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
@Component
public class DataInitializer {
    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // Check if the admin user already exists
            long userCount = (long) session.createQuery("SELECT COUNT(u) FROM UserEntity u").uniqueResult();
            if (userCount == 0) {
                // Create admin employee
                EmployeeEntity adminEmployee = new EmployeeEntity();
                adminEmployee.setEmployeeId(1);
                adminEmployee.setRoleId(1);
                adminEmployee.setName("Admin");
                adminEmployee.setDob(LocalDate.of(1980, 1, 1));
                adminEmployee.setDoJoin(LocalDate.now());
                adminEmployee.setAddress("Admin Address");
                adminEmployee.setEmail("admin@example.com");
                adminEmployee.setContactNo("1234567890");

                // Create admin user
                UserEntity adminUser = new UserEntity();
                adminUser.setUserID(1);
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword(EncryptionUtil.encrypt("admin_password")); // Ensure to hash the password in a real application
                adminUser.setRoleID(1);
                //adminUser.setEmployee(adminEmployee);
                adminUser.setEmployeeID(1);

                // Save entities
                session.merge(adminEmployee);
                session.merge(adminUser);

                // Commit transaction
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
