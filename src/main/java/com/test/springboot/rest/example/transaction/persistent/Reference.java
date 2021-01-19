package com.test.springboot.rest.example.transaction.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REFERENCE")
public class Reference {

  @Id
  @SequenceGenerator(name = "referenceSequence", sequenceName = "reference_seq", initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referenceSequence")
  private Long id;

  @Column(name = "VALUE", length = 6, nullable = true, unique = true)
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

  public Reference clone() {
    return new Reference()
      .id(this.id)
      .value(this.value);
  }
}
