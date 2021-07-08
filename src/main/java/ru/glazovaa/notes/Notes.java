package ru.glazovaa.notes;

import javax.persistence.*;

@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String body;

    private String head;

    @ManyToOne(fetch = FetchType.LAZY)
    Customer customer;

    public Notes() {
    }

    public Notes(String head, String body, Customer customer) {
        this.head = head;
        this.body = body;
        this.customer = customer;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
