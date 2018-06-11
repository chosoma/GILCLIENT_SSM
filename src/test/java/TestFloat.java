import java.text.DecimalFormat;

public class TestFloat {

    public static void main(String[] args) {
    	
        Float f = 11.500F;
        DecimalFormat dFormat = new DecimalFormat("#0.00");
        String string = dFormat.format(f);
        System.out.println(string);
    }

}
