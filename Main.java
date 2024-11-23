import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Alumni class
    static class Alumni {
        private String name;
        private String email;
        private String graduationYear;
        private String major;
        private String currentWorkingIndustry;
        private String description;

        public Alumni(String name, String email, String graduationYear, String major, String currentWorkingIndustry, String description) {
            this.name = name;
            this.email = email;
            this.graduationYear = graduationYear;
            this.major = major;
            this.currentWorkingIndustry = currentWorkingIndustry;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getGraduationYear() {
            return graduationYear;
        }

        public String getMajor() {
            return major;
        }

        public String getCurrentWorkingIndustry() {
            return currentWorkingIndustry;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Name: " + name + ", Email: " + email + ", Graduation Year: " + graduationYear + ", Major: " + major +
                   ", Current Industry: " + currentWorkingIndustry + ", Description: " + description;
        }
    }

    // AlumniDatabase class
    static class AlumniDatabase {
        private static final String URL = "jdbc:mysql://localhost:3306/ndb?useSSL=false"; // Corrected database name
        private static final String USER = "root";
        private static final String PASSWORD = "1111";

        static {
            try {
                // Load MySQL driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            }
        }

        // Method to add alumni to the database
        public void addAlumni(Alumni alumni) {
            String query = "INSERT INTO Alumni (name, email, graduationYear, major, currentWorkingIndustry, description) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, alumni.getName());
                preparedStatement.setString(2, alumni.getEmail());
                preparedStatement.setString(3, alumni.getGraduationYear());
                preparedStatement.setString(4, alumni.getMajor());
                preparedStatement.setString(5, alumni.getCurrentWorkingIndustry());
                preparedStatement.setString(6, alumni.getDescription());
                
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Alumni added to the database successfully.");
                } else {
                    System.out.println("Failed to add alumni.");
                }
            } catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }
        }

        // Method to get all alumni from the database
        public List<Alumni> getAllAlumni() {
            List<Alumni> alumniList = new ArrayList<>();
            String query = "SELECT * FROM Alumni";
            
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String graduationYear = resultSet.getString("graduationYear");
                    String major = resultSet.getString("major");
                    String currentWorkingIndustry = resultSet.getString("currentWorkingIndustry");
                    String description = resultSet.getString("description");
                    alumniList.add(new Alumni(name, email, graduationYear, major, currentWorkingIndustry, description));
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving alumni: " + e.getMessage());
            }
            return alumniList;
        }

        // Method to search alumni by name
        public Alumni searchAlumni(String name) {
            String query = "SELECT * FROM Alumni WHERE name = ?";
            
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    String email = resultSet.getString("email");
                    String graduationYear = resultSet.getString("graduationYear");
                    String major = resultSet.getString("major");
                    String currentWorkingIndustry = resultSet.getString("currentWorkingIndustry");
                    String description = resultSet.getString("description");
                    return new Alumni(name, email, graduationYear, major, currentWorkingIndustry, description);
                }
            } catch (SQLException e) {
                System.out.println("Error searching alumni: " + e.getMessage());
            }
            return null;
        }

        // Method to update alumni information
        public boolean updateAlumni(String name, Alumni updatedAlumni) {
            String query = "UPDATE Alumni SET name = ?, email = ?, graduationYear = ?, major = ?, currentWorkingIndustry = ?, description = ? WHERE name = ?";
            
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, updatedAlumni.getName());
                preparedStatement.setString(2, updatedAlumni.getEmail());
                preparedStatement.setString(3, updatedAlumni.getGraduationYear());
                preparedStatement.setString(4, updatedAlumni.getMajor());
                preparedStatement.setString(5, updatedAlumni.getCurrentWorkingIndustry());
                preparedStatement.setString(6, updatedAlumni.getDescription());
                preparedStatement.setString(7, name);
                
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Error updating alumni: " + e.getMessage());
            }
            return false;
        }

        // Method to delete alumni by name
        public boolean deleteAlumni(String name) {
            String query = "DELETE FROM Alumni WHERE name = ?";
            
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, name);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Error deleting alumni: " + e.getMessage());
            }
            return false;
        }
    }

    // Main method to interact with the program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AlumniDatabase alumniDatabase = new AlumniDatabase();

        while (true) {
            System.out.println("1. Add Alumni");
            System.out.println("2. View All Alumni");
            System.out.println("3. Search Alumni by Name");
            System.out.println("4. Update Alumni Information");
            System.out.println("5. Delete Alumni");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();  // Use nextLine() to read the input

            try {
                int option = Integer.parseInt(choice);  // Parse the string into an integer
                switch (option) {
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter Graduation Year: ");
                        String graduationYear = scanner.nextLine();
                        System.out.print("Enter Major: ");
                        String major = scanner.nextLine();
                        System.out.print("Enter Current Working Industry: ");
                        String currentWorkingIndustry = scanner.nextLine();
                        System.out.print("Enter Description (Paragraph): ");
                        String description = scanner.nextLine();

                        alumniDatabase.addAlumni(new Alumni(name, email, graduationYear, major, currentWorkingIndustry, description));
                        break;
                    case 2:
                        for (Alumni alumni : alumniDatabase.getAllAlumni()) {
                            System.out.println(alumni);
                        }
                        break;
                    case 3:
                        System.out.print("Enter Name to Search: ");
                        String searchName = scanner.nextLine();
                        Alumni foundAlumni = alumniDatabase.searchAlumni(searchName);
                        if (foundAlumni != null) {
                            System.out.println(foundAlumni);
                        } else {
                            System.out.println("Alumni not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter Name to Update: ");
                        String updateName = scanner.nextLine();
                        System.out.println("Enter updated details for the alumni:");

                        System.out.print("Enter Name: ");
                        String updatedName = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String updatedEmail = scanner.nextLine();
                        System.out.print("Enter Graduation Year: ");
                        String updatedGraduationYear = scanner.nextLine();
                        System.out.print("Enter Major: ");
                        String updatedMajor = scanner.nextLine();
                        System.out.print("Enter Current Working Industry: ");
                        String updatedIndustry = scanner.nextLine();
                        System.out.print("Enter Description (Paragraph): ");
                        String updatedDescription = scanner.nextLine();

                        Alumni updatedAlumni = new Alumni(updatedName, updatedEmail, updatedGraduationYear, updatedMajor, updatedIndustry, updatedDescription);
                        if (alumniDatabase.updateAlumni(updateName, updatedAlumni)) {
                            System.out.println("Alumni updated successfully.");
                        } else {
                            System.out.println("Alumni not found or update failed.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter Name to Delete: ");
                        String deleteName = scanner.nextLine();
                        if (alumniDatabase.deleteAlumni(deleteName)) {
                            System.out.println("Alumni deleted successfully.");
                        } else {
                            System.out.println("Alumni not found or delete failed.");
                        }
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;  // Exit the loop and program
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
