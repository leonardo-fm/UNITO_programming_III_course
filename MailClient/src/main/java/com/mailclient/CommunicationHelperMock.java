package com.mailclient;

import com.sharedmodels.Email;

import java.util.ArrayList;
import java.util.List;

public class CommunicationHelperMock {

    public List<Email> GetInboxEmailsMock() {
        List<Email> data = new ArrayList<>();
        List<String> to = new ArrayList<>();
        to.add("567667556@gamil.com");
        to.add("Furr@yahoo.com");

        data.add(new Email("abc@gmail.com", to, "jec wdwdt", "Some of to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("loller@gmail.com", to, "emailObjecd wdwwqwdqq t", "Some, I.E: to know which fits better your needs."));
        data.add(new Email("abc@gmail.com", to, "emailObjecd ss  sdds  dst", "Some of those can be  class). Use their links to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("Sdaaaa@gmail.com", to, "emai sdds lObject", "Some of those can  to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("abc@gmail.com", to, "emailOb", "Some of those can be instantiated (the ones that are not defined as abstract class). Use their links to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("165413.135@gmail.com", to, "emailObject sdd s", "Some of those  not defined as abstract class). Use their links to know more about them, I.E fits better your needs."));
        data.add(new Email("kjhgfdreuhfg@gmail.com", to, "emsdds ds ds ailObject", "Some are not to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("abc@gmail.com", to, "emailOsdsd bject", "Some of those can be  more about them, I.E: to know  better your needs."));
        data.add(new Email("jfsdhgkfd@gmail.com", to, "emailObject", "Some of those can be instantiated (the ones that are not defined as abstract class). Use their links to know more about them, I.E: to know which fits better your needs."));

        return  data;
    }
}
