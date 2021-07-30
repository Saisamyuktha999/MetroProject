package metro.presentation;

import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf;
import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.helper.MetroOutput;
import metro.service.MetroSerivceInterface;
import metro.service.MetroService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


public class MetroPresentation implements MetroPresentationInterface{

    public MetroSerivceInterface metroService=  new MetroService();
    @Override
    public void showOptions() {
        System.out.println("1. Swipe in");
        System.out.println("2. Swipe out");
        System.out.println("3. Check balance");
        System.out.println("4. View Transaction Details");
        System.out.println("5. Recharge Card");
        System.out.println("6. Exit");

    }

    @Override
    public void performOption(int choice) {
        Scanner scanner = new Scanner(System.in);
        switch(choice)
        {
            case 1:
                System.out.println("Enter cardId:");
                int id = scanner.nextInt();
                System.out.println("Enter Source Station: ");
                String source = scanner.nextLine();
                try{
                    boolean status = metroService.swipeIn(id,source);
                    if(status==true)
                    {
                        System.out.println("Swipe in successfull.");
                    }
                    else
                    {
                        System.out.println("Enter valid data");
                    }
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            case 2:System.out.println("Enter cardId:");
                int id1 = scanner.nextInt();
                System.out.println("Enter Source Station: ");
                String destination = scanner.nextLine();
                try{
                    boolean status = metroService.swipeOut(id1,destination);
                    if(status==true)
                    {
                        System.out.println("Swipe out successfull. Balance Updated");
                    }
                    else
                    {
                        System.out.println("Swipe out unsuccessfull");
                    }
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            case 3:
                Collection<Card> card = new ArrayList<Card>();
                System.out.println("Enter card Id:");
                int cardid = scanner.nextInt();
                try{
                    card = metroService.getCardDetails(cardid);
                    for(Card card1:card)
                    {
                        System.out.print(" balance is "+card1.getBalance());
                    }
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;

            case 4:
                System.out.println("Enter cardId:");
                int cardId = scanner.nextInt();
                Collection<TransactionHistory> transactionHistory = new ArrayList<TransactionHistory>();
                try {
                    transactionHistory = metroService.getTransactionDetails(cardId);
                    MetroOutput.displayTransactions(transactionHistory);
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            case 5:
                System.out.println("Enter card Id:");
                int cardID = scanner.nextInt();
                if(isCard(cardID)==true)
                {
                    System.out.println("Enter Amount to recharge: ");
                    int amount = scanner.nextInt();
                    metroService.rechargeBalance(cardID,amount);
                }

        }

    }

    @Override
    public void newUser() {
        try {
            boolean status = false;
            status = metroService.createNewCard();
            if (status) {
                System.out.println("New Card Generated.");
            }
        }catch(SQLException| IOException| ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCard(int cardId) {
        boolean status = false;
        try {
            status= metroService.cardExists(cardId);
        }catch(SQLException | IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return status;
    }
}
