package telran.employees.net;

import telran.employees.Employee;
import telran.net.*;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CompanyRobot {
    private static final int PORT = 5000;
	static AtomicInteger employeeId = new AtomicInteger(1000000);
    static int N_THREADS = 10;
    static int N_RUNS = 10000;

    @SuppressWarnings("resource")
	public static void main(String[] args) {
        TcpClient[] clients = new TcpClient[N_THREADS];
        startClients(clients);
        
        // !!! BELOW CODE WAS ADDED JUST FOR TEST: test if added employees were removed
        // Wait for all add operations to complete before starting remove operations
        try {
            Thread.sleep(5000); // Adjust the sleep time as necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        
        // Prompt user input before starting remove operation
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to remove employees? (y/n)");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
        	removeClients(clients);
        }
    }

    private static void startClients(TcpClient[] clients) {
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new TcpClient("localhost", PORT);
            TcpClient client = clients[i];
            new Thread(() -> {
                for (int j = 0; j < N_RUNS; j++) {
                    client.sendAndReceive(new Request("addEmployee",
                            new Employee(employeeId.getAndIncrement(),
                                    10000, "Dep").getJSON()));
                }
                client.close();
            }).start();

        }
    }

    private static void removeClients(TcpClient[] clients) {
        employeeId = new AtomicInteger(1000000);
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new TcpClient("localhost", 4000);
            TcpClient client = clients[i];
            new Thread(() -> {
                for (int j = 0; j < N_RUNS; j++) {
                    client.sendAndReceive(new Request("removeEmployee",
                            String.valueOf(employeeId.getAndIncrement())));
                }
                client.close();
            }).start();

        }
    }
}