package test;

public class HelloWorld {
  HelloWorld(){
    System.out.println("hello");
    waitInput();
  }
  public void waitInput(){
    System.out.println("waiting.. ");
    try{
    System.in.read();
    } catch(Exception e){
    }
  }
  public static void main(String[] args) {
    HelloWorld h = new HelloWorld();
  }
}

