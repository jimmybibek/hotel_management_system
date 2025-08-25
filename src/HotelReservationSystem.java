import java.sql.Statement;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

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
                        break;
//                    case 2:
//                        viewReservation();
//                        break;
//                    case 3:
//                        getRoomNo();
//                        break;
//                    case 4:
//                        updateReservation();
//                        break;
//                    case 5:
//                        DeleteReservation();
//                        break;
//                    case 0:
//                        exit();
//                        return;
                    default:
                        System.out.println("!!!!!!!INVALID ENTRY!!!!!!!");
                }
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
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

}




