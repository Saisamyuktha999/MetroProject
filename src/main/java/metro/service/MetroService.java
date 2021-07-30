package metro.service;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.helper.MetroOutput;
import metro.persistence.MetroDAO;
import metro.persistence.MetroDaoInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class MetroService implements MetroSerivceInterface{

    private MetroDaoInterface metroDao=new MetroDAO();
    private MetroOutput metroOutput = new MetroOutput();
    @Override
    public Collection<TransactionHistory> getTransactionDetails(int cardId) throws SQLException, IOException, ClassNotFoundException {
        int id=cardId;
        return metroDao.getTransactionDetails(id);
    }

    @Override
    public Collection<Card> getCardDetails(int cardId) throws SQLException, IOException, ClassNotFoundException{
        return metroDao.getCardDetails(cardId);
    }

    @Override
    public void updateBalance() throws SQLException, IOException, ClassNotFoundException{

    }

    @Override
    public boolean swipeIn(int cardId, String Source) throws SQLException, IOException, ClassNotFoundException{
        boolean status = metroDao.swipeIn(cardId,Source);
        return status;
    }

    @Override
    public boolean swipeOut(int cardId, String destination) throws SQLException, IOException, ClassNotFoundException{
        boolean status = metroDao.swipeIn(cardId,destination);
        return false;
    }

    @Override
    public Collection<Station> getStationDetails() throws SQLException, IOException, ClassNotFoundException{
        return metroDao.getStationDetails();
    }

    @Override
    public boolean createNewCard() throws SQLException, IOException, ClassNotFoundException {
         return metroDao.createNewCard();

    }

    @Override
    public void showCardException() {
        String message = "Enter correct card id. Swipe in and swipe out should be on same card Id";
        metroOutput.DisplayMessage(message);
    }

    @Override
    public void showBalanceException() {
        String message =" Minimum balance has to be maintained. Change the swipe out options.";
        metroOutput.DisplayMessage(message);
    }

    @Override
    public boolean cardExists(int cardId) throws SQLException, IOException, ClassNotFoundException   {
        boolean status = false;
        status = metroDao.cardExists(cardId);
        return status;
    }

    @Override
    public int rechargeBalance(int cardId, int amount) {
        if(amount>0)
        {
            metroDao.rechargeBalance( cardId, amount);
        }
        else
        {
            String message = "Amount invalid";
            metroOutput.DisplayMessage(message);
        }
        return 0;
    }
}
