import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String passward = "jim781084";

    public static void main(String[] args) throws SQLException,ClassNotFoundException{

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            Connection connection =  DriverManager.getConnection(url,username,passward);
            Scanner scanner = new Scanner(System.in);
            while(true) {
                System.out.println("-----ITC HOTEL MANAGEMENT SYSTEM-----");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // <-- eat the leftover newline
                switch (choice) {
                    case 1:
                        reserveRoom(connection,scanner);
                        System.out.println();
                        break;
                    case 2:
                        viewReservation(connection,scanner);
                        System.out.println();
                        break;
                    case 3:
                        getRoomNo(connection,scanner);
                        System.out.println();
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        DeleteReservation(connection,scanner);
                        System.out.println();
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("!!!!!!!INVALID ENTRY!!!!!!!");
                }
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public static void reserveRoom(Connection connection,Scanner scanner){
        System.out.print("Enter Guest Name: ");
        String guestname = scanner.nextLine();
        System.out.print("Enter Guest's phone no: ");
        String phone=scanner.nextLine();
        System.out.print("Enter the room no: ");
        int room = scanner.nextInt();

        String query="INSERT INTO reservations (guest_name,room_number,contact_number)"+
                "VALUES ('"+guestname+"',"+room+",'"+phone+"')";

        try(Statement statement = connection.createStatement()) {
            int rowsaffected = statement.executeUpdate(query);
            if (rowsaffected > 0) {
                System.out.println("Reservation successful!");
            } else {
                System.out.println("Reservation failed.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    public static void viewReservation(Connection connection,Scanner scanner)throws SQLException{
        String query = "SELECT reservation_id,guest_name,room_number,contact_number,reservation_date FROM reservations ";

        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query)) {

            System.out.println("........................| HOTEL RESERVATIONS |...................................\n");
            System.out.println("+----------------+------------------------+-------------+--------------------+----------------------------------+");
            System.out.println("| Reservation ID |     Guest  Name        | Room Number |   Contact Number   |      Reservation Date & Time     |");
            System.out.println("+----------------+------------------------+-------------+--------------------+----------------------------------+");

            while (result.next()) {
                int reservationID = result.getInt("reservation_id");
                String name = result.getString("guest_name");
                String room = result.getString("room_number");
                String phno = result.getString("contact_number");
                String date = result.getTimestamp("reservation_date").toString();

                System.out.printf("|  %-14d|  %-22s| %-11s | %-18s |  %-32s|\n", reservationID, name, room, phno, date);

                System.out.println("+----------------+------------------------+-------------+--------------------+----------------------------------+");
            }


        }


    }




    public  static void getRoomNo(Connection connection,Scanner scanner)throws SQLException{
        System.out.println();
        System.out.print("Enter the ID: ");
        int reservation_id=scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the guest name: ");
        String guest_name=scanner.nextLine();


        String query="SELECT room_number FROM reservations " +
                "WHERE reservation_id = " + reservation_id +
                " AND guest_name = '" + guest_name + "'";


        try (Statement statement=connection.createStatement();
        ResultSet resultSet= statement.executeQuery(query)){

            if (resultSet.next()) {
                int roomNumber = resultSet.getInt("room_number");
                System.out.println("Room number for Reservation ID: '" + reservation_id +
                        "' and Guest: '" + guest_name + "' is: " + roomNumber );
            } else {
                System.out.println("!!!!! Reservation not found for the given ID and guest name !!!!!");
            }

        }
    }


    public static void updateReservation(Connection connection,Scanner scanner) throws SQLException{
        System.out.println();
        System.out.print("Enter the id to update: ");
        int id=scanner.nextInt();
        scanner.nextLine();


        if(!reservationID_Exist(connection,id)){
            System.out.println("reservation not found!!");
            return;
        }
        System.out.print("Enter new guest name: ");
        String newGuestName = scanner.nextLine();
        System.out.print("Enter new room number: ");
        int newRoomNumber = scanner.nextInt();
        System.out.print("Enter new contact number: ");
        String newContactNumber = scanner.next();

        String query = "UPDATE reservations " +
                "SET guest_name = '"+ newGuestName +
                "', room_number = "+ newRoomNumber +
                ", contact_number = '"+ newContactNumber +
                "' WHERE reservation_id = "+ id;

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(query);

            if(rowsAffected>0){
                System.out.println("Update done Successfully...");
            }else{
                System.out.println("Not Successful!!!!!!!");
            }
        }
    }

    public static boolean reservationID_Exist(Connection connection,int id)throws SQLException{
        String query = "SELECT reservation_id FROM reservations WHERE reservation_id="+id;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            return resultSet.next();
        }

    }


    public static void DeleteReservation(Connection connection,Scanner scanner) throws SQLException{
        System.out.println();
        System.out.print("Enter the ID to delete: ");
        int id = scanner.nextInt();

        if(!reservationID_Exist(connection,id)){
            System.out.println("This ID: "+id+" do not exist inside the reservation chart!!!!!");
            return;
        }

        String query="DELETE FROM reservations WHERE reservation_id = "+id;

        try(Statement statement=connection.createStatement()){
            int rowsAffected= statement.executeUpdate(query);

            if(rowsAffected>0){
                System.out.println("Deletion of ID: "+id+" is successful.......");
            }else{
                System.out.println("Deletion of ID: "+id+" is failed!!!");
            }

        }

    }



    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("........ThankYou For Using ITC HOTEL MANAGEMENT SYSTEM .........");
    }

}




