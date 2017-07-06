package component;

import java.io.Serializable;
import java.util.List;

public class Project implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2073845914105771686L;
	private String name;
	private List<Order> orderList;
	private List<Parser> parserList;
	
	
	public Project(String name) {	//new empty Project 新建项目
		this.name = name;
	}
	
	public Project(String name, List<Order> orderList, List<Parser> parserList) {
		super();
		this.name = name;
		this.orderList = orderList;
		this.parserList = parserList;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Order> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	public List<Parser> getParserList() {
		return parserList;
	}
	public void setParserList(List<Parser> parserList) {
		this.parserList = parserList;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
