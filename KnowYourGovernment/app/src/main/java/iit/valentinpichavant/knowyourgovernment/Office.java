package iit.valentinpichavant.knowyourgovernment;

import java.io.Serializable;

/**
 * Created by valentinpichavant on 4/1/17.
 */

public class Office implements Serializable {

    private String name;
    private Official official;

    public Office(String name, Official official) {
        this.name = name;
        this.official = official;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Official getOfficial() {
        return official;
    }

    public void setOfficial(Official official) {
        this.official = official;
    }

    @Override
    public String toString() {
        return "Office{" +
                "name='" + name + '\'' +
                ", official=" + official +
                '}';
    }
}
