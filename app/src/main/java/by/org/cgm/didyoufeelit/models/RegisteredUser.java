package by.org.cgm.didyoufeelit.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Anatol Salanevich
 * Date: 11.05.2015
 */
public class RegisteredUser {

    @Setter @Getter private String firstName;
    @Setter @Getter private String secondName;
    @Setter @Getter private String phone;
    @Setter @Getter private String email;

}
