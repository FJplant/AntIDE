import java.lang.reflect.*;

public class ViewObject {
  Object viewee;
  Class viewClass;

  public ViewObject( Object viewee ) {
    this.viewee = viewee;
    viewClass = viewee.getClass();
  }

  public void printAll( ) {
    Field[] fields = viewClass.getDeclaredFields();
    for ( int i = 0; i < fields.length; i ++ ) {
      int modifier = fields[i].getModifiers();
      if ( modifier != 0 ) {
        System.out.print( "Modifiers: " );
        System.out.print( ( Modifier.isAbstract( modifier ) ) ? "abstract" : "" );
        System.out.print( ( Modifier.isFinal( modifier ) ) ? "final" : "" );
        System.out.print( ( Modifier.isStatic( modifier ) ) ? "static" : "" );
      }
      Class type = fields[i].getType();
      System.out.print( ", Type: " + type.getName() );
      System.out.println( ", Name: " + fields[i].getName() );
    }
  }

  public static void main( String[] args ) {
    Main test = new Main();
    ViewObject view = new ViewObject( test );
    view.printAll();
  }
}
