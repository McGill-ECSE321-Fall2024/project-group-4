/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

// line 107 "model.ump"
// line 213 "model.ump"
public class Purchase
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Purchase Attributes
  private Date purchaseDate;
  private int purchasePrice;

  //Purchase Associations
  private RefundRequest refund;
  private Game game;
  private Review review;
  private Customer customer;
  private CreditCard paymentCard;
  private Address deliveryAddress;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Purchase(Date aPurchaseDate, int aPurchasePrice, Game aGame, Customer aCustomer, CreditCard aPaymentCard, Address aDeliveryAddress)
  {
    purchaseDate = aPurchaseDate;
    purchasePrice = aPurchasePrice;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create purchase due to game. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create purchase due to customer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddPaymentCard = setPaymentCard(aPaymentCard);
    if (!didAddPaymentCard)
    {
      throw new RuntimeException("Unable to create purchase due to paymentCard. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddDeliveryAddress = setDeliveryAddress(aDeliveryAddress);
    if (!didAddDeliveryAddress)
    {
      throw new RuntimeException("Unable to create delivery due to deliveryAddress. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setPurchasePrice(int aPurchasePrice)
  {
    boolean wasSet = false;
    purchasePrice = aPurchasePrice;
    wasSet = true;
    return wasSet;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public int getPurchasePrice()
  {
    return purchasePrice;
  }
  /* Code from template association_GetOne */
  public RefundRequest getRefund()
  {
    return refund;
  }

  public boolean hasRefund()
  {
    boolean has = refund != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Review getReview()
  {
    return review;
  }

  public boolean hasReview()
  {
    boolean has = review != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public CreditCard getPaymentCard()
  {
    return paymentCard;
  }
  /* Code from template association_GetOne */
  public Address getDeliveryAddress()
  {
    return deliveryAddress;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setRefund(RefundRequest aNewRefund)
  {
    boolean wasSet = false;
    if (refund != null && !refund.equals(aNewRefund) && equals(refund.getPurchase()))
    {
      //Unable to setRefund, as existing refund would become an orphan
      return wasSet;
    }

    refund = aNewRefund;
    Purchase anOldPurchase = aNewRefund != null ? aNewRefund.getPurchase() : null;

    if (!this.equals(anOldPurchase))
    {
      if (anOldPurchase != null)
      {
        anOldPurchase.refund = null;
      }
      if (refund != null)
      {
        refund.setPurchase(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removePurchase(this);
    }
    game.addPurchase(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setReview(Review aNewReview)
  {
    boolean wasSet = false;
    if (review != null && !review.equals(aNewReview) && equals(review.getGame()))
    {
      //Unable to setReview, as existing review would become an orphan
      return wasSet;
    }

    review = aNewReview;
    Purchase anOldGame = aNewReview != null ? aNewReview.getGame() : null;

    if (!this.equals(anOldGame))
    {
      if (anOldGame != null)
      {
        anOldGame.review = null;
      }
      if (review != null)
      {
        review.setGame(this);
      }
    }
    wasSet = true;
    return wasSet;
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
      existingCustomer.removePurchase(this);
    }
    customer.addPurchase(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setPaymentCard(CreditCard aPaymentCard)
  {
    boolean wasSet = false;
    if (aPaymentCard == null)
    {
      return wasSet;
    }

    CreditCard existingPaymentCard = paymentCard;
    paymentCard = aPaymentCard;
    if (existingPaymentCard != null && !existingPaymentCard.equals(aPaymentCard))
    {
      existingPaymentCard.removePurchase(this);
    }
    paymentCard.addPurchase(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setDeliveryAddress(Address aDeliveryAddress)
  {
    boolean wasSet = false;
    if (aDeliveryAddress == null)
    {
      return wasSet;
    }

    Address existingDeliveryAddress = deliveryAddress;
    deliveryAddress = aDeliveryAddress;
    if (existingDeliveryAddress != null && !existingDeliveryAddress.equals(aDeliveryAddress))
    {
      existingDeliveryAddress.removeDelivery(this);
    }
    deliveryAddress.addDelivery(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    RefundRequest existingRefund = refund;
    refund = null;
    if (existingRefund != null)
    {
      existingRefund.delete();
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePurchase(this);
    }
    Review existingReview = review;
    review = null;
    if (existingReview != null)
    {
      existingReview.delete();
    }
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removePurchase(this);
    }
    CreditCard placeholderPaymentCard = paymentCard;
    this.paymentCard = null;
    if(placeholderPaymentCard != null)
    {
      placeholderPaymentCard.removePurchase(this);
    }
    Address placeholderDeliveryAddress = deliveryAddress;
    this.deliveryAddress = null;
    if(placeholderDeliveryAddress != null)
    {
      placeholderDeliveryAddress.removeDelivery(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "purchasePrice" + ":" + getPurchasePrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "refund = "+(getRefund()!=null?Integer.toHexString(System.identityHashCode(getRefund())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "review = "+(getReview()!=null?Integer.toHexString(System.identityHashCode(getReview())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "paymentCard = "+(getPaymentCard()!=null?Integer.toHexString(System.identityHashCode(getPaymentCard())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "deliveryAddress = "+(getDeliveryAddress()!=null?Integer.toHexString(System.identityHashCode(getDeliveryAddress())):"null");
  }
}