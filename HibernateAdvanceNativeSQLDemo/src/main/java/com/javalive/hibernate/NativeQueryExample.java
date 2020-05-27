package com.javalive.hibernate;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

public class NativeQueryExample {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			// Native query selecting all columns
			List<Object[]> departments = session.createNativeQuery("SELECT * FROM department").list();
			for (Object[] objects : departments) {
				BigInteger id = (BigInteger) objects[0];
				String name = (String) objects[1];
				System.out.println("Department[" + id + "," + name + "]");
			}

			// Native query with custom column selection (scaler query)
			System.out.println("--------------------------------------------------------------");
			List<Object[]> employees = session.createNativeQuery("SELECT * FROM employee")
					.addScalar("emp_id", IntegerType.INSTANCE).addScalar("name", StringType.INSTANCE)
					.addScalar("designation", StringType.INSTANCE).list();
			for (Object[] objects : employees) {
				Integer id = (Integer) objects[0];
				String name = (String) objects[1];
				String designation = (String) objects[2];
				System.out.println("Employee[" + id + "," + name + "," + designation + "]");
			}

			// Native query with JOIN
			System.out.println("--------------------------------------------------------------");
			List<Object[]> empDepts = session
					.createNativeQuery("" + "select e.name as emp_name, e.designation, d.name as dep_name "
							+ "from employee e inner join department d " + "on e.dept_id=d.dept_id")
					.list();
			for (Object[] objects : empDepts) {
				String employee = (String) objects[0];
				String designation = (String) objects[1];
				String department = (String) objects[2];
				System.out.println("Employee[" + employee + "," + designation + "," + department + "]");
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
