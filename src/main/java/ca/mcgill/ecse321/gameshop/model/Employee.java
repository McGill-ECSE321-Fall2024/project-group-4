/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 42 "model.ump"
// line 162 "model.ump"
public class Employee extends Account
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestStatus { Approved, Denied, Pending }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Employee Attributes
  private boolean is_active;

  //Employee Associations
  private Manager creator;
  private List<GameRequest> requests;
  private List<RefundRequest> assignedRefund;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Employee(String aUsername, String aPassword, boolean aIs_active, Manager aCreator)
  {
    super(aUsername, aPassword);
    is_active = aIs_active;
    boolean didAddCreator = setCreator(aCreator);
    if (!didAddCreator)
    {
      throw new RuntimeException("Unable to create employee due to creator. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    requests = new ArrayList<GameRequest>();
    assignedRefund = new ArrayList<RefundRequest>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIs_active(boolean aIs_active)
  {
    boolean wasSet = false;
    is_active = aIs_active;
    wasSet = true;
    return wasSet;
  }

  public boolean getIs_active()
  {
    return is_active;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIs_active()
  {
    return is_active;
  }
  /* Code from template association_GetOne */
  public Manager getCreator()
  {
    return creator;
  }
  /* Code from template association_GetMany */
  public GameRequest getRequest(int index)
  {
    GameRequest aRequest = requests.get(index);
    return aRequest;
  }

  public List<GameRequest> getRequests()
  {
    List<GameRequest> newRequests = Collections.unmodifiableList(requests);
    return newRequests;
  }

  public int numberOfRequests()
  {
    int number = requests.size();
    return number;
  }

  public boolean hasRequests()
  {
    boolean has = requests.size() > 0;
    return has;
  }

  public int indexOfRequest(GameRequest aRequest)
  {
    int index = requests.indexOf(aRequest);
    return index;
  }
  /* Code from template association_GetMany */
  public RefundRequest getAssignedRefund(int index)
  {
    RefundRequest aAssignedRefund = assignedRefund.get(index);
    return aAssignedRefund;
  }

  public List<RefundRequest> getAssignedRefund()
  {
    List<RefundRequest> newAssignedRefund = Collections.unmodifiableList(assignedRefund);
    return newAssignedRefund;
  }

  public int numberOfAssignedRefund()
  {
    int number = assignedRefund.size();
    return number;
  }

  public boolean hasAssignedRefund()
  {
    boolean has = assignedRefund.size() > 0;
    return has;
  }

  public int indexOfAssignedRefund(RefundRequest aAssignedRefund)
  {
    int index = assignedRefund.indexOf(aAssignedRefund);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCreator(Manager aCreator)
  {
    boolean wasSet = false;
    if (aCreator == null)
    {
      return wasSet;
    }

    Manager existingCreator = creator;
    creator = aCreator;
    if (existingCreator != null && !existingCreator.equals(aCreator))
    {
      existingCreator.removeEmployee(this);
    }
    creator.addEmployee(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameRequest addRequest(RequestStatus aStatus, String aExternalReviews, Game aGame, Manager aApprover)
  {
    return new GameRequest(aStatus, aExternalReviews, aGame, this, aApprover);
  }

  public boolean addRequest(GameRequest aRequest)
  {
    boolean wasAdded = false;
    if (requests.contains(aRequest)) { return false; }
    Employee existingRequestor = aRequest.getRequestor();
    boolean isNewRequestor = existingRequestor != null && !this.equals(existingRequestor);
    if (isNewRequestor)
    {
      aRequest.setRequestor(this);
    }
    else
    {
      requests.add(aRequest);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRequest(GameRequest aRequest)
  {
    boolean wasRemoved = false;
    //Unable to remove aRequest, as it must always have a requestor
    if (!this.equals(aRequest.getRequestor()))
    {
      requests.remove(aRequest);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRequestAt(GameRequest aRequest, int index)
  {  
    boolean wasAdded = false;
    if(addRequest(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequests()) { index = numberOfRequests() - 1; }
      requests.remove(aRequest);
      requests.add(index, aRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRequestAt(GameRequest aRequest, int index)
  {
    boolean wasAdded = false;
    if(requests.contains(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequests()) { index = numberOfRequests() - 1; }
      requests.remove(aRequest);
      requests.add(index, aRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRequestAt(aRequest, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssignedRefund()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addAssignedRefund(RefundRequest aAssignedRefund)
  {
    boolean wasAdded = false;
    if (assignedRefund.contains(aAssignedRefund)) { return false; }
    Employee existingReviewer = aAssignedRefund.getReviewer();
    if (existingReviewer == null)
    {
      aAssignedRefund.setReviewer(this);
    }
    else if (!this.equals(existingReviewer))
    {
      existingReviewer.removeAssignedRefund(aAssignedRefund);
      addAssignedRefund(aAssignedRefund);
    }
    else
    {
      assignedRefund.add(aAssignedRefund);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAssignedRefund(RefundRequest aAssignedRefund)
  {
    boolean wasRemoved = false;
    if (assignedRefund.contains(aAssignedRefund))
    {
      assignedRefund.remove(aAssignedRefund);
      aAssignedRefund.setReviewer(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAssignedRefundAt(RefundRequest aAssignedRefund, int index)
  {  
    boolean wasAdded = false;
    if(addAssignedRefund(aAssignedRefund))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignedRefund()) { index = numberOfAssignedRefund() - 1; }
      assignedRefund.remove(aAssignedRefund);
      assignedRefund.add(index, aAssignedRefund);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAssignedRefundAt(RefundRequest aAssignedRefund, int index)
  {
    boolean wasAdded = false;
    if(assignedRefund.contains(aAssignedRefund))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignedRefund()) { index = numberOfAssignedRefund() - 1; }
      assignedRefund.remove(aAssignedRefund);
      assignedRefund.add(index, aAssignedRefund);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAssignedRefundAt(aAssignedRefund, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Manager placeholderCreator = creator;
    this.creator = null;
    if(placeholderCreator != null)
    {
      placeholderCreator.removeEmployee(this);
    }
    for(int i=requests.size(); i > 0; i--)
    {
      GameRequest aRequest = requests.get(i - 1);
      aRequest.delete();
    }
    while( !assignedRefund.isEmpty() )
    {
      assignedRefund.get(0).setReviewer(null);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "is_active" + ":" + getIs_active()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "creator = "+(getCreator()!=null?Integer.toHexString(System.identityHashCode(getCreator())):"null");
  }
}