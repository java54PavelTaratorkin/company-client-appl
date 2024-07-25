package telran.employees.net;

import java.util.Arrays;
import java.util.Iterator;

import telran.employees.Company;
import telran.employees.Employee;
import telran.employees.Manager;
import telran.net.Request;
import telran.net.TcpClient;

public class CompanyProxy implements Company {
     TcpClient tcpClient;
     
	public CompanyProxy(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	@Override
	public Iterator<Employee> iterator() {
		throw new UnsupportedOperationException("Iterator is not supported fro TCP proxy");
	}

	@Override
	public void addEmployee(Employee empl) {
		tcpClient.sendAndReceive(new Request("addEmployee", empl.getJSON()));

	}

	@Override
	public Employee getEmployee(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee removeEmployee(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDepartmentBudget(String department) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getDepartments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Manager[] getManagersWithMostFactor() {
		String managersJSON = tcpClient.sendAndReceive(new Request("getManagersWithMostFactor", ""));
		
		return Arrays.stream(managersJSON.split(";"))
				.map(s -> (Manager)new Employee().setObject(s))
				.toArray(Manager[]::new);
	}

}
