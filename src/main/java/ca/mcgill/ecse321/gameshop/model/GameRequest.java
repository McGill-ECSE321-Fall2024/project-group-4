/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 46 "model.ump"
// line 165 "model.ump"
public class GameRequest
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestStatus { Approved, Denied, Pending }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameRequest Attributes
  private RequestStatus status;
  private String externalReviews;

  //GameRequest Associations
  private Game game;
  private Employee requestor;
  private Manager approver;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameRequest(RequestStatus aStatus, String aExternalReviews, Game aGame, Employee aRequestor, Manager aApprover)
  {
    status = aStatus;
    externalReviews = aExternalReviews;
    if (aGame == null || aGame.getRequest() != null)
    {
      throw new RuntimeException("Unable to create GameRequest due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    game = aGame;
    boolean didAddRequestor = setRequestor(aRequestor);
    if (!didAddRequestor)
    {
      throw new RuntimeException("Unable to create request due to requestor. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddApprover = setApprover(aApprover);
    if (!didAddApprover)
    {
      throw new RuntimeException("Unable to create request due to approver. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public GameRequest(RequestStatus aStatus, String aExternalReviews, String aNameForGame, String aDescriptionForGame, String aCoverPictureForGame, int aPriceForGame, boolean aIs_activeForGame, String aStockForGame, Customer aCustomer_wishForGame, Customer aCustomer_cartForGame, Category... allCatalogForGame, Employee aRequestor, Manager aApprover)
  {
    status = aStatus;
    externalReviews = aExternalReviews;
    game = new Game(aNameForGame, aDescriptionForGame, aCoverPictureForGame, aPriceForGame, aIs_activeForGame, aStockForGame, this, aCustomer_wishForGame, aCustomer_cartForGame, allCatalogForGame);
    boolean didAddRequestor = setRequestor(aRequestor);
    if (!didAddRequestor)
    {
      throw new RuntimeException("Unable to create request due to requestor. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddApprover = setApprover(aApprover);
    if (!didAddApprover)
    {
      throw new RuntimeException("Unable to create request due to approver. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStatus(RequestStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setExternalReviews(String aExternalReviews)
  {
    boolean wasSet = false;
    externalReviews = aExternalReviews;
    wasSet = true;
    return wasSet;
  }

  public RequestStatus getStatus()
  {
    return status;
  }

  public String getExternalReviews()
  {
    return externalReviews;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Employee getRequestor()
  {
    return requestor;
  }
  /* Code from template association_GetOne */
  public Manager getApprover()
  {
    return approver;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRequestor(Employee aRequestor)
  {
    boolean wasSet = false;
    if (aRequestor == null)
    {
      return wasSet;
    }

    Employee existingRequestor = requestor;
    requestor = aRequestor;
    if (existingRequestor != null && !existingRequestor.equals(aRequestor))
    {
      existingRequestor.removeRequest(this);
    }
    requestor.addRequest(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setApprover(Manager aApprover)
  {
    boolean wasSet = false;
    if (aApprover == null)
    {
      return wasSet;
    }

    Manager existingApprover = approver;
    approver = aApprover;
    if (existingApprover != null && !existingApprover.equals(aApprover))
    {
      existingApprover.removeRequest(this);
    }
    approver.addRequest(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Game existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.delete();
    }
    Employee placeholderRequestor = requestor;
    this.requestor = null;
    if(placeholderRequestor != null)
    {
      placeholderRequestor.removeRequest(this);
    }
    Manager placeholderApprover = approver;
    this.approver = null;
    if(placeholderApprover != null)
    {
      placeholderApprover.removeRequest(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "externalReviews" + ":" + getExternalReviews()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "requestor = "+(getRequestor()!=null?Integer.toHexString(System.identityHashCode(getRequestor())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "approver = "+(getApprover()!=null?Integer.toHexString(System.identityHashCode(getApprover())):"null");
  }
}