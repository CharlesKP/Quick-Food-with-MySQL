public class Driver {
    //attributes for class object Driver
    String name;
    String location;
    int load;

    //constructor
    Driver(String name, String location, int load) {
        this.name = name;
        this.location = location;
        this.load = load;
    }

    //method to print the values
    public String toString() {
        String output = "\nDriver name: " + this.name;
        output += "\nLocation: " + this.location;
        output += "\nLoad: " + this.load;
        return output;
    }
}