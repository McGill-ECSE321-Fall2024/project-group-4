/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;
import java.sql.Date;

// line 69 "model.ump"
// line 185 "model.ump"
public class Customer extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Attributes
  private String email;
  private int phoneNumber;

  //Customer Associations
  private List<Game> wishlist;
  private List<Game> cart;
  private List<Purchase> purchases;
  private List<Review> likes;
  private List<CreditCard> cards;
  private List<Address> address;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aUsername, String aPassword, String aEmail, int aPhoneNumber)
  {
    super(aUsername, aPassword);
    email = aEmail;
    phoneNumber = aPhoneNumber;
    wishlist = new ArrayList<Game>();
    cart = new ArrayList<Game>();
    purchases = new ArrayList<Purchase>();
    likes = new ArrayList<Review>();
    cards = new ArrayList<CreditCard>();
    address = new ArrayList<Address>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(int aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public int getPhoneNumber()
  {
    return phoneNumber;
  }
  /* Code from template association_GetMany */
  public Game getWishlist(int index)
  {
    Game aWishlist = wishlist.get(index);
    return aWishlist;
  }

  public List<Game> getWishlist()
  {
    List<Game> newWishlist = Collections.unmodifiableList(wishlist);
    return newWishlist;
  }

  public int numberOfWishlist()
  {
    int number = wishlist.size();
    return number;
  }

  public boolean hasWishlist()
  {
    boolean has = wishlist.size() > 0;
    return has;
  }

  public int indexOfWishlist(Game aWishlist)
  {
    int index = wishlist.indexOf(aWishlist);
    return index;
  }
  /* Code from template association_GetMany */
  public Game getCart(int index)
  {
    Game aCart = cart.get(index);
    return aCart;
  }

  public List<Game> getCart()
  {
    List<Game> newCart = Collections.unmodifiableList(cart);
    return newCart;
  }

  public int numberOfCart()
  {
    int number = cart.size();
    return number;
  }

  public boolean hasCart()
  {
    boolean has = cart.size() > 0;
    return has;
  }

  public int indexOfCart(Game aCart)
  {
    int index = cart.indexOf(aCart);
    return index;
  }
  /* Code from template association_GetMany */
  public Purchase getPurchase(int index)
  {
    Purchase aPurchase = purchases.get(index);
    return aPurchase;
  }

  public List<Purchase> getPurchases()
  {
    List<Purchase> newPurchases = Collections.unmodifiableList(purchases);
    return newPurchases;
  }

  public int numberOfPurchases()
  {
    int number = purchases.size();
    return number;
  }

  public boolean hasPurchases()
  {
    boolean has = purchases.size() > 0;
    return has;
  }

  public int indexOfPurchase(Purchase aPurchase)
  {
    int index = purchases.indexOf(aPurchase);
    return index;
  }
  /* Code from template association_GetMany */
  public Review getLike(int index)
  {
    Review aLike = likes.get(index);
    return aLike;
  }

  public List<Review> getLikes()
  {
    List<Review> newLikes = Collections.unmodifiableList(likes);
    return newLikes;
  }

  public int numberOfLikes()
  {
    int number = likes.size();
    return number;
  }

  public boolean hasLikes()
  {
    boolean has = likes.size() > 0;
    return has;
  }

  public int indexOfLike(Review aLike)
  {
    int index = likes.indexOf(aLike);
    return index;
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
  public Address getAddress(int index)
  {
    Address aAddress = address.get(index);
    return aAddress;
  }

  public List<Address> getAddress()
  {
    List<Address> newAddress = Collections.unmodifiableList(address);
    return newAddress;
  }

  public int numberOfAddress()
  {
    int number = address.size();
    return number;
  }

  public boolean hasAddress()
  {
    boolean has = address.size() > 0;
    return has;
  }

  public int indexOfAddress(Address aAddress)
  {
    int index = address.indexOf(aAddress);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWishlist()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addWishlist(Game aWishlist)
  {
    boolean wasAdded = false;
    if (wishlist.contains(aWishlist)) { return false; }
    wishlist.add(aWishlist);
    if (aWishlist.indexOfCustomer_wish(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aWishlist.addCustomer_wish(this);
      if (!wasAdded)
      {
        wishlist.remove(aWishlist);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeWishlist(Game aWishlist)
  {
    boolean wasRemoved = false;
    if (!wishlist.contains(aWishlist))
    {
      return wasRemoved;
    }

    int oldIndex = wishlist.indexOf(aWishlist);
    wishlist.remove(oldIndex);
    if (aWishlist.indexOfCustomer_wish(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aWishlist.removeCustomer_wish(this);
      if (!wasRemoved)
      {
        wishlist.add(oldIndex,aWishlist);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishlistAt(Game aWishlist, int index)
  {  
    boolean wasAdded = false;
    if(addWishlist(aWishlist))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlist()) { index = numberOfWishlist() - 1; }
      wishlist.remove(aWishlist);
      wishlist.add(index, aWishlist);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishlistAt(Game aWishlist, int index)
  {
    boolean wasAdded = false;
    if(wishlist.contains(aWishlist))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlist()) { index = numberOfWishlist() - 1; }
      wishlist.remove(aWishlist);
      wishlist.add(index, aWishlist);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishlistAt(aWishlist, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCart()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addCart(Game aCart)
  {
    boolean wasAdded = false;
    if (cart.contains(aCart)) { return false; }
    cart.add(aCart);
    if (aCart.indexOfCustomer_cart(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aCart.addCustomer_cart(this);
      if (!wasAdded)
      {
        cart.remove(aCart);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeCart(Game aCart)
  {
    boolean wasRemoved = false;
    if (!cart.contains(aCart))
    {
      return wasRemoved;
    }

    int oldIndex = cart.indexOf(aCart);
    cart.remove(oldIndex);
    if (aCart.indexOfCustomer_cart(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aCart.removeCustomer_cart(this);
      if (!wasRemoved)
      {
        cart.add(oldIndex,aCart);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCartAt(Game aCart, int index)
  {  
    boolean wasAdded = false;
    if(addCart(aCart))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCart()) { index = numberOfCart() - 1; }
      cart.remove(aCart);
      cart.add(index, aCart);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCartAt(Game aCart, int index)
  {
    boolean wasAdded = false;
    if(cart.contains(aCart))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCart()) { index = numberOfCart() - 1; }
      cart.remove(aCart);
      cart.add(index, aCart);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCartAt(aCart, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPurchases()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addPurchase(Date aPurchaseDate, int aPurchasePrice, Game aGame, CreditCard aPaymentCard, Address aDeliveryAddress)
  {
    return new Purchase(aPurchaseDate, aPurchasePrice, aGame, this, aPaymentCard, aDeliveryAddress);
  }

  public boolean addPurchase(Purchase aPurchase)
  {
    boolean wasAdded = false;
    if (purchases.contains(aPurchase)) { return false; }
    Customer existingCustomer = aPurchase.getCustomer();
    boolean isNewCustomer = existingCustomer != null && !this.equals(existingCustomer);
    if (isNewCustomer)
    {
      aPurchase.setCustomer(this);
    }
    else
    {
      purchases.add(aPurchase);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePurchase(Purchase aPurchase)
  {
    boolean wasRemoved = false;
    //Unable to remove aPurchase, as it must always have a customer
    if (!this.equals(aPurchase.getCustomer()))
    {
      purchases.remove(aPurchase);
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
      if(index > numberOfPurchases()) { index = numberOfPurchases() - 1; }
      purchases.remove(aPurchase);
      purchases.add(index, aPurchase);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePurchaseAt(Purchase aPurchase, int index)
  {
    boolean wasAdded = false;
    if(purchases.contains(aPurchase))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchases()) { index = numberOfPurchases() - 1; }
      purchases.remove(aPurchase);
      purchases.add(index, aPurchase);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPurchaseAt(aPurchase, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLikes()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addLike(Review aLike)
  {
    boolean wasAdded = false;
    if (likes.contains(aLike)) { return false; }
    likes.add(aLike);
    if (aLike.indexOfLikedBy(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aLike.addLikedBy(this);
      if (!wasAdded)
      {
        likes.remove(aLike);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeLike(Review aLike)
  {
    boolean wasRemoved = false;
    if (!likes.contains(aLike))
    {
      return wasRemoved;
    }

    int oldIndex = likes.indexOf(aLike);
    likes.remove(oldIndex);
    if (aLike.indexOfLikedBy(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aLike.removeLikedBy(this);
      if (!wasRemoved)
      {
        likes.add(oldIndex,aLike);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLikeAt(Review aLike, int index)
  {  
    boolean wasAdded = false;
    if(addLike(aLike))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLikes()) { index = numberOfLikes() - 1; }
      likes.remove(aLike);
      likes.add(index, aLike);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLikeAt(Review aLike, int index)
  {
    boolean wasAdded = false;
    if(likes.contains(aLike))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLikes()) { index = numberOfLikes() - 1; }
      likes.remove(aLike);
      likes.add(index, aLike);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLikeAt(aLike, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCards()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CreditCard addCard(int aCardNumber, Date aExpiry, int aCvv, Address aBilingAddress)
  {
    return new CreditCard(aCardNumber, aExpiry, aCvv, this, aBilingAddress);
  }

  public boolean addCard(CreditCard aCard)
  {
    boolean wasAdded = false;
    if (cards.contains(aCard)) { return false; }
    Customer existingName = aCard.getName();
    boolean isNewName = existingName != null && !this.equals(existingName);
    if (isNewName)
    {
      aCard.setName(this);
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
    //Unable to remove aCard, as it must always have a name
    if (!this.equals(aCard.getName()))
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
  public static int minimumNumberOfAddress()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Address addAddress(String aStreet, String aCity, String aProvince, String aCountry, String aPostalCode)
  {
    return new Address(aStreet, aCity, aProvince, aCountry, aPostalCode, this);
  }

  public boolean addAddress(Address aAddress)
  {
    boolean wasAdded = false;
    if (address.contains(aAddress)) { return false; }
    Customer existingCustomer = aAddress.getCustomer();
    boolean isNewCustomer = existingCustomer != null && !this.equals(existingCustomer);
    if (isNewCustomer)
    {
      aAddress.setCustomer(this);
    }
    else
    {
      address.add(aAddress);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAddress(Address aAddress)
  {
    boolean wasRemoved = false;
    //Unable to remove aAddress, as it must always have a customer
    if (!this.equals(aAddress.getCustomer()))
    {
      address.remove(aAddress);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAddressAt(Address aAddress, int index)
  {  
    boolean wasAdded = false;
    if(addAddress(aAddress))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAddress()) { index = numberOfAddress() - 1; }
      address.remove(aAddress);
      address.add(index, aAddress);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAddressAt(Address aAddress, int index)
  {
    boolean wasAdded = false;
    if(address.contains(aAddress))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAddress()) { index = numberOfAddress() - 1; }
      address.remove(aAddress);
      address.add(index, aAddress);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAddressAt(aAddress, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ArrayList<Game> copyOfWishlist = new ArrayList<Game>(wishlist);
    wishlist.clear();
    for(Game aWishlist : copyOfWishlist)
    {
      aWishlist.removeCustomer_wish(this);
    }
    ArrayList<Game> copyOfCart = new ArrayList<Game>(cart);
    cart.clear();
    for(Game aCart : copyOfCart)
    {
      aCart.removeCustomer_cart(this);
    }
    while (purchases.size() > 0)
    {
      Purchase aPurchase = purchases.get(purchases.size() - 1);
      aPurchase.delete();
      purchases.remove(aPurchase);
    }
    
    while (likes.size() > 0)
    {
      Review aLike = likes.get(likes.size() - 1);
      aLike.delete();
      likes.remove(aLike);
    }
    
    for(int i=cards.size(); i > 0; i--)
    {
      CreditCard aCard = cards.get(i - 1);
      aCard.delete();
    }
    for(int i=address.size(); i > 0; i--)
    {
      Address aAddress = address.get(i - 1);
      aAddress.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "]";
  }
}