/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 54 "model.ump"
// line 172 "model.ump"
public class Reply
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reply Attributes
  private String text;

  //Reply Associations
  private Review review;
  private Manager manager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reply(String aText, Review aReview, Manager aManager)
  {
    text = aText;
    boolean didAddReview = setReview(aReview);
    if (!didAddReview)
    {
      throw new RuntimeException("Unable to create reply due to review. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddManager = setManager(aManager);
    if (!didAddManager)
    {
      throw new RuntimeException("Unable to create comment due to manager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setText(String aText)
  {
    boolean wasSet = false;
    text = aText;
    wasSet = true;
    return wasSet;
  }

  public String getText()
  {
    return text;
  }
  /* Code from template association_GetOne */
  public Review getReview()
  {
    return review;
  }
  /* Code from template association_GetOne */
  public Manager getManager()
  {
    return manager;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setReview(Review aNewReview)
  {
    boolean wasSet = false;
    if (aNewReview == null)
    {
      //Unable to setReview to null, as reply must always be associated to a review
      return wasSet;
    }
    
    Reply existingReply = aNewReview.getReply();
    if (existingReply != null && !equals(existingReply))
    {
      //Unable to setReview, the current review already has a reply, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Review anOldReview = review;
    review = aNewReview;
    review.setReply(this);

    if (anOldReview != null)
    {
      anOldReview.setReply(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setManager(Manager aManager)
  {
    boolean wasSet = false;
    if (aManager == null)
    {
      return wasSet;
    }

    Manager existingManager = manager;
    manager = aManager;
    if (existingManager != null && !existingManager.equals(aManager))
    {
      existingManager.removeComment(this);
    }
    manager.addComment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Review existingReview = review;
    review = null;
    if (existingReview != null)
    {
      existingReview.setReply(null);
    }
    Manager placeholderManager = manager;
    this.manager = null;
    if(placeholderManager != null)
    {
      placeholderManager.removeComment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "text" + ":" + getText()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "review = "+(getReview()!=null?Integer.toHexString(System.identityHashCode(getReview())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "manager = "+(getManager()!=null?Integer.toHexString(System.identityHashCode(getManager())):"null");
  }
}