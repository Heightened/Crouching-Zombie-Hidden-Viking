package controller;

import java.util.Collection;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

public class SimpleAIController extends AIController
{
	private Controller leader;
	private Collection<SimpleAIController> followers;
	private GameCharacter zombie;
	private State currentState = State.WANDER;
	
	public SimpleAIController(Game game, GameCharacter gameChar)
	{
		super(game, gameChar);
	}

	enum State
	{
		WANDER,
		ATTACK,
		FLEE;
	}
	
	public void lookAround()
	{
		Collection<Cell> cells = this.getGame().getMap().getNearbyCells(
				this.zombie.getCell().getX(),
				this.zombie.getCell().getY(),
				10
			);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
