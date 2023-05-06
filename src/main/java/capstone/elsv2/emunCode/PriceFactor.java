package capstone.elsv2.emunCode;

public enum PriceFactor {
    HOLIDAY("HOLIDAY"),
    WEEKEND("WEEKEND"),
    MIDNIGHT("MIDNIGHT"),
    NORMAL("NORMAL");

    private String priceFactor;

    PriceFactor(String priceFactor) {
        this.priceFactor = priceFactor;
    }

    public String getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(String priceFactor) {
        this.priceFactor = priceFactor;
    }

    @Override
    public String toString() {
        return priceFactor;
    }
}
