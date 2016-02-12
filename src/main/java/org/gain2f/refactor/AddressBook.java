package org.gain2f.refactor;

import java.util.List;
import java.util.stream.Collectors;

public class AddressBook {

  public static final String SWEDEN_CODE = "070";

  private AddressDb addressDb = new AddressDb("admin", "beefhead");

  public boolean hasMobile(String name) {
    return addressDb.findPerson(name).getPhoneNumber().getNumber().startsWith(SWEDEN_CODE);
  }

  public int getSize() {
    return addressDb.getAllPersons().size() - 1;
    // In original logic method returns size of people collection - 1. Corresponded refactoring task it was saved but looks strange
    // In the task has written: "...однако общее поведение классов должно остаться прежним."
  }

  public String getMobile(String name) {
    PhoneNumber phone = addressDb.findPerson(name).getPhoneNumber();
    return phone.getNumber();
  }

  /**
   * Returns all names in the book truncated to the given length.
   *
   * @param maxLength max length of the names
   * @return all names in the book truncated to the given length
   */
  public List<String> getNames(int maxLength) {
    return addressDb.getAllPersons().stream()
                    .map(person -> person.getName().length() > maxLength
                           ? person.getName().substring(0, maxLength)
                           : person.getName()).collect(Collectors.toList());
  }

  /**
   * Returns all people who have mobile phone numbers.
   *
   * @return all people who have mobile phone numbers
   */
  public List getPeopleWithPhone() {
    return addressDb.getAllPersons().stream()
                    .filter(person -> person.getPhoneNumber().getNumber().startsWith(SWEDEN_CODE))
                    .collect(Collectors.toList());
  }
}
