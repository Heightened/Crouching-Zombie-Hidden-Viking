package controller;

import java.util.Collection;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

public class AIController extends ThreadedController
{
	private Controller leader;
	private Collection<AIController> followers;
	private GameCharacter zombie;
	private State currentState = State.WANDER;
	
	public AIController(Game game)
	{
		super(game);
	}

	enum State
	{
		WANDER,
		ATTACK,
		FLEE;
	}
	
	@Override
	public void run() // and don't look back
	{
		
	}
	
	public void lookAround()
	{
		Collection<Cell> cells = this.getGame().getMap().getNearbyCells(
				this.zombie.getCell().getX(),
				this.zombie.getCell().getY(),
				10
			);
	}
}
