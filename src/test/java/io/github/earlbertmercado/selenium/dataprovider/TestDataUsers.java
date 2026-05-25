package io.github.earlbertmercado.selenium.dataprovider;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataUsers {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String postalCode;

    public TestDataUsers() {}

}
