/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 60 "model.ump"
// line 178 "model.ump"
public class Review
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  private String rating;
  private String text;

  //Review Associations
  private Purchase game;
  private Reply reply;
  private List<Customer> likedBy;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review(String aRating, String aText, Purchase aGame)
  {
    rating = aRating;
    text = aText;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create review due to game. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    likedBy = new ArrayList<Customer>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRating(String aRating)
  {
    boolean wasSet = false;
    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public boolean setText(String aText)
  {
    boolean wasSet = false;
    text = aText;
    wasSet = true;
    return wasSet;
  }

  public String getRating()
  {
    return rating;
  }

  public String getText()
  {
    return text;
  }
  /* Code from template association_GetOne */
  public Purchase getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Reply getReply()
  {
    return reply;
  }

  public boolean hasReply()
  {
    boolean has = reply != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Customer getLikedBy(int index)
  {
    Customer aLikedBy = likedBy.get(index);
    return aLikedBy;
  }

  public List<Customer> getLikedBy()
  {
    List<Customer> newLikedBy = Collections.unmodifiableList(likedBy);
    return newLikedBy;
  }

  public int numberOfLikedBy()
  {
    int number = likedBy.size();
    return number;
  }

  public boolean hasLikedBy()
  {
    boolean has = likedBy.size() > 0;
    return has;
  }

  public int indexOfLikedBy(Customer aLikedBy)
  {
    int index = likedBy.indexOf(aLikedBy);
    return index;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setGame(Purchase aNewGame)
  {
    boolean wasSet = false;
    if (aNewGame == null)
    {
      //Unable to setGame to null, as review must always be associated to a game
      return wasSet;
    }
    
    Review existingReview = aNewGame.getReview();
    if (existingReview != null && !equals(existingReview))
    {
      //Unable to setGame, the current game already has a review, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Purchase anOldGame = game;
    game = aNewGame;
    game.setReview(this);

    if (anOldGame != null)
    {
      anOldGame.setReview(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setReply(Reply aNewReply)
  {
    boolean wasSet = false;
    if (reply != null && !reply.equals(aNewReply) && equals(reply.getReview()))
    {
      //Unable to setReply, as existing reply would become an orphan
      return wasSet;
    }

    reply = aNewReply;
    Review anOldReview = aNewReply != null ? aNewReply.getReview() : null;

    if (!this.equals(anOldReview))
    {
      if (anOldReview != null)
      {
        anOldReview.reply = null;
      }
      if (reply != null)
      {
        reply.setReview(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLikedBy()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addLikedBy(Customer aLikedBy)
  {
    boolean wasAdded = false;
    if (likedBy.contains(aLikedBy)) { return false; }
    likedBy.add(aLikedBy);
    if (aLikedBy.indexOfLike(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aLikedBy.addLike(this);
      if (!wasAdded)
      {
        likedBy.remove(aLikedBy);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeLikedBy(Customer aLikedBy)
  {
    boolean wasRemoved = false;
    if (!likedBy.contains(aLikedBy))
    {
      return wasRemoved;
    }

    int oldIndex = likedBy.indexOf(aLikedBy);
    likedBy.remove(oldIndex);
    if (aLikedBy.indexOfLike(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aLikedBy.removeLike(this);
      if (!wasRemoved)
      {
        likedBy.add(oldIndex,aLikedBy);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLikedByAt(Customer aLikedBy, int index)
  {  
    boolean wasAdded = false;
    if(addLikedBy(aLikedBy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLikedBy()) { index = numberOfLikedBy() - 1; }
      likedBy.remove(aLikedBy);
      likedBy.add(index, aLikedBy);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLikedByAt(Customer aLikedBy, int index)
  {
    boolean wasAdded = false;
    if(likedBy.contains(aLikedBy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLikedBy()) { index = numberOfLikedBy() - 1; }
      likedBy.remove(aLikedBy);
      likedBy.add(index, aLikedBy);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLikedByAt(aLikedBy, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Purchase existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.setReview(null);
    }
    Reply existingReply = reply;
    reply = null;
    if (existingReply != null)
    {
      existingReply.delete();
    }
    ArrayList<Customer> copyOfLikedBy = new ArrayList<Customer>(likedBy);
    likedBy.clear();
    for(Customer aLikedBy : copyOfLikedBy)
    {
      aLikedBy.removeLike(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "rating" + ":" + getRating()+ "," +
            "text" + ":" + getText()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reply = "+(getReply()!=null?Integer.toHexString(System.identityHashCode(getReply())):"null");
  }
}