import java.util.*;

public class View_Console implements Observer{
	Model_Line model;
	
	View_Console(Model_Line model){
		this.model = model;
	}
	static enum Opcode {
				UP_ARROW, DOWN_ARROW, RIGHT_ARROW,
				LEFT_ARROW, SPACE, CTRLD, CTRLS,
				SUPRIMIR, DELETE, HOME,
				END, ENTER, INSERT,
				TAB, DELROW, GOTOXY, SOBREESCIURE
		}

	static class Command {
	  Opcode op;
	  
	  Command(Opcode op) {
	    this.op = op;
	  }
	}
	
	
	public void update(Observable o, Object arg) {
		Command comm = (Command) arg;
		char escCode = 0x1B;
		
		switch(comm.op) {
			case UP_ARROW:
				System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
				break;
			case DOWN_ARROW:
				System.out.print(String.format("%c[%d%s", escCode, 1, "B"));
				break;
			case RIGHT_ARROW:
				System.out.print(String.format("%c[%d%s", escCode, 1, "C"));
				break;
			case LEFT_ARROW:
				System.out.print(String.format("%c[%d%s", escCode, 1, "D"));
				break;
			case SPACE:
				System.out.print(String.format("%c[%d%s", escCode, 1, "@"));
				System.out.print(String.format("%c[%d%s", escCode, 1, "C"));
				break;
			/*case CTRLD:
				
				break;*/
			case CTRLS:
				
				break;
			case SUPRIMIR:
				System.out.print(String.format("%c[%d%s", escCode, 1, "P"));
				break;
			case DELETE:
				System.out.print(String.format("%c[%d%s", escCode, 1, "D"));
				System.out.print(String.format("%c[%d%s", escCode, 1, "P"));
				break;
			case HOME:
				System.out.print(String.format("%c[%d%s", escCode, 1, "G"));
				break;
			case END:
				System.out.print(String.format("%c[%d%s", escCode,model.getMaxColumn(currentrow) + 1, "G"));
				break;
			case ENTER:
				System.out.print(String.format("%c[%d%s", escCode, 1, "E"));
				break;
			case INSERT:
				System.out.print(String.format("%c[%d%s", escCode, 4, "h"));
				break;
			case SOBREESCIURE:
				System.out.print(String.format("%c[%d%s", escCode, 4, "l"));
				break;
			case TAB:
				System.out.print(String.format("%c[%d%s", escCode, 4, "@"));
				System.out.print(String.format("%c[%d%s", escCode, 4, "C"));
				break;
			case DELROW:
				System.out.print(String.format("%c[%d%s", escCode, 2, "K"));
				break;
			case GOTOXY:
				
				break;
			default:
				break;
		}
	}
}
