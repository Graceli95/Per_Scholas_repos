package com.Li.service;

import com.Li.model.Address;
import com.Li.model.Book;
import com.Li.model.Library;
import com.Li.model.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class LibraryService {

//    many to one mapping
    public static void insertLibraryAndMembers(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        //create Libraries
        Library cityLibrary = new Library();
        cityLibrary.setName("City Library");

        Library midtownLibrary = new Library();
        midtownLibrary.setName("Midtown Library");

        //Persist Libraries
        session.persist(cityLibrary);
        session.persist(midtownLibrary);

        //Create Members
        Member member1 = new Member();
        member1.setName("Alice");
        member1.setEmail("alice@gmail.com");
        member1.setLibrary(cityLibrary);

        Member member2 = new Member();
        member2.setName("Bob");
        member2.setEmail("Bob@gmail.com");
        member2.setLibrary(cityLibrary);

        Member member3 = new Member();
        member3.setName("Charlie");
        member3.setEmail("Charlie@gmail.com");
        member3.setLibrary(midtownLibrary);

        //Persist member
        session.persist(member1);
        session.persist(member2);
        session.persist(member3);

        //commit transaction
        transaction.commit();


        //close session and factory
        session.close();
        sessionFactory.close();

        System.out.println("Data inserted successfully");

    }

//    use HQL to retrieve data
    public static void retrieveMembers(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        List<Member> members = session.createQuery("from Member", Member.class).getResultList();
        for (Member member : members) {
            System.out.println(member.getName() + " belongs to " +  member.getLibrary().getName());
        }

    }

//    one to many mapping
    public static void insertBooksToLibraries(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        //Fetch existing Libraries
        Library cityLibrary = session.createQuery("from Library where name = :name", Library.class)
                .setParameter("name", "City Library")
                .uniqueResult();

        Library midtownLibrary = session.createQuery("from Library where name = :name", Library.class)
                .setParameter("name", "Midtown Library")
                .uniqueResult();


        if(cityLibrary !=null){
            Book book1 = new Book();
            book1.setTitle("Microservice");

            Book book2 = new Book();
            book2.setTitle("Java Concurrency");

            cityLibrary.getBooks().add(book1);
            cityLibrary.getBooks().add(book2);

            session.persist(cityLibrary); //save changes to the Library and Books
        }else{
            System.out.println("City Library not found");
        }

        if(midtownLibrary !=null){
            //add Books to Midtown library
            Book book3 = new Book();
            book3.setTitle("Data Structures");

            Book book4 = new Book();
            book4.setTitle("Design Patterns");

            midtownLibrary.getBooks().add(book3);
            midtownLibrary.getBooks().add(book4);

            session.persist(midtownLibrary); //save changes to the Library and Books
        }else{
            System.out.println("Midtown Library not found");
        }

        //commit the transaction
        transaction.commit();

        //close the session and factory
        sessionFactory.close();
        session.close();

        System.out.println("Books inserted to Library successfully");

    }

    public static void accessBooks(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();


        Library retrievedLibrary = session.get(Library.class, 1L);
        retrievedLibrary.getBooks().forEach(book ->System.out.println(book.getTitle()));

    }

//    One to one mapping
    public static void updateMemberWithAddress(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        //fetch existing members
        List<Member> members = session.createQuery("from Member", Member.class).getResultList();
        if(members.isEmpty()){
            System.out.println("Member not found");
        }else{
            for (Member member : members) {
                Address address = new Address();

                //assign different address based on member names
                if(member.getName().equals("Alice")){
                    address.setStreet("123 Main Street");
                }else if(member.getName().equals("Bob")){
                    address.setStreet("45 Elm Street");
                }else if(member.getName().equals("Charlie")){
                    address.setStreet("809 oak Street");
                }

                // Persist address
//                session.persist(address);

                // Associate the address with the member, set the address in the Member entity
                member.setAddress(address);

                //persist the member
                session.persist(member);
            }
        }
        transaction.commit();
        session.close();
        sessionFactory.close();
        System.out.println("Members address updated successfully");
    }


//    many to many relationship mapping
    public static void insertMembersAndBooks(){

        // Create SessionFactory
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        // Begin transaction
        Transaction transaction = session.beginTransaction();

        //Create Books
        Book book1 = new Book();
        book1.setTitle("Java Basics");

        Book book2 = new Book();
        book2.setTitle("Spring in Action");

        Book book3 = new Book();
        book3.setTitle("Hibernate for Beginners");

        //Create Members
        Member alice = new Member();
        alice.setName("Alice");
        alice.setEmail("alice@gmail.com");

        Member bob = new Member();
        bob.setName("Bob");
        bob.setEmail("bob@example.com");

        //Assign books to members
        alice.getBooks().add(book1);
        alice.getBooks().add(book2);

        bob.getBooks().add(book3);


        //Persist Members (cascading saves books)
        session.persist(alice);
        session.persist(bob);

        //commit transaction
        transaction.commit();

        //Close session and factory
        session.close();
        sessionFactory.close();

        System.out.println("Members and books inserted successfully.");




    }


}

