package com.javalive.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.javalive.entity.Department;
import com.javalive.entity.Employee;

public class NativeQueryEntityExample {
   @SuppressWarnings("unchecked")
   public static void main(String[] args) {
      Session session = null;
      Transaction transaction = null;
      try {
         session = HibernateUtil.getSessionFactory().openSession();
         transaction = session.beginTransaction();

         //Mapping Native query to Entity
         List<Employee> employees = session.createNativeQuery("SELECT * FROM employee",Employee.class)
               .list();
         for (Employee employee : employees) {
            Integer id=employee.getId();
            String name=employee.getName();
            String designation=employee.getDesignation();
            System.out.println("Employee["+id+","+name+","+designation+"]");
         }
         
         //Mapping Native JOIN query to Entities
         System.out.println("--------------------------------------------------------------");
         List<Object[]> departments=session.createNativeQuery(""
               + "select e.*, d.* "
               + "from department d inner join  employee e "
               + "on d.dept_id=e.dept_id")
               .addEntity("d", Department.class)
               .addJoin("e", "d.employees")
               .list();
         
         for (Object[] object : departments) {
            Department department=(Department)object[0];
            System.out.println("Department - "+department.getName());
            for (Employee employee : department.getEmployees()) {
               System.out.println("\t Employee - "+employee.getName()+" \t Designation - "+employee.getDesignation());
            }
         }
         
         transaction.commit();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (session != null) {
            session.close();
         }
      }

      HibernateUtil.shutdown();
   }
}
