/**
 * Quick Food ordering/delivery app with GUI
 * reads and writes from MySQL DB
 *
 * @author Charles Knighton-Pullin
 * @version 1.00, 21 July 2021
 */

//imports
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;



public class QuickFood {
    public static void main(String[] args) {

        //calls the main menu method
        MainMenu();

 }


    /**
     * main menu for the program
     *
     */
    //main menu for the program
    public static void MainMenu() {
        //logo file for main menu
        ImageIcon logo = new ImageIcon("./logo.png");

        Object[] options = {"Add Customer", "Update Customer Info", "Place Order", "Finalize Order", "Exit"};
        //logo passed in as "message"
        int whichChoice = JOptionPane.showOptionDialog(null, logo, "",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);


        //switch statement for getting values from the restaurant selected from above
        //values retrieved from restaurant methods
        switch (whichChoice) {
            case -1, 4:
                //exit
                System.exit(0);
                break;
            case 0:
                //add customer
                Custom(0);
                break;
            case 1:
                //update customer
                findUpdate();
                break;
            case 2:
                //place order
                findCustomer();
            case 3:
                //search for orders that ar not finalized
                searchCustomer();
                break;
        }
    }




    /**
     * method that's called for various reasons String is passed in depending on the reason and sends back to main menu
     *
     */
    //method that's called for various reasons String is passed in depending on the reason and sends back to main menu
    public static void popUp(String msg) {
        JOptionPane.showMessageDialog(null, "***" + msg + "***");
        MainMenu();
    }




    //method to make cancel click on panel exit program
    public static void cancel(int result) {
        //if the result value from clicking cancel is true, then exit program
        if (result == JOptionPane.CANCEL_OPTION) {
            MainMenu();
        }
    }








    /************************************************************************

                        ADD CUSTOMER

     ************************************************************************/


    /**
     * method to obtain customer information and GUI
     * @param choice is the options from the main menu
     */
    //method to obtain customer information and GUI
    public static void Custom(int choice) {
        //text fields to be added to the form below
        JTextField nameText = new JTextField(15);
        JTextField emailText = new JTextField(15);
        JTextField phNumberText = new JTextField(15);
        JTextField streetText = new JTextField(15);
        JTextField suburbText = new JTextField(15);
        JTextField cityText = new JTextField(15);


        //labels for the form to capture user information
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Name:"));
        myPanel.add(nameText);

        myPanel.add(new JLabel("Email:"));
        myPanel.add(emailText);

        myPanel.add(new JLabel("Tel:"));
        myPanel.add(phNumberText);

        myPanel.add(new JLabel("Street:"));
        myPanel.add(streetText);

        myPanel.add(new JLabel("Suburb:"));
        myPanel.add(suburbText);

        myPanel.add(new JLabel("City:"));
        myPanel.add(cityText);


        //show panel with text fields. Send to cancel, if cancel option clicked the program exits
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Add customer to the Database", JOptionPane.OK_CANCEL_OPTION);

        //if window closed by clicking X then mainmenu is called
        if (result == -1) {
            MainMenu();
        }

        cancel(result);


        //storing the text field entries into better named variables
        String name = nameText.getText();
        String email = emailText.getText();
        String number = phNumberText.getText();
        String street = streetText.getText();
        String suburb = suburbText.getText();
        String city = cityText.getText();

        //The customer class object is populated with the values from above "Customer.java" class file
        //return the value of customer to "main"
        Customer customer = new Customer(name, email, number, street, suburb, city);

        //checking to see if information is empty, if it is then send to error function+exit(try again)
        //msg to be sent to the method - used in other areas also
        String msg = " Please make sure full information is given and try again ";

        if (name.equals("")) {
            popUp(msg);
        }
        if (email.equals("")) {
            popUp(msg);
        }
        if (number.equals("")) {
            popUp(msg);
        }
        if (street.equals("")) {
            popUp(msg);
        }
        if (suburb.equals("")) {
            popUp(msg);
        }
        if (city.equals("")) {
            popUp(msg);
        }

        System.out.println(choice);

        if (result == JOptionPane.OK_OPTION) {
            Driver driver = findDriver(customer.city);

            addCustomer(customer, driver);
            popUp("New customer added to the Database!");

            } else {
                MainMenu();
            }
         }



