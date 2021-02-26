import com.imyiren.gateway.GatewayServer;

/**
 * mailto:yiren.dev@gmail.com
 *
 * @author yiren
 * @date 2021/2/16
 */
public class GatewayApplication {
    public static void main(String[] args) {
        new GatewayServer(8888).run();
    }
}
