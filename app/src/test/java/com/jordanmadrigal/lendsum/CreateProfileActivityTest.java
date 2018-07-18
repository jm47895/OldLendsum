package com.jordanmadrigal.lendsum;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreateProfileActivityTest {

    @Test
    public void isValidUserForm() {
        String firstName = "Jordan";
        String lastName = "Madrigal";
        String email= "jordan";
        String password = "JayeLeM008_";


        CreateProfileActivity validUser = new CreateProfileActivity();

        boolean condition = validUser.isValidPassword(password);

        assertTrue(condition);

    }


    @Test
    public void getUserInitials(){

        String fName = "jordan";
        String lName = "madrigal";
        String expected = "JM";

        CreateProfileActivity initialsTest = new CreateProfileActivity();

        String actual = initialsTest.getUserInitials(fName, lName);



        assertEquals(expected, actual);
    }


}