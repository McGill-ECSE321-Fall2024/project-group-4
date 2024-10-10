/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;
import java.sql.Date;

// line 28 "model.ump"
// line 148 "model.ump"
public class Manager extends Account
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestStatus { Approved, Denied, Pending }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Manager Associations
  private List<GameRequest> requests;
  private List<Reply> comments;
  private List<Employee> employee;
  private List<Promotion> promotion;
  private List<Policy> policies;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Manager(String aUsername, String aPassword)
  {
    super(aUsername, aPassword);
    requests = new ArrayList<GameRequest>();
    comments = new ArrayList<Reply>();
    employee = new ArrayList<Employee>();
    promotion = new ArrayList<Promotion>();
    policies = new ArrayList<Policy>();
  }

  //------------------------
  // INTERFACE
  //------------------------
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
  public Reply getComment(int index)
  {
    Reply aComment = comments.get(index);
    return aComment;
  }

  public List<Reply> getComments()
  {
    List<Reply> newComments = Collections.unmodifiableList(comments);
    return newComments;
  }

  public int numberOfComments()
  {
    int number = comments.size();
    return number;
  }

  public boolean hasComments()
  {
    boolean has = comments.size() > 0;
    return has;
  }

  public int indexOfComment(Reply aComment)
  {
    int index = comments.indexOf(aComment);
    return index;
  }
  /* Code from template association_GetMany */
  public Employee getEmployee(int index)
  {
    Employee aEmployee = employee.get(index);
    return aEmployee;
  }

  public List<Employee> getEmployee()
  {
    List<Employee> newEmployee = Collections.unmodifiableList(employee);
    return newEmployee;
  }

  public int numberOfEmployee()
  {
    int number = employee.size();
    return number;
  }

  public boolean hasEmployee()
  {
    boolean has = employee.size() > 0;
    return has;
  }

  public int indexOfEmployee(Employee aEmployee)
  {
    int index = employee.indexOf(aEmployee);
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
  /* Code from template association_GetMany */
  public Policy getPolicy(int index)
  {
    Policy aPolicy = policies.get(index);
    return aPolicy;
  }

  public List<Policy> getPolicies()
  {
    List<Policy> newPolicies = Collections.unmodifiableList(policies);
    return newPolicies;
  }

  public int numberOfPolicies()
  {
    int number = policies.size();
    return number;
  }

  public boolean hasPolicies()
  {
    boolean has = policies.size() > 0;
    return has;
  }

  public int indexOfPolicy(Policy aPolicy)
  {
    int index = policies.indexOf(aPolicy);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameRequest addRequest(RequestStatus aStatus, String aExternalReviews, Game aGame, Employee aRequestor)
  {
    return new GameRequest(aStatus, aExternalReviews, aGame, aRequestor, this);
  }

  public boolean addRequest(GameRequest aRequest)
  {
    boolean wasAdded = false;
    if (requests.contains(aRequest)) { return false; }
    Manager existingApprover = aRequest.getApprover();
    boolean isNewApprover = existingApprover != null && !this.equals(existingApprover);
    if (isNewApprover)
    {
      aRequest.setApprover(this);
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
    //Unable to remove aRequest, as it must always have a approver
    if (!this.equals(aRequest.getApprover()))
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
  public static int minimumNumberOfComments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Reply addComment(String aText, Review aReview)
  {
    return new Reply(aText, aReview, this);
  }

  public boolean addComment(Reply aComment)
  {
    boolean wasAdded = false;
    if (comments.contains(aComment)) { return false; }
    Manager existingManager = aComment.getManager();
    boolean isNewManager = existingManager != null && !this.equals(existingManager);
    if (isNewManager)
    {
      aComment.setManager(this);
    }
    else
    {
      comments.add(aComment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeComment(Reply aComment)
  {
    boolean wasRemoved = false;
    //Unable to remove aComment, as it must always have a manager
    if (!this.equals(aComment.getManager()))
    {
      comments.remove(aComment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCommentAt(Reply aComment, int index)
  {  
    boolean wasAdded = false;
    if(addComment(aComment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfComments()) { index = numberOfComments() - 1; }
      comments.remove(aComment);
      comments.add(index, aComment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCommentAt(Reply aComment, int index)
  {
    boolean wasAdded = false;
    if(comments.contains(aComment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfComments()) { index = numberOfComments() - 1; }
      comments.remove(aComment);
      comments.add(index, aComment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCommentAt(aComment, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployee()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Employee addEmployee(String aUsername, String aPassword, boolean aIs_active)
  {
    return new Employee(aUsername, aPassword, aIs_active, this);
  }

  public boolean addEmployee(Employee aEmployee)
  {
    boolean wasAdded = false;
    if (employee.contains(aEmployee)) { return false; }
    Manager existingCreator = aEmployee.getCreator();
    boolean isNewCreator = existingCreator != null && !this.equals(existingCreator);
    if (isNewCreator)
    {
      aEmployee.setCreator(this);
    }
    else
    {
      employee.add(aEmployee);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployee(Employee aEmployee)
  {
    boolean wasRemoved = false;
    //Unable to remove aEmployee, as it must always have a creator
    if (!this.equals(aEmployee.getCreator()))
    {
      employee.remove(aEmployee);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeAt(Employee aEmployee, int index)
  {  
    boolean wasAdded = false;
    if(addEmployee(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployee()) { index = numberOfEmployee() - 1; }
      employee.remove(aEmployee);
      employee.add(index, aEmployee);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAt(Employee aEmployee, int index)
  {
    boolean wasAdded = false;
    if(employee.contains(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployee()) { index = numberOfEmployee() - 1; }
      employee.remove(aEmployee);
      employee.add(index, aEmployee);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEmployeeAt(aEmployee, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotion()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Promotion addPromotion(String aDiscount, Date aStartdate, Date aEndDate, Game... allGame)
  {
    return new Promotion(aDiscount, aStartdate, aEndDate, this, allGame);
  }

  public boolean addPromotion(Promotion aPromotion)
  {
    boolean wasAdded = false;
    if (promotion.contains(aPromotion)) { return false; }
    Manager existingCreator = aPromotion.getCreator();
    boolean isNewCreator = existingCreator != null && !this.equals(existingCreator);
    if (isNewCreator)
    {
      aPromotion.setCreator(this);
    }
    else
    {
      promotion.add(aPromotion);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePromotion(Promotion aPromotion)
  {
    boolean wasRemoved = false;
    //Unable to remove aPromotion, as it must always have a creator
    if (!this.equals(aPromotion.getCreator()))
    {
      promotion.remove(aPromotion);
      wasRemoved = true;
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
  public static int minimumNumberOfPolicies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Policy addPolicy(String aDescription)
  {
    return new Policy(aDescription, this);
  }

  public boolean addPolicy(Policy aPolicy)
  {
    boolean wasAdded = false;
    if (policies.contains(aPolicy)) { return false; }
    Manager existingManager = aPolicy.getManager();
    boolean isNewManager = existingManager != null && !this.equals(existingManager);
    if (isNewManager)
    {
      aPolicy.setManager(this);
    }
    else
    {
      policies.add(aPolicy);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePolicy(Policy aPolicy)
  {
    boolean wasRemoved = false;
    //Unable to remove aPolicy, as it must always have a manager
    if (!this.equals(aPolicy.getManager()))
    {
      policies.remove(aPolicy);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPolicyAt(Policy aPolicy, int index)
  {  
    boolean wasAdded = false;
    if(addPolicy(aPolicy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPolicies()) { index = numberOfPolicies() - 1; }
      policies.remove(aPolicy);
      policies.add(index, aPolicy);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePolicyAt(Policy aPolicy, int index)
  {
    boolean wasAdded = false;
    if(policies.contains(aPolicy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPolicies()) { index = numberOfPolicies() - 1; }
      policies.remove(aPolicy);
      policies.add(index, aPolicy);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPolicyAt(aPolicy, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=requests.size(); i > 0; i--)
    {
      GameRequest aRequest = requests.get(i - 1);
      aRequest.delete();
    }
    for(int i=comments.size(); i > 0; i--)
    {
      Reply aComment = comments.get(i - 1);
      aComment.delete();
    }
    for(int i=employee.size(); i > 0; i--)
    {
      Employee aEmployee = employee.get(i - 1);
      aEmployee.delete();
    }
    for(int i=promotion.size(); i > 0; i--)
    {
      Promotion aPromotion = promotion.get(i - 1);
      aPromotion.delete();
    }
    for(int i=policies.size(); i > 0; i--)
    {
      Policy aPolicy = policies.get(i - 1);
      aPolicy.delete();
    }
    super.delete();
  }

}