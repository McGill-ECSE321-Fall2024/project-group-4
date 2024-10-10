/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;
import java.sql.Date;

// line 8 "model.ump"
// line 136 "model.ump"
public class Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private String name;
  private String description;
  private String coverPicture;
  private int price;
  private boolean is_active;
  private String stock;

  //Game Associations
  private List<Category> catalog;
  private List<Promotion> promotion;
  private GameRequest request;
  private List<Customer> customer_wish;
  private List<Customer> customer_cart;
  private List<Purchase> purchase;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aName, String aDescription, String aCoverPicture, int aPrice, boolean aIs_active, String aStock, GameRequest aRequest, Category... allCatalog)
  {
    name = aName;
    description = aDescription;
    coverPicture = aCoverPicture;
    price = aPrice;
    is_active = aIs_active;
    stock = aStock;
    catalog = new ArrayList<Category>();
    boolean didAddCatalog = setCatalog(allCatalog);
    if (!didAddCatalog)
    {
      throw new RuntimeException("Unable to create Game, must have at least 1 catalog. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    promotion = new ArrayList<Promotion>();
    if (aRequest == null || aRequest.getGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aRequest. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    request = aRequest;
    customer_wish = new ArrayList<Customer>();
    customer_cart = new ArrayList<Customer>();
    purchase = new ArrayList<Purchase>();
  }

  public Game(String aName, String aDescription, String aCoverPicture, int aPrice, boolean aIs_active, String aStock, RequestStatus aStatusForRequest, String aExternalReviewsForRequest, Employee aRequestorForRequest, Manager aApproverForRequest, Category... allCatalog)
  {
    name = aName;
    description = aDescription;
    coverPicture = aCoverPicture;
    price = aPrice;
    is_active = aIs_active;
    stock = aStock;
    catalog = new ArrayList<Category>();
    boolean didAddCatalog = setCatalog(allCatalog);
    if (!didAddCatalog)
    {
      throw new RuntimeException("Unable to create Game, must have at least 1 catalog. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    promotion = new ArrayList<Promotion>();
    request = new GameRequest(aStatusForRequest, aExternalReviewsForRequest, this, aRequestorForRequest, aApproverForRequest);
    customer_wish = new ArrayList<Customer>();
    customer_cart = new ArrayList<Customer>();
    purchase = new ArrayList<Purchase>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setCoverPicture(String aCoverPicture)
  {
    boolean wasSet = false;
    coverPicture = aCoverPicture;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setIs_active(boolean aIs_active)
  {
    boolean wasSet = false;
    is_active = aIs_active;
    wasSet = true;
    return wasSet;
  }

  public boolean setStock(String aStock)
  {
    boolean wasSet = false;
    stock = aStock;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public String getCoverPicture()
  {
    return coverPicture;
  }

  public int getPrice()
  {
    return price;
  }

  public boolean getIs_active()
  {
    return is_active;
  }

  public String getStock()
  {
    return stock;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIs_active()
  {
    return is_active;
  }
  /* Code from template association_GetMany */
  public Category getCatalog(int index)
  {
    Category aCatalog = catalog.get(index);
    return aCatalog;
  }

  public List<Category> getCatalog()
  {
    List<Category> newCatalog = Collections.unmodifiableList(catalog);
    return newCatalog;
  }

  public int numberOfCatalog()
  {
    int number = catalog.size();
    return number;
  }

  public boolean hasCatalog()
  {
    boolean has = catalog.size() > 0;
    return has;
  }

  public int indexOfCatalog(Category aCatalog)
  {
    int index = catalog.indexOf(aCatalog);
    return index;
  }
  /* Code from template association_GetMany */
  public Promotion getPromotion(int index)
  {
    Promotion aPromotion = promotion.get(index);
    return aPromotion;
  }

  public List<Promotion> getPromotion()
  {
    List<Promotion> newPromotion = Collections.unmodifiableList(promotion);
    return newPromotion;
  }

  public int numberOfPromotion()
  {
    int number = promotion.size();
    return number;
  }

  public boolean hasPromotion()
  {
    boolean has = promotion.size() > 0;
    return has;
  }

  public int indexOfPromotion(Promotion aPromotion)
  {
    int index = promotion.indexOf(aPromotion);
    return index;
  }
  /* Code from template association_GetOne */
  public GameRequest getRequest()
  {
    return request;
  }
  /* Code from template association_GetMany */
  public Customer getCustomer_wish(int index)
  {
    Customer aCustomer_wish = customer_wish.get(index);
    return aCustomer_wish;
  }

  public List<Customer> getCustomer_wish()
  {
    List<Customer> newCustomer_wish = Collections.unmodifiableList(customer_wish);
    return newCustomer_wish;
  }

  public int numberOfCustomer_wish()
  {
    int number = customer_wish.size();
    return number;
  }

  public boolean hasCustomer_wish()
  {
    boolean has = customer_wish.size() > 0;
    return has;
  }

  public int indexOfCustomer_wish(Customer aCustomer_wish)
  {
    int index = customer_wish.indexOf(aCustomer_wish);
    return index;
  }
  /* Code from template association_GetMany */
  public Customer getCustomer_cart(int index)
  {
    Customer aCustomer_cart = customer_cart.get(index);
    return aCustomer_cart;
  }

  public List<Customer> getCustomer_cart()
  {
    List<Customer> newCustomer_cart = Collections.unmodifiableList(customer_cart);
    return newCustomer_cart;
  }

  public int numberOfCustomer_cart()
  {
    int number = customer_cart.size();
    return number;
  }

  public boolean hasCustomer_cart()
  {
    boolean has = customer_cart.size() > 0;
    return has;
  }

  public int indexOfCustomer_cart(Customer aCustomer_cart)
  {
    int index = customer_cart.indexOf(aCustomer_cart);
    return index;
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
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfCatalogValid()
  {
    boolean isValid = numberOfCatalog() >= minimumNumberOfCatalog();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCatalog()
  {
    return 1;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addCatalog(Category aCatalog)
  {
    boolean wasAdded = false;
    if (catalog.contains(aCatalog)) { return false; }
    catalog.add(aCatalog);
    if (aCatalog.indexOfGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aCatalog.addGame(this);
      if (!wasAdded)
      {
        catalog.remove(aCatalog);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMStarToMany */
  public boolean removeCatalog(Category aCatalog)
  {
    boolean wasRemoved = false;
    if (!catalog.contains(aCatalog))
    {
      return wasRemoved;
    }

    if (numberOfCatalog() <= minimumNumberOfCatalog())
    {
      return wasRemoved;
    }

    int oldIndex = catalog.indexOf(aCatalog);
    catalog.remove(oldIndex);
    if (aCatalog.indexOfGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aCatalog.removeGame(this);
      if (!wasRemoved)
      {
        catalog.add(oldIndex,aCatalog);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMStarToMany */
  public boolean setCatalog(Category... newCatalog)
  {
    boolean wasSet = false;
    ArrayList<Category> verifiedCatalog = new ArrayList<Category>();
    for (Category aCatalog : newCatalog)
    {
      if (verifiedCatalog.contains(aCatalog))
      {
        continue;
      }
      verifiedCatalog.add(aCatalog);
    }

    if (verifiedCatalog.size() != newCatalog.length || verifiedCatalog.size() < minimumNumberOfCatalog())
    {
      return wasSet;
    }

    ArrayList<Category> oldCatalog = new ArrayList<Category>(catalog);
    catalog.clear();
    for (Category aNewCatalog : verifiedCatalog)
    {
      catalog.add(aNewCatalog);
      if (oldCatalog.contains(aNewCatalog))
      {
        oldCatalog.remove(aNewCatalog);
      }
      else
      {
        aNewCatalog.addGame(this);
      }
    }

    for (Category anOldCatalog : oldCatalog)
    {
      anOldCatalog.removeGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCatalogAt(Category aCatalog, int index)
  {  
    boolean wasAdded = false;
    if(addCatalog(aCatalog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCatalog()) { index = numberOfCatalog() - 1; }
      catalog.remove(aCatalog);
      catalog.add(index, aCatalog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCatalogAt(Category aCatalog, int index)
  {
    boolean wasAdded = false;
    if(catalog.contains(aCatalog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCatalog()) { index = numberOfCatalog() - 1; }
      catalog.remove(aCatalog);
      catalog.add(index, aCatalog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCatalogAt(aCatalog, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotion()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addPromotion(Promotion aPromotion)
  {
    boolean wasAdded = false;
    if (promotion.contains(aPromotion)) { return false; }
    promotion.add(aPromotion);
    if (aPromotion.indexOfGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aPromotion.addGame(this);
      if (!wasAdded)
      {
        promotion.remove(aPromotion);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removePromotion(Promotion aPromotion)
  {
    boolean wasRemoved = false;
    if (!promotion.contains(aPromotion))
    {
      return wasRemoved;
    }

    int oldIndex = promotion.indexOf(aPromotion);
    promotion.remove(oldIndex);
    if (aPromotion.indexOfGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aPromotion.removeGame(this);
      if (!wasRemoved)
      {
        promotion.add(oldIndex,aPromotion);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotionAt(Promotion aPromotion, int index)
  {  
    boolean wasAdded = false;
    if(addPromotion(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotion()) { index = numberOfPromotion() - 1; }
      promotion.remove(aPromotion);
      promotion.add(index, aPromotion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotionAt(Promotion aPromotion, int index)
  {
    boolean wasAdded = false;
    if(promotion.contains(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotion()) { index = numberOfPromotion() - 1; }
      promotion.remove(aPromotion);
      promotion.add(index, aPromotion);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPromotionAt(aPromotion, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomer_wish()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addCustomer_wish(Customer aCustomer_wish)
  {
    boolean wasAdded = false;
    if (customer_wish.contains(aCustomer_wish)) { return false; }
    customer_wish.add(aCustomer_wish);
    if (aCustomer_wish.indexOfWishlist(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aCustomer_wish.addWishlist(this);
      if (!wasAdded)
      {
        customer_wish.remove(aCustomer_wish);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeCustomer_wish(Customer aCustomer_wish)
  {
    boolean wasRemoved = false;
    if (!customer_wish.contains(aCustomer_wish))
    {
      return wasRemoved;
    }

    int oldIndex = customer_wish.indexOf(aCustomer_wish);
    customer_wish.remove(oldIndex);
    if (aCustomer_wish.indexOfWishlist(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aCustomer_wish.removeWishlist(this);
      if (!wasRemoved)
      {
        customer_wish.add(oldIndex,aCustomer_wish);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomer_wishAt(Customer aCustomer_wish, int index)
  {  
    boolean wasAdded = false;
    if(addCustomer_wish(aCustomer_wish))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomer_wish()) { index = numberOfCustomer_wish() - 1; }
      customer_wish.remove(aCustomer_wish);
      customer_wish.add(index, aCustomer_wish);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomer_wishAt(Customer aCustomer_wish, int index)
  {
    boolean wasAdded = false;
    if(customer_wish.contains(aCustomer_wish))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomer_wish()) { index = numberOfCustomer_wish() - 1; }
      customer_wish.remove(aCustomer_wish);
      customer_wish.add(index, aCustomer_wish);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomer_wishAt(aCustomer_wish, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomer_cart()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addCustomer_cart(Customer aCustomer_cart)
  {
    boolean wasAdded = false;
    if (customer_cart.contains(aCustomer_cart)) { return false; }
    customer_cart.add(aCustomer_cart);
    if (aCustomer_cart.indexOfCart(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aCustomer_cart.addCart(this);
      if (!wasAdded)
      {
        customer_cart.remove(aCustomer_cart);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeCustomer_cart(Customer aCustomer_cart)
  {
    boolean wasRemoved = false;
    if (!customer_cart.contains(aCustomer_cart))
    {
      return wasRemoved;
    }

    int oldIndex = customer_cart.indexOf(aCustomer_cart);
    customer_cart.remove(oldIndex);
    if (aCustomer_cart.indexOfCart(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aCustomer_cart.removeCart(this);
      if (!wasRemoved)
      {
        customer_cart.add(oldIndex,aCustomer_cart);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomer_cartAt(Customer aCustomer_cart, int index)
  {  
    boolean wasAdded = false;
    if(addCustomer_cart(aCustomer_cart))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomer_cart()) { index = numberOfCustomer_cart() - 1; }
      customer_cart.remove(aCustomer_cart);
      customer_cart.add(index, aCustomer_cart);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomer_cartAt(Customer aCustomer_cart, int index)
  {
    boolean wasAdded = false;
    if(customer_cart.contains(aCustomer_cart))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomer_cart()) { index = numberOfCustomer_cart() - 1; }
      customer_cart.remove(aCustomer_cart);
      customer_cart.add(index, aCustomer_cart);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomer_cartAt(aCustomer_cart, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPurchase()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addPurchase(Date aPurchaseDate, int aPurchasePrice, Customer aCustomer, CreditCard aPaymentCard, Address aDeliveryAddress)
  {
    return new Purchase(aPurchaseDate, aPurchasePrice, this, aCustomer, aPaymentCard, aDeliveryAddress);
  }

  public boolean addPurchase(Purchase aPurchase)
  {
    boolean wasAdded = false;
    if (purchase.contains(aPurchase)) { return false; }
    Game existingGame = aPurchase.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);
    if (isNewGame)
    {
      aPurchase.setGame(this);
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
    //Unable to remove aPurchase, as it must always have a game
    if (!this.equals(aPurchase.getGame()))
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

  public void delete()
  {
    ArrayList<Category> copyOfCatalog = new ArrayList<Category>(catalog);
    catalog.clear();
    for(Category aCatalog : copyOfCatalog)
    {
      aCatalog.removeGame(this);
    }
    ArrayList<Promotion> copyOfPromotion = new ArrayList<Promotion>(promotion);
    promotion.clear();
    for(Promotion aPromotion : copyOfPromotion)
    {
      if (aPromotion.numberOfGame() <= Promotion.minimumNumberOfGame())
      {
        aPromotion.delete();
      }
      else
      {
        aPromotion.removeGame(this);
      }
    }
    GameRequest existingRequest = request;
    request = null;
    if (existingRequest != null)
    {
      existingRequest.delete();
    }
    ArrayList<Customer> copyOfCustomer_wish = new ArrayList<Customer>(customer_wish);
    customer_wish.clear();
    for(Customer aCustomer_wish : copyOfCustomer_wish)
    {
      aCustomer_wish.removeWishlist(this);
    }
    ArrayList<Customer> copyOfCustomer_cart = new ArrayList<Customer>(customer_cart);
    customer_cart.clear();
    for(Customer aCustomer_cart : copyOfCustomer_cart)
    {
      aCustomer_cart.removeCart(this);
    }
    for(int i=purchase.size(); i > 0; i--)
    {
      Purchase aPurchase = purchase.get(i - 1);
      aPurchase.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "coverPicture" + ":" + getCoverPicture()+ "," +
            "price" + ":" + getPrice()+ "," +
            "is_active" + ":" + getIs_active()+ "," +
            "stock" + ":" + getStock()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "request = "+(getRequest()!=null?Integer.toHexString(System.identityHashCode(getRequest())):"null");
  }
}