public class Main {
  static final String[] �̸��� = { "�ǿ���", "����", "�����", "������",
                                   "�迵��", "������", "�輺��", "��ö��" };

  static String �̸� = new String( "�ǿ���" );
  String �ǿ��� = "�ǿ���";
  public static void main( String args[] ) {
    Main main = new Main();
    System.out.println("One");
    System.out.println("Two");
    System.out.println(�̸�);

    for ( int i = 0; i < �̸���.length; i++ ) {
      System.out.println( i + ": " + �̸���[i] );
    }
  }
}
