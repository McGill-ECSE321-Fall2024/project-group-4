/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.util.*;

// line 80 "model.ump"
// line 195 "model.ump"
public class CreditCard
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CreditCard Attributes
  private int cardNumber;
  private Date expiry;
  private int cvv;

  //CreditCard Associations
  private Customer name;
  private List<Purchase> purchase;
  private Address bilingAddress;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CreditCard(int aCardNumber, Date aExpiry, int aCvv, Customer aName, Address aBilingAddress)
  {
    cardNumber = aCardNumber;
    expiry = aExpiry;
    cvv = aCvv;
    boolean didAddName = setName(aName);
    if (!didAddName)
    {
      throw new RuntimeException("Unable to create card due to name. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    purchase = new ArrayList<Purchase>();
    boolean didAddBilingAddress = setBilingAddress(aBilingAddress);
    if (!didAddBilingAddress)
    {
      throw new RuntimeException("Unable to create card due to bilingAddress. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCardNumber(int aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpiry(Date aExpiry)
  {
    boolean wasSet = false;
    expiry = aExpiry;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvv(int aCvv)
  {
    boolean wasSet = false;
    cvv = aCvv;
    wasSet = true;
    return wasSet;
  }

  public int getCardNumber()
  {
    return cardNumber;
  }

  public Date getExpiry()
  {
    return expiry;
  }

  public int getCvv()
  {
    return cvv;
  }
  /* Code from template association_GetOne */
  public Customer getName()
  {
    return name;
  }
  /* Code from template association_GetMany */
  public Purchase getPurchase(int index)
  {
    Purchase aPurchase = purchase.get(index);
    return aPurchase;
  }

  public List<Purchase> getPurchase()
  {
    List<Purchase> newPurchase = Collections.unmodifiableList(purchase);
    return newPurchase;
  }

  public int numberOfPurchase()
  {
    int number = purchase.size();
    return number;
  }

  public boolean hasPurchase()
  {
    boolean has = purchase.size() > 0;
    return has;
  }

  public int indexOfPurchase(Purchase aPurchase)
  {
    int index = purchase.indexOf(aPurchase);
    return index;
  }
  /* Code from template association_GetOne */
  public Address getBilingAddress()
  {
    return bilingAddress;
  }
  /* Code from template association_SetOneToMany */
  public boolean setName(Customer aName)
  {
    boolean wasSet = false;
    if (aName == null)
    {
      return wasSet;
    }

    Customer existingName = name;
    name = aName;
    if (existingName != null && !existingName.equals(aName))
    {
      existingName.removeCard(this);
    }
    name.addCard(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPurchase()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addPurchase(Date aPurchaseDate, int aPurchasePrice, Game aGame, Customer aCustomer, Address aDeliveryAddress)
  {
    return new Purchase(aPurchaseDate, aPurchasePrice, aGame, aCustomer, this, aDeliveryAddress);
  }

  public boolean addPurchase(Purchase aPurchase)
  {
    boolean wasAdded = false;
    if (purchase.contains(aPurchase)) { return false; }
    CreditCard existingPaymentCard = aPurchase.getPaymentCard();
    boolean isNewPaymentCard = existingPaymentCard != null && !this.equals(existingPaymentCard);
    if (isNewPaymentCard)
    {
      aPurchase.setPaymentCard(this);
    }
    else
    {
      purchase.add(aPurchase);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePurchase(Purchase aPurchase)
  {
    boolean wasRemoved = false;
    //Unable to remove aPurchase, as it must always have a paymentCard
    if (!this.equals(aPurchase.getPaymentCard()))
    {
      purchase.remove(aPurchase);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPurchaseAt(Purchase aPurchase, int index)
  {  
    boolean wasAdded = false;
    if(addPurchase(aPurchase))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchase()) { index = numberOfPurchase() - 1; }
      purchase.remove(aPurchase);
      purchase.add(index, aPurchase);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePurchaseAt(Purchase aPurchase, int index)
  {
    boolean wasAdded = false;
    if(purchase.contains(aPurchase))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchase()) { index = numberOfPurchase() - 1; }
      purchase.remove(aPurchase);
      purchase.add(index, aPurchase);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPurchaseAt(aPurchase, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBilingAddress(Address aBilingAddress)
  {
    boolean wasSet = false;
    if (aBilingAddress == null)
    {
      return wasSet;
    }

    Address existingBilingAddress = bilingAddress;
    bilingAddress = aBilingAddress;
    if (existingBilingAddress != null && !existingBilingAddress.equals(aBilingAddress))
    {
      existingBilingAddress.removeCard(this);
    }
    bilingAddress.addCard(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderName = name;
    this.name = null;
    if(placeholderName != null)
    {
      placeholderName.removeCard(this);
    }
    for(int i=purchase.size(); i > 0; i--)
    {
      Purchase aPurchase = purchase.get(i - 1);
      aPurchase.delete();
    }
    Address placeholderBilingAddress = bilingAddress;
    this.bilingAddress = null;
    if(placeholderBilingAddress != null)
    {
      placeholderBilingAddress.removeCard(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "cardNumber" + ":" + getCardNumber()+ "," +
            "cvv" + ":" + getCvv()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "expiry" + "=" + (getExpiry() != null ? !getExpiry().equals(this)  ? getExpiry().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "name = "+(getName()!=null?Integer.toHexString(System.identityHashCode(getName())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bilingAddress = "+(getBilingAddress()!=null?Integer.toHexString(System.identityHashCode(getBilingAddress())):"null");
  }
}