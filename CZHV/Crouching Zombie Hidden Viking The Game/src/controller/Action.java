package controller;

public class Action {
	private ActionType a;
//	Source x,y
//	Dest x,y
	
	
	public Action(){}
	
	public Action(ActionType a){
		setActionType(a);
	}
	/*
	public Action(ActionType a, Source x, Source y, Dest x, Dest y){
		
	}
	*/

	public ActionType getActionType() {
		return a;
	}

	public void setActionType(ActionType a) {
		this.a = a;
	}
}
