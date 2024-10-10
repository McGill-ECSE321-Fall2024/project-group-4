/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 101 "model.ump"
// line 210 "model.ump"
public class RefundRequest
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestStatus { Approved, Denied, Pending }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RefundRequest Attributes
  private RequestStatus status;
  private String reason;

  //RefundRequest Associations
  private Employee reviewer;
  private Purchase purchase;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RefundRequest(RequestStatus aStatus, String aReason, Purchase aPurchase)
  {
    status = aStatus;
    reason = aReason;
    boolean didAddPurchase = setPurchase(aPurchase);
    if (!didAddPurchase)
    {
      throw new RuntimeException("Unable to create refund due to purchase. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setReason(String aReason)
  {
    boolean wasSet = false;
    reason = aReason;
    wasSet = true;
    return wasSet;
  }

  public RequestStatus getStatus()
  {
    return status;
  }

  public String getReason()
  {
    return reason;
  }
  /* Code from template association_GetOne */
  public Employee getReviewer()
  {
    return reviewer;
  }

  public boolean hasReviewer()
  {
    boolean has = reviewer != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Purchase getPurchase()
  {
    return purchase;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setReviewer(Employee aReviewer)
  {
    boolean wasSet = false;
    Employee existingReviewer = reviewer;
    reviewer = aReviewer;
    if (existingReviewer != null && !existingReviewer.equals(aReviewer))
    {
      existingReviewer.removeAssignedRefund(this);
    }
    if (aReviewer != null)
    {
      aReviewer.addAssignedRefund(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setPurchase(Purchase aNewPurchase)
  {
    boolean wasSet = false;
    if (aNewPurchase == null)
    {
      //Unable to setPurchase to null, as refund must always be associated to a purchase
      return wasSet;
    }
    
    RefundRequest existingRefund = aNewPurchase.getRefund();
    if (existingRefund != null && !equals(existingRefund))
    {
      //Unable to setPurchase, the current purchase already has a refund, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Purchase anOldPurchase = purchase;
    purchase = aNewPurchase;
    purchase.setRefund(this);

    if (anOldPurchase != null)
    {
      anOldPurchase.setRefund(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (reviewer != null)
    {
      Employee placeholderReviewer = reviewer;
      this.reviewer = null;
      placeholderReviewer.removeAssignedRefund(this);
    }
    Purchase existingPurchase = purchase;
    purchase = null;
    if (existingPurchase != null)
    {
      existingPurchase.setRefund(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "reason" + ":" + getReason()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewer = "+(getReviewer()!=null?Integer.toHexString(System.identityHashCode(getReviewer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "purchase = "+(getPurchase()!=null?Integer.toHexString(System.identityHashCode(getPurchase())):"null");
  }
}