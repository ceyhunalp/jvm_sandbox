package sample;

public class Foo {

    @Override
    public String toString() {
        return "Foo{}";
    }

    public void write() {
//        System.out.println(this);
        System.out.println(super.toString());
    }
}
