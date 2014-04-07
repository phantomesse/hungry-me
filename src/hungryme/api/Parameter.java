package hungryme.api;

public class Parameter {
	private String key;
	private String value;
	
	public Parameter(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public Parameter(String key, double value) {
		this(key, Double.toString(value));
	}
	
	public String toString() {
		return key + "=" + value;
	}
}
