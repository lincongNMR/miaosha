import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CompilerTest
{
    public static void main(String args[]) throws IOException
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int results = compiler.run(null, null, null, "D:\\codes\\gitCodes\\miaosha\\src\\test\\java\\ConsistentHashWithoutVirtualNodes.java");
        System.out.println((results == 0)?"编译成功":"编译失败");
// 在程式中运行test
        Runtime run = Runtime.getRuntime();
        Process p = run.exec("java ConsistentHashWithoutVirtualNodes");
        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String s;
        while ((s = br.readLine()) != null)
            System.out.println(s);
    }
}

