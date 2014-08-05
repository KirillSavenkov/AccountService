/**
 * Class of client commands.
 * Format of command in String is: "<type> <id> <value>", where
 * <type> is "add" or "get" or "stop", <id> is integer, 
 * <value> is usable only for commands of add type. 
 * Register doesn't matter.
 *
 */
public class Command {
	
	/**
	 * Type of command. 0 -- incorrect command, 1 -- add command, 2 -- get command, 3 -- drop server statistics
	 */
	
	private int type;
	
	/**
	 * Id of account in command
	 */
	
	private int id;
	
	/**
	 * Value to be added to amount. Used only for commands add type.
	 */
	
	private Long value;
	
	/**
	 * Writes 0, 1 or 2 into field type for incorrect, add or get command respectively.
	 * 
	 * @param type input command type.
	 */
	
	public void setType(String type){
		type = type.toLowerCase();
		this.type = 0;
		if (type.equals("add")) {
			this.type = 1;
		}
		if (type.equals("get")) {
			this.type = 2;
		}
		if (type.equals("drop")) {
			this.type = 3;
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setValue(Long value) {
		this.value = value;
	}
	
	public int getType() {
		return this.type;
	}
	
	public Long getValue() {
		return this.value;
	}
	
	public int getId() {
		return this.id;
	}
	
	/**
	 * Fulfill fields of current object using input command in form of String.
	 * 
	 * @param command.
	 */
	
	public void scanCommand(String command){
		try {
			String[] params = command.split(" ");
			this.setType(params[0]);
			if (this.getType() == 3) {
				return;
			}
			this.setId(Integer.parseInt(params[1]));
			if (this.getType() == 1) {
				this.setValue(Long.parseLong(params[2]));
			}
		}
		catch (Exception e) {
			this.type = 0;
		}
	}
}
