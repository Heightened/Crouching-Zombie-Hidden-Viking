package controller;

import model.Game;

public abstract class ThreadedController extends Thread implements Controller
{
	private Controller controller;
	
	public ThreadedController(Game game)
	{
		this(new ConcreteController(game));
	}
	
	public ThreadedController(Controller controller)
	{
		this.controller = controller;
	}

	@Override
	public Game getGame() {
		return this.controller.getGame();
	}

	@Override
	public void setGame(Game game) {
		this.controller.setGame(game);
	}
	
	public abstract void run();
}
