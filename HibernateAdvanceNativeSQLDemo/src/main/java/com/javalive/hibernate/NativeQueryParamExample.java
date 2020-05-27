package com.javalive.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import com.javalive.entity.Department;

public class NativeQueryParamExample {
   @SuppressWarnings("unchecked")
   public static void main(String[] args) {
      Session session = null;
      Transaction transaction = null;
      try {
         session = HibernateUtil.getSessionFactory().openSession();
         transaction = session.beginTransaction();

         //Binding parameter for native query
         List<Object[]> employees = session.createNativeQuery("SELECT * FROM employee where dept_id=?")
               .addScalar("emp_id", IntegerType.INSTANCE)
               .addScalar( "name", StringType.INSTANCE )
               .addScalar( "designation", StringType.INSTANCE )
               .setParameter(1, 11L)
               .list();
         for (Object[] objects : employees) {
            Integer id=(Integer)objects[0];
            String name=(String)objects[1];
            String designation=(String)objects[2];
            System.out.println("Employee["+id+","+name+","+designation+"]");
         }
         
         //Mapping Native query with Entity
         System.out.println("--------------------------------------------------------------");
         List<Department> departments = session.createNativeQuery("SELECT * FROM department where name like :deptName")
               .addEntity(Department.class)
               .setParameter("deptName", "S%")
               .list();
         for (Department department : departments) {
            System.out.println("Department["+department.getId()+","+department.getName()+"]");
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
