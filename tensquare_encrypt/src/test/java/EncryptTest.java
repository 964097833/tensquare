import com.tensquare.encrypt.EncryptApplication;
import com.tensquare.encrypt.rsa.RsaKeys;
import com.tensquare.encrypt.service.RsaService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EncryptApplication.class)
public class EncryptTest {

    @Autowired
    private RsaService rsaService;

    @Before
    public void before() throws Exception{
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void genEncryptDataByPubKey() {
        String data = "{\"title\":\"测试修改\"}";

        try {

            String encData = rsaService.RSAEncryptDataPEM(data, RsaKeys.getServerPubKey());

            System.out.println("data: " + data);
            System.out.println("encData: " + encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        String requestData = "DQ4sK2CKbXVMEcts6hkVP9Go3Zci8cUVrsHzhAkmoBOWXcBQT9VTJG2x6YJJg/QZnYy0jpXV3X6Ol2tHXZ7gBda3LTJNv+sXqmxFtGJ8+Q5Ryus7jLkRTkjTMUCSsjT2v7EUKQxdkMs5haHcQRbM9LnGCPcYe0XQvnPY0waYg3Fsc0/AUOI4g80pZPL9+m3770DQ8YscaGFlbFDAgiRBWbTKJ+awjP1yAKcoQlQ125MmqUB1TzN34371C7EoRhdCQw5ue0V19kKyo2O1cBB9Gc6mMiFj/9/o38BnaNryQMKoQe+2vCZcHrq6xYiplp/buFDXB1gRZVpDKFAO82nF+g==";

        String decryptData = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());

        System.out.println(decryptData);
    }
}