    /**
     * method to add customer
     * @param customer object with information of the customer added
     * @param driver object with information of the driver added
     */
    public static void addCustomer(Customer customer, Driver driver) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO customer(Name, Email, Phone, Street, Suburb, City, Driver_DriverName) VALUES ('" + customer.name  + "', '" + customer.email + "', '" + customer.number + "', '" + customer.street + "', '" + customer.suburb + "', '" + customer.city + "', '" + driver.name + "')");

            //close SQL connection
            connection.close();

            //error handling for connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    /************************************************************************

                            UPDATE CUSTOMER

     ************************************************************************/

    /**
     *  method to find a customer to update
     *
     */

    public static void findUpdate() {
        //text fields to be added to the form below
        JTextField nameText = new JTextField(15);

        //labels for the form to capture user information
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Search By Customer Name:"));
        myPanel.add(nameText);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Database Search", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameText.getText();

            updateCustomer(name);
        } else {
            cancel(result);
        }

    }


    /**
     *  method to update a customer
     *
     */

    //method to update customer information
    public static void updateCustomer(String customer) {

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM Customer WHERE Name='" + customer + "'");

            ArrayList<String> user = new ArrayList<>();

            //print the entry of that given toy from the MySQL database
            while (results.next()) {

                user.add(results.getInt("CustomerID") + ", "
                        + results.getString("Name") + ", "
                        + results.getString("Email") + ", "
                        + results.getString("Phone") + ", "
                        + results.getString("Street") + ", "
                        + results.getString("Suburb") + ", "
                        + results.getString("City") + "\n");
            }

            //close SQL connection
            connection.close();

            if (user.isEmpty()) {
                popUp("'" + customer + "' unfortunately doesn't exist in the Database");
            } else {
                chooseUpdate(user);
            }



            //error handling for connection to MySQL
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




    /**
     *  method to find a customer to update
     * @param userArray
     */

    //choose customer to be updated, via drop down menu
    public static void chooseUpdate(ArrayList userArray) {

        //converting user array into a string[] to populate the JComboBox
        int size = userArray.size();
        String[] array = new String[size];

        for (int i = 0; i < size; i++) {
            array[i] = String.valueOf(userArray.get(i));

        }


        //make Jpanel add a combo box to it
        JPanel myPanel = new JPanel();
        JComboBox<String> list = new JComboBox<>(array);
        myPanel.add(list);



        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please select the customer to update", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // get the selected item from drop down list
            //pass that to the update info page
            String selectedCustomer = (String) list.getSelectedItem();
            String[] selectedArray = selectedCustomer.split(", ");
            updateEnterInfo(selectedArray);

        } else {
            cancel(result);
        }

    }



    /**
     *  text fields populated with previous information of the customer
     * @param fields added to GUI fields
     */
    //method displays text fields populated with current values of the customer
    public static void updateEnterInfo(String[] fields) {

        //text fields to be added to the form below
        //given the existing values of the stored fields in MySQL database
        JTextField nameText = new JTextField(15);
        nameText.setText(fields[1]);

        JTextField emailText = new JTextField(15);
        emailText.setText(fields[2]);

        JTextField phNumberText = new JTextField(15);
        phNumberText.setText(fields[3]);

        JTextField streetText = new JTextField(15);
        streetText.setText(fields[4]);

        JTextField suburbText = new JTextField(15);
        suburbText.setText(fields[5]);


        JTextField cityText = new JTextField(15);
        cityText.setText(fields[6]);


        //labels for the form to capture user information
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Name:"));
        myPanel.add(nameText);

        myPanel.add(new JLabel("Email:"));
        myPanel.add(emailText);

        myPanel.add(new JLabel("Tel:"));
        myPanel.add(phNumberText);

        myPanel.add(new JLabel("Street:"));
        myPanel.add(streetText);

        myPanel.add(new JLabel("Suburb:"));
        myPanel.add(suburbText);

        myPanel.add(new JLabel("City:"));
        myPanel.add(cityText);


        //show panel with text fields. Send to cancel, if cancel option clicked the program exits
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please edit details to be updated:", JOptionPane.OK_CANCEL_OPTION);

        //if window closed by clicking X then mainmenu is called
        if (result == -1) {
            MainMenu();
        }

        cancel(result);


        //storing the text field entries into better named variables
        String name = nameText.getText();
        String email = emailText.getText();
        String number = phNumberText.getText();
        String street = streetText.getText();
        String suburb = suburbText.getText();
        String city = cityText.getText();

        //The customer class object is populated with the values from above "Customer.java" class file
        //return the value of customer to "main"
        Customer customer = new Customer(name, email, number, street, suburb, city);

        //checking to see if information is empty, if it is then send to error function+exit(try again)
        //msg to be sent to the method - used in other areas also
        String msg = " Please make sure full information is given and try again ";

        if (name.equals("")) {
            popUp(msg);
        }
        if (email.equals("")) {
            popUp(msg);
        }
        if (number.equals("")) {
            popUp(msg);
        }
        if (street.equals("")) {
            popUp(msg);
        }
        if (suburb.equals("")) {
            popUp(msg);
        }
        if (city.equals("")) {
            popUp(msg);
        }


        if (result == JOptionPane.OK_OPTION) {
            System.out.println(customer.city);
            Driver driver = findDriver(customer.city);
            int id = Integer.parseInt(fields[0]);
            update(customer, driver, id);
        }
    }


    /**
     *  update MySQL row
     * @param customer
     * @param driver
     * @param id
     */

    //gets the customer, driver and id from the Custom method to update in SQL
    public static void update(Customer customer, Driver driver, int id) {

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //create statement and execute update
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE customer SET Name = '" + customer.name + "', Email = '" + customer.email + "', Phone = '" + customer.number + "', Street = '" + customer.street + "', Suburb = '" + customer.suburb + "', City = '" + customer.city + "', Driver_DriverName = '" + driver.name + "' WHERE CustomerID = '" + id + "'");


            //close SQL connection
            connection.close();

            //error handling for connection to MySQL
        }  catch (SQLException e) {
            e.printStackTrace();
        }

        popUp("Customer information successfully updated!");

    }





    /************************************************************************

                             PLACE ORDER

    ************************************************************************/


    /**
     *  find customer to place an order for by name
     *
     */
    //find customer to place order for
    public static void findCustomer() {
        //text fields to be added to the form below
        JTextField nameText = new JTextField(15);

        //labels for the form to capture user information
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Search By Customer Name:"));
        myPanel.add(nameText);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Database Search", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameText.getText();

            displayCustomer(name);
        } else {
            cancel(result);
        }

    }

    /**
     *  display customers with name entered
     * @param customer string customer name
     */

    //display the customers with the name entered
    public static void displayCustomer(String customer) {

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM Customer WHERE Name='" + customer + "'");

            ArrayList<String> user = new ArrayList<>();

            //print the entry of that given toy from the MySQL database
            while (results.next()) {

                user.add(results.getInt("CustomerID") + ", "
                        + results.getString("Name") + ", "
                        + results.getString("Phone") + ", "
                        + results.getString("Street") + ", "
                        + results.getString("Suburb") + ", "
                        + results.getString("City") + "\n");
            }

            //close SQL connection
            connection.close();

            if (user.isEmpty()) {
                popUp("'" + customer + "' unfortunately doesn't exist in the Database");
            } else {
                chooseId(user);
            }


            //error handling for connection to MySQL
            } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




    /**
     *  select the specific user via dropdown menu
     * @param userArray array of users with matching name
     */
    //slectes to the customer id for the chosen customer
    public static void chooseId(ArrayList userArray) {


        //converting user array into a string[] to populate the JComboBox
        int size = userArray.size();
        String[] array = new String[size];

        for (int i = 0; i < size; i++) {
            array[i] = String.valueOf(userArray.get(i));

        }


        //make Jpanel add a combo box to it
        JPanel myPanel = new JPanel();
        JComboBox<String> list = new JComboBox<>(array);
        myPanel.add(list);



        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please select the customer to place an order for", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // get the selected item from drop down list
            //convert into an array and get the ID as an Integer
            //pass that for the order form
            String selectedCustomer = (String) list.getSelectedItem();
            String[] selectedArray = selectedCustomer.split(", ");
            int customer = Integer.parseInt(selectedArray[0]);

            System.out.println(customer);
            takeaways(customer);

        } else {
            cancel(result);
        }

    }


    /**
     *  order information
     *
     */

    //method to obtain restaurant information and GUI
    public static void takeaways(Integer customerId) {     //customer location passed in to trigger error if not equal to restaurant location

        //image files for each restaurant button
        ImageIcon burger = new ImageIcon("./burgers.png");
        ImageIcon wok = new ImageIcon("./wok.png");
        ImageIcon pizza = new ImageIcon("./pizza.png");

        Object[] options = {burger, wok, pizza};
        int whichRestaurant = JOptionPane.showOptionDialog(null, "", "Restaurants to place order from:",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);


        //switch statement for getting values from the restaurant selected from above
        //values retrieved from restaurant methods
        switch (whichRestaurant) {
            case 0 -> billies(customerId);
            case 1 -> wok(customerId);
            case 2 -> pete(customerId);
        }
    }



    //customer location is shared for location of restaurant
    //driver operation area still needs to align with customer location
    //menus are below method for restaurant
    public static void billies(Integer customerId) {
        Restaurant billies = new Restaurant("Billie's Burgers", "666-1324");
        orderForm(billies, customerId);
    }


    public static void wok(Integer customerId) {
        Restaurant wok = new Restaurant("Wok This Way", "555-4325");
        orderForm(wok, customerId);
    }



    public static void pete(Integer customerId) {
        Restaurant pete = new Restaurant("Pete's Pizzas", "777-8279");
        orderForm(pete, customerId);
    }


    public static ArrayList Menu(String restaurant) {

        ArrayList<String> menu = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //query for menu ID and then query to get all relevant items with that MenuID code
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Menu WHERE Restaurant_RestaurantName = ?");
            statement.setString(1, restaurant);
            ResultSet getMenuID = statement.executeQuery();

            String menuCode = "";

            if (getMenuID.next()) {
                menuCode = getMenuID.getString("MenuID");
            }

            ResultSet results = statement.executeQuery("SELECT * FROM product WHERE Menu_MenuID = '" + menuCode + "'");


            //print the entry of that given toy from the MySQL database
            while (results.next()) {

                menu.add(results.getString("ProductID"));
                menu.add(results.getString("ItemName"));
                menu.add(results.getString("ItemPrice"));
            }

            //close SQL connection
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menu;
    }


    /**
     *  method for the menu to be populated
     *
     */

    //method for ordering items and returning array of items ordered + cost
    public static void orderForm(Restaurant restaurant, Integer customerId) {
        ArrayList<String> orderList = new ArrayList<>();
        ArrayList<String> menu = new ArrayList<>();
        String itemString = "";
        String shoppingCart = "";
        String[] itemArray;
        String itemPrice = "";
        int total = 0;

        //preparing the the date and time + ordernum
        String date = getDate();
        String[] dateTime = date.split(" ");
        date = dateTime[0];
        String time = dateTime[1];
        int orderNum = orderNumber();
        createOrderSQL(customerId, date, time, orderNum);

        //print to console the ordernum/date/time
        System.out.println(date);
        System.out.println(time);
        System.out.println(orderNum);

        ArrayList<String> productDetails = switch (restaurant.name) {
            case "Billie's Burgers" -> Menu("Billie's Burgers");
            case "Wok This Way" -> Menu("Wok This Way");
            case "Pete's Pizzas" -> Menu("Pete's Pizzas");
            default -> new ArrayList<>();
        };

        ArrayList<String> productId = new ArrayList<>();

        //separate the arraylist of product details into 2
        //product id contains on the SQL id for the database
        //menu contains item name and cost for the menu screen
        for (int i = 0; i < productDetails.size() - 2; i+=3) {
            productId.add(productDetails.get(i));
            menu.add(productDetails.get(i + 1) + " @ R" + productDetails.get(i + 2));
        }


        String menuScreen = "";

        for (int i = 0, j = 1; i < menu.size(); i++, j++) {
            menuScreen += j + ") " + menu.get(i) + "\n";
        }


        //text fields
        JTextArea info = new JTextArea (menuScreen);
        JTextField orderText = new JTextField(0);


        //labels for the form
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(info);
        myPanel.add(orderText);


        int next = 0;
        //loop to continue ordering, continue to checkout or cancel
        while (next == 0) {
            //sets text fields to empty for each loop of adding another item
            orderText.setText("");



            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Select the number of the item to order:", JOptionPane.OK_CANCEL_OPTION);

            //if the result of the option pane above is true (ie OK)
            if (result == JOptionPane.OK_OPTION) {

                //creating an index from the number input from user
                String itemNum = orderText.getText();
                int index = Integer.parseInt(itemNum);

                if (index >= 1 && index <= 10) {

                    //gets the string from the ArrayList eg: "Bacon Avo Feta @ R130"
                    itemString = menu.get(index - 1);
                    //splits into 2 index array. [0] is the item and [1] is the cost
                    itemArray = itemString.split(" @ R");

                    //adding the item and cost String to the orderList array
                    orderList.add(itemArray[0]);
                    orderList.add(itemArray[1]);
                    itemPrice = itemArray[1];

                    String id = productId.get(index - 1);
                    orderItem(orderNum, id);


                } else {
                    //else the user did not enter a number between 1 and 10... error & exit
                    JOptionPane.showMessageDialog(null ,"Unfortunately that is not a valid order, please try again :) ");
                    popUp("Error, unavailable request.... menu numbered from 1 to 10");
                }

                //creates an object that holds the values of the customized options
                Object[] options = {"Add Item", "Checkout", "Exit"};


                //creating a string array from the array list
                //ordered Items is the final result being shown to
                //the user in the JOption pane
                ArrayList<String> items = new ArrayList<>();
                items.add(itemString);

                for (String s : items) {
                    shoppingCart += s + "\n";
                }


                int item = Integer.parseInt(itemPrice);
                total += item;
                System.out.println(itemPrice);

                //add another item, checkout with ordered items shown
                next = JOptionPane.showOptionDialog(null, "What would you like to do next? \n \n" + shoppingCart + "\n" + "Total: R" + total, "Order Progress....",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        options, options[0]);

                System.out.println(next);

                //else if next equals = check out (1)
            } if (next == 1) {
                instructions(orderNum, total);
            } else if (next == 2) {
                try {
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost/quickfooddb",
                            "root",
                            "root");

                    //pass connection into the menu method
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("UPDATE `order` SET Total=" + total + " WHERE OrderNum = " + orderNum);

                    //close SQL connection
                    connection.close();

                    popUp(" Your order has not been finalized ");

                    //error handling for connection
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     *  get the date method
     *
     */


    //method to get date and time and format to MySQL - format given as "pattern"
    public static String getDate() {
        Date now = new Date();
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(now);
    }



    /**
     *  method to create random order number
     *
     */
    //method to create a random order number
    public static Integer orderNumber() {
        double orderNumberRandom = Math.random() * 1000000000;
        return (int) orderNumberRandom % 10000;
    }




    /**
     *  method to create order entry in MySQL
     * @param customerId
     * @param orderNum
     * @param date
     * @param time
     */

    //method to create the order. multiple items can be attached to a single order form
    public static void createOrderSQL(Integer customerId, String date, String time, Integer orderNum) {

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO `order` (OrderNum, Date, Time, Customer_CustomerID) VALUES (" + orderNum  + ", '" + date + "', '" + time + "', " + customerId + ")");

            //close SQL connection
            connection.close();

            //error handling for connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *  create specific orders for each product
     * @param orderNum
     * @param productId
     */

    //method for the order of each product linked to a specific orderNumber/form
    public static void orderItem(Integer orderNum, String productId) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            String menuID = productId.substring(0,2);

            System.out.println(orderNum + " " + productId + " " + menuID);

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO `orderproduct` (Order_OrderNum, Product_ProductID, Product_Menu_MenuID) VALUES ('" + orderNum  + "', '" + productId + "', '" + menuID + "')");

            //close SQL connection
            connection.close();

            //error handling for connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    /************************************************************************

                                FINALIZE ORDER

     ************************************************************************/


    /**
     *  method to find a customer to finalize and order for
     *
     */
    //search for customer who's order is needed to be finalized
    public static void searchCustomer() {
        //text fields to be added to the form below
        JTextField nameText = new JTextField(15);

        //labels for the form to capture user information
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Search By Customer Name:"));
        myPanel.add(nameText);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please enter the customer ID to place order", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameText.getText();
            System.out.println(name);

            displayOrder(name);
        } else {
            cancel(result);
        }

    }

    /**
     *  method to display order, if exists, for that customer
     *
     */

    //displays customers with open orders, by name, ordernum, customerID, total and date
    public static void displayOrder(String customer) {

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            ResultSet customerIdResult = statement.executeQuery("SELECT * FROM `customer` WHERE Name='" + customer + "'");

            int id = 0;
            ArrayList<Integer> idList = new ArrayList<>();
            while (customerIdResult.next()) {
                id = customerIdResult.getInt("CustomerID");
                idList.add(id);
            }

            ArrayList<String> order = new ArrayList<>();

            //get results where the id matches the customer, but the finalized status is marked false (0)
            for (int index : idList) {
                ResultSet results = statement.executeQuery("SELECT * FROM `order` WHERE Customer_CustomerID=" + index + "&& Finalized = 0");

                //print the entry of that given toy from the MySQL database
                while (results.next()) {

                    order.add(results.getInt("OrderNum") + ", "
                            + customer + ", "
                            + results.getInt("Customer_CustomerID") + ", R"
                            + results.getString("Total") + ", "
                            + results.getString("Date") + "\n");
                }
            }

            System.out.println(idList);

            //close SQL connection
            connection.close();

            if (order.isEmpty()) {
                popUp(" '" + customer + "' unfortunately doesn't have any orders to finalize ");
            } else {
                chooseOrder(order);
            }


            //error handling for connection to MySQL
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     *  choose the order found
     *
     */

    //choose an order to be finalized from the drop down menu
    public static void chooseOrder(ArrayList orderArray) {


        //converting user array into a string[] to populate the JComboBox
        int size = orderArray.size();
        String[] array = new String[size];

        for (int i = 0; i < size; i++) {
            array[i] = String.valueOf(orderArray.get(i));

        }


        //make Jpanel add a combo box to it
        JPanel myPanel = new JPanel();
        JComboBox<String> list = new JComboBox<>(array);
        myPanel.add(list);



        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please select the order to be finalized", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // get the selected item from drop down list
            //convert into an array and get the orderNum and total as an Integer
            //pass that for the instructions
            String selectedCustomer = (String) list.getSelectedItem();
            String[] selectedArray = selectedCustomer.split(", ");
            int orderNum = Integer.parseInt(selectedArray[0]);

            //the total now includes the R before the number, so this needs to be removed
            String cost = selectedArray[3].substring(1);
            int total = Integer.parseInt(cost);

            instructions(orderNum, total);

        } else {
            cancel(result);
        }

    }

    /**
     *  add instructions and finalize
     * @param orderNum
     * @param total
     */

    //method/menu/input form for the special instructions
    public static void instructions(int orderNum, int total) {

        JTextField specialInstructions = new JTextField(20);

        //labels for the form
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Food Preparation Instructions:"));
        myPanel.add(specialInstructions);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Food Preparation Preferences", JOptionPane.OK_CANCEL_OPTION);

        //store the instructions in this variable to be returned
        String instructions = specialInstructions.getText();

        //if the user does not enter any instructions
        //the value inserted into the database will be "none"
        if (instructions == "") {
            instructions = "none";
        }

        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost/quickfooddb",
                        "root",
                        "root");

                //update instructions and set the finalization of the order to 1 (0 meaning not finalized and 1 meaning finalized - boolean)
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE `order` SET Instructions = '" + instructions + "', Total = " + total +", Finalized = " + 1 +" WHERE OrderNum = " + orderNum);


                //query to get customer id
                ResultSet search = statement.executeQuery("SELECT * FROM `order` WHERE OrderNum = " + orderNum);
                int id = 0;

                while (search.next()) {
                    id = search.getInt("Customer_CustomerID");
                }

                //query to get driver name from customer id
                String driver = "";
                search = statement.executeQuery("SELECT * FROM `customer` WHERE CustomerID = " + id);
                while (search.next()) {
                    driver = search.getString("Driver_DriverName");
                }


                //update driver load by 1 when order is finalized
                statement.executeUpdate("UPDATE driver SET DriverLoad = DriverLoad+1 WHERE DriverName = '" + driver + "';");

                //pass order number to finalize invoice
                invoice(orderNum, total, id);

                //close SQL connection
                connection.close();

                //error handling for connection
            } catch (SQLException e) {
                e.printStackTrace();
            }


         } else if (result == JOptionPane.CANCEL_OPTION) {
                popUp("Order not finalized");
            }
        }


    /**
     *  create invoice based off the orderform finalization
     *
     */
    //creates a SQL entry for the finalized order
    public static void invoice(int orderNum, int total, int id) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");

            //pass connection into the menu method
            Statement statement = connection.createStatement();
            ResultSet customerInfo = statement.executeQuery("SELECT * FROM customer WHERE CustomerID = " + id);

            String name = "";
            String driver= "";
            while (customerInfo.next()) {
                name = customerInfo.getString("Name");
                driver= customerInfo.getString("Driver_DriverName");
            }

            statement.executeUpdate("INSERT INTO `invoice` VALUES (" + orderNum  + ", '" + name + "', '" + driver + "', " + total + ")");

            //close SQL connection
            connection.close();

            popUp(" Order finalized and Invoice generated ");

            //error handling for connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /************************************************************************

                                DRIVER

     ************************************************************************/



    /**
     *  method to find driver
     * @param location pass the location of the customer to find match
     */
    //method to find the driver with minimum load in the customer location
    private static Driver findDriver(String location) {

        //finding out which of the drivers are in the customer location
        ArrayList<Driver> allDrivers = new ArrayList<>();

        System.out.println(location);
        //try connect to MySQL database, user and password is "root"
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/quickfooddb",
                    "root",
                    "root");



            //pass connection into the menu method
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM driver");

            //while there is another row in the database print it
            while (results.next()) {
                String name = results.getString("DriverName");
                String loc = results.getString("DriverLocation");
                int load = results.getInt("DriverLoad");

                //adding all of the drivers from MySQL database to an arraylist of objects - drivers class
                Driver driver = new Driver(name, loc, load);
                allDrivers.add(driver);
            }


            //System.out.println(location);
            //close SQL connection
            connection.close();


            //out of those drivers above, based on location we chose which are suitable based on the areas of the customer
            ArrayList<Driver> suitableDrivers = new ArrayList<>();
            for (int i = 0; i < allDrivers.size(); i++) {
                Driver driver = allDrivers.get(i);
                if (driver.location.equalsIgnoreCase(location)) {
                    suitableDrivers.add(driver);
                }
            }


            //if no suitable drivers are found in the area of the customer, then popUP message and back to main menu
            //the arraylist of suitable drivers would then be empty as no area match
            if (suitableDrivers.isEmpty()) {
                String msg = "Sorry! Our drivers are currently not operating in your area.";
                popUp(msg);
            }

            //array list of the loads of all the possible drivers
            ArrayList<Integer> load = new ArrayList<>();
            for (Driver driver : suitableDrivers) {
                load.add(driver.load);
            }


            //finding the lowest index for the load of the driver
            int i = smallestIndex(load);
            Driver yourDriver = suitableDrivers.get(i);

            //Driver found and printed to console
            System.out.println("YOUR DRIVER: " + yourDriver.name + ", " + yourDriver.location + ", " + yourDriver.load);

            //return driver
            return yourDriver;

            //error handling for connection
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //setting up a false possibility
        Driver yourDriver = new Driver("false","false",0);
        System.out.println("YOUR DRIVER: " + yourDriver.name + ", " + yourDriver.location + ", " + yourDriver.load);
        return yourDriver;
    }



    /**
     *  returns an int of the smallest index of load
     * @param intLoadArray array featuring the loads of the drivers in suitable area
     */

    //This method returns the index of the smallest value in the array of given size
    public static int smallestIndex(ArrayList intLoadArray) {
        //current and smallest positions
        int current = Integer.parseInt(String.valueOf(intLoadArray.get(0)));
        int smallest = 0;

        //smallest is equal to 0 so we start at 1, assuming that 0 is the smallest
        for (int j = 1; j < intLoadArray.size(); j++) {
            int place = Integer.parseInt(String.valueOf(intLoadArray.get(j)));
            //if it isn't then switch
            if (place < current) {
                current = place;
                smallest = j;
            }
        }
        //return the index of the smallest value in load array
        return smallest;
    }

}