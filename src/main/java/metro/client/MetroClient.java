package metro.client;

import metro.presentation.MetroPresentation;
import metro.presentation.MetroPresentationInterface;

import java.util.Scanner;

public class MetroClient {
    public static void main(String[] args){

        int swipeinId=0;
        MetroPresentationInterface metroPresentation=new MetroPresentation();
        Scanner scanner=new Scanner(System.in);
        System.out.println("1. New User");
        System.out.println("2. Existing User");
        System.out.println("3. Exit");
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();
        switch(choice)
        {
            case 1:metroPresentation.newUser();
                while(true) {
                    metroPresentation.showOptions();
                    System.out.println("Enter Card Id:");
                    int cardId = scanner.nextInt();
                    boolean cardStatus = metroPresentation.isCard(cardId);
                    if(cardStatus==true)
                    {

                    }
                    System.out.println("Enter choice : ");
                    int choice1 = scanner.nextInt();
                    metroPresentation.performOption(choice1);
                }
        }


    }
}
