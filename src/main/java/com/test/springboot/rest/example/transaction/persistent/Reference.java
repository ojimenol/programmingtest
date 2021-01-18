package com.test.springboot.rest.example.transaction.persistent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Reference {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "VALUE", length = 6, nullable = false, unique = true)
  private String value;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Reference id(Long id) {
    this.id = id;
    return this;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Reference value(String value) {
    this.value = value;
    return this;
  }
}
