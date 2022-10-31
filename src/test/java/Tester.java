import checker.Checker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.HashSet;

public class Tester {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Checker checker = new Checker();
        String fpath = new String(args[0]);
        String fStr = checker.readFileToString(fpath);
        String[] classPaths= {args[1]};
        String[] srcPaths = {args[2]};
        HashSet<String> classNames = checker.parse(args[0], classPaths, srcPaths, fStr);

        if (!classNames.isEmpty()) {
            for (String cname : classNames) {
                System.out.println(cname);
            }
        }
    }
}
