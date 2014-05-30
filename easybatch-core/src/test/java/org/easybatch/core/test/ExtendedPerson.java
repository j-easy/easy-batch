package org.easybatch.core.test;

/**
 * Extended Person bean.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class ExtendedPerson extends Person {

    protected String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
