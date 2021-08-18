class Restaurant {
    //attributes for class object Restaurant
    String name;
    String number;

    //constructor
    Restaurant(String name, String number) {
        this.name = name;
        this.number = number;
    }

    //method to print the object
    public String toString() {
        String output = "\nRestaurant: " + this.name;
        output += "\nNumber: " + this.number;
        return output;
    }
}