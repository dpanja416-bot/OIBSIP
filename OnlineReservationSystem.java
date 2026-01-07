import java.io.*;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;

public class OnlineReservationSystem {

    /* ===================== MODELS ===================== */

    static class User implements Serializable {
        String userId;
        String passwordHash;

        User(String userId, String passwordHash) {
            this.userId = userId;
            this.passwordHash = passwordHash;
        }
    }
    
    static class Train implements Serializable {
        int trainNo;
        String trainName;

        Train(int trainNo, String trainName) {
            this.trainNo = trainNo;
            this.trainName = trainName;
        }
    }

    static class Reservation implements Serializable {
        int reservationId;
        String userId;
        int trainNo;
        String classType;
        LocalDate journeyDate;
        String source;
        String destination;
        boolean cancelled = false;

        Reservation(int reservationId, String userId, int trainNo,
                    String classType, LocalDate journeyDate,
                    String source, String destination) {
            this.reservationId = reservationId;
            this.userId = userId;
            this.trainNo = trainNo;
            this.classType = classType;
            this.journeyDate = journeyDate;
            this.source = source;
            this.destination = destination;
        }
    }

    /* ===================== DATA STORE ===================== */

    static Map<String, User> users = new HashMap<>();
    static Map<Integer, Train> trains = new HashMap<>();
    static Map<Integer, Reservation> reservations = new HashMap<>();

    static int reservationCounter = 1001;

    /* ===================== FILE STORAGE ===================== */

    static final String USER_FILE = "users.dat";
    static final String TRAIN_FILE = "trains.dat";
    static final String RES_FILE = "reservations.dat";

    static void saveData() {
        try {
            save(USER_FILE, users);
            save(TRAIN_FILE, trains);
            save(RES_FILE, reservations);
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    @SuppressWarnings("unchecked")
    static void loadData() {
        try {
            users = (Map<String, User>) load(USER_FILE);
            trains = (Map<Integer, Train>) load(TRAIN_FILE);
            reservations = (Map<Integer, Reservation>) load(RES_FILE);
        } catch (Exception ignored) {}
    }

    static void save(String file, Object data) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(data);
        oos.close();
    }

    static Object load(String file) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    /* ===================== SECURITY ===================== */

    static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /* ===================== AUTH ===================== */

    static boolean login(Scanner sc) throws Exception {
        System.out.print("User ID: ");
        String uid = sc.next();
        System.out.print("Password: ");
        String pwd = sc.next();

        User user = users.get(uid);
        if (user == null) return false;
        return user.passwordHash.equals(hashPassword(pwd));
    }

    /* ===================== MAIN ===================== */

    public static void main(String[] args) throws Exception {

        loadData();
        preloadTrains();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== ONLINE RESERVATION SYSTEM ====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int ch = sc.nextInt();

            if (ch == 1) register(sc);
            else if (ch == 2) {
                if (login(sc)) {
                    System.out.println("Login successful!");
                    userMenu(sc);
                } else {
                    System.out.println("Invalid credentials!");
                }
            }
            else break;
        }

        saveData();
        sc.close();
    }

    /* ===================== REGISTER ===================== */

    static void register(Scanner sc) throws Exception {
        System.out.print("Choose User ID: ");
        String uid = sc.next();

        if (users.containsKey(uid)) {
            System.out.println("User already exists!");
            return;
        }

        System.out.print("Choose Password: ");
        String pwd = sc.next();

        users.put(uid, new User(uid, hashPassword(pwd)));
        saveData();
        System.out.println("Registration successful!");
    }

    /* ===================== USER MENU ===================== */

    static void userMenu(Scanner sc) {
        while (true) {
            System.out.println("\n1. Make Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. View Reservations");
            System.out.println("4. Logout");
            System.out.print("Choice: ");

            int ch = sc.nextInt();

            if (ch == 1) makeReservation(sc);
            else if (ch == 2) cancelReservation(sc);
            else if (ch == 3) viewReservations();
            else break;
        }
    }

    /* ===================== RESERVATION ===================== */

    static void makeReservation(Scanner sc) {
        try {
            System.out.print("User ID: ");
            String uid = sc.next();

            System.out.print("Train Number: ");
            int tno = sc.nextInt();

            Train train = trains.get(tno);
            if (train == null) {
                System.out.println("Invalid train number!");
                return;
            }

            System.out.println("Train Name: " + train.trainName);

            System.out.print("Class (Sleeper/3A/2A/1A): ");
            String cls = sc.next();

            System.out.print("Journey Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.next());

            System.out.print("Source: ");
            String src = sc.next();

            System.out.print("Destination: ");
            String dest = sc.next();

            Reservation r = new Reservation(
                    reservationCounter++, uid, tno, cls, date, src, dest
            );

            reservations.put(r.reservationId, r);
            saveData();

            System.out.println("Reservation Successful!");
            System.out.println("Reservation ID: " + r.reservationId);

        } catch (Exception e) {
            System.out.println("Error in reservation!");
        }
    }

    /* ===================== CANCELLATION ===================== */

    static void cancelReservation(Scanner sc) {
        System.out.print("Reservation ID: ");
        int rid = sc.nextInt();

        Reservation r = reservations.get(rid);
        if (r == null || r.cancelled) {
            System.out.println("Invalid reservation!");
            return;
        }

        r.cancelled = true;
        saveData();
        System.out.println("Reservation cancelled successfully!");
    }

    /* ===================== VIEW ===================== */

    static void viewReservations() {
        for (Reservation r : reservations.values()) {
            System.out.println("---------------------------------");
            System.out.println("ID: " + r.reservationId);
            System.out.println("User: " + r.userId);
            System.out.println("Train No: " + r.trainNo);
            System.out.println("Class: " + r.classType);
            System.out.println("Date: " + r.journeyDate);
            System.out.println("From: " + r.source + " To: " + r.destination);
            System.out.println("Status: " + (r.cancelled ? "CANCELLED" : "CONFIRMED"));
        }
    }

    /* ===================== PRELOAD TRAINS ===================== */

    static void preloadTrains() {
        if (!trains.isEmpty()) return;
        trains.put(101, new Train(101, "Howrah Rajdhani"));
        trains.put(102, new Train(102, "Shatabdi Express"));
        trains.put(103, new Train(103, "Duronto Express"));
        saveData();
    }
}
