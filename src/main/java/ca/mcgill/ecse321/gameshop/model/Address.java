/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;
import java.sql.Date;

// line 87 "model.ump"
// line 199 "model.ump"
public class Address
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Address Attributes
  private String street;
  private String city;
  private String province;
  private String country;
  private String postalCode;

  //Address Associations
  private Customer customer;
  private List<CreditCard> cards;
  private List<Purchase> delivery;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Address(String aStreet, String aCity, String aProvince, String aCountry, String aPostalCode, Customer aCustomer)
  {
    street = aStreet;
    city = aCity;
    province = aProvince;
    country = aCountry;
    postalCode = aPostalCode;
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create address due to customer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    cards = new ArrayList<CreditCard>();
    delivery = new ArrayList<Purchase>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStreet(String aStreet)
  {
    boolean wasSet = false;
    street = aStreet;
    wasSet = true;
    return wasSet;
  }

  public boolean setCity(String aCity)
  {
    boolean wasSet = false;
    city = aCity;
    wasSet = true;
    return wasSet;
  }

  public boolean setProvince(String aProvince)
  {
    boolean wasSet = false;
    province = aProvince;
    wasSet = true;
    return wasSet;
  }

  public boolean setCountry(String aCountry)
  {
    boolean wasSet = false;
    country = aCountry;
    wasSet = true;
    return wasSet;
  }

  public boolean setPostalCode(String aPostalCode)
  {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public String getStreet()
  {
    return street;
  }

  public String getCity()
  {
    return city;
  }

  public String getProvince()
  {
    return province;
  }

  public String getCountry()
  {
    return country;
  }

  public String getPostalCode()
  {
    return postalCode;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetMany */
  public CreditCard getCard(int index)
  {
    CreditCard aCard = cards.get(index);
    return aCard;
  }

  public List<CreditCard> getCards()
  {
    List<CreditCard> newCards = Collections.unmodifiableList(cards);
    return newCards;
  }

  public int numberOfCards()
  {
    int number = cards.size();
    return number;
  }

  public boolean hasCards()
  {
    boolean has = cards.size() > 0;
    return has;
  }

  public int indexOfCard(CreditCard aCard)
  {
    int index = cards.indexOf(aCard);
    return index;
  }
  /* Code from template association_GetMany */
  public Purchase getDelivery(int index)
  {
    Purchase aDelivery = delivery.get(index);
    return aDelivery;
  }

  public List<Purchase> getDelivery()
  {
    List<Purchase> newDelivery = Collections.unmodifiableList(delivery);
    return newDelivery;
  }

  public int numberOfDelivery()
  {
    int number = delivery.size();
    return number;
  }

  public boolean hasDelivery()
  {
    boolean has = delivery.size() > 0;
    return has;
  }

  public int indexOfDelivery(Purchase aDelivery)
  {
    int index = delivery.indexOf(aDelivery);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeAddress(this);
    }
    customer.addAddress(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCards()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CreditCard addCard(int aCardNumber, Date aExpiry, int aCvv, Customer aName)
  {
    return new CreditCard(aCardNumber, aExpiry, aCvv, aName, this);
  }

  public boolean addCard(CreditCard aCard)
  {
    boolean wasAdded = false;
    if (cards.contains(aCard)) { return false; }
    Address existingBilingAddress = aCard.getBilingAddress();
    boolean isNewBilingAddress = existingBilingAddress != null && !this.equals(existingBilingAddress);
    if (isNewBilingAddress)
    {
      aCard.setBilingAddress(this);
    }
    else
    {
      cards.add(aCard);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCard(CreditCard aCard)
  {
    boolean wasRemoved = false;
    //Unable to remove aCard, as it must always have a bilingAddress
    if (!this.equals(aCard.getBilingAddress()))
    {
      cards.remove(aCard);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCardAt(CreditCard aCard, int index)
  {  
    boolean wasAdded = false;
    if(addCard(aCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCards()) { index = numberOfCards() - 1; }
      cards.remove(aCard);
      cards.add(index, aCard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCardAt(CreditCard aCard, int index)
  {
    boolean wasAdded = false;
    if(cards.contains(aCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCards()) { index = numberOfCards() - 1; }
      cards.remove(aCard);
      cards.add(index, aCard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCardAt(aCard, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDelivery()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addDelivery(Date aPurchaseDate, int aPurchasePrice, Game aGame, Customer aCustomer, CreditCard aPaymentCard)
  {
    return new Purchase(aPurchaseDate, aPurchasePrice, aGame, aCustomer, aPaymentCard, this);
  }

  public boolean addDelivery(Purchase aDelivery)
  {
    boolean wasAdded = false;
    if (delivery.contains(aDelivery)) { return false; }
    Address existingDeliveryAddress = aDelivery.getDeliveryAddress();
    boolean isNewDeliveryAddress = existingDeliveryAddress != null && !this.equals(existingDeliveryAddress);
    if (isNewDeliveryAddress)
    {
      aDelivery.setDeliveryAddress(this);
    }
    else
    {
      delivery.add(aDelivery);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDelivery(Purchase aDelivery)
  {
    boolean wasRemoved = false;
    //Unable to remove aDelivery, as it must always have a deliveryAddress
    if (!this.equals(aDelivery.getDeliveryAddress()))
    {
      delivery.remove(aDelivery);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDeliveryAt(Purchase aDelivery, int index)
  {  
    boolean wasAdded = false;
    if(addDelivery(aDelivery))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDelivery()) { index = numberOfDelivery() - 1; }
      delivery.remove(aDelivery);
      delivery.add(index, aDelivery);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDeliveryAt(Purchase aDelivery, int index)
  {
    boolean wasAdded = false;
    if(delivery.contains(aDelivery))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDelivery()) { index = numberOfDelivery() - 1; }
      delivery.remove(aDelivery);
      delivery.add(index, aDelivery);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDeliveryAt(aDelivery, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeAddress(this);
    }
    for(int i=cards.size(); i > 0; i--)
    {
      CreditCard aCard = cards.get(i - 1);
      aCard.delete();
    }
    for(int i=delivery.size(); i > 0; i--)
    {
      Purchase aDelivery = delivery.get(i - 1);
      aDelivery.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "street" + ":" + getStreet()+ "," +
            "city" + ":" + getCity()+ "," +
            "province" + ":" + getProvince()+ "," +
            "country" + ":" + getCountry()+ "," +
            "postalCode" + ":" + getPostalCode()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}