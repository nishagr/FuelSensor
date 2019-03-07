package ketank.bloodbank.Urls;


import ketank.bloodbank.Adapters.StockAdapter;

/**
 * Created by Ketan-PC on 11/1/2017.
 */

public class All_urls {
    public static abstract class values {
        public static final String UserLogin = "http://eclectika.org/api/blood.php/UserLogin";


        public static final String UserRegistration = "http://eclectika.org/api/blood.php/UserRegistration";
        public static final String GetAllBloodBanks = "http://eclectika.org/api/blood.php/GetAllBloodBanks";
        public static final String GetContacts= "http://eclectika.org/api/blood.php/GetContacts";
        public static final String NotifyByUser= "http://eclectika.org/api/blood.php/NotifyByUser";
        public static final String BankLogin= "http://eclectika.org/api/blood.php/BloodbankLogin";
        public static final String Getbankstock(String bbid){
            return "http://eclectika.org/api/blood.php/Getbankstock?bbid="+bbid;
        }
        public static  final String UserNearBank="http://eclectika.org/api/blood.php/UserNearBank";



    }
}





