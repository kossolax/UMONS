package reso.LSProuting;

public class LSDBEntry {
	
	public LSMessage message;
	public double timestamp;
	
	public LSDBEntry(LSMessage msg, double timestamp) {
		this.message = msg;
		this.timestamp = timestamp;
	}
	public String toString() {
		return message.toString();
	}
}