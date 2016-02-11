package org.gain2f.refactor;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AddressDb {

  private final String user;
  private final String password;

  static {
    try {
      Class.forName("oracle.jdbc.ThinDriver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public AddressDb(String user, String password) {
    this.user = user;
    this.password = password;
  }

  /**
   * Insert new person to a database
   *
   * @param person inserted person
   */
  public void addPerson(Person person) {
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", user, password);
         PreparedStatement statement = connection.prepareStatement("INSERT INTO AddressEntry VALUES (?, ?, ?)")) {
      statement.setLong(1, System.currentTimeMillis());
      statement.setString(2, person.getName());
      statement.setString(3, person.getPhoneNumber().getNumber());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Looks up the given person, null if not found.   *
   *
   * @param name looked person name
   * @return found person, or null if not found.
   */
  public Person findPerson(String name) {
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", user, password);
         PreparedStatement statement = connection.prepareStatement("SELECT name, phoneNumber FROM AddressEntry WHERE name = ? AND ROWNUM = 1");) {
      statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        String foundName = resultSet.getString("name");
        PhoneNumber phoneNumber = new PhoneNumber(resultSet.getString("phoneNumber"));
        return new Person(foundName, phoneNumber);
      } else {
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets all persons from a database
   *
   * @return all persons
   */
  public List<Person> getAllPersons() {
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", user, password);
         PreparedStatement statement = connection.prepareStatement("SELECT name, phoneNumber FROM AddressEntry");
         ResultSet result = statement.executeQuery()) {

      List<Person> entries = new LinkedList<>();
      while (result.next()) {
        String name = result.getString("name");
        PhoneNumber phoneNumber = new PhoneNumber(result.getString("phoneNumber"));
        Person person = new Person(name, phoneNumber);
        entries.add(person);
      }
      return entries;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
