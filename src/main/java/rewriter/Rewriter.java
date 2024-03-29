package rewriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Rewriter {

    private static final String prefix = "lib/DMath";
    public String mathLib;

    public Rewriter(String mathLib) {
        this.mathLib = prefix + mathLib;
    }

    public void rewriteClass(String path) throws IOException {
        FileInputStream inStream = new FileInputStream(path);
        ClassReader reader = new ClassReader(inStream);
        //TODO: Try with COMPUTE_MAX
        ClassWriter cw = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassAdapter cp = new ClassAdapter(cw, mathLib);
        reader.accept(cp,0);

        FileOutputStream outStream = new FileOutputStream(path);
        outStream.write(cw.toByteArray());
        outStream.close();
    }
}
