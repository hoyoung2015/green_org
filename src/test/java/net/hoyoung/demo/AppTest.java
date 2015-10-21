package net.hoyoung.demo;

import cn.edu.hfut.dmic.webcollector.model.Page;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Matcher matcher = Pattern.compile("province=(\\d+)(&city=(\\d+)(&area=(\\d+))?)?")
                .matcher("http://www.hyi.org.cn/go/index/filter?province=112&city=14&area=1120108");
        while (matcher.find()){
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
        }
        System.out.println("---------------------------");
        matcher = Pattern.compile("province=(\\d+)(&city=(\\d+)(&area=(\\d+))?)?")
                .matcher("http://www.hyi.org.cn/go/index/filter?province=112&city=14");
        if(matcher.find()){
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
        }
        System.out.println("---------------------------");
        matcher = Pattern.compile("province=(\\d+)(&city=(\\d+)(&area=(\\d+))?)?")
                .matcher("http://www.hyi.org.cn/go/index/filter?province=112");
        if(matcher.find()){
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
        }
    }
    public void testApp2()
    {
//        String url = "http://www.hyi.org.cn/go/index/filter?province=112&city=14&area=1120108";
//        String url = "http://www.hyi.org.cn/go/index/filter?province=112&city=14";
        String url = "http://www.hyi.org.cn/go/index/filter?province=112";
        getLoc(url);
    }
    private Map<String,String> getLoc(String url){
        Map<String,String> map = new HashMap<>();
        String[] tt = {"province","city","area"};
        String[] sstr = url.substring(url.indexOf("?")+1).split("&");
        int i=0;
        for (; i <sstr.length ; i++) {
            String id = sstr[i].split("=")[1];
            map.put(tt[i],id);
        }
        for (;i<tt.length;i++){
            map.put(tt[i],"0");
        }
        System.out.println(map);
        return map;
    }
    public void testApp3() throws IOException {
        Page page = new Page();
        page.setUrl("http://www.hyi.org.cn/go/Index/detail/id/41.html");
        page.setHtml(FileUtils.readFileToString(new File("data/detail.html")));
        Document doc = page.getDoc();
        System.out.println(doc.title());
        doc.select("//dd[]");
    }
    public void testApp4() throws IOException {


        Html html = new Html(FileUtils.readFileToString(new File("data/detail.html")));
        Selectable table = html.xpath("//table");
        String leader = table.xpath("//tr[@class=row2_tr_no]/td/text()").get();
        String email = table.xpath("//tr[2]/td/text()").get();
        String web = table.xpath("//tr[3]/td/a/text()").get();
        String addr = table.xpath("//tr[4]/td/text()").get();
        String tel = table.xpath("//tr[5]/td/text()").get();
        String postcode = table.xpath("//tr[6]/td/text()").get();
        String domain = table.xpath("//tr[7]/td/text()").get();

        String intro = html.xpath("//dd[@class='fr row2_fr']/table//tr[2]/td/html()").get();
        String eintro = html.xpath("//dd[@class='fr row2_fr']/table[2]//tr[2]/td/html()").get();
        String project = html.xpath("//dd[@class='fr row2_fr']/table[3]//tr[2]/td//html()").get();

        System.out.println(project);
    }
}
