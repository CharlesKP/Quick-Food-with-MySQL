class Customer {
    //attributes for class object Customer
    String name;
    String email;
    String number;
    String street;
    String suburb;
    String city;

    //constructor
    Customer(String name, String email, String number, String street, String suburb, String city) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.street = street;
        this.suburb = suburb;
        this.city = city;

    }

    //printing out elements of a person + their values
    public String toString() {
        String output = "\nName: " + this.name;
        output += "\nEmail: " + this.email;
        output += "\nNumber: " + this.number;
        output += "\nAddress: " + this.street;
        output += "\nSuburb: " + this.suburb;
        output += "\nCity: " + this.city;
        return output;
    }
}