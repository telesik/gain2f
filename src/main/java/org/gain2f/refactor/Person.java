package org.gain2f.refactor;

public class Person {

  private String      name;
  private PhoneNumber phoneNumber;

  public Person(String name, PhoneNumber phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (name != null ? !name.equals(person.name) : person.name != null) return false;
    return !(phoneNumber != null ? !phoneNumber.equals(person.phoneNumber) : person.phoneNumber != null);

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Person{" +
           "name='" + name + '\'' +
           ", phoneNumber=" + phoneNumber +
           '}';
  }
}
