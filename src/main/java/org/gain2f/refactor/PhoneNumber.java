package org.gain2f.refactor;

public class PhoneNumber {

  private String number;

  public PhoneNumber(String number) {
    this.number = number;
  }

  public String getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PhoneNumber that = (PhoneNumber) o;

    return !(number != null ? !number.equals(that.number) : that.number != null);

  }

  @Override
  public int hashCode() {
    return number != null ? number.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "PhoneNumber{" +
           "number='" + number + '\'' +
           '}';
  }
}
