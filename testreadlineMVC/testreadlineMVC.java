import java.io.IOException;
import java.io.InputStreamReader;

public class testreadlineMVC {

	/**
	 * @param args
	 */

	
	public static void main(String[] args) {
		
		Model_Line model = new Model_Line();
		
		View_Console view = new View_Console(model);
		Controller_EditableBufferedReader controller = new Controller_EditableBufferedReader(new InputStreamReader(System.in), model,view);
		model.addObserver(view);
		
		
		controller.setRaw();
        String str= null;
        try {
                str = controller.readLine();
            } catch(IOException e) {
                e.printStackTrace();
            }
        System.out.print(str);
        controller.unsetRaw();     

	}

}
