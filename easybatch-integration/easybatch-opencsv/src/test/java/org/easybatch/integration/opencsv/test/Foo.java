package org.easybatch.integration.opencsv.test;

/**
 * Dummy class used for tests.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class Foo {

    private String firstName;

    private String lastName;

    private int age;

    private boolean married;

    public Foo() {
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
