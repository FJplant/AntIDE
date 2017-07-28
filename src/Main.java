public class Main {
  static final String[] 이름들 = { "권영모", "김상균", "김아현", "백진우",
                                   "김영주", "김윤경", "김성훈", "이철목" };

  static String 이름 = new String( "권영모" );
  String 권영모 = "권영모";
  public static void main( String args[] ) {
    Main main = new Main();
    System.out.println("One");
    System.out.println("Two");
    System.out.println(이름);

    for ( int i = 0; i < 이름들.length; i++ ) {
      System.out.println( i + ": " + 이름들[i] );
    }
  }
}
