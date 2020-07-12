package test;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class MongoTest {

    private MongoClient client;
    private MongoCollection<Document> comment;

    @Test
    public void test1() {
        //创建连接
        client = new MongoClient("192.168.145.131");
        //打开数据库
        MongoDatabase commentdb = client.getDatabase("commentdb");
        //获取集合
        comment = commentdb.getCollection("comment");
        //查询
        FindIterable<Document> documents = comment.find();

        //查询记录获取文档集合
        for (Document document : documents) {
            System.out.println("_id：" + document.get("_id"));
            System.out.println("内容：" + document.get("content"));
            System.out.println("用户ID:" + document.get("userid"));
            System.out.println("点赞数：" + document.get("thumbup"));
        }

        //关闭连接
        client.close();
    }

    @Before
    public void init() {
        //创建连接
        client = new MongoClient("192.168.145.131");
        //打开数据库
        MongoDatabase commentdb = client.getDatabase("commentdb");
        //获取集合
        comment = commentdb.getCollection("comment");
    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void test2() {
        FindIterable<Document> documents = comment.find(new BasicDBObject("_id", "2"));

        for (Document document : documents) {
            System.out.println("_id：" + document.get("_id"));
            System.out.println("内容：" + document.get("content"));
            System.out.println("用户ID:" + document.get("userid"));
            System.out.println("点赞数：" + document.get("thumbup"));
        }

    }

    @Test
    public void test3() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("_id","6");
        map.put("content","很棒！");
        map.put("userid","999");
        map.put("thumbup",123);

        Document document = new Document(map);

        comment.insertOne(document);
    }

    @Test
    public void test4() {
        Bson filter = new BasicDBObject("_id", "6");
        Bson uodate = new BasicDBObject("$set", new Document("userid", "8888"));
        comment.updateOne(filter,uodate);
    }

    @Test
    public void test5() {
        Bson filter = new BasicDBObject("_id", "6");
        comment.deleteOne(filter);
    }

}
