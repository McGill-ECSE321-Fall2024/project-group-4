/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 19 "model.ump"
// line 139 "model.ump"
public class Promotion
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Promotion Attributes
  private String discount;

  //Promotion Associations
  private List<Game> game;
  private Manager creator;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Promotion(String aDiscount, Manager aCreator, Game... allGame)
  {
    discount = aDiscount;
    game = new ArrayList<Game>();
    boolean didAddGame = setGame(allGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create Promotion, must have at least 1 game. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddCreator = setCreator(aCreator);
    if (!didAddCreator)
    {
      throw new RuntimeException("Unable to create promotion due to creator. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDiscount(String aDiscount)
  {
    boolean wasSet = false;
    discount = aDiscount;
    wasSet = true;
    return wasSet;
  }

  public String getDiscount()
  {
    return discount;
  }
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = game.get(index);
    return aGame;
  }

  public List<Game> getGame()
  {
    List<Game> newGame = Collections.unmodifiableList(game);
    return newGame;
  }

  public int numberOfGame()
  {
    int number = game.size();
    return number;
  }

  public boolean hasGame()
  {
    boolean has = game.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = game.indexOf(aGame);
    return index;
  }
  /* Code from template association_GetOne */
  public Manager getCreator()
  {
    return creator;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfGameValid()
  {
    boolean isValid = numberOfGame() >= minimumNumberOfGame();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGame()
  {
    return 1;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (game.contains(aGame)) { return false; }
    game.add(aGame);
    if (aGame.indexOfPromotion(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aGame.addPromotion(this);
      if (!wasAdded)
      {
        game.remove(aGame);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMStarToMany */
  public boolean removeGame(Game aGame)
  {
    boolean wasRemoved = false;
    if (!game.contains(aGame))
    {
      return wasRemoved;
    }

    if (numberOfGame() <= minimumNumberOfGame())
    {
      return wasRemoved;
    }

    int oldIndex = game.indexOf(aGame);
    game.remove(oldIndex);
    if (aGame.indexOfPromotion(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aGame.removePromotion(this);
      if (!wasRemoved)
      {
        game.add(oldIndex,aGame);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMStarToMany */
  public boolean setGame(Game... newGame)
  {
    boolean wasSet = false;
    ArrayList<Game> verifiedGame = new ArrayList<Game>();
    for (Game aGame : newGame)
    {
      if (verifiedGame.contains(aGame))
      {
        continue;
      }
      verifiedGame.add(aGame);
    }

    if (verifiedGame.size() != newGame.length || verifiedGame.size() < minimumNumberOfGame())
    {
      return wasSet;
    }

    ArrayList<Game> oldGame = new ArrayList<Game>(game);
    game.clear();
    for (Game aNewGame : verifiedGame)
    {
      game.add(aNewGame);
      if (oldGame.contains(aNewGame))
      {
        oldGame.remove(aNewGame);
      }
      else
      {
        aNewGame.addPromotion(this);
      }
    }

    for (Game anOldGame : oldGame)
    {
      anOldGame.removePromotion(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameAt(Game aGame, int index)
  {  
    boolean wasAdded = false;
    if(addGame(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(game.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameAt(aGame, index);
    }
    return wasAdded;
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
      existingCreator.removePromotion(this);
    }
    creator.addPromotion(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ArrayList<Game> copyOfGame = new ArrayList<Game>(game);
    game.clear();
    for(Game aGame : copyOfGame)
    {
      aGame.removePromotion(this);
    }
    Manager placeholderCreator = creator;
    this.creator = null;
    if(placeholderCreator != null)
    {
      placeholderCreator.removePromotion(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "discount" + ":" + getDiscount()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "creator = "+(getCreator()!=null?Integer.toHexString(System.identityHashCode(getCreator())):"null");
  }
}