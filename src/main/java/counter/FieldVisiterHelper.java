package counter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class FieldVisiterHelper extends ClassVisitor {

    public FieldVisiterHelper(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);

    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println(name + " " + descriptor);
        return super.visitField(access, name, descriptor, signature, value);
    }

}
