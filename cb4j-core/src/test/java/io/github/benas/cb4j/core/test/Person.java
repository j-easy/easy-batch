package io.github.benas.cb4j.core.test;

/**
 * A person Java bean used for tests.
 *
 * @author benas (md.benhassine@gmail.Com)
 */
public class Person {

    private String firstName;

    private String lastName;

    /**
     * Default constructor needed by default mapper
     */
    void Person() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}