package io.github.benas.easybatch.core.test;

/**
 * Extended Person bean.
 *
 * @author benas (md.benhassine@gmail.com)
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
