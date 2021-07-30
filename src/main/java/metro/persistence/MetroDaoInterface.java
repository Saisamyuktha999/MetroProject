package metro.persistence;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public interface MetroDaoInterface {
    public Collection<TransactionHistory> getTransactionDetails(int cardId) throws ClassNotFoundException, SQLException, IOException;
    public Collection<Card> getCardDetails(int cardId) throws SQLException, IOException, ClassNotFoundException ;
    public boolean updateBalance ()throws SQLException, IOException, ClassNotFoundException;
    public boolean swipeIn(int cardId,String Source) throws SQLException, IOException, ClassNotFoundException ;
    public boolean swipeOut(int cardId,String destination) throws SQLException, IOException, ClassNotFoundException ;
    public Collection<Station> getStationDetails() throws SQLException, IOException, ClassNotFoundException ;
    public boolean createNewCard() throws SQLException, IOException, ClassNotFoundException;
    public void updateTransactionHistory(int cardId,int sourceId, int destinationId,int fare)throws SQLException, IOException, ClassNotFoundException ;
    public boolean cardExists(int cardId) throws SQLException, IOException, ClassNotFoundException  ;
}
