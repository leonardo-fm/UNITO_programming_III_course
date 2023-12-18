package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.sharedmodels.MethodType.GET_ALL_EMAILS;
import static com.sharedmodels.MethodType.SEND_EMAIL;

public class CommunicationHelperMock {

    public List<Email> GetInboxEmailsMock() {
        List<Email> data = new ArrayList<>();
        List<String> to = new ArrayList<>();
        to.add("567667556@gamil.com");
        to.add("Furr@yahoo.com");

        data.add(new Email("abc@gmail.com", to, "Some of to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("loller@gmail.com", to, "Some, I.E: to know which fits better your needs."));
        data.add(new Email("abc@gmail.com", to, "Some of those can be  class). Use their links to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("Sdaaaa@gmail.com", to, "Some of those can  to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("abc@gmail.com", to, "Some of those can be instantiated (the ones that are not defined as abstract class). Use their links to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("165413.135@gmail.com", to, "Some of those  not defined as abstract class). Use their links to know more about them, I.E fits better your needs."));
        data.add(new Email("kjhgfdreuhfg@gmail.com", to, "Some are not to know more about them, I.E: to know which fits better your needs."));
        data.add(new Email("abc@gmail.com", to, "Some of those can be  more about them, I.E: to know  better your needs."));
        data.add(new Email("jfsdhgkfd@gmail.com", to, "Some of those can be instantiated (the ones that are not defined as abstract class). Use their links to know more about them, I.E: to know which fits better your needs."));

        return  data;
    }
}
