package metro.persistence;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.helper.MetroDBConnection;
import metro.service.MetroSerivceInterface;
import metro.service.MetroService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MetroDAO implements MetroDaoInterface{

    MetroSerivceInterface metroService = new MetroService();
    public int sourceCardId=0;
    public int destinationCardId=0;
    public int sourceId=0;
    public int destinationId=0;
    public int fare=0;
    @Override
    public Collection<TransactionHistory> getTransactionDetails(int cardId) throws ClassNotFoundException, SQLException, IOException
    {

        Connection connection = MetroDBConnection.getConnection();
        Collection<TransactionHistory> transactionHistory = new ArrayList<TransactionHistory>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TRANSACTIONDETAILS WHERE CARDID=?");
        preparedStatement.setInt(1, cardId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            TransactionHistory transaction = new TransactionHistory();
            transaction.setTransactionId(resultSet.getInt("TRANSACTIONID"));
            transaction.setCardId(resultSet.getInt("CARDID"));
            transaction.setSourceId(resultSet.getInt("SOURCEID"));
            transaction.setDestinationId(resultSet.getInt("DESTINATIONID"));
            transaction.setSwapInTime(resultSet.getString("SWIPEINTIME"));
            transaction.setSwapOutTime(resultSet.getString("SWIPEOUTTIME"));
            transaction.setFare(resultSet.getInt("FARE"));

            transactionHistory.add(transaction);

        }
        connection.close();
        return transactionHistory;
    }

    @Override
    public Collection<Card> getCardDetails(int cardId) throws SQLException, IOException, ClassNotFoundException {
        Connection connection = MetroDBConnection.getConnection();

        Collection<Card> cardDetails = new ArrayList<Card>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARDDETAILS WHERE CARDID=?");
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next())
        {
            Card card = new Card();
            card.setCardId(resultSet.getInt("CARDID"));
            card.setBalance(resultSet.getInt("BALANCE"));

            cardDetails.add(card);

        }
        connection.close();
        return cardDetails;

    }

    @Override
    public boolean updateBalance() throws SQLException, IOException, ClassNotFoundException{
        Connection connection = MetroDBConnection.getConnection();
        int distance = 0;
        boolean updateStatus=false;
            if(sourceCardId==destinationCardId)
            {
                if(destinationId>sourceId)
                {
                    distance=destinationId-sourceId;
                }
                else
                {
                    distance=sourceId-destinationId;
                }
            }
            else
            {
                metroService.showCardException();
            }
            fare = distance*5;
            int balance =0;
            Collection<Card> card = new ArrayList<Card>();
            card = getCardDetails(sourceCardId);
            for(Card c:card)
            {
                balance = c.getBalance();
            }
            if(balance-fare>20)
            {
                balance = balance-fare;
                PreparedStatement updateBalance = connection.prepareStatement("UPDATE CARD SET Balance = ? WHERE CardId = ?");
                updateBalance.setInt(1, balance);
                updateBalance.setInt( 2,sourceCardId);

                ResultSet resultSet = updateBalance.executeQuery();
                updateStatus=true;
            }
            else {
                metroService.showBalanceException();
            }
            connection.close();
            if(updateStatus=true)
            {

                return true;
            }
            return false;
    }

    @Override
    public boolean swipeIn(int cardId, String Source) throws SQLException, IOException, ClassNotFoundException {
        boolean cardStatus = false;
        boolean stationStatus=false;
        Connection connection = MetroDBConnection.getConnection();
        sourceCardId=cardId;
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARDDETAILS WHERE CARDID=? AND BALANCE>20");
        preparedStatement.setInt(0, sourceCardId);

        ResultSet resultSet = preparedStatement.executeQuery();
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT STATIONID FROM STATION WHERE STATIONNAME=?");
        preparedStatement1.setString(1, Source);
        ResultSet resultSet1 = preparedStatement.executeQuery();

        int rows=0;
        while(resultSet.next())
        {
            rows+=1;
        }
        if(rows>0)
        {
            cardStatus= true;
        }
        Collection<Station> stations = new ArrayList<Station>();
        while(resultSet1.next())
        {
            stationStatus=true;
            Station station = new Station();
            station.setStationId(resultSet1.getInt("StationId"));
            station.setStationName(resultSet1.getString("StationName"));
            stations.add(station);
            sourceId=station.getStationId();
        }
        connection.close();
        if(cardStatus==true && stationStatus==true)
        {
            return true;
        }
        return false;

    }

    @Override
    public boolean swipeOut(int cardId, String destination) throws SQLException, IOException, ClassNotFoundException {
        boolean cardStatus = false;
        boolean stationStatus=false;
        Connection connection = MetroDBConnection.getConnection();
        destinationCardId=cardId;
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARDDETAILS WHERE CARDID=? AND BALANCE>20");
        preparedStatement.setInt(0, destinationCardId);

        ResultSet resultSet = preparedStatement.executeQuery();
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT STATIONID FROM STATION WHERE STATIONNAME=?");
        preparedStatement1.setString(1, destination);
        ResultSet resultSet1 = preparedStatement.executeQuery();

        int rows=0;
        while(resultSet.next())
        {
            rows+=1;
        }
        if(rows>0)
        {
            cardStatus= true;
        }
        Collection<Station> stations = new ArrayList<Station>();
        while(resultSet1.next())
        {
            stationStatus=true;
            Station station = new Station();
            station.setStationId(resultSet1.getInt("StationId"));
            station.setStationName(resultSet1.getString("StationName"));
            stations.add(station);
            destinationId=station.getStationId();
        }
        connection.close();
        boolean status = false;
        if(cardStatus==true && stationStatus==true)
        {
            status = updateBalance();
            if(status == true)
            {
                updateTransactionHistory(destinationCardId,sourceId,destinationId,fare);
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Station> getStationDetails() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = MetroDBConnection.getConnection();

        Collection<Station> stations = new ArrayList<Station>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM STATION");
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next())
        {
            Station station = new Station();
            station.setStationId(resultSet.getInt("STATIONID"));
            station.setStationName(resultSet.getString("STATIONNAME"));

            stations.add(station);

        }
        connection.close();
        return stations;
    }

    @Override
    public boolean createNewCard() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = MetroDBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CARDDETAILS(cardId,balance,issueDate) VALUES(?,?,SYSDATE())");
        preparedStatement.setInt(1, 100);

        int rows = preparedStatement.executeUpdate();
        return (rows>0);
    }

    @Override
    public void updateTransactionHistory(int cardId, int sourceId, int destinationId, int fare) throws SQLException, IOException, ClassNotFoundException  {
        Connection connection = MetroDBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TRANSACTIONHISTORY(transactionId,cardId,sourceId,destinationId,swipeInTime,swipeOutTime,fare) VALUES(?,?,?,?,SYSDATE(),SYSDATE(),?)");
        preparedStatement.setInt(1, cardId);
        preparedStatement.setInt(2, sourceId);
        preparedStatement.setInt(3, destinationId);
        preparedStatement.setInt(6, fare);

        ResultSet resultSet = preparedStatement.executeQuery();
        connection.close();
    }

    @Override
    public boolean cardExists(int cardId) throws SQLException, IOException, ClassNotFoundException  {
        Connection connection = MetroDBConnection.getConnection();
        String query = "SELECT * FROM CARD WHERE CARDID=?";
        PreparedStatement checkCard = connection.prepareStatement(query);
        ResultSet result = checkCard.executeQuery();
        connection.close();
        while(result.next())
        {
            return true;
        }
        return false;

    }
}
