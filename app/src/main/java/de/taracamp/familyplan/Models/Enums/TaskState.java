package de.taracamp.familyplan.Models.Enums;

public enum TaskState
{
	OPEN("Offen"),
	IN_PROCESS("In Bearbeitung"),
	FINISH("Fertig"),
	WAITING("Warten");

	private String state;

	private TaskState(String category){
		this.state = state;
	}

	@Override public String toString(){
		return state;
	}
}
