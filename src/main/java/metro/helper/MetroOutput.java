package metro.helper;

import metro.bean.Station;
import metro.bean.TransactionHistory;

import java.util.Collection;

public class MetroOutput {
    public static void displayStation(Station station) {
        System.out.print("Id: " + station.getStationId());
        System.out.print(" Name : " + station.getStationName());
    }

    public void DisplayMessage(String message) {
        System.out.println(message);
    }

    public static void displayTransactions(Collection<TransactionHistory> transactionHistory) {
        for (TransactionHistory transactionHistory1 : transactionHistory) {
            System.out.print("[ transaction id: "+transactionHistory1.getTransactionId());
            System.out.println("source id: "+transactionHistory1.getSourceId());
            System.out.println("destination id: "+transactionHistory1.getDestinationId());
            System.out.println("transaction id: "+transactionHistory1.getTransactionId());
            System.out.println("swipe in time: "+transactionHistory1.getSwapInTime());
            System.out.println("swipe out time: "+transactionHistory1.getSwapOutTime());
            System.out.println("fare: "+transactionHistory1.getFare());
            System.out.print("]");
        }

    }
}
