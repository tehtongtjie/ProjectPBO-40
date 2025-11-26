public class MenuItem {
    private String name;
    private double price;
    private String imageUrl;

    public MenuItem(String name, double price, double rating, String imageUrl) {
        this.name = name;
        this.price = price;

        this.imageUrl = (imageUrl == null || imageUrl.isEmpty()) ? "default.png" : imageUrl;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    @Override
    public String toString() {
        return name + " - Rp " + (int)price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Double.compare(menuItem.price, price) == 0 &&
               name.equals(menuItem.name);
    }

    
}