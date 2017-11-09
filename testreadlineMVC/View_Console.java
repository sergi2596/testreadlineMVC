import java.io.IOException;
import java.util.*;

public class View_Console implements Observer{
	Model_Line model;
	char escCode = 0x1B;
	
	View_Console(Model_Line model){
		this.model = model;
	}
	static enum Opcode {
				UP_ARROW, DOWN_ARROW, RIGHT_ARROW,
				LEFT_ARROW,UP_ARROWL, DOWN_ARROWL, RIGHT_ARROWD,
				LEFT_ARROWU, SPACE, CTRLD, CTRLS, CTRLSOFF,
				SUPRIMIR, DELETE, HOME,FIRSTROW,LASTROW,
				END, ENTER, INSERT, CHAR, DELROWUP,
				TAB, DELROW, SOBREESCRIURE
		}

	
	//no va aqui pero per enrecordarme: per pillar maxcols,en terminal 'export COLUMNS' luego en
	//una funcion hacer Integer.parseInt(getenv("COLUMNS"))
	
	
	static class Command {
	  Opcode op;
	  
	  Command(Opcode op) {
	    this.op = op;
	  }
	}
	/**
	 * EL PROFE A CLASSE HA DIT QUE FEM METODES PER CADA ACCIÓ I QUE AL UPDATE NOMES ELS CRIDEM
	 */
	public void writechar() {
		System.out.print(Character.toString((char) model.getChar()));
	}
	public void up_arrow() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "A"));	
	}
	public void up_arrowL() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
		System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(model.getRow() - 1), "G"));
	}
	public void down_arrow() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "B"));
	}
	public void down_arrowL() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "B"));
		System.out.print(String.format("%c[%d%s", escCode,model.getMaxColumn(model.getRow() + 1), "G"));
	}
	
	public void right_arrow() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "C"));
	}
	public void right_arrowD() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "C"));
		System.out.print(String.format("%c[%d%s", escCode, 1, "E"));
	}
	public void left_arrow() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "D"));
	}
	public void left_arrowU() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
		System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(model.getRow()-1), "C"));
	}
	public void first_row() {
		System.out.print(String.format("%c[%d%s", escCode, model.getFirstRow(), "H"));
	}
	public void last_row() {
		System.out.print(String.format("%c[%d%s", escCode, model.getLastRow(), "H"));				
		System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(model.getLastRow())+1, "G"));
	}
	public void space() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "@"));
		System.out.print(String.format("%c[%d%s", escCode, 1, "C"));
	}
	public void ctrls() {
		String[] cmd = { "bash", "-c", "tput rmul > /dev/tty" };
		execute(cmd);
	}
	public void ctrlsoff() {
		String[] cmd = { "bash", "-c", "tput smul > /dev/tty" };
		execute(cmd);
	}
	public void suprimir() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "P"));
	}
	public void delete() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "D"));
		System.out.print(String.format("%c[%d%s", escCode, 1, "P"));
	}
	public void home() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "G"));
	}
	public void end() {
		System.out.print(String.format("%c[%d%s", escCode,model.getMaxColumn(model.getRow()) + 1, "G"));
	}
	public void enter() {
		System.out.print(String.format("%c[%d%s", escCode, 1, "E"));
	}
	public void insert() {
		System.out.print(String.format("%c[%d%s", escCode, 4, "h"));
	}
	public void sobreesciure() {
		System.out.print(String.format("%c[%d%s", escCode, 4, "l"));
	}
	public void tab() {
		System.out.print(String.format("%c[%d%s", escCode, 4, "@"));
		System.out.print(String.format("%c[%d%s", escCode, 4, "C"));
	}
	public void delrow() {
		System.out.print(String.format("%c[%d%s", escCode, 2, "K"));
		System.out.print(String.format("%c[%d%s", escCode, 1, "G"));
	}
	public void delrowup() {
		System.out.print(String.format("%c[%d%s", escCode, 2, "K"));
		System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
		System.out.print(String.format("%c[%d%s", escCode,model.getMaxColumn(model.getRow()) + 1, "G"));
		
	}
	public void execute(String[] cmd) {
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void update(Observable o, Object arg) {
		Command comm = (Command) arg;

		
		switch(comm.op) {
			case CHAR:
				writechar();
				break;
			case UP_ARROW:
				up_arrow();
				break;
			case UP_ARROWL:
				up_arrowL();
				break;
			case DOWN_ARROW:
				down_arrow();
				break;
			case DOWN_ARROWL:
				down_arrowL();
				break;
			case RIGHT_ARROW:
				right_arrow();
				break;
			case RIGHT_ARROWD:
				right_arrowD();
				break;
			case LEFT_ARROW:
				left_arrow();
				break;
			case LEFT_ARROWU:
				left_arrowU();
				break;
			case FIRSTROW:
				first_row();
				break;
			case LASTROW:
				last_row();
				break;
			case SPACE:
				space();
				break;
			case CTRLS:
				ctrls();
				break;
			case CTRLSOFF:
				ctrlsoff();
				break;
			case SUPRIMIR:
				suprimir();
				break;
			case DELETE:
				delete();
				break;
			case HOME:
				home();
				break;
			case END:
				end();
				break;
			case ENTER:
				enter();
				break;
			case INSERT:
				insert();
				break;
			case SOBREESCRIURE:
				sobreesciure();
				break;
			case TAB:
				tab();
				break;
			case DELROW:
				delrow();
				break;
			case DELROWUP:
				delrowup();
				break;
			default:
				break;
		}
	}
}
