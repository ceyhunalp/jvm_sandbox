package checker;

import java.util.HashMap;
import java.util.HashSet;

public class ASTData {

    public HashSet<String> classes;
    public HashMap<String, HashSet<String>> methods;

    public ASTData() {
        classes = new HashSet<>();
        methods = new HashMap<>();
    }
}
